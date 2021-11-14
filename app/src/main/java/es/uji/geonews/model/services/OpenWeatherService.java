package es.uji.geonews.model.services;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import okhttp3.Request;
import okhttp3.Response;

public class OpenWeatherService extends ServiceHttp implements DataGetterStrategy {
    public OpenWeatherService() {
        super(ServiceName.OPEN_WEATHER, "Wheather service");
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
    public boolean checkConnection() {
        //TODO: This method should connect to the API to check if it is possible to connect
        // ping -c1 api.openweathermap.org //Yo creo que deberia funcionar
        return true;
    }

    @Override
    public Data getData(Location location) throws ServiceNotAvailableException {
        String url = "https://api.openweathermap.org/data/2.5/weather?"
                + "q=" + location.getPlaceName()
                + "&appid=" + apiKey;
        Request request = new Request.Builder().url(url).build();
        final JSONObject jsonObject;

        try (Response response = client.newCall(request).execute()) {
            jsonObject = new JSONObject(response.body().string());
            if (jsonObject.getInt("cod") == 200){
                OpenWeatherData openWeatherLocationData = new OpenWeatherData();
                openWeatherLocationData.setActTemp(jsonObject.getJSONObject("main").getDouble("temp"));
                openWeatherLocationData.setMaxTemp(jsonObject.getJSONObject("main").getDouble("temp_max"));
                openWeatherLocationData.setMinTemp(jsonObject.getJSONObject("main").getDouble("temp_min"));
                openWeatherLocationData.setMain(jsonObject.getJSONArray("weather").getJSONObject(0).getString("main"));
                openWeatherLocationData.setDescription(jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"));
                openWeatherLocationData.setIcon(jsonObject.getJSONArray("weather").getJSONObject(0).getString("main"));
                return openWeatherLocationData;
            }
            return null;

        } catch (IOException | JSONException exception){
            throw new ServiceNotAvailableException();
        }
    }
}
