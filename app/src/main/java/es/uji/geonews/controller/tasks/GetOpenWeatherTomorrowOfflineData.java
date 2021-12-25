package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;

import com.squareup.picasso.Picasso;

import es.uji.geonews.controller.adapters.PrecipitationListAdapter;
import es.uji.geonews.controller.charts.WeatherLineChart;
import es.uji.geonews.controller.template.WeatherTemplate;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.DailyWeather;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class GetOpenWeatherTomorrowOfflineData extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final WeatherTemplate weatherTemplate;
    private final int locationId;
    private final Context context;
    private OpenWeatherData data;
    private String error;

    public GetOpenWeatherTomorrowOfflineData(int locationId, WeatherTemplate weatherTemplate, Context context) {
        this.geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.locationId = locationId;
        this.weatherTemplate = weatherTemplate;
        this.context = context;
    }

    @Override
    public void execute() {
        try {
            Location location = geoNewsManager.getLocation(locationId);
            weatherTemplate.getWeatherTitleOutput().setText("Mañana" + " en " + location.getMainName());
        } catch (NoLocationRegisteredException e) {
            weatherTemplate.getWeatherTitleOutput().setText("Mañana" + " en " + "...");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    data = (OpenWeatherData) geoNewsManager.getOfflineData(ServiceName.OPEN_WEATHER, locationId);
                } catch (NoLocationRegisteredException e) {
                    error = e.getMessage();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (error != null) showAlertError();
                        else if (data != null)
                        {
                            DailyWeather tomorrow = data.getDailyWeatherList().get(1);
                            WeatherLineChart weatherLineChart = new WeatherLineChart(weatherTemplate.getLineChart());
                            weatherLineChart.fillChart(data);
                            weatherTemplate.getCurrentTempOutput().setText(Math.round(tomorrow.getCurrentTemp()) + "º C");
                            weatherTemplate.getMinTempOutput().setText(Math.round(tomorrow.getTempMin()) + "º");
                            weatherTemplate.getMaxTempOutput().setText(Math.round(tomorrow.getTempMax()) + "º");
                            Picasso.get()
                                    .load("https://openweathermap.org/img/wn/" + tomorrow.getIcon() + "@4x.png")
                                    .into(weatherTemplate.getIconOutput());
                            String description = tomorrow.getDescription().substring(0, 1).toUpperCase()
                                    + tomorrow.getDescription().substring(1);
                            weatherTemplate.getDescriptionOutput().setText(description);
                            weatherTemplate.getSunriseOutput().setText(getFormatedTimestamp(tomorrow.getSunrise()));
                            weatherTemplate.getSunsetOuptut().setText(getFormatedTimestamp(tomorrow.getSunset()));
                            weatherTemplate.getMoonriseOutput().setText(getFormatedTimestamp(tomorrow.getMoonrise()));
                            weatherTemplate.getMoonsetOutput().setText(getFormatedTimestamp(tomorrow.getMoonset()));
                            PrecipitationListAdapter adapter = (PrecipitationListAdapter)
                                    weatherTemplate.getPrecipitationsOutput().getAdapter();
                            if (adapter != null) adapter.updatePrecipitations(data.getHourlyWeatherList().
                                    subList(getHoursLeftOfToday(), getHoursLeftOfToday() + 24));
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
