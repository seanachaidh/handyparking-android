package com.seanachaidh.handyparking.Resources;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

public class RevertResource extends Resource<RESTResult> {
    public RevertResource(CloseableHttpClient client) {
        super(RESTResult[].class, "/revert", client);
    }
}
