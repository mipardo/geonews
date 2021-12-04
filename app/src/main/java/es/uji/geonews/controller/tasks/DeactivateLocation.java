package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;

import es.uji.geonews.model.managers.GeoNewsManager;

public class DeactivateLocation extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final Context context;
    private final int locationId;
    private String error;


    public DeactivateLocation(GeoNewsManager geoNewsManager, Context context, int locationId){
        this.geoNewsManager = geoNewsManager;
        this.context = context;
        this.locationId = locationId;
    }

    @Override
    public void execute() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean res = geoNewsManager.deactivateLocation(locationId);
                if (!res) error = "Esta ubicación ya está desactivada";
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
        builder.setTitle(error);
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
