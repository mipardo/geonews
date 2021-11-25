package es.uji.geonews.model.database;

import android.content.Context;

import com.google.gson.JsonSyntaxException;

import es.uji.geonews.model.dao.UserDao;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;

public class DatabaseManager  {
    private final RemoteDBManager remoteDBManager;
    private final LocalDBManager localDBManager;

    public DatabaseManager(LocalDBManager localDBManager, RemoteDBManager remoteDBManager) {
        this.localDBManager = localDBManager;
        this.remoteDBManager = remoteDBManager;
    }

    public DatabaseManager(){
        remoteDBManager = new RemoteDBManager();
        localDBManager = new LocalDBManager();
    }


    public void loadAll(String userId, LocationManager locationManager, ServiceManager serviceManager) {
        // First we load the data from the local databse.
        // If any problem is find then we load the data from the remote db
        localDBManager.loadAll(userId, new Callback() {
            @Override
            public void onSuccess(UserDao userDao) {
                userDao.fillLocationManager(locationManager);
                userDao.fillServiceManager(serviceManager);
            }
            @Override
            public void onFailure(Exception e) {
                if (e instanceof JsonSyntaxException) {
                    //If we get this exception is the first time the app is running
                    return;
                }
                remoteDBManager.loadAll(userId, new Callback() {
                    @Override
                    public void onSuccess(UserDao userDao) {
                        userDao.fillLocationManager(locationManager);
                        userDao.fillServiceManager(serviceManager);
                        //localDBManager.saveAll().... todo: por que funciona????
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

    public void saveAll(String userId, LocationManager locationManager, ServiceManager serviceManager) {
        localDBManager.saveAll(userId, locationManager,serviceManager);
        remoteDBManager.saveAll(userId, locationManager, serviceManager);
    }

    public String getUserId(Context context) {
        return localDBManager.getUserId(context);
    }
}
