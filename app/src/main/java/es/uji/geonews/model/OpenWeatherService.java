package es.uji.geonews.model;

import java.util.Date;

public class OpenWeatherService extends Service implements AtmosphericInterface, WeatherInterface {
    public OpenWeatherService(String serviceName, String serviceType, Date activationDate) {
        super(serviceName, serviceType, activationDate);
    }
}
