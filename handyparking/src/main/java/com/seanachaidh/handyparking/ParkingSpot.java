package com.seanachaidh.handyparking;

import com.google.gson.annotations.SerializedName;

public class ParkingSpot {
    @SerializedName("idParkingSpots")
    private int id;
    private boolean occupied;
    private Coordinate coordinate;

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

    public ParkingSpot(boolean occupied, Coordinate coordinate) {
        this.occupied = occupied;
        this.coordinate = coordinate;
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
