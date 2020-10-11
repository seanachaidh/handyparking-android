package com.seanachaidh.handyparking.Resources;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.org.apache.xpath.internal.operations.Bool;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    private final boolean mock;
    private String rooturl;
    private CloseableHttpClient client;
    private Class<T[]> klass;

    private ArrayList<T> mockDB;
    
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

    private ClassicHttpRequest createRequest(RequestType t, HashMap<String, String> params, String postbody, HashMap<String, String> headers) {
        /*
        Het is belangrijk dat de charset utf-8 is
         */
        StringEntity ent = new StringEntity(postbody, ContentType.create("application/x-www-form-urlencoded", StandardCharsets.UTF_8));
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
        //Setting the headers
        if(headers != null) {
            for(HashMap.Entry<String, String> e: headers.entrySet()) {
                request.addHeader(e.getKey(), e.getValue());
            }
        }

        return request;
    }
    public CompletableFuture<Boolean> performRequestBooleanAsync(RequestType t, HashMap<String, String> params, String postbody, HashMap<String,String> headers) {
        CompletableFuture<Boolean> retval;
        Resource<T> parent = this;

        retval = CompletableFuture.supplyAsync(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                ClassicHttpRequest request = createRequest(t, params, postbody, headers);
                CloseableHttpResponse resp = null;
                String responseBody = null;

                try {
                    resp = parent.client.execute(request);
                    responseBody = EntityUtils.toString(resp.getEntity());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                JsonObject parser = new JsonParser().parse(responseBody).getAsJsonObject();
                Boolean r = parser.get("result").getAsBoolean();

                return r;
            }
        });

        return retval;
    }
    public CompletableFuture<T[]> performRequestAsync(RequestType t, HashMap<String, String> params, String postbody, HashMap<String, String> headers) {
        CompletableFuture<T[]> retval;
        /*Vreemde code. Nodig om Resource te kunnen bereiken in supplyAsync*/
        Resource<T> parent = this;

        retval = CompletableFuture.supplyAsync(new Supplier<T[]>() {
            @Override
            public T[] get() {
                ClassicHttpRequest request = createRequest(t, params, postbody, headers);
                CloseableHttpResponse resp = null;
                String responseBody = null;
                try {
                    resp = parent.client.execute(request);
                    responseBody = EntityUtils.toString(resp.getEntity());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                GsonBuilder mybuilder = buildGSON();
                T[] result = mybuilder.create().fromJson(responseBody, parent.klass);

                return result;
            }
        });
        return retval;
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
        this.mock = false;
        parseConfiguration();
    }

    public Resource(Class<T[]> klass, String restURL, CloseableHttpClient client, boolean mocking) {
        this.restURL = restURL;
        this.client = client;
        this.klass = klass;
        this.mock = mocking;

        parseConfiguration();
    }


    public CompletableFuture<T[]> get(HashMap<String, String> params, HashMap<String, String> body, HashMap<String, String> headers) {
        CompletableFuture<T[]> retval = this.performRequestAsync(RequestType.GET, params,  "", headers);
        return retval;
    }
    public CompletableFuture<Boolean> post(HashMap<String, String> params, HashMap<String, String> body, HashMap<String, String> headers) {
        
        String urlEncodedBody = createUrlEncodedString(body);
        CompletableFuture<Boolean> retval = this.performRequestBooleanAsync(RequestType.POST, params, urlEncodedBody, headers);
        return retval;
    }
    public CompletableFuture<Boolean> put(HashMap<String, String> params, HashMap<String, String> body, HashMap<String, String> headers){
        String urlEncodedBody = createUrlEncodedString(body);
        CompletableFuture<Boolean> retval= this.performRequestBooleanAsync(RequestType.PUT, params, urlEncodedBody, headers);
        return retval;
    }
    public CompletableFuture<Boolean> delete(HashMap<String, String> params, HashMap<String, String> body, HashMap<String, String> headers){
        String urlEncodedBody = createUrlEncodedString(body);
        CompletableFuture<Boolean> retval = this.performRequestBooleanAsync(RequestType.DELETE, params, urlEncodedBody, headers);
        return retval;
    }
/*
    private CompletableFuture<T[]> performGetMock(HashMap<String, String> params, HashMap<String, String> body, HashMap<String, String> headers) {

    }

    private CompletableFuture<Boolean> performPostMock(HashMap<String, String> params, HashMap<String, String> body, HashMap<String, String> headers) {

    }

    private CompletableFuture<Boolean> performPutMock(HashMap<String, String> params, HashMap<String, String> body, HashMap<String, String> headers) {

    }

    private CompletableFuture<Boolean> performDeleteMock(HashMap<String, String> params, HashMap<String, String> body, HashMap<String, String> headers) {

    }
*/
}
