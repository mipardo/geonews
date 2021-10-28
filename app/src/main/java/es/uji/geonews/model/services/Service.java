package es.uji.geonews.model.services;

import java.time.LocalDate;

public abstract class Service {
    private String serviceName;
    private String serviceType;
    private LocalDate activationDate;

    public Service(String serviceName, String serviceType, LocalDate activationDate) {
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.activationDate = activationDate;
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
}
