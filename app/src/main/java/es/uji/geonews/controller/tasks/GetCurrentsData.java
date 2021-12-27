package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class GetCurrentsData extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final ViewGroup loadingLayout;
    private final RecyclerView recyclerView;
    private final TextView noCurrentsTextview;
    private final int locationId;
    private final Context context;
    private String error;
    private List<News> news;

    public GetCurrentsData(int locationId, RecyclerView recyclerView,
                           ViewGroup loadingLayout, TextView noCurrentsTextview, Context context) {
        geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.locationId = locationId;
        this.loadingLayout = loadingLayout;
        this.recyclerView = recyclerView;
        this.noCurrentsTextview = noCurrentsTextview;
        this.context = context;
    }

    @Override
    public void execute() {
        showLoadingAnimation(loadingLayout);
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
                        hideLoadingAnimation(loadingLayout);
                        if (error != null) showAlertError();
                        else if (news != null && news.size() > 0){
                            noCurrentsTextview.setVisibility(View.GONE);
                            CurrentsAdapter adapter = (CurrentsAdapter) recyclerView.getAdapter();
                            if (adapter != null && news != null) adapter.updateNews(news);
                        } else {
                            noCurrentsTextview.setVisibility(View.VISIBLE);
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