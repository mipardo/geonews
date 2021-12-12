package es.uji.geonews.model.services;

public enum ServiceName {
    GEOCODE ("Geocode", "Coordenadas"),
    OPEN_WEATHER ("OpenWeather", "Tiempo"),
    AIR_VISUAL ("AirVisual", "Calidad del aire"),
    CURRENTS ("Currents", "Noticias"),
    GPS ("Gps", "Gps");

    public final String name;
    public final String label;

    ServiceName(String name, String label){
        this.name = name;
        this.label = label;
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
