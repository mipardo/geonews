package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;

import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.services.ServiceName;

public class AddServiceToLocation extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final Context context;
    private final ServiceName serviceName;
    private final int locationId;
    private String error;


    public AddServiceToLocation(GeoNewsManager geoNewsManager, Context context, ServiceName serviceName, int locationId){
        this.geoNewsManager = geoNewsManager;
        this.context = context;
        this.serviceName = serviceName;
        this.locationId = locationId;
    }

    @Override
    public void execute() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    geoNewsManager.addServiceToLocation(serviceName, locationId);
                } catch (NoLocationRegisteredException | ServiceNotAvailableException e) {
                    error = e.getMessage();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (error != null) showAlertError();
                    }
                });
            }
        }).start();
    }

    private void showAlertError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error al activar el sevicio de la ubicación ");
        builder.setMessage(error);
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
