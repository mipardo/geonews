package es.uji.geonews.controller.tasks;

import android.content.Context;

import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.TimeZone;

import es.uji.geonews.controller.template.WeatherTemplate;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.DailyWeather;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.data.Weather;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class GetOpenWeatherTodayOfflineData extends UserTask{
    private final GeoNewsManager geoNewsManager;
    private final WeatherTemplate weatherTemplate;
    private final int locationId;
    private final Context context;
    private OpenWeatherData data;
    private String error;

    public GetOpenWeatherTodayOfflineData(int locationId, WeatherTemplate weatherTemplate, Context context) {
        this.geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.locationId = locationId;
        this.weatherTemplate = weatherTemplate;
        this.context = context;
    }

    @Override
    public void execute() {
        try {
            Location location = geoNewsManager.getLocation(locationId);
            weatherTemplate.getWeatherTitleOutput().setText("Hoy" + " en " + location.getMainName());
        } catch (NoLocationRegisteredException e) {
            weatherTemplate.getWeatherTitleOutput().setText("Hoy" + " en " + "...");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    data = (OpenWeatherData) geoNewsManager.getOfflineData(ServiceName.OPEN_WEATHER, locationId);
                    if (data == null) error = "Esta ubicación no esta suscrita al servicio de clima";
                } catch (NoLocationRegisteredException e) {
                    error = e.getMessage();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (error != null);
                        else
                        {
                            Weather current = data.getCurrentWeather();
                            DailyWeather today = data.getDailyWeatherList().get(0);
                            weatherTemplate.getCurrentTempOutput().setText(Math.round(current.getCurrentTemp()) + "º C");
                            weatherTemplate.getMinTempOutput().setText(Math.round(today.getTempMin()) + "º");
                            weatherTemplate.getMaxTempOutput().setText(Math.round(today.getTempMax()) + "º");
                            Picasso.get()
                                    .load("https://openweathermap.org/img/wn/" + current.getIcon() + "@4x.png")
                                    .into(weatherTemplate.getIconOutput());
                            String description = current.getDescription().substring(0, 1).toUpperCase()
                                    + current.getDescription().substring(1);
                            weatherTemplate.getDescriptionOutput().setText(description);
                            weatherTemplate.getSunriseOutput().setText(getFormatedTimestamp(current.getSunrise()));
                            weatherTemplate.getSunsetOuptut().setText(getFormatedTimestamp(current.getSunset()));
                            weatherTemplate.getVisibilityOutput().setText(current.getUvi() + "");
                            weatherTemplate.getVisibilityOutput().setText(current.getVisibility() + "m ");
                        }
                    }

                });
            }

        }).start();
    }

}
