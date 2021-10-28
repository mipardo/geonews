package es.uji.geonews.model.services;

import java.time.LocalDate;

public class OpenWeatherService extends Service implements AtmosphericInterface, WeatherInterface {
    public OpenWeatherService(String serviceName, String serviceType, LocalDate activationDate) {
        super(serviceName, serviceType, activationDate);
    }
}
