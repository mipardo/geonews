package es.uji.geonews.model.data;


import android.graphics.drawable.Drawable;

import es.uji.geonews.R;

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

    public String getAqiMainText(){
        String info = "";
        if (aqiUs < 51) info = "Bueno";
        else if (aqiUs < 101) info = "Moderado";
        else if (aqiUs < 151) info = "Dañino para grupos sensibles";
        else if (aqiUs < 201) info = "Dañino";
        else if (aqiUs < 251) info = "Muy dañino";
        else info = "Muy peligroso";
        return info;
    }

    public String getAqiText(){
        String info = "";
        if (aqiUs < 51) info = "La calidad del aire es buena y no supone ningún riesgo para la salud de las personas";
        else if (aqiUs < 101) info = "La calidad del aire es aceptable, aunque puede tener pequeños efectos en la salud en personas con problemas respiratorios";
        else if (aqiUs < 151) info = "El público general no se ve afectado, aunque sí puede suponer un riesgo para la salud de ciertos grupos sensibles";
        else if (aqiUs < 201) info = "La calidad del aire no es buena y por tanto la salud de muchas personas puede verse afectada";
        else if (aqiUs < 251) info = "El índice de calidad del aire es muy elevedo, esto expresa que la calidad del aire es muy baja y por tanto afecta gravemente la salud de las personas";
        else info = "Todas las personas afectadas pueden experimentar graves problemas de salud debido a la calidad del aire";
        return info;
    }

    public int getAqiImage(){
        int image;
        if (aqiUs < 51) image = R.drawable.aqi1;
        else if (aqiUs < 101) image = R.drawable.aqi2;
        else if (aqiUs < 151) image = R.drawable.aqi3;
        else if (aqiUs < 201) image = R.drawable.aqi4;
        else if (aqiUs < 251) image = R.drawable.aqi5;
        else image = R.drawable.aqi6;
        return image;
    }

}

