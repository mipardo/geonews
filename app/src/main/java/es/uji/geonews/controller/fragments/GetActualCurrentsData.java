package es.uji.geonews.controller.fragments;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

import es.uji.geonews.controller.tasks.UserTask;
import es.uji.geonews.controller.template.CurrentsTemplate;
import es.uji.geonews.controller.template.WeatherTemplate;
import es.uji.geonews.model.data.CurrentsData;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class GetActualCurrentsData extends UserTask {
    private CurrentsTemplate currentsTemplate;

    private int locationId;
    private String sitio;
    private Context context;
    private String error;
    private CurrentsData data;


    public GetActualCurrentsData (int locationId, CurrentsTemplate currentsTemplate, Context context) {
        this.locationId = locationId;
        this.currentsTemplate = currentsTemplate;
        this.context = context;
    }

    @Override
    public void execute() {
        currentsTemplate.getProgressBar().setVisibility(View.VISIBLE);
        currentsTemplate.getProgressBar().bringToFront();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // TODO borrar addServiceToLocation de aquí
                    GeoNewsManagerSingleton.getInstance(context).addServiceToLocation(ServiceName.CURRENTS, locationId);
                    data = (CurrentsData) GeoNewsManagerSingleton.getInstance(context).getData(ServiceName.CURRENTS, locationId);
                    sitio = GeoNewsManagerSingleton.getInstance(context).getLocation(locationId).getPlaceName();
                } catch (ServiceNotAvailableException | NoLocationRegisteredException e) {
                    error = e.getMessage();

                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        currentsTemplate.getProgressBar().setVisibility(View.INVISIBLE);
                        if (error != null);
                        else {
                            if (data != null) {
                                Log.d("GeoNews", data.getNewsList().get(0).getCategory().get(0));
                                //Log.d("GeoNews", data.getNewsList().get(0).getCategory().get(1));
                                Log.d("GeoNews", data.getNewsList().get(0).getAuthor());
                                currentsTemplate.getLugerTextview().setText(sitio);
                                currentsTemplate.getAuthorTextview().setText(data.getNewsList().get(0).getAuthor());
                                currentsTemplate.getCategoryTextview().setText(data.getNewsList().get(0).getCategory().get(0));
                                currentsTemplate.getDescripcionTextview().setText(data.getNewsList().get(0).getDescription());
                                currentsTemplate.getTituloTextview().setText(data.getNewsList().get(0).getTitle());

                                if(data.getNewsList().get(0).getImage().equals("None")) {
                                    currentsTemplate.getImageView().setVisibility(View.GONE);
                                }else {
                                    Picasso.get().load(data.getNewsList().get(0).getImage()).into(currentsTemplate.getImageView());
                                }
                                currentsTemplate.getDateTextview().setText(getActualDateAndTime());

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
}