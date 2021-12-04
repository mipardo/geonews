package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;

import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.services.ServiceName;

public class RemoveLocation extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final Context context;
    private final int locationId;
    private String error;


    public RemoveLocation(GeoNewsManager geoNewsManager, Context context, int locationId){
        this.geoNewsManager = geoNewsManager;
        this.context = context;
        this.locationId = locationId;
    }

    @Override
    public void execute() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean res = geoNewsManager.removeLocation(locationId);
                if (!res) error = "No se puede dar de baja un ubicación activa";
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (error != null) showAlertError();
                        // todo: else navigate to locations list
                    }
                });
            }
        }).start();
    }

    private void showAlertError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(error);
        builder.setMessage("Si realmente desea eliminar la ubicación primero desactivela");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
