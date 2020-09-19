package com.seanachaidh.handyparking.Resources;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


enum RequestType {
    GET,
    DELETE,
    PUT,
    POST
}

public abstract class Resource<T> {
    private final String restURL;
    private String rooturl;
    private CloseableHttpClient client;
    private Class<T[]> klass;
    
    private GsonBuilder buildGSON() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(UUID.class, new UUIDJson());
        return builder;
    }

    public String getRestURL() {
        return restURL;
    }

    /**
     * Formats the RESTURL with the values in the hashmap
     * @param parameters
     * @return formated URL
     */
    public String formatURL(HashMap<String, String> parameters) {
        String url = this.getFullURL();
        if(parameters != null) {
            for (Entry<String, String> e : parameters.entrySet()) {
                String to_search = "{" + e.getKey() + "}";
                url = url.replace(to_search, e.getValue());
            }            
        }
        return url;
    }

    public String createUrlEncodedString(HashMap<String, String> toConvert) {
        String retval = "";
        String final_retval = null;
        if(toConvert != null) {
            for(Map.Entry<String,String> keyvals: toConvert.entrySet()){
                String encodedValue = null;
                try {
                    encodedValue = URLEncoder.encode(keyvals.getValue(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    encodedValue = "";
                }
                if(retval.equals("")){
                    retval += keyvals.getKey() + "=" + encodedValue;
                } else {
                    retval += "&" + keyvals.getKey() + "=" + encodedValue;
                }
            }            
        }
        return retval;
    }

    private ClassicHttpRequest createRequest(RequestType t, HashMap<String, String> params, String postbody) {
        StringEntity ent = new StringEntity(postbody);
        ClassicHttpRequest request = null;

        String url = this.formatURL(params);
        switch (t) {
            case GET:
                request = new HttpGet(url);
                ((HttpGet) request).setEntity(ent);
                break;
            case POST:
                request = new HttpPost(url);
                ((HttpPost) request).setEntity(ent);
                break;
            case PUT:
                request = new HttpPut(url);
                ((HttpPut) request).setEntity(ent);
                break;
            case DELETE:
                request = new HttpDelete(url);
                break;
            default:
                break;
        }

        return request;
    }

    public CompletableFuture<CloseableHttpResponse> performRequestAsync(RequestType t, HashMap<String, String> params, String postbody) {
        CompletableFuture<CloseableHttpResponse> retval;
        /*Vreemde code. Nodig om Resource te kunnen bereiken in supplyAsync*/
        Resource<T> parent = this;

        retval = CompletableFuture.supplyAsync(new Supplier<CloseableHttpResponse>() {
            @Override
            public CloseableHttpResponse get() {
                ClassicHttpRequest request = createRequest(t, params, postbody);
                CloseableHttpResponse resp = null;
                try {
                    resp = parent.client.execute(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return resp;
            }
        });
        return retval;
    }

    public CloseableHttpResponse performRequest(RequestType t, HashMap<String, String> params, String postbody){
        CloseableHttpResponse resp = null;
        ClassicHttpRequest request = createRequest(t, params, postbody);
        try {
            resp = this.client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    private void parseConfiguration() {
        InputStream configurationStream = getClass().getClassLoader().getResourceAsStream("configuration.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        String rooturl = "";

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(configurationStream);
            
            Element root = doc.getDocumentElement();
            NodeList nlist = root.getElementsByTagName("rooturl");
            Node rooturlnode = nlist.item(0);
            rooturl = rooturlnode.getTextContent();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.rooturl = rooturl;
        }
    }

    public String getFullURL(){
        return this.rooturl + this.restURL;
    }

    public Resource(Class<T[]> klass, String restURL, CloseableHttpClient client) {
        this.restURL = restURL;
        this.client = client;
        this.klass = klass;
        parseConfiguration();
    }
    
    public T[] get(HashMap<String, String> params, HashMap<String, String> body) {
        CloseableHttpResponse response = this.performRequest(RequestType.GET, params,  "");
        String retval = null;
        try {
            retval = EntityUtils.toString(response.getEntity());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        GsonBuilder mybuilder = buildGSON();
        T[] result = mybuilder.create().fromJson(retval, this.klass);
        return result;
    }
    public Boolean post(HashMap<String, String> params, HashMap<String, String> body) {
        
        String urlEncodedBody = createUrlEncodedString(body);
        CloseableHttpResponse response = this.performRequest(RequestType.POST, params, urlEncodedBody);
        String retval = null;
        try {
            retval = EntityUtils.toString(response.getEntity());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        JsonObject json = new JsonParser().parse(retval).getAsJsonObject();
        return json.get("result").getAsBoolean();
    }
    public Boolean put(HashMap<String, String> params, HashMap<String, String> body){
        String urlEncodedBody = createUrlEncodedString(body);
        CloseableHttpResponse response = this.performRequest(RequestType.PUT, params, urlEncodedBody);
        String retval = null;
        try {
            retval = EntityUtils.toString(response.getEntity());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        JsonObject json = new JsonParser().parse(retval).getAsJsonObject();
        return json.get("result").getAsBoolean();
    }
    public Boolean delete(HashMap<String, String> params, HashMap<String, String> body){
        String urlEncodedBody = createUrlEncodedString(body);
        CloseableHttpResponse response = this.performRequest(RequestType.DELETE, params, urlEncodedBody);
        String retval = null;
        try {
            retval = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JsonObject json = new JsonParser().parse(retval).getAsJsonObject();
        return json.get("result").getAsBoolean();    
    }
}
