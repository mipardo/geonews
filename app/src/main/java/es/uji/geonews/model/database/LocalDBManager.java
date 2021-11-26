package es.uji.geonews.model.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.UUID;

import es.uji.geonews.model.dao.UserDao;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;


import androidx.test.platform.app.InstrumentationRegistry;


public class LocalDBManager implements DataBase{

    private final Gson json;
    private final SharedPreferences sharedPreferences;

    public LocalDBManager() {
        json = new Gson();
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        sharedPreferences = appContext.getSharedPreferences("LocalDB", Context.MODE_PRIVATE);

    }

    @Override
    public void saveAll(String userId, LocationManager locationManager, ServiceManager serviceManager) {
        //Crear Un conjunto de datos con to-do lo que queremos guardar de Sistema
        UserDao userDao = new UserDao(userId, locationManager, serviceManager);
        //Pasarlo a un Json
        String configuracionUser = json.toJson(userDao);
        //Guardarlo con Context y sharedpreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("configuracion", configuracionUser);

        editor.apply();
    }


    @Override
    public void loadAll(String userId, Callback callback) {
        //Cargar un conjunto de datos guardado en el sistema
        String configuracion = sharedPreferences.getString("configuracion","No existe la informacion");
        //Transformalo en la clase UserDao
        try {
            UserDao userDao = json.fromJson(configuracion, UserDao.class);
            callback.onSuccess(userDao);
        } catch (JsonSyntaxException exception) {
            callback.onFailure(exception);
        }
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
            editor.apply();
        }
        return userId;
    }

    public void removeUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("configuracion");
        editor.apply();
    }
}
