package es.uji.geonews.model.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
    /**
     *  PABLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO: SHARED PREFERENCES
     */
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

        GeographCoords geographCoords = new GeographCoords(1.0,2.0);
        Location location = new Location(1,"castellon",geographCoords ,LocalDate.now());
        // si le paso el location manager no tira XD con una location si
        String configuracion = json.toJson(location);
        // el contexto F

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("LocalDB", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putString("configuracion",configuracion);

        editor.commit();
    }


    @Override
    public UserDao loadData(int userId) {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("LocalDB", Context.MODE_PRIVATE);
        String configuracion = sharedPreferences.getString("configuracion","No existe la informacion");

        Location lc = json.fromJson(configuracion,Location.class);
        UserDao userDao= new UserDao();
        return userDao;
    }

    @Override
    public void loadAll(int userId, Callback callback) {

    }

    public String getUserId(Context context) {
        String userId;
        SharedPreferences sharedPreferences = context.getSharedPreferences("LocalDB", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);
        if (userId == null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            userId = UUID.randomUUID().toString();
            editor.putString("userId", userId);
            editor.commit();
        }
        return userId;
    }
}
