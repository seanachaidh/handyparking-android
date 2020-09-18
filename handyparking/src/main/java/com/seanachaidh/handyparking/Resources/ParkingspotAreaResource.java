package com.seanachaidh.handyparking.Resources;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

import com.seanachaidh.handyparking.ParkingSpot;

public class ParkingspotAreaResource extends Resource<ParkingSpot> {
    public ParkingspotAreaResource(CloseableHttpClient client) {
        super(ParkingSpot[].class, "/area/{id}/parkingspots", client);
    }
}
