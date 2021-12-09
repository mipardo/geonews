package es.uji.geonews.controller.tasks;

import android.content.Context;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import es.uji.geonews.controller.template.WeatherTemplate;
import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.data.OpenWeatherForecastData;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class GetTomorrowWeatherData extends UserTask {
    private WeatherTemplate weatherTemplate;
    private int locationId;
    private Context context;
    private OpenWeatherData data;
    private String error;

    public GetTomorrowWeatherData (int locationId, WeatherTemplate weatherTemplate, Context context) {
        this.locationId = locationId;
        this.weatherTemplate = weatherTemplate;
        this.context = context;
    }

    @Override
    public void execute() {
        weatherTemplate.getProgressBar().setVisibility(View.VISIBLE);
        weatherTemplate.getProgressBar().bringToFront();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // TODO borrar addServiceToLocation de aquí
                    GeoNewsManagerSingleton.getInstance(context).addServiceToLocation(ServiceName.OPEN_WEATHER, locationId);
                    List<Data> forecast = GeoNewsManagerSingleton.getInstance(context).getFutureData(ServiceName.OPEN_WEATHER, locationId);
                    data = getTomorrowDataFromForecast(forecast);
                } catch (ServiceNotAvailableException | NoLocationRegisteredException e) {
                    error = e.getMessage();

                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        weatherTemplate.getProgressBar().setVisibility(View.INVISIBLE);
                        if (error != null);
                        else
                        {
                            weatherTemplate.getDateTextview().setText(getTomorrowDateAndTime());
                            weatherTemplate.getMinTempTextview().setText(Math.round(data.getMinTemp()) + "ºC");
                            weatherTemplate.getMaxTempTextview().setText(Math.round(data.getMaxTemp()) + "ºC");
                            weatherTemplate.getActualTempTextview().setText(Math.round(data.getActTemp()) + "ºC");
                            Picasso.get()
                                    .load("https://openweathermap.org/img/wn/" + data.getIcon() + "@2x.png")
                                    .into(weatherTemplate.getWeatherIcon());
                            String description = data.getDescription().substring(0, 1).toUpperCase() + data.getDescription().substring(1);
                            weatherTemplate.getWeatherDescriptionTextview().setText(description);
                        }
                    }

                });
            }

            private OpenWeatherData getTomorrowDataFromForecast(List<Data> forecast) {
                double minTemp = 100;
                double maxTemp = -100;
                double expectedTemp = 0;
                int sectionCounter = 0;
                OpenWeatherData result = null;
                int tomorrowDay = LocalDate.now().getDayOfYear() + 1;
                for (Data d : forecast) {
                    OpenWeatherForecastData section = (OpenWeatherForecastData) d;
                    int sectionDay = LocalDateTime.ofInstant(Instant.ofEpochMilli(section.getTimestamp() * 1000),
                            TimeZone.getDefault().toZoneId()).getDayOfYear();
                    if (sectionDay == tomorrowDay) {
                        expectedTemp += section.getActTemp();
                        sectionCounter++;
                        if (section.getMinTemp() < minTemp) minTemp = section.getMinTemp();
                        if (section.getMaxTemp() > maxTemp) maxTemp = section.getMaxTemp();
                        if (sectionCounter == 4) result = section; // 4 = Mediodia
                    }
                }
                double meanTemp = expectedTemp / sectionCounter;
                // TODO En este punto tenemos la tempMin, tempMax y media de mañana, pero como sacar "la media" de
                // los iconos por ejemplo?
                result.setActTemp(Math.round(meanTemp));
                result.setMinTemp(Math.round(minTemp));
                result.setMaxTemp(Math.round(maxTemp));
                return result;
            }

        }).start();
    }

    private String getTomorrowDateAndTime() {
        DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                .appendPattern("dd MMMM")
                .toFormatter(new Locale("es", "ES"));
        return LocalDateTime.now().plusDays(1).format(fmt);
    }
}
