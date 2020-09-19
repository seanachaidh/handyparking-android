package com.seanachaidh.handyparking;

import com.seanachaidh.handyparking.Resources.UserResource;

import junit.framework.TestCase;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class UserTest extends TestCase {
    private CloseableHttpClient client;
    @Before
    public void setUp(){
        this.client = HttpClients.createDefault();
    }

    @Test
    public void testGet(){
        UserResource resource = new UserResource(this.client);
        User[] result = resource.get(null, null);
        User first = result[0];
        assertEquals("Pieter", first.getName());
    }

    @Test
    public void testPost() {
        UserResource resource = new UserResource(this.client);
        HashMap<String, String> postBody = new HashMap<String, String>();

        postBody.put("name", "Jan");
        postBody.put("email", "jan@jantje.com");
        postBody.put("password", "12345");
        postBody.put("guide", "1");

        Boolean success = resource.post(null, postBody);
        assertTrue(success);
    }
}
