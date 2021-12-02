package es.uji.geonews.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.uji.geonews.R;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.AirVisualData;
import es.uji.geonews.model.data.CurrentsData;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.exceptions.DatabaseNotAvailableException;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.services.ServiceName;

public class NewsActivity extends AppCompatActivity {

    private GeoNewsManager geoNewsManager;
    private List<Location> locations;
    private EditText locationInput;
    private Context context;
    private String result;
    AirVisualData NewsData;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        context = this;


        geoNewsManager = new GeoNewsManager(this);
        ProgressBar progressBar = findViewById(R.id.my_progress_bar);
        TextView textViewTitulo = findViewById(R.id.textViewTitulo);
        TextView textViewCuerpo = findViewById(R.id.textViewCuerpo);
        ImageView imageViewPrincipal = findViewById(R.id.imageView);
        ImageView imageViewPrincipal2 = findViewById(R.id.imageView2);
        ImageView imageViewPrincipal3 = findViewById(R.id.imageView3);
        ImageView imageViewPrincipal4 = findViewById(R.id.imageView4);
        ImageView imageViewPrincipal5 = findViewById(R.id.imageView5);
        Button actualizaButton = findViewById(R.id.newsButton);



        actualizaButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // Show the dialog
                progressBar.setVisibility(View.VISIBLE);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            location = geoNewsManager.addLocation("Castello de la plana");
                            //location.activate();
                            geoNewsManager.addServiceToLocation(ServiceName.AIR_VISUAL, location);
                            //geoNewsManager.addServiceToLocation(ServiceName.CURRENTS, location);
                            //geoNewsManager.loadAll();
                            NewsData = (AirVisualData) geoNewsManager.getData(ServiceName.AIR_VISUAL,geoNewsManager.getLocation(1));
                        } catch (NotValidCoordinatesException | UnrecognizedPlaceNameException | ServiceNotAvailableException | NoLocationRegisteredException e) {
                            result = e.getMessage();
                        }

                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (result == null && NewsData!=null){
                                    //textViewTitulo.setText(location.getPlaceName());
                                    textViewCuerpo.setText(NewsData.getMainCn());
                                }
                                else textViewTitulo.setText(result);
                                Picasso.get().load("https://www.tonica.la/__export/1595866796960/sites/debate/img/2020/07/27/el-ssj4-muestra-nuevo-arte-visual-goku-super-saiyan-4.jpg_1902800913.jpg").into(imageViewPrincipal);
                                Picasso.get().load("https://i1.sndcdn.com/artworks-000480708312-x3sdcx-t500x500.jpg").into(imageViewPrincipal2);
                                Picasso.get().load("https://areajugones.sport.es/wp-content/uploads/2021/05/imagen-2021-05-29-092038.jpg").into(imageViewPrincipal3);
                                Picasso.get().load("https://1.bp.blogspot.com/--F0PCsS9mdo/X6u9SD75CAI/AAAAAAAFvOo/fV6T56jowyQGV5CfShQuFa9O1P_SI78dQCLcBGAsYHQ/s1114/VegitoSS4LB.jpg").into(imageViewPrincipal4);
                                Picasso.get().load("https://i.pinimg.com/564x/0e/0f/05/0e0f056f001cb9e97a72828cd2ffbf2a.jpg").into(imageViewPrincipal5);

                                textViewTitulo.setText("Air Visual Funciona");
                                progressBar.setVisibility(View.INVISIBLE);

                            }
                        });
                    }
                }).start();

            }
        });
/*
        @Override
        public void execute() {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.bringToFront();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        geoNewsManager.addLocation(location);
                        geoNewsManager.getData(ServiceName.CURRENTS,location);
                    } catch (UnrecognizedPlaceNameException | ServiceNotAvailableException | NotValidCoordinatesException e) {
                        error = e.getMessage();
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (error != null) showAlertError();
                            else{
                                List<Location> locations = geoNewsManager.getNonActiveLocations();
                                recyclerView.setAdapter(new LocationListAdapter(locations));
                                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }).start();
        }

 */
    }

}