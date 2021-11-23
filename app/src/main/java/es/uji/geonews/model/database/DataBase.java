package es.uji.geonews.model.database;

import es.uji.geonews.model.dao.LocationDao;
import es.uji.geonews.model.dao.UserDao;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;

public interface DataBase {
    void saveLocation(LocationDao locationDao);
    void saveFavLocation(LocationDao locationDao);
    void saveAll(int userId, LocationManager locationManager, ServiceManager serviceManager);
    UserDao loadData(int userId);
    void loadAll(int userId, Callback callback);
}
