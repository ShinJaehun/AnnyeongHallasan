package com.shinjaehun.annyeonghallasan.data;

/**
 * Created by shinjaehun on 2017-01-31.
 */

public class WeatherReport {
    String location;
    WeatherCondition weatherCondition;

    public WeatherReport(String location, WeatherCondition weatherCondition) {
        this.location = location;
        this.weatherCondition = weatherCondition;
    }

    public String getLocation() {
        return location;
    }

    public WeatherCondition getWeatherCondition() {
        return weatherCondition;
    }
}
