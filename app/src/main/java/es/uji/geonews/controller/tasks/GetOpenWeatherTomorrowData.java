package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
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
import es.uji.geonews.model.data.ServiceData;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.data.OpenWeatherForecastData;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class GetOpenWeatherTomorrowData extends UserTask {
    private final WeatherTemplate weatherTemplate;
    private final int locationId;
    private final Context context;
    private OpenWeatherData data;
    private String error;

    public GetOpenWeatherTomorrowData(int locationId, WeatherTemplate weatherTemplate, Context context) {
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
                    ServiceData forecast = GeoNewsManagerSingleton.getInstance(context).getFutureData(ServiceName.OPEN_WEATHER, locationId);
                    data = getTomorrowDataFromForecast((OpenWeatherForecastData) forecast);
                } catch (ServiceNotAvailableException | NoLocationRegisteredException e) {
                    error = e.getMessage();

                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        weatherTemplate.getProgressBar().setVisibility(View.INVISIBLE);
                        if (error != null) showAlertError();
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

            private OpenWeatherData getTomorrowDataFromForecast(OpenWeatherForecastData forecast) {
                double minTemp = 100;
                double maxTemp = -100;
                double expectedTemp = 0;
                int sectionCounter = 0;
                OpenWeatherData result = null;
                int tomorrowDay = LocalDate.now().getDayOfYear() + 1;
                for (OpenWeatherData section : forecast.getOpenWeatherDataList()) {
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

    private void showAlertError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error al obtener la predicción del tiempo");
        builder.setMessage("Pruebe más tarde");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
