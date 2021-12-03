package es.uji.geonews.controller.tasks;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.AirVisualData;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.services.ServiceName;

public class GetAirVisualData extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final ProgressBar progressBar;
    private Location location;
    private final TextView tempertaureOutput;
    private final TextView preassureOutput;
    private final TextView humidityOutput;
    private AirVisualData data;
    private String error;

    public GetAirVisualData(GeoNewsManager geoNewsManager, TextView tempertaureOutput, TextView preassureOutput, TextView humidityOutput, ProgressBar progressBar){
        this.geoNewsManager = geoNewsManager;
        this.progressBar = progressBar;
        this.tempertaureOutput = tempertaureOutput;
        this.humidityOutput = humidityOutput;
        this.preassureOutput = preassureOutput;
    }

    @Override
    public void execute() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //TODO: Coger este id de la vista (de seleecionar la ubicacion)
                    geoNewsManager.addServiceToLocation(ServiceName.AIR_VISUAL, 1);
                    data = (AirVisualData) geoNewsManager.getData(ServiceName.AIR_VISUAL, 1);
                } catch (ServiceNotAvailableException | NoLocationRegisteredException e) {
                    error = e.getMessage();
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (error != null || data == null){
                            tempertaureOutput.setText("Problema: " + error);
                        }
                        else {
                            tempertaureOutput.setText("Temperatura: " + data.getTemperature());
                            preassureOutput.setText("Presión atomosférica: " + data.getPressure());
                            humidityOutput.setText("Humedad relativa: " + data.getHumidity());
                            //Picasso.get().load(currentsData.getNewsList().get(0).getImage()).into(imageViewPrincipal);
                        }
                    }
                });
            }
        }).start();
    }
}
