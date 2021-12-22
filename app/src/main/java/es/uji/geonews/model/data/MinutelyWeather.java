package es.uji.geonews.model.data;

public class MinutelyWeather {
    private long timestamp;
    private int precipitation;

    public MinutelyWeather() {}

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(int precipitation) {
        this.precipitation = precipitation;
    }
}
