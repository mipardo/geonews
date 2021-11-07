package es.uji.geonews.model.services;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.Location;
import okhttp3.Request;
import okhttp3.Response;

public class CurrentsService extends ServiceHttp implements DataGetterInterface {
    public CurrentsService() {
        super("Currents", "News service");
        apiKey = "uVh9kGUA3ZArfrYzCaLkX4iW6nR1vy2LMHwesz40aEY4OHaj";
    }

    public boolean validateLocation(Location location){
        String url = "https://api.currentsapi.services/v1/search?language=es&amp;"
                + "keywords=" + location.getPlaceName()
                + "&amp;apiKey=" + apiKey;

        Request request = new Request.Builder().url(url).build();
        final JSONObject jsonObject;

        try (Response response = client.newCall(request).execute()) {
            jsonObject = new JSONObject(response.body().string());
            if (jsonObject.getString("status").equals("ok")){
                return true;
            }
            return false;

        } catch (IOException | JSONException exception){
            return false;
        }
    }

    @Override
    public void checkConnection() {
        //TODO: This method should connect to the API to check if it is possible to connect
        isActive = true;
    }

    @Override
    public Data getData(Location location) {
        return null;
    }
}
