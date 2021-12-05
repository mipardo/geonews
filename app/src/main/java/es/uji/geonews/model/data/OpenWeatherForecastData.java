package es.uji.geonews.model.data;

import java.util.List;

public class OpenWeatherForecastData extends OpenWeatherData {
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
