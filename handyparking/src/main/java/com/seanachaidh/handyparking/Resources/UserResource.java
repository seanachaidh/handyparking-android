package com.seanachaidh.handyparking.Resources;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

import com.seanachaidh.handyparking.User;

public class UserResource extends Resource<User> {
    public UserResource(CloseableHttpClient client) {
        super(User[].class, "/user", client);
    }
}
