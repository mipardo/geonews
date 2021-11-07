package es.uji.geonews.model.services;

import java.time.LocalDate;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.OpenWeatherLocationData;
import es.uji.geonews.model.ServiceLocationData;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import okhttp3.OkHttpClient;

public abstract class Service {
    private String serviceName;
    private String serviceType;
    private LocalDate activationDate;
    protected boolean isActive;

    public Service(String serviceName, String serviceType) {
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.activationDate = LocalDate.now();
    }

    public boolean isAvailable(){
        return isActive;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public LocalDate getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(LocalDate activationDate) {
        this.activationDate = activationDate;
    }

    public void deactivate() {
        isActive = false;
    }

    public abstract ServiceLocationData getDataFrom(Location location) throws ServiceNotAvailableException;
}
