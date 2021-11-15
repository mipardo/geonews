package es.uji.geonews.model.data;

/* AQI: Air quality index
    aqiUs: Air quality index based on EPA standard (US)
        De 0-50     Good
        De 51-100   Acceptable/Moderate
        De 101-150  Unhealthy for sensitive groups of population
        De 151-200  Unhealthy
        De 201-250  Very Unhealthy
        De 251-300  Hazardous
    aqiCn: Air quality index based on MEP standard (CHINA) NO CAMBIA???
        De 0-50     Good
        De 51-100   Acceptable/Moderate
        De 101-150  Unhealthy for sensitive groups of population
        De 151-200  Unhealthy
        De 201-250  Very Unhealthy
        De 251-300  Hazardous
* */

public class AirVisualData {
    private int temperature;        // In celsius
    private int pressure;           // In hPa
    private int humidity;           // In %
    private float windSpeed;        // In m/s
    private int windDirection;      // In angle (N=0, E=90, S=180, W=270)
    private float weatherIcon;

    private int aqiUs;              // Air quality index based on the EPA standard
    private int aqiCn;              // Air quality index based on the MEP standard
    private String mainUs;          // Main pollutant for US AQI
    private String mainCn;          // Main pollutant for Chinese AQI


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

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    public float getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(float weatherIcon) {
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

