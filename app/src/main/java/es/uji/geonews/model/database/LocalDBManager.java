package es.uji.geonews.model.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import es.uji.geonews.model.dao.ServiceDataDao;
import es.uji.geonews.model.dao.UserDao;
import es.uji.geonews.model.data.ServiceData;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;


import androidx.test.platform.app.InstrumentationRegistry;


public class LocalDBManager implements DataBase{
    private final Gson json;
    private final SharedPreferences sharedPreferences;

    public LocalDBManager() {
        json = new Gson();
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        sharedPreferences = context.getSharedPreferences("LocalDB", Context.MODE_PRIVATE);
    }

    public LocalDBManager(Context context) {
        json = new Gson();
        sharedPreferences = context.getSharedPreferences("LocalDB", Context.MODE_PRIVATE);
    }

    @Override
    public void saveAll(String userId, LocationManager locationManager, ServiceManager serviceManager) {
        UserDao userDao = new UserDao(userId, locationManager, serviceManager);
        ServiceDataDao serviceDataDao = new ServiceDataDao(serviceManager.getOfflineData());
        String user = json.toJson(userDao);
        String offlineData = json.toJson(serviceDataDao);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("configuracion", user);
        editor.putString("offlineServiceData" , offlineData);
        editor.apply();
    }


    @Override
    public void loadAll(String userId, Callback callback) {
        String configuracion = sharedPreferences.getString("configuracion","No existe la informacion");
        String offlineServiceData = sharedPreferences.getString("offlineServiceData", "No existe informacion guardada de servicios");
        try {
            Type serviceDataType = new TypeToken<List<ServiceData>>() {}.getType();
            ServiceDataDao serviceDataDao = json.fromJson(offlineServiceData, serviceDataType);
            UserDao userDao = json.fromJson(configuracion, UserDao.class);
            callback.onSuccess(userDao, serviceDataDao);
        } catch (Exception exception) {
            callback.onFailure(exception);
        }
    }

    @Override
    public boolean isAvailable() {
        return true;
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
