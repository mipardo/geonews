package es.uji.geonews.model.Services;

import java.util.Date;

public abstract class Service {
    private String serviceName;
    private String serviceType;
    private Date activationDate;

    public Service(String serviceName, String serviceType, Date activationDate) {
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

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }
}
