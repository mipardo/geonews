package es.uji.geonews.model.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import es.uji.geonews.model.daos.UserDao;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;

public class RemoteDBManager implements DataBase {

    private final FirebaseFirestore db;
    private final HashMap<String, String> sharedCodes;

    public RemoteDBManager(){
        db = FirebaseFirestore.getInstance();
        sharedCodes = new HashMap<>();
    }

    @Override
    public void saveAll(String userId, LocationManager locationManager, ServiceManager serviceManager) {
        UserDao userDao = new UserDao(userId, locationManager, serviceManager);
        db.collection("users").document(String.valueOf(userId)).set(userDao);
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

    @Override
    public boolean isAvailable() {
        try {
            InetAddress inet = InetAddress.getByName("firebase.google.com");
            return inet.isReachable(2000);
        } catch (IOException e) {
            return false;
        }
    }

    public void removeUser(String remoteUserId) {
        db.collection("users").document(String.valueOf(remoteUserId)).delete();
    }

    public String saveGenerateCode(String userId) {
        String code = createCode();
        Map<String, String> data = new HashMap<>();
        data.put("userId", userId);
        db.collection("share_codes").document(code).set(data);
        return code;
    }

    private String createCode() {
        String[] name = {
                "nave", "cometa", "meteorito", "avestruz", "ballena", "castor", "zebra", "pingüino", "dromedario", "camello", "rana",
                "serpiente", "sapo", "puercoespín", "pikachu", "planeta", "bisonte", "canario",
                "frodo", "bolsón", "sauron", "aragorn", "legolas", "gimli", "galadriel", "eowin", "nazgul", "gandalf", "gothmog",
                "tom", "bombadil", "kinton"
        };

        String[] surname = {
                "azul", "rojo", "amarillo", "verde", "alto", "bajo", "feo", "gordo", "elfo", "bueno", "malo",
                "pequeño", "grande", "naranja", "morado", "violeta", "rosa", "gris", "negro", "blanco", "marrón",
                "rápido", "lento", "rácano", "egoista", "ávaro", "rico", "pobre", "simpático", "triste", "alegre",
                "oscuro", "claro"
        };
        int r1 = ThreadLocalRandom.current().nextInt(0, name.length);
        int r2 = ThreadLocalRandom.current().nextInt(0, surname.length);
        return name[r1] + "_" + surname[r2];
    }

    public void loadAllSharedCodes() {
        db.collection("share_codes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    sharedCodes.put(document.getId(), document.getString("userId"));
                }
            }
        });
    }

    public boolean checkImportCode(String importCode) {
        return sharedCodes.containsKey(importCode);
    }

    public String getUserIdFromSharedCodes(String importCode) {
        return sharedCodes.get(importCode);
    }
}
