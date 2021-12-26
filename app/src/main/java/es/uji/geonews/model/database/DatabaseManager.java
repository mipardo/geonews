package es.uji.geonews.model.database;

import android.content.Context;

import com.google.gson.JsonSyntaxException;

import es.uji.geonews.model.daos.UserDao;
import es.uji.geonews.model.daos.UserDaoConverter;
import es.uji.geonews.model.exceptions.DatabaseNotAvailableException;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;

public class DatabaseManager  {
    private final RemoteDBManager remoteDBManager;
    private final LocalDBManager localDBManager;

    public DatabaseManager(LocalDBManager localDBManager, RemoteDBManager remoteDBManager) {
        this.localDBManager = localDBManager;
        this.remoteDBManager = remoteDBManager;
    }

    public DatabaseManager(Context context) {
        remoteDBManager = new RemoteDBManager();
        localDBManager = new LocalDBManager(context);
    }


    public void loadAll(String userId, LocationManager locationManager, ServiceManager serviceManager) throws DatabaseNotAvailableException {
        if (!localDBManager.isAvailable() && !remoteDBManager.isAvailable()) throw new DatabaseNotAvailableException();
        // First we load the data from the local databse.
        // If any problem is find then we load the data from the remote db
        localDBManager.loadAll(userId, new Callback() {
            @Override
            public void onSuccess(UserDao userDao) {
                UserDaoConverter.fillLocationManager(locationManager, userDao);
                UserDaoConverter.fillServiceManager(serviceManager, userDao);
            }
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
                if (e instanceof JsonSyntaxException) {
                    //If we get this exception is the first time the app is running
                    return;
                } else {
                    e.printStackTrace();
                }
                remoteDBManager.loadAll(userId, new Callback() {
                    @Override
                    public void onSuccess(UserDao userDao) {
                        UserDaoConverter.fillLocationManager(locationManager, userDao);
                        UserDaoConverter.fillServiceManager(serviceManager, userDao);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        // Local and remote db is not available or empty
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public void loadRemoteState(String importCode, LocationManager locationManager, ServiceManager serviceManager)
            throws DatabaseNotAvailableException {
        if (! remoteDBManager.isAvailable()) throw new DatabaseNotAvailableException();
        String userId = remoteDBManager.getUserIdFromSharedCodes(importCode);
        remoteDBManager.loadAll(userId, new Callback() {
            @Override
            public void onSuccess(UserDao userDao) {
                if (userDao != null){
                    UserDaoConverter.fillLocationManager(locationManager, userDao);
                    UserDaoConverter.fillServiceManager(serviceManager, userDao);
                }
            }
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });

    }

    public void saveAll(String userId, LocationManager locationManager, ServiceManager serviceManager) {
        if (localDBManager.isAvailable())
            localDBManager.saveAll(userId, locationManager,serviceManager);
        if (remoteDBManager.isAvailable())
            remoteDBManager.saveAll(userId, locationManager, serviceManager);
    }

    public String getUserId(Context context) {
        return localDBManager.getUserId(context);
    }

    public void removeUser(String remoteUserId) {
        localDBManager.removeUser();
        remoteDBManager.removeUser(remoteUserId);
    }

    public String saveGeneratedCode(String userId) {
        return remoteDBManager.saveGenerateCode(userId);
    }

    public void loadAllSharedCodes() {
        remoteDBManager.loadAllSharedCodes();
    }

    public boolean checkImportCode(String importCode) {
        return remoteDBManager.checkImportCode(importCode);
    }
}
