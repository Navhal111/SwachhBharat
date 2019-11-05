package com.india.dataset;

public class Complain {

    public String des;
    public String adress;
    public String username;
    public Boolean approve;
    public Double Latitude;
    public  Double Longitude;

    public Complain() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Complain(String des, String adress,String username,Boolean approve,Double Latitude, Double Longitude) {
        this.adress = adress;
        this.des = des;
        this.username =username;
        this.approve = approve;
        this.Longitude   = Longitude;
        this.Latitude =    Latitude;
    }
}
