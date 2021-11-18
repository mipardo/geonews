package es.uji.geonews.model.managers;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.dao.LocationDao;

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
