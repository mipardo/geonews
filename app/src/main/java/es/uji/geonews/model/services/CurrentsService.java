package es.uji.geonews.model.services;

import java.time.LocalDate;

public class CurrentsService extends Service implements NewsInterface {
    public CurrentsService(String serviceName, String serviceType, LocalDate activationDate) {
        super(serviceName, serviceType, activationDate);
    }
}
