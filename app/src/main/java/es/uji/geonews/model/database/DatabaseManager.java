package es.uji.geonews.model.database;

import es.uji.geonews.model.Location;

public class DatabaseManager {
    private final RemoteDBManager remoteDBManager;
    private final LocalDBManager localDBManager;

    public DatabaseManager(){
        remoteDBManager = new RemoteDBManager();
        localDBManager = new LocalDBManager();
    }

    public void saveData(Location location){
        remoteDBManager.saveData(location);
        localDBManager.saveData(location);
    }



}
