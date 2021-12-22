package es.uji.geonews.model.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.DailyWeather;
import es.uji.geonews.model.data.HourlyWeather;
import es.uji.geonews.model.data.MinutelyWeather;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.data.ServiceData;
import es.uji.geonews.model.data.Weather;
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
        String url = "https://api.openweathermap.org/data/2.5/onecall?"
                + "lat=" + location.getGeographCoords().getLatitude()
                + "&lon=" + location.getGeographCoords().getLongitude()
                + "&units=metric"
                + "&lang=es"
                + "&appid=" + apiKey;
        Request request = new Request.Builder().url(url).build();
        final JSONObject jsonObject;

        try (Response response = client.newCall(request).execute()) {
            jsonObject = new JSONObject(response.body().string());
            if (!jsonObject.has("cod")){
                return convertToOpenWeatherData(jsonObject);
            }
            return null;

        } catch (IOException | JSONException exception) {
            exception.printStackTrace();
            throw new ServiceNotAvailableException();
        }
    }

    private OpenWeatherData convertToOpenWeatherData(JSONObject jsonObject) throws JSONException {
        OpenWeatherData openWeatherLocationData = new OpenWeatherData();

        Weather currentWeather = new Weather();
        currentWeather.setTimestamp(jsonObject.getJSONObject("current").getLong("dt"));
        currentWeather.setSunrise(jsonObject.getJSONObject("current").getLong("sunrise"));
        currentWeather.setSunset(jsonObject.getJSONObject("current").getLong("sunset"));
        currentWeather.setCurrentTemp(jsonObject.getJSONObject("current").getDouble("temp"));
        currentWeather.setFeelsLikeTemp(jsonObject.getJSONObject("current").getDouble("feels_like"));
        currentWeather.setHumidity(jsonObject.getJSONObject("current").getInt("humidity"));
        currentWeather.setUvi(jsonObject.getJSONObject("current").getDouble("uvi"));
        currentWeather.setClouds(jsonObject.getJSONObject("current").getInt("clouds"));
        currentWeather.setVisibility(jsonObject.getJSONObject("current").getInt("visibility"));
        currentWeather.setMain(jsonObject.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("main"));
        currentWeather.setDescription(jsonObject.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("description"));
        currentWeather.setIcon(jsonObject.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("icon"));
        openWeatherLocationData.setCurrentWeather(currentWeather);

        List<MinutelyWeather> minutelyWeatherList = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("minutely");
        for (int i = 0; i < jsonArray.length(); i++) {
            MinutelyWeather minutelyWeather = new MinutelyWeather();
            minutelyWeather.setTimestamp(jsonArray.getJSONObject(i).getLong("dt"));
            minutelyWeather.setPrecipitation(jsonArray.getJSONObject(i).getInt("precipitation"));
            minutelyWeatherList.add(minutelyWeather);
        }
        openWeatherLocationData.setMinutelyWeatherList(minutelyWeatherList);

        List<HourlyWeather> hourlyWeatherList = new ArrayList<>();
        jsonArray = jsonObject.getJSONArray("hourly");
        for (int i = 0; i < jsonArray.length(); i++) {
            HourlyWeather hourlyWeather = new HourlyWeather();
            hourlyWeather.setTimestamp(jsonArray.getJSONObject(i).getLong("dt"));
            hourlyWeather.setCurrentTemp(jsonArray.getJSONObject(i).getDouble("temp"));
            hourlyWeather.setFeelsLikeTemp(jsonArray.getJSONObject(i).getDouble("feels_like"));
            hourlyWeather.setHumidity(jsonArray.getJSONObject(i).getInt("humidity"));
            hourlyWeather.setUvi(jsonArray.getJSONObject(i).getDouble("uvi"));
            hourlyWeather.setClouds(jsonArray.getJSONObject(i).getInt("clouds"));
            hourlyWeather.setVisibility(jsonArray.getJSONObject(i).getInt("visibility"));
            hourlyWeather.setMain(jsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main"));
            hourlyWeather.setDescription(jsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description"));
            hourlyWeather.setIcon(jsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon"));
            hourlyWeather.setPop(jsonArray.getJSONObject(i).getInt("pop"));
            hourlyWeatherList.add(hourlyWeather);
        }
        openWeatherLocationData.setHourlyWeatherList(hourlyWeatherList);


        List<DailyWeather> dailyWeatherList = new ArrayList<>();
        jsonArray = jsonObject.getJSONArray("daily");
        for (int i = 0; i < jsonArray.length(); i++) {
            DailyWeather dailyWeather = new DailyWeather();
            dailyWeather.setTimestamp(jsonArray.getJSONObject(i).getLong("dt"));
            dailyWeather.setSunrise(jsonArray.getJSONObject(i).getLong("sunrise"));
            dailyWeather.setSunset(jsonArray.getJSONObject(i).getLong("sunset"));
            dailyWeather.setMoonrise(jsonArray.getJSONObject(i).getLong("moonrise"));
            dailyWeather.setMoonset(jsonArray.getJSONObject(i).getLong("moonset"));
            dailyWeather.setMoonPhase(jsonArray.getJSONObject(i).getDouble("moon_phase"));
            dailyWeather.setCurrentTemp(jsonArray.getJSONObject(i).getJSONObject("temp").getDouble("day"));
            dailyWeather.setTempMin(jsonArray.getJSONObject(i).getJSONObject("temp").getDouble("min"));
            dailyWeather.setTempMax(jsonArray.getJSONObject(i).getJSONObject("temp").getDouble("max"));
            dailyWeather.setTempNight(jsonArray.getJSONObject(i).getJSONObject("temp").getDouble("night"));
            dailyWeather.setTempEvening(jsonArray.getJSONObject(i).getJSONObject("temp").getDouble("eve"));
            dailyWeather.setTempMorning(jsonArray.getJSONObject(i).getJSONObject("temp").getDouble("morn"));
            dailyWeather.setFeelsLikeTemp(jsonArray.getJSONObject(i).getJSONObject("feels_like").getDouble("day"));
            dailyWeather.setFeelsLikeNight(jsonArray.getJSONObject(i).getJSONObject("feels_like").getDouble("night"));
            dailyWeather.setFeelsLikeEvening(jsonArray.getJSONObject(i).getJSONObject("feels_like").getDouble("eve"));
            dailyWeather.setFeelsLikeMorning(jsonArray.getJSONObject(i).getJSONObject("feels_like").getDouble("morn"));
            dailyWeather.setHumidity(jsonArray.getJSONObject(i).getInt("humidity"));
            dailyWeather.setClouds(jsonArray.getJSONObject(i).getInt("clouds"));
            dailyWeather.setPop(jsonArray.getJSONObject(i).getInt("pop"));
            dailyWeather.setUvi(jsonArray.getJSONObject(i).getDouble("uvi"));
            dailyWeather.setMain(jsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main"));
            dailyWeather.setDescription(jsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description"));
            dailyWeather.setIcon(jsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon"));
            dailyWeatherList.add(dailyWeather);
        }
        openWeatherLocationData.setDailyWeatherList(dailyWeatherList);

        return openWeatherLocationData;
    }
}
