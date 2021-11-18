package es.uji.geonews.model.database;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.services.Service;

public interface DataBase {
    void saveData(Location location);
    void saveData(Service service);
}
