package es.uji.geonews.controller.tasks;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.AirVisualData;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class GetAirVisualData extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final ProgressBar progressBar;
    private final Context context;
    private final TextView tempertaureOutput;
    private final TextView preassureOutput;
    private final TextView humidityOutput;
    private final int locationId;
    private AirVisualData data;
    private String error;

    public GetAirVisualData(int locationId, TextView tempertaureOutput, TextView preassureOutput, TextView humidityOutput, ProgressBar progressBar, Context context){
        this.geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.progressBar = progressBar;
        this.tempertaureOutput = tempertaureOutput;
        this.humidityOutput = humidityOutput;
        this.preassureOutput = preassureOutput;
        this.context = context;
        this.locationId = locationId;
    }

    @Override
    public void execute() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    data = (AirVisualData) geoNewsManager.getData(ServiceName.AIR_VISUAL, locationId);
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
                        }
                    }
                });
            }
        }).start();
    }
}
