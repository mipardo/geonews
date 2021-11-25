package es.uji.geonews.model.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import es.uji.geonews.model.dao.UserDao;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;

public class RemoteDBManager implements DataBase {

    private FirebaseFirestore db;

    public RemoteDBManager(){
        db = FirebaseFirestore.getInstance();
    }



    @Override
    public void saveAll(String userId, LocationManager locationManager, ServiceManager serviceManager) {
        UserDao userDao = new UserDao(userId, locationManager, serviceManager);
        db.collection("users").document(String.valueOf(userId))
                .set(userDao)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("GeoNews", "Added userDao");
                    }
                });
    }

    @Override
    public void loadAll(String userId, Callback callback) {
        db.collection("users").document(String.valueOf(userId))
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserDao userDao = documentSnapshot.toObject(UserDao.class);
                callback.onSuccess(userDao);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e);
            }
        });
    }
}
