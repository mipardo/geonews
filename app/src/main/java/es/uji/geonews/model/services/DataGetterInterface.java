package es.uji.geonews.model.services;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;

public interface DataGetterInterface {
    Data getData(Location location) throws ServiceNotAvailableException;
}
