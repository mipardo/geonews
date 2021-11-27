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
        url = "api.openweathermap.org";;
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
            return jsonObject.getInt("cod") == 200;

        } catch (IOException | JSONException exception){
            return false;
        }
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
                return convertToOpenWeatherData(jsonObject);
            }
            return null;

        } catch (IOException | JSONException exception){
            throw new ServiceNotAvailableException();
        }
    }

    private OpenWeatherData convertToOpenWeatherData(JSONObject jsonObject) throws JSONException {
        OpenWeatherData openWeatherLocationData = new OpenWeatherData();
        openWeatherLocationData.setActTemp(jsonObject.getJSONObject("main").getDouble("temp"));
        openWeatherLocationData.setMaxTemp(jsonObject.getJSONObject("main").getDouble("temp_max"));
        openWeatherLocationData.setMinTemp(jsonObject.getJSONObject("main").getDouble("temp_min"));
        openWeatherLocationData.setMain(jsonObject.getJSONArray("weather").getJSONObject(0).getString("main"));
        openWeatherLocationData.setDescription(jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"));
        openWeatherLocationData.setIcon(jsonObject.getJSONArray("weather").getJSONObject(0).getString("main"));
        return openWeatherLocationData;
    }
}
