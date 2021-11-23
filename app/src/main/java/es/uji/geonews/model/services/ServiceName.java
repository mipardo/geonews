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

    public static ServiceName fromString (String text) {
        for (ServiceName serviceName : ServiceName.values()) {
            if (serviceName.name.equalsIgnoreCase(text)) {
                return serviceName;
            }
        }
        throw new IllegalArgumentException("No se ha encontrado una constante con el texto " + text);
    }
}
