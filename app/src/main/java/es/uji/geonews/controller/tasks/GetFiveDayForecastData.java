package es.uji.geonews.controller.tasks;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uji.geonews.controller.FiveDaysForecastAdapter;
import es.uji.geonews.model.data.ServiceData;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class GetFiveDayForecastData extends UserTask {
    private final ProgressBar progressBar;
    private final Context context;
    private final int locationId;
    private final RecyclerView recyclerView;
    private String error;
    private List<ServiceData> data;

    public GetFiveDayForecastData(int locationId, RecyclerView recyclerView, ProgressBar progressBar, Context context) {
        this.locationId = locationId;
        this.recyclerView = recyclerView;
        this.progressBar = progressBar;
        this.context = context;
    }

    @Override
    public void execute() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // TODO borrar addServiceToLocation de aquí
                    GeoNewsManagerSingleton.getInstance(context).addServiceToLocation(ServiceName.OPEN_WEATHER, locationId);
                    data = GeoNewsManagerSingleton.getInstance(context).getFutureData(ServiceName.OPEN_WEATHER, locationId);
                } catch (ServiceNotAvailableException | NoLocationRegisteredException e) {
                    error = e.getMessage();

                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (error != null);
                        else
                        {
                            FiveDaysForecastAdapter adapter = (FiveDaysForecastAdapter) recyclerView.getAdapter();
                            if (adapter != null) adapter.updateForecast(data);
                        }
                    }

                });
            }
        }).start();
    }
}
