package es.uji.geonews.model.services;

public enum ServiceName {
    GEOCODE ("Geocode"),
    OPEN_WEATHER ("OpenWeather"),
    AIR_VISUAL ("AirVisual"),
    CURRENTS ("Currents"),
    GPS ("Gps");

    public final String name;

    ServiceName(String name){
        this.name = name;
    }
}
