package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

import es.uji.geonews.controller.template.WeatherTemplate;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class GetActualWeatherData extends UserTask {
    private final WeatherTemplate weatherTemplate;
    private final int locationId;
    private final Context context;
    private OpenWeatherData data;
    private String error;

    public GetActualWeatherData (int locationId, WeatherTemplate weatherTemplate, Context context) {
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
                    //GeoNewsManagerSingleton.getInstance(context).addServiceToLocation(ServiceName.OPEN_WEATHER, locationId);
                    data = (OpenWeatherData) GeoNewsManagerSingleton.getInstance(context).getData(ServiceName.OPEN_WEATHER, locationId);
                    if (data == null) error = "Esta ubicación no esta suscrita al servicio de clima";
                } catch (ServiceNotAvailableException | NoLocationRegisteredException e) {
                    error = e.getMessage();

                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        weatherTemplate.getProgressBar().setVisibility(View.INVISIBLE);
                        if (error != null) showAlertError();
                        else
                        {
                            weatherTemplate.getDateTextview().setText(getActualDateAndTime());
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

        }).start();
    }

    private String getActualDateAndTime() {
        DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                .appendPattern("dd MMMM HH:mm")
                .toFormatter(new Locale("es", "ES"));
        return LocalDateTime.now().format(fmt);
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
