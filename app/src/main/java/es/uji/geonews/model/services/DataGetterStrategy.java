package es.uji.geonews.model.services;

import java.util.List;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;

public interface DataGetterStrategy {
    Data getData(Location location) throws ServiceNotAvailableException;
    List<Data> getFutureData(Location location) throws ServiceNotAvailableException;
}
