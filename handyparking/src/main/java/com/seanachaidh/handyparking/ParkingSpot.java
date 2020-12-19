package com.seanachaidh.handyparking;

import com.google.gson.annotations.SerializedName;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.concurrent.CompletedFuture;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ParkingSpot {
    @SerializedName("idParkingSpots")
    private int id;
    private boolean occupied;
    private String image;
    private float rating;
    private Coordinate coordinate;


    public ParkingSpot(String image, float rating, boolean occupied, Coordinate coordinate) {
        this.occupied = occupied;
        this.coordinate = coordinate;
        this.rating = rating;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public CompletableFuture<ByteBuffer> downloadImage(final CloseableHttpClient client) {
        CompletableFuture<ByteBuffer> retval;
        final ParkingSpot parent = this;

        retval = CompletableFuture.supplyAsync(new Supplier<ByteBuffer>() {
            @Override
            public ByteBuffer get() {
                ByteBuffer retval = null;
                CloseableHttpResponse response = null;
                String url = HandyConfiguration.getInstance().getRooturl() + parent.getImage();

                ClassicHttpRequest request = new HttpGet(url);
                try {
                    response = client.execute(request);
                    InputStream steam = response.getEntity().getContent();
                    

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return retval;
            }
        });

        return retval;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ParkingSpot other = (ParkingSpot) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
