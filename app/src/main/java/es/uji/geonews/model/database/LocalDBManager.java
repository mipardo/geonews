package es.uji.geonews.model.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.firestore.auth.User;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.util.UUID;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.dao.LocationDao;
import es.uji.geonews.model.dao.UserDao;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;


import androidx.test.platform.app.InstrumentationRegistry;


public class LocalDBManager implements DataBase{

    Gson json;
    public LocalDBManager() {
        json= new Gson();
    }

    @Override
    public void saveLocation(LocationDao locationDao) {

    }

    @Override
    public void saveFavLocation(LocationDao locationDao) {

    }

    @Override
    public void saveAll(int userId, LocationManager locationManager, ServiceManager serviceManager) {
        //Crear Un conjunto de datos con to-do lo que queremos guardar de Sistema
        UserDao userDao = new UserDao(userId,locationManager,serviceManager);
        //Pasarlo a un Json
        String configuracionUser = json.toJson(userDao);
        //Guardarlo con Context y sharedpreferences
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("LocalDB", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putString("configuracion",configuracionUser);

        editor.commit();
    }


    @Override
    public UserDao loadData(int userId) {
        //Cargar un conjunto de datos guardado en el sistema
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("LocalDB", Context.MODE_PRIVATE);
        String configuracion = sharedPreferences.getString("configuracion","No existe la informacion");
        //Transformalo en la clase UserDao
        UserDao userDao = json.fromJson(configuracion,UserDao.class);
        return userDao;
    }

    @Override
    public void loadAll(int userId, Callback callback) {

    }

    public String getUserId(Context context) {
        String userId;
        SharedPreferences sharedPreferences = context.getSharedPreferences("LocalDB", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);
        //Generador de IDs aleatorio para los usuarios
        if (userId == null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            userId = UUID.randomUUID().toString();
            editor.putString("userId", userId);
            editor.commit();
        }
        return userId;
    }
}
