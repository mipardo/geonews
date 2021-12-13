package es.uji.geonews.controller.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Locale;

import es.uji.geonews.controller.tasks.UserTask;
import es.uji.geonews.controller.template.CurrentsTemplate;
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
    private final List<CurrentsTemplate> currentsTemplateList;
    private final int locationId;
    private final Context context;
    private String error;
    private CurrentsData data;
    private List<News> lista;
    private int posicion;


    public GetActualCurrentsData (int locationId, List<CurrentsTemplate> currentsTemplateList,
                                  ProgressBar progressBar, Context context) {
        geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.locationId = locationId;
        this.progressBar = progressBar;
        this.currentsTemplateList = currentsTemplateList;
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
                    data = (CurrentsData) geoNewsManager.getData(ServiceName.CURRENTS, locationId);
                    lista =  data.getNewsList();
                    if( lista == null ) error = "No news available";
                } catch (ServiceNotAvailableException | NoLocationRegisteredException e) {
                    error = e.getMessage();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (error != null) showAlertError();
                        else {
                            for(CurrentsTemplate currentsTemplate : currentsTemplateList){
                                currentsTemplate.getDateTextview().setText(getActualDateAndTime());
                                currentsTemplate.getTituloTextview().setText(data.getNewsList().get(posicion).getTitle());
                                currentsTemplate.getCategoryTextview().setText(data.getNewsList().get(posicion).getCategory().get(posicion));
                                currentsTemplate.getAuthorTextview().setText(data.getNewsList().get(posicion).getAuthor());
                                currentsTemplate.getDescripcionTextview().setText(data.getNewsList().get(posicion).getDescription());
                                if(data.getNewsList().get(posicion).getImage().equals("None")) {
                                    currentsTemplate.getImageView().setVisibility(View.GONE);
                                }else {
                                    Picasso.get().load(data.getNewsList().get(posicion).getImage()).into(currentsTemplate.getImageView());
                                }
                            }
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
        builder.setTitle("Error al obtener nuevas noticias");
        builder.setMessage("Pruebe más tarde");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}