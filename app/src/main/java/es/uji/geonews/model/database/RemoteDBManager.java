package es.uji.geonews.model.database;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import es.uji.geonews.model.dao.LocationDao;
import es.uji.geonews.model.dao.UserDao;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;

public class RemoteDBManager implements DataBase {
    private FirebaseFirestore db;

    public RemoteDBManager(){
        db = FirebaseFirestore.getInstance();
    }


    @Override
    public void saveLocation(LocationDao locationDao) {
        db.collection("locations").document(locationDao.getPlaceName())
                .set(locationDao)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("GeoNews", "Added location");
                    }
                });
    }

    @Override
    public void saveFavLocation(LocationDao locationDao) {

    }

    @Override
    public void saveAll(int userId, LocationManager locationManager, ServiceManager serviceManager) {
        UserDao userDao = new UserDao(userId, locationManager, serviceManager);
        db.collection("locations").document(String.valueOf(userId))
                .set(userDao)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("GeoNews", "Added userDao");
                    }
                });
    }

    @Override
    public UserDao loadData(int userId) {
        return null;
    }
}
