package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uji.geonews.controller.adapters.FiveDaysForecastAdapter;
import es.uji.geonews.model.data.OpenWeatherForecastData;
import es.uji.geonews.model.data.ServiceData;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class GetOpenWeatherFiveDaysData extends UserTask {
    private final ViewGroup loadingLayout;
    private final Context context;
    private final int locationId;
    private final RecyclerView recyclerView;
    private String error;
    private ServiceData data;

    public GetOpenWeatherFiveDaysData(int locationId, RecyclerView recyclerView, ViewGroup loadingLayout, Context context) {
        this.locationId = locationId;
        this.recyclerView = recyclerView;
        this.loadingLayout = loadingLayout;
        this.context = context;
    }

    @Override
    public void execute() {
        loadingLayout.setVisibility(View.VISIBLE);
        loadingLayout.bringToFront();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    data = GeoNewsManagerSingleton.getInstance(context).getFutureData(ServiceName.OPEN_WEATHER, locationId);
                } catch (ServiceNotAvailableException | NoLocationRegisteredException e) {
                    error = e.getMessage();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        loadingLayout.setVisibility(View.GONE);
                        if (error != null) showAlertError();
                        else
                        {
                            FiveDaysForecastAdapter adapter = (FiveDaysForecastAdapter) recyclerView.getAdapter();
                            if (adapter != null) adapter.updateForecast((OpenWeatherForecastData) data);
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
