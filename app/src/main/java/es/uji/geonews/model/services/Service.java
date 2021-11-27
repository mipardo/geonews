package es.uji.geonews.model.services;

import java.time.LocalDate;

public class Service {
    private ServiceName serviceName;
    private String description;
    private String activationDate;
    protected boolean isActive;

    public Service() {}

    public Service(ServiceName serviceName, String serviceType) {
        this.serviceName = serviceName;
        this.description = serviceType;
        this.activationDate = LocalDate.now().toString();
    }

    public boolean isAvailable(){
        return isActive;
    }

    public void deactivate() {
        isActive = false;
        activationDate = "";
    }

    public void activate() {
        isActive = true;
        activationDate = LocalDate.now().toString();
    }

    public boolean isActive(){
        return isActive;
    }

    public void setServiceName(ServiceName serviceName) {
        this.serviceName = serviceName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActivationDate(String activationDate) {
        this.activationDate = activationDate;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public ServiceName getServiceName() {
        return serviceName;
    }

    public String getDescription() {
        return description;
    }

    public String getActivationDate() {
        return activationDate;
    }
}
