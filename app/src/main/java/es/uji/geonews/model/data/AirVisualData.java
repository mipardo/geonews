package es.uji.geonews.model.data;


public class AirVisualData extends ServiceData {
    private int temperature;        // In celsius
    private int pressure;           // In hPa
    private int humidity;           // In %
    private double windSpeed;        // In m/s
    private int windDirection;      // In angle (N=0, E=90, S=180, W=270)
    private String weatherIcon;

    private int aqiUs;              // Air quality index based on the EPA standard
    private int aqiCn;              // Air quality index based on the MEP standard
    private String mainUs;          // Main pollutant for US AQI
    private String mainCn;          // Main pollutant for Chinese AQI

    public AirVisualData (){}

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public int getAqiUs() {
        return aqiUs;
    }

    public void setAqiUs(int aqiUs) {
        this.aqiUs = aqiUs;
    }

    public int getAqiCn() {
        return aqiCn;
    }

    public void setAqiCn(int aqiCn) {
        this.aqiCn = aqiCn;
    }

    public String getMainUs() {
        return mainUs;
    }

    public void setMainUs(String mainUs) {
        this.mainUs = mainUs;
    }

    public String getMainCn() {
        return mainCn;
    }

    public void setMainCn(String mainCn) {
        this.mainCn = mainCn;
    }

}

