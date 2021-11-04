package es.uji.geonews.model.services;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import es.uji.geonews.model.Location;
import okhttp3.Request;
import okhttp3.Response;

public class OpenWeatherService extends Service implements AtmosphericInterface, WeatherInterface {
    public OpenWeatherService() {
        super("OpenWeather", "Wheather service");
        apiKey = "4002e2da22764a672b4a488d77b9b54a";
    }

    @Override
    public boolean validateLocation(Location location){
        String url = "https://api.openweathermap.org/data/2.5/weather?"
                + "lat=" + location.getGeographCoords().getLatitude()
                + "&lon=" + location.getGeographCoords().getLongitude()
                + "&appid=" + apiKey;
        Request request = new Request.Builder().url(url).build();
        final JSONObject jsonObject;

        try (Response response = client.newCall(request).execute()) {
            jsonObject = new JSONObject(response.body().string());
            if (jsonObject.getInt("cod") == 200){
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
}
