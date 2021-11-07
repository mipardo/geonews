package es.uji.geonews.model.services;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.OpenWeatherLocationData;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import okhttp3.Request;
import okhttp3.Response;

public class AirVisualService extends ServiceHttp {

    public AirVisualService() {
        super("AirVisual", "Air cuality descritption");
        apiKey = "bd76ecb7-90bb-4f78-ad71-8107453e8890";
    }

    @Override
    public boolean validateLocation(Location location) {
        String url = "http://api.airvisual.com/v2/nearest_city?"
                + "lat=" + location.getGeographCoords().getLatitude()
                + "&lon=" + location.getGeographCoords().getLongitude()
                + "&key=" + apiKey;
        Request request = new Request.Builder().url(url).build();
        final JSONObject jsonObject;

        try (Response response = client.newCall(request).execute()) {
            jsonObject = new JSONObject(response.body().string());
            if (jsonObject.getString("status").equals("success")){
                return true;
            }
            return false;

        } catch (IOException | JSONException exception){
            //TODO: Al validar la ubicacion, si no tenemos conexion no es
            // mejor lanzar ServiceNotAvailableException ??
            return false;
        }
    }

    @Override
    public void checkConnection() {
        //TODO: This method should connect to the API to check if it is possible to connect
        isActive = true;
    }

    @Override
    public OpenWeatherLocationData getDataFrom(Location location) {
        return null;
    }
}
