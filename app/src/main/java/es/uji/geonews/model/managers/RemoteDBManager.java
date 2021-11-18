package es.uji.geonews.model.managers;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.dao.LocationDao;
import es.uji.geonews.model.services.Service;

public class RemoteDBManager implements DataBase {
    private FirebaseFirestore db;

    public RemoteDBManager(){
        db = FirebaseFirestore.getInstance();
    }

    public void saveData(Location location) {
        LocationDao locationDao = new LocationDao(location);
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
    public void saveData(Service service) {

    }
}
