package es.uji.geonews.model.database;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.dao.LocationDao;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;

public class DatabaseManager  {
    private final RemoteDBManager remoteDBManager;
    private final LocalDBManager localDBManager;

    public DatabaseManager(){
        remoteDBManager = new RemoteDBManager();
        localDBManager = new LocalDBManager();
    }

    public void saveLocation(Location location){
        LocationDao locationDao = new LocationDao(location);
        localDBManager.saveLocation(locationDao);
        remoteDBManager.saveLocation(locationDao);
    }

    public void saveFavLocation(Location location){
        LocationDao locationDao = new LocationDao(location);
        //localDBManager.removeLocation(locationDao);
        //remoteDBManager.removeLocation(locationDao);
        //localDBManager.saveFavLocation(locationDao);
        //remoteDBManager.saveFavLocation(locationDao);
    }

    public void loadData(LocationManager locationManager, ServiceManager serviceManager){
        // FIRST: Check which db has the most recent information.
        // SECOND: loadData from the most recent db
        // THIRD: Inject the data in locationManager and in ServiceManager
        // MAYBE: Return lastData of all Services for the view??
    }




}
