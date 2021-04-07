package com.example.project.Beacon;

import android.view.LayoutInflater;

public class Userdata {
    private String minors;
    private String distances;
    private String majors;
    private String uuid;
    private String date;
    private String latitude;
    private String longitude;
    private String LatLng;


    public Userdata(String minors, String distances, String majors, String uuid, String date, String latitude,String longitude, String LatLng){
        this.minors=minors;
        this.distances=distances;
        this.majors=majors;
        this.uuid=uuid;
        this.date=date;
       this.latitude=latitude;
       this.longitude=longitude;
       this.LatLng=LatLng;
    }

    public Userdata(){}

    public String getLatLng(){return LatLng; }

    public void setLatLng(String LatLng){
        this.LatLng=LatLng;
    }

    public String getLatitude(){return latitude; }

    public void setLatitude(String latitude){
        this.latitude=latitude;
    }

    public String getLongitude(){return longitude; }

    public void setLongitude(String longitude){
        this.longitude=longitude;
    }

    public String getMinors(){
        return minors;
    }

    public void setMinors(String minors){
        this.minors=minors; }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date=date; }

    public String getDistances(){
        return distances;
    }

    public void setDistances(String distances) {
        this.distances=distances; }

    public String getMajors(){
        return majors;
    }

    public void setMajors(String majors){
        this.majors=majors; }

        public void setUuid(String uuid){
        this.uuid=uuid;
        }

        public String getUuid(){
        return uuid;
    }

}
