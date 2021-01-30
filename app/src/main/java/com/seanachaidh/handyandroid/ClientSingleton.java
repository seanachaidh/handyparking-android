package com.seanachaidh.handyandroid;

import com.seanachaidh.handyparking.Resources.ParkingspotSpecificResource;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

public class ClientSingleton {

    private static ClientSingleton instance = null;

    private CloseableHttpClient client;
    private ParkingspotSpecificResource parkingspotSpecificResource;

    private ClientSingleton() {
        this.client = HttpClients.createDefault();
        this.parkingspotSpecificResource = new ParkingspotSpecificResource(this.client);
    }

    public CloseableHttpClient getClient() {
        return client;
    }

    public ParkingspotSpecificResource getParkingspotSpecificResource() {
        return parkingspotSpecificResource;
    }

    public static ClientSingleton getInstance() {
        if(instance == null) {
            instance = new ClientSingleton();
        }

        return instance;
    }
}
