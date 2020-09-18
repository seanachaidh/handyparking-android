package com.seanachaidh.handyparking.Resources;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

import com.seanachaidh.handyparking.ParkingSpot;

public class ParkingspotResource extends Resource<ParkingSpot> {

    public ParkingspotResource(CloseableHttpClient client) {
        super(ParkingSpot[].class, "/parkingspot", client);
    }
    
}
