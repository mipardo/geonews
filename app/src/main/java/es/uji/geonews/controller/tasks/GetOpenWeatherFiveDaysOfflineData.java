package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import es.uji.geonews.controller.adapters.ForecastAdapter;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class GetOpenWeatherFiveDaysOfflineData extends UserTask {
    private final Context context;
    private final int locationId;
    private final RecyclerView recyclerView;
    private String error;
    private OpenWeatherData data;

    public GetOpenWeatherFiveDaysOfflineData(int locationId, RecyclerView recyclerView, Context context) {
        this.locationId = locationId;
        this.recyclerView = recyclerView;
        this.context = context;
    }

    @Override
    public void execute() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    data = (OpenWeatherData) GeoNewsManagerSingleton.getInstance(context).getOfflineData(ServiceName.OPEN_WEATHER, locationId);
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
