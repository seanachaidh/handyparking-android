package com.seanachaidh.handyparking;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class HandyConfiguration {
    private final String rooturl;

    private HandyConfiguration() {
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
    private static HandyConfiguration instance = null;
    public static HandyConfiguration getInstance() {
        if(instance == null) {
            instance = new HandyConfiguration();
        }
        return instance;
    }

    /*
    Getters and setters
     */

    public String getRooturl() {
        return rooturl;
    }
}
