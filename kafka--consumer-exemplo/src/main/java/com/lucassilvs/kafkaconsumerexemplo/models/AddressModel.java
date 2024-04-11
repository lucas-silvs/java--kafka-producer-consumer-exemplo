package com.lucassilvs.kafkaconsumerexemplo.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddressModel {

    @JsonProperty("city")
    private  String city;

    @JsonProperty("state")
    private  String state;

    @JsonProperty("zipcode")
    private  int zipcode;

    public AddressModel(String city, String state, int zipcode) {
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
    }

    public AddressModel() {
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public int getZipcode() {
        return zipcode;
    }
}
