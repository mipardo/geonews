package es.uji.geonews.model.services;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.ServiceData;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;

public interface DataGetterStrategy {

    ServiceData getData(Location location) throws ServiceNotAvailableException;
}
