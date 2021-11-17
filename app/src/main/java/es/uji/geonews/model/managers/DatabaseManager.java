package es.uji.geonews.model.managers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.dao.LocationDao;

public class DatabaseManager {
    private FirebaseFirestore db;

    public DatabaseManager(){
        db = FirebaseFirestore.getInstance();
    }

    public void saveData(Location location) {
        LocationDao locationDao = new LocationDao(location);
        db.collection("localizaciones")
                .add(locationDao)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("SPIKE", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("SPIKE", "Error adding document", e);
                    }
                });
    }

}
