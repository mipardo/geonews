package es.uji.geonews.model.database;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonSyntaxException;

import es.uji.geonews.model.dao.ServiceDataDao;
import es.uji.geonews.model.dao.UserDao;
import es.uji.geonews.model.dao.UserDaoConverter;
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
            public void onSuccess(UserDao userDao, ServiceDataDao serviceDataDao) {
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
                    public void onSuccess(UserDao userDao, ServiceDataDao serviceDataDao) {
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

    public void loadRemoteState(String userId, LocationManager locationManager, ServiceManager serviceManager)
            throws DatabaseNotAvailableException {
        if (! remoteDBManager.isAvailable()) throw new DatabaseNotAvailableException();
        remoteDBManager.loadAll(userId, new Callback() {
            @Override
            public void onSuccess(UserDao userDao, ServiceDataDao serviceDataDao) {
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
        Log.e("SAVEALL", "DB local: " + localDBManager.isAvailable());
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
}
