package es.uji.geonews.model.services;

import java.time.LocalDate;

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


    public String getServiceType() {
        return serviceType;
    }


    public LocalDate getActivationDate() {
        return activationDate;
    }

    public void deactivate() {
        isActive = false;
    }

}
