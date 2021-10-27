package es.uji.geonews.model.Services;

import java.util.Date;

public class AirVisualService extends Service implements AtmosphericInterface, WeatherInterface {
    public AirVisualService(String serviceName, String serviceType, Date activationDate) {
        super(serviceName, serviceType, activationDate);
    }
}
