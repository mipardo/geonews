package es.uji.geonews.model.database;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.dao.LocationDao;
import es.uji.geonews.model.dao.UserDao;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.Service;

public class LocalDBManager implements DataBase{

    public LocalDBManager(){

    }

    @Override
    public void saveLocation(LocationDao locationDao) {

    }

    @Override
    public void saveFavLocation(LocationDao locationDao) {

    }


    @Override
    public UserDao loadData(int userId) {
        return null;
    }
}
