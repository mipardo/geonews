package es.uji.geonews.model.services;

import java.io.IOException;
import java.net.InetAddress;

import es.uji.geonews.model.Location;
import okhttp3.OkHttpClient;

public abstract class ServiceHttp extends Service{
    protected transient final OkHttpClient client;
    protected String apiKey;
    protected String url;

    public ServiceHttp(ServiceName serviceName, String serviceType) {
        super(serviceName, serviceType);
        this.client = new OkHttpClient();
        isActive = checkConnection();
    }

    @Override
    public boolean isAvailable(){
        return isActive && checkConnection();
    }

    public abstract boolean validateLocation(Location location);

    public boolean checkConnection() {
        try {
            InetAddress inet = InetAddress.getByName(url);
            return inet.isReachable(2000);
        } catch (IOException e) {
            return false;
        }
    }
}
