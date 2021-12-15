package es.uji.geonews.model.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.data.OpenWeatherForecastData;
import es.uji.geonews.model.data.ServiceData;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import okhttp3.Request;
import okhttp3.Response;

public class OpenWeatherService extends ServiceHttp implements DataGetterStrategy {
    private static final String description = "OpenWeather es un servicio que proporciona datos " +
            "meteorológicos globales incluyendo datos meteorológicos actuales, previsiones, nowcasts " +
            "y datos meteorológicos históricos para cualquier ubicación geográfica.";

    public OpenWeatherService() {
        super(ServiceName.OPEN_WEATHER, description);
        apiKey = "4002e2da22764a672b4a488d77b9b54a";
        url = "api.openweathermap.org";
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
    public ServiceData getData(Location location) throws ServiceNotAvailableException {
        String url = "https://api.openweathermap.org/data/2.5/weather?"
                + "lat=" + location.getGeographCoords().getLatitude()
                + "&lon=" + location.getGeographCoords().getLongitude()
                + "&units=metric"
                + "&lang=es"
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

    @Override
    public List<ServiceData> getFutureData(Location location) throws ServiceNotAvailableException {
        String url = "https://api.openweathermap.org/data/2.5/forecast?"
                + "lat=" + location.getGeographCoords().getLatitude()
                + "&lon=" + location.getGeographCoords().getLongitude()
                + "&units=metric"
                + "&lang=es"
                + "&appid=" + apiKey;
        Request request = new Request.Builder().url(url).build();
        final JSONObject jsonObject;

        try (Response response = client.newCall(request).execute()) {
            jsonObject = new JSONObject(response.body().string());
            if (jsonObject.getInt("cod") == 200){
                return convertToOpenWeatherForecastFutureData(jsonObject);
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
        openWeatherLocationData.setIcon(jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon"));
        return openWeatherLocationData;
    }

    private List<ServiceData> convertToOpenWeatherForecastFutureData(JSONObject jsonObject) throws JSONException {
        List<ServiceData> forecastDataList = new ArrayList<>();
        JSONArray futureData = jsonObject.getJSONArray("list");
        for (int i = 0; i < futureData.length(); i++) {
            JSONObject actualObject = futureData.getJSONObject(i);
            OpenWeatherForecastData forecastData = new OpenWeatherForecastData();
            forecastData.setTimestamp(actualObject.getLong("dt"));
            forecastData.setActTemp(actualObject.getJSONObject("main").getDouble("temp"));
            forecastData.setMaxTemp(actualObject.getJSONObject("main").getDouble("temp_max"));
            forecastData.setMinTemp(actualObject.getJSONObject("main").getDouble("temp_min"));
            forecastData.setMain(actualObject.getJSONArray("weather").getJSONObject(0).getString("main"));
            forecastData.setDescription(actualObject.getJSONArray("weather").getJSONObject(0).getString("description"));
            forecastData.setIcon(actualObject.getJSONArray("weather").getJSONObject(0).getString("icon"));
            forecastDataList.add(forecastData);
        }
        return forecastDataList;
    }
}
