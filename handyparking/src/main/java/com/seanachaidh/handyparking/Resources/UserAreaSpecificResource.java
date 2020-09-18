package com.seanachaidh.handyparking.Resources;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

import com.seanachaidh.handyparking.Area;

public class UserAreaSpecificResource extends Resource<Area> {

    public UserAreaSpecificResource(CloseableHttpClient client) {
        super(Area[].class, "/user/{uid}/area/{aid}", client);
    }
    
}
