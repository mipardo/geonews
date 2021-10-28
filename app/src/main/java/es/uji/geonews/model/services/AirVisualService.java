package es.uji.geonews.model.services;

import java.time.LocalDate;

public class AirVisualService extends Service implements AtmosphericInterface, WeatherInterface {
    public AirVisualService(String serviceName, String serviceType, LocalDate activationDate) {
        super(serviceName, serviceType, activationDate);
    }
}
