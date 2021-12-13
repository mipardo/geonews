package es.uji.geonews.model.data;

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
