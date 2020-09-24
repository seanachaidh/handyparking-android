package com.seanachaidh.handyparking.Resources;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

public class LoginResource extends Resource<LoginResource.LoginResult> {
    public LoginResource(CloseableHttpClient client) {
        super(LoginResource.LoginResult[].class, "/login", client);
    }

    public static class LoginResult {
        String token;
    }
}
