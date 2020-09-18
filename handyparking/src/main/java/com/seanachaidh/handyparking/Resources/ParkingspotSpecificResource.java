package com.seanachaidh.handyparking.Resources;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

import com.seanachaidh.handyparking.ParkingSpot;

public class ParkingspotSpecificResource extends Resource<ParkingSpot> {

    public ParkingspotSpecificResource(CloseableHttpClient client) {
        super(ParkingSpot[].class, "/parkingspot/{id}", client);
    }
    
}
