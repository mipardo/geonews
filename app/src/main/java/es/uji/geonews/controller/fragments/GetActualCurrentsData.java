package es.uji.geonews.controller.fragments;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Locale;

import es.uji.geonews.controller.tasks.UserTask;
import es.uji.geonews.controller.template.CurrentsTemplate;
import es.uji.geonews.controller.template.WeatherTemplate;
import es.uji.geonews.model.data.CurrentsData;
import es.uji.geonews.model.data.News;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class GetActualCurrentsData extends UserTask {
    private CurrentsTemplate currentsTemplate;
    private CurrentsTemplate currentsTemplate2;
    private CurrentsTemplate currentsTemplate3;

    private int locationId;
    private String sitio;
    private Context context;
    private String error;
    private CurrentsData data;
    private List<News> lista;
    private int posicion;




    public GetActualCurrentsData (int locationId, CurrentsTemplate currentsTemplate,CurrentsTemplate currentsTemplate2 ,CurrentsTemplate currentsTemplate3 ,Context context) {
        this.locationId = locationId;
        this.currentsTemplate = currentsTemplate;
        this.context = context;
        this.currentsTemplate2 = currentsTemplate2;
        this.currentsTemplate3 = currentsTemplate3;
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
                    lista =  data.getNewsList();
                    sitio = GeoNewsManagerSingleton.getInstance(context).getLocation(locationId).getPlaceName();
                } catch (ServiceNotAvailableException | NoLocationRegisteredException e) {
                    error = e.getMessage();

                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        currentsTemplate.getProgressBar().setVisibility(View.INVISIBLE);
                        if (error != null);
                        else {
                            if (data != null && lista!=null) {
                                currentsTemplate.getDateTextview().setText(getActualDateAndTime());
                                currentsTemplate.getLugerTextview().setText(sitio);
                                currentsTemplate.getTituloTextview().setText(data.getNewsList().get(posicion).getTitle());
                                currentsTemplate.getCategoryTextview().setText(data.getNewsList().get(posicion).getCategory().get(posicion));
                                currentsTemplate.getAuthorTextview().setText(data.getNewsList().get(posicion).getAuthor());
                                currentsTemplate.getDescripcionTextview().setText(data.getNewsList().get(posicion).getDescription());


                                if(data.getNewsList().get(posicion).getImage().equals("None")) {
                                    currentsTemplate.getImageView().setVisibility(View.GONE);
                                }else {
                                    Picasso.get().load(data.getNewsList().get(posicion).getImage()).into(currentsTemplate.getImageView());
                                }

                                if (lista.size()>=2) {
                                    posicion++;
                                    currentsTemplate2.getTituloTextview().setText(data.getNewsList().get(posicion).getTitle());
                                    currentsTemplate2.getCategoryTextview().setText(data.getNewsList().get(posicion).getCategory().get(0));
                                    currentsTemplate2.getAuthorTextview().setText(data.getNewsList().get(posicion).getAuthor());
                                    currentsTemplate2.getDescripcionTextview().setText(data.getNewsList().get(posicion).getDescription());


                                    if (data.getNewsList().get(posicion).getImage().equals("None")) {
                                        currentsTemplate2.getImageView().setVisibility(View.GONE);
                                    } else {
                                        Picasso.get().load(data.getNewsList().get(posicion).getImage()).into(currentsTemplate2.getImageView());
                                    }
                                    if (lista.size() >= 3) {
                                        posicion++;
                                        currentsTemplate3.getTituloTextview().setText(data.getNewsList().get(posicion).getTitle());
                                        //Posible no categorias?????
                                        currentsTemplate3.getCategoryTextview().setText(data.getNewsList().get(posicion).getCategory().get(0));
                                        currentsTemplate3.getAuthorTextview().setText(data.getNewsList().get(posicion).getAuthor());
                                        currentsTemplate3.getDescripcionTextview().setText(data.getNewsList().get(posicion).getDescription());


                                        if (data.getNewsList().get(posicion).getImage().equals("None")) {
                                            currentsTemplate3.getImageView().setVisibility(View.GONE);
                                        } else {
                                            Picasso.get().load(data.getNewsList().get(posicion).getImage()).into(currentsTemplate3.getImageView());
                                        }
                                    }
                                }


                            }
                            else{
                                currentsTemplate.getCardview().setVisibility(View.GONE);
                                currentsTemplate.getDateTextview().setText(getActualDateAndTime());
                                currentsTemplate.getLugerTextview().setText(sitio);
                                currentsTemplate.getAuthorTextview().setVisibility(View.GONE);
                                currentsTemplate.getTituloTextview().setVisibility(View.GONE);
                                currentsTemplate.getDescripcionTextview().setVisibility(View.GONE);
                                currentsTemplate.getCategoryTextview().setVisibility(View.GONE);
                                currentsTemplate.getImageView().setVisibility(View.GONE);


                                currentsTemplate2.getCardview().setVisibility(View.GONE);
                                currentsTemplate2.getAuthorTextview().setVisibility(View.GONE);
                                currentsTemplate2.getTituloTextview().setVisibility(View.GONE);
                                currentsTemplate2.getDescripcionTextview().setVisibility(View.GONE);
                                currentsTemplate2.getCategoryTextview().setVisibility(View.GONE);
                                currentsTemplate2.getImageView().setVisibility(View.GONE);

                                currentsTemplate3.getCardview().setVisibility(View.GONE);
                                currentsTemplate3.getAuthorTextview().setVisibility(View.GONE);
                                currentsTemplate3.getTituloTextview().setVisibility(View.GONE);
                                currentsTemplate3.getDescripcionTextview().setVisibility(View.GONE);
                                currentsTemplate3.getCategoryTextview().setVisibility(View.GONE);
                                currentsTemplate3.getImageView().setVisibility(View.GONE);

                                currentsTemplate.getAviso().setVisibility(View.VISIBLE);
                                currentsTemplate.getAviso().setTextSize(40);

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
    /*
    public void actualiza() {
        posicion++;
    }

     */
}