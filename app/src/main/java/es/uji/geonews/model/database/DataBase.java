package es.uji.geonews.model.database;

import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;

public interface DataBase {
    void saveAll(String userId, LocationManager locationManager, ServiceManager serviceManager);
    void loadAll(String userId, Callback callback);
    boolean isAvailable();
}
