package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;

import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

import es.uji.geonews.controller.template.WeatherTemplate;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.data.ServiceData;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class GetOpenWeatherTomorrowOfflineData extends UserTask {
    private final WeatherTemplate weatherTemplate;
    private final int locationId;
    private final Context context;
    private OpenWeatherData data;
    private String error;

    public GetOpenWeatherTomorrowOfflineData(int locationId, WeatherTemplate weatherTemplate, Context context) {
        this.locationId = locationId;
        this.weatherTemplate = weatherTemplate;
        this.context = context;
    }

    @Override
    public void execute() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServiceData data = GeoNewsManagerSingleton.getInstance(context).getOfflineData(ServiceName.OPEN_WEATHER, locationId);
                } catch (NoLocationRegisteredException e) {
                    error = e.getMessage();

                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (error != null) showAlertError();
                        else if (data != null)
                        {
                            weatherTemplate.getDateTextview().setText(getTomorrowDateAndTime());
                            weatherTemplate.getMinTempTextview().setText(Math.round(data.getDailyWeatherList().get(1).getTempMin()) + "ºC");
                            weatherTemplate.getMaxTempTextview().setText(Math.round(data.getDailyWeatherList().get(1).getTempMax()) + "ºC");
                            weatherTemplate.getActualTempTextview().setText(Math.round(data.getDailyWeatherList().get(1).getCurrentTemp()) + "ºC");
                            Picasso.get()
                                    .load("https://openweathermap.org/img/wn/" + data.getDailyWeatherList().get(1).getIcon() + "@2x.png")
                                    .into(weatherTemplate.getWeatherIcon());
                            String description = data.getDailyWeatherList().get(1).getDescription().substring(0, 1).toUpperCase() + data.getDailyWeatherList().get(1).getDescription().substring(1);
                            weatherTemplate.getWeatherDescriptionTextview().setText(description);
                        }
                    }

                });
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
