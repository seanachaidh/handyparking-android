package com.seanachaidh.handyandroid;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

public class ClientSingleton {

    private static ClientSingleton instance = null;

    private CloseableHttpClient client;

    private ClientSingleton() {
        this.client = HttpClients.createDefault();
    }

    public CloseableHttpClient getClient() {
        return client;
    }

    public static ClientSingleton getInstance() {
        if(instance == null) {
            instance = new ClientSingleton();
        }

        return instance;
    }
}
