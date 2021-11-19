package es.uji.geonews.model.database;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.dao.LocationDao;
import es.uji.geonews.model.dao.UserDao;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.Service;

public interface DataBase {
    void saveLocation(LocationDao locationDao);
    void saveFavLocation(LocationDao locationDao);
    UserDao loadData(int userId);
}
