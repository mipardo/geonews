package es.uji.geonews.model.Services;

import java.util.List;

import es.uji.geonews.model.Services.Service;

public class ServiceManager {
    private List<Service> services;

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }
}
