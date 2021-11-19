package es.uji.geonews.model.database;

import es.uji.geonews.model.dao.LocationDao;
import es.uji.geonews.model.dao.UserDao;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;

public class LocalDBManager implements DataBase{
    /**
     *  PABLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO: SHARED PREFERENCES
     */

    public LocalDBManager(){

    }

    @Override
    public void saveLocation(LocationDao locationDao) {

    }

    @Override
    public void saveFavLocation(LocationDao locationDao) {

    }

    @Override
    public void saveAll(int userId, LocationManager locationManager, ServiceManager serviceManager) {

    }


    @Override
    public UserDao loadData(int userId) {
        return null;
    }
}
