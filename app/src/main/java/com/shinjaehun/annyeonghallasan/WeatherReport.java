package com.shinjaehun.annyeonghallasan;

/**
 * Created by shinjaehun on 2017-01-31.
 */

public class WeatherReport {
    String name;
    WeatherCondition weatherCondition;

    public WeatherReport(String name, WeatherCondition weatherCondition) {
        this.name = name;
        this.weatherCondition = weatherCondition;
    }

    public String getName() {
        return name;
    }

    public WeatherCondition getWeatherCondition() {
        return weatherCondition;
    }
}
