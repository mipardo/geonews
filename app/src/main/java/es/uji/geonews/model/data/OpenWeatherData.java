package es.uji.geonews.model.data;

import java.util.List;

public class OpenWeatherData extends ServiceData {
    private Weather currentWeather;
    private List<MinutelyWeather> minutelyWeatherList;
    private List<HourlyWeather> hourlyWeatherList;
    private List<DailyWeather> dailyWeatherList;

    public OpenWeatherData() {}

    public Weather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(Weather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public List<MinutelyWeather> getMinutelyWeatherList() {
        return minutelyWeatherList;
    }

    public void setMinutelyWeatherList(List<MinutelyWeather> minutelyWeatherList) {
        this.minutelyWeatherList = minutelyWeatherList;
    }

    public List<HourlyWeather> getHourlyWeatherList() {
        return hourlyWeatherList;
    }

    public void setHourlyWeatherList(List<HourlyWeather> hourlyWeatherList) {
        this.hourlyWeatherList = hourlyWeatherList;
    }

    public List<DailyWeather> getDailyWeatherList() {
        return dailyWeatherList;
    }

    public void setDailyWeatherList(List<DailyWeather> dailyWeatherList) {
        this.dailyWeatherList = dailyWeatherList;
    }
}
