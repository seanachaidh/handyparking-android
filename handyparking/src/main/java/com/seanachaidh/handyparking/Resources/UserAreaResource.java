package com.seanachaidh.handyparking.Resources;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

import com.seanachaidh.handyparking.Area;

public class UserAreaResource extends Resource<Area> {
    public UserAreaResource(CloseableHttpClient client) {
        super(Area[].class, "/user/{uid}/area", client);
    }
}
