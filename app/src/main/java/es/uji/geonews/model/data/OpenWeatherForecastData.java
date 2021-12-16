package es.uji.geonews.model.data;

import java.util.ArrayList;
import java.util.List;

public class OpenWeatherForecastData extends ServiceData {
    private List<OpenWeatherData> openWeatherDataList;

    public OpenWeatherForecastData(){
        openWeatherDataList = new ArrayList<>();
    }

    public List<OpenWeatherData> getOpenWeatherDataList() {
        return openWeatherDataList;
    }

    public void setOpenWeatherDataList(List<OpenWeatherData> openWeatherDataList) {
        this.openWeatherDataList = openWeatherDataList;
    }

    public void addForecast(OpenWeatherData openWeatherData) {
        openWeatherDataList.add(openWeatherData);
    }
}
