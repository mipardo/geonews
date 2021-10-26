package es.uji.geonews.model;

import java.util.Date;

public class CurrentsService extends Service implements NewsInterface {
    public CurrentsService(String serviceName, String serviceType, Date activationDate) {
        super(serviceName, serviceType, activationDate);
    }
}
