package com.example.project.Beacon;

import android.view.LayoutInflater;

public class Userdata {
    private String minors;
    private String distances;
    private String majors;
    private String uuid;
    private String date;

    public Userdata(String minors, String distances, String majors, String uuid, String date){
        this.minors=minors;
        this.distances=distances;
        this.majors=majors;
        this.uuid=uuid;
        this.date=date;
    }

    public Userdata(){}

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
