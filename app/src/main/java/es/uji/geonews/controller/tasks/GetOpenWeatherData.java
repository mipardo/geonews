package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;

import com.squareup.picasso.Picasso;

import es.uji.geonews.controller.adapters.PrecipitationListAdapter;
import es.uji.geonews.controller.template.WeatherTemplate;
import es.uji.geonews.model.data.DailyWeather;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.data.Weather;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class GetOpenWeatherData extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final WeatherTemplate weatherTemplate;
    private final int locationId;
    private final Context context;
    private OpenWeatherData data;
    private String error;

    public GetOpenWeatherData(int locationId, WeatherTemplate weatherTemplate, Context context) {
        geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.locationId = locationId;
        this.weatherTemplate = weatherTemplate;
        this.context = context;
    }

    @Override
    public void execute() {
        showLoadingAnimation(weatherTemplate.getLoadingLayout());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    data = (OpenWeatherData) geoNewsManager.getData(ServiceName.OPEN_WEATHER, locationId);
                    if (data == null) error = "Esta ubicación no esta suscrita al servicio de clima";
                } catch (ServiceNotAvailableException | NoLocationRegisteredException e) {
                    error = e.getMessage();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        hideLoadingAnimation(weatherTemplate.getLoadingLayout());
                        if (error != null) showAlertError();
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
                            weatherTemplate.getVisibilityOutput().setText(current.getVisibility() + " m");
                            weatherTemplate.getMoonriseOutput().setText(getFormatedTimestamp(today.getMoonrise()));
                            weatherTemplate.getMoonsetOutput().setText(getFormatedTimestamp(today.getMoonset()));
                            weatherTemplate.getFeelsLikeOutput().setText(Math.round(current.getFeelsLikeTemp()) + "º C");
                            weatherTemplate.getCloudsPercentageOutput().setText(current.getClouds() + "%");
                            PrecipitationListAdapter adapter = (PrecipitationListAdapter)
                                    weatherTemplate.getPrecipitationsOutput().getAdapter();
                            if (adapter != null) adapter.updatePrecipitations(data.getHourlyWeatherList().subList(0, getHoursLeftOfToday()));
                        }
                    }

                });
            }

        }).start();
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
