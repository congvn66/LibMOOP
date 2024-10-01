package com.example.proj;

public class Rack {

    private int number;
    private String locationIdentifier;

    public Rack() {
        this.number = 0;
        this.locationIdentifier = "";
    }

    public Rack(int number, String locationIdentifier) {
        this.number = number;
        this.locationIdentifier = locationIdentifier;
    }

    public String getLocationIdentifier() {
        return locationIdentifier;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setLocationIdentifier(String locationIdentifier) {
        this.locationIdentifier = locationIdentifier;
    }

}
