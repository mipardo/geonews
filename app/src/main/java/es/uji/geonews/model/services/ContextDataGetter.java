package es.uji.geonews.model.services;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;

public class ContextDataGetter {
    DataGetterStrategy service;

    public ContextDataGetter() {
        super();
    }

    public void setService(DataGetterStrategy service) {
        this.service = service;
    }

    public Data getData(Location location) throws ServiceNotAvailableException {
        if (service != null) {
            return service.getData(location);
        }
        return null;
    }
}
