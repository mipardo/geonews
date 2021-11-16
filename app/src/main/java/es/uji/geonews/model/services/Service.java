package es.uji.geonews.model.services;

import java.time.LocalDate;

public abstract class Service {
    private ServiceName serviceName;
    private String description;
    private LocalDate activationDate;
    protected boolean isActive;

    public Service(ServiceName serviceName, String serviceType) {
        this.serviceName = serviceName;
        this.description = serviceType;
        this.activationDate = LocalDate.now();
    }

    public boolean isAvailable(){
        return isActive;
    }

    public ServiceName getServiceName() {
        return serviceName;
    }


    public String getDescription() {
        return description;
    }


    public LocalDate getActivationDate() {
        return activationDate;
    }

    public void deactivate() {
        isActive = false;
    }

    public boolean activate() {
        if (!isActive) {
            isActive = true;
            return true;
        }
        return false;
    }
}
