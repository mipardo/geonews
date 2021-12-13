package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import es.uji.geonews.controller.adapters.CurrentsAdapter;
import es.uji.geonews.model.data.CurrentsData;
import es.uji.geonews.model.data.News;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class GetActualCurrentsData extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final ProgressBar progressBar;
    private final RecyclerView recyclerView;
    private final int locationId;
    private final Context context;
    private String error;
    private List<News> news;

    public GetActualCurrentsData (int locationId, RecyclerView recyclerView,
                                  ProgressBar progressBar, Context context) {
        geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.locationId = locationId;
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
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
                    CurrentsData data = ((CurrentsData) geoNewsManager.getData(ServiceName.CURRENTS, locationId));
                    if( data == null ) error = "No news available";
                    else news = data.getNewsList();
                } catch (ServiceNotAvailableException | NoLocationRegisteredException e) {
                    error = e.getMessage();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (error != null) showAlertError();
                        else {
                            CurrentsAdapter adapter = (CurrentsAdapter) recyclerView.getAdapter();
                            if (adapter != null && news != null) adapter.updateNews(news);
                        }
                    }

                });
            }

        }).start();
    }

    private void showAlertError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error al obtener nuevas noticias");
        builder.setMessage("Pruebe más tarde");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}