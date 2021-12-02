package es.uji.geonews.model.services;

import java.io.IOException;
import java.net.InetAddress;

import es.uji.geonews.model.Location;
import okhttp3.OkHttpClient;

public abstract class ServiceHttp extends Service{
    protected transient OkHttpClient client;
    protected String apiKey;
    protected String url;

    public ServiceHttp(ServiceName serviceName, String serviceType) {
        super(serviceName, serviceType);
        isActive = true;
        initializeClient();
    }

    @Override
    public boolean isAvailable(){
        return isActive && checkConnection();
    }

    protected void initializeClient() {
        this.client = new OkHttpClient();
    }

    public abstract boolean validateLocation(Location location);

    public boolean checkConnection() {
        return true;
        /*
        try {
            InetAddress inet = InetAddress.getByName(url);
            return inet.isReachable(2000);
        } catch (IOException e) {
            return false;
        }

         */
    }
}
