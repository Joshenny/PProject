package com.example.project.Beacon;

import java.util.HashMap;
import java.util.Map;

public class BeaconLocationData {
    public Map<String, Map<String, String>> locations = new HashMap<>();

    public BeaconLocationData(){
        initLocationData();
    }

  private void initLocationData() {
       Map<String, String> minorLocations = new HashMap<>();
       minorLocations.put("77", "理工二館");
       minorLocations.put("23366", "行雲莊");
       locations.put("10001", minorLocations);
       locations.put("10001", minorLocations);
   }

    public String getLocation(String major, String minor){
        String location;

        Map<String, String> minorMap = locations.get(major);
        if(minorMap==null || minorMap.size()==0){
            return "No data";
        }

        location = minorMap.get(minor);
        if (location == null || location.equals("")) {
            return "No data";
        }
        return location;

    }
}
