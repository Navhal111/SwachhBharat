package com.india.dataset;

public class Complain {

    public String des;
    public String adress;
    public String username;
    public Boolean approve;

    public Complain() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Complain(String des, String adress,String username,Boolean approve) {
        this.adress = adress;
        this.des = des;
        this.username =username;
        this.approve = approve;
    }
}
