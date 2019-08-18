package com.londonappbrewery.climapm;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherDataModel {

    // TODO: Declare the member variables here
        private String mcity;
        private String mTemperature;
        private int mcondition;
        private String iconName;


    // TODO: Create a WeatherDataModel from a JSON:

    public static WeatherDataModel fromJson(JSONObject jsonObject){
        try {
            WeatherDataModel obj = new WeatherDataModel();
            obj.mcity = jsonObject.getString("name");
            obj.mcondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            double temp = jsonObject.getJSONObject("main").getDouble("temp") - 273.15;
            int int_temp = (int) Math.rint(temp);
            obj.mTemperature = Integer.toString(int_temp);
            obj.iconName = updateWeatherIcon(obj.mcondition);
            return obj;

        } catch (JSONException e){
            e.printStackTrace();
            WeatherDataModel obj = new WeatherDataModel();
            obj.mcity = "No Data received.";
            return obj;
        }
    }

   //  TODO: Uncomment to this to get the weather image name from the condition:
    private static String updateWeatherIcon(int condition) {

        if (condition >= 0 && condition < 300) {
            return "tstorm1";
        } else if (condition >= 300 && condition < 500) {
            return "light_rain";
        } else if (condition >= 500 && condition < 600) {
            return "shower3";
        } else if (condition >= 600 && condition <= 700) {
            return "snow4";
        } else if (condition >= 701 && condition <= 771) {
            return "fog";
        } else if (condition >= 772 && condition < 800) {
            return "tstorm3";
        } else if (condition == 800) {
            return "sunny";
        } else if (condition >= 801 && condition <= 804) {
            return "cloudy2";
        } else if (condition >= 900 && condition <= 902) {
            return "tstorm3";
        } else if (condition == 903) {
            return "snow5";
        } else if (condition == 904) {
            return "sunny";
        } else if (condition >= 905 && condition <= 1000) {
            return "tstorm3";
        }

        return "dunno";
    }

   //  TODO: Create getter methods for temperature, city, and icon name:

    public String getMcity() {
        return mcity;
    }

    public String getTemperature() {
        return mTemperature + "°";
    }

    public String getIconName() {
        return iconName;
    }
}
