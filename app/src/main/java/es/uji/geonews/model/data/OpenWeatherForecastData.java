package es.uji.geonews.model.data;

import java.io.Serializable;
import java.util.List;

public class OpenWeatherForecastData extends OpenWeatherData {
    private long timestamp;

    public OpenWeatherForecastData(){}

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
