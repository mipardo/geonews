package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import es.uji.geonews.controller.adapters.ForecastAdapter;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class GetOpenWeatherForecastOfflineData extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final Context context;
    private final int locationId;
    private final RecyclerView recyclerView;
    private final TextView titleOutput;
    private String error;
    private OpenWeatherData data;

    public GetOpenWeatherForecastOfflineData(int locationId, TextView titleOutput, RecyclerView recyclerView, Context context) {
        this.geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.titleOutput = titleOutput;
        this.locationId = locationId;
        this.recyclerView = recyclerView;
        this.context = context;
    }

    @Override
    public void execute() {
        try {
            Location location = geoNewsManager.getLocation(locationId);
            titleOutput.setText("Previsión general" + " en " + location.getMainName());
        } catch (NoLocationRegisteredException e) {
            titleOutput.setText("Previsión general" + " en " + "...");
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
                            ForecastAdapter adapter = (ForecastAdapter) recyclerView.getAdapter();
                            if (adapter != null) adapter.updateForecast(data.getDailyWeatherList());
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
