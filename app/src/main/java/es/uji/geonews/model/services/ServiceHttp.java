package es.uji.geonews.model.services;

import es.uji.geonews.model.Location;
import okhttp3.OkHttpClient;

public abstract class ServiceHttp extends Service{
    protected final OkHttpClient client;
    protected String apiKey;

    public ServiceHttp(String serviceName, String serviceType) {
        super(serviceName, serviceType);
        this.client = new OkHttpClient();
        checkConnection(); //isActive = ckeckConnection();
    }

    public abstract boolean validateLocation(Location location);

    public abstract void checkConnection();
}
