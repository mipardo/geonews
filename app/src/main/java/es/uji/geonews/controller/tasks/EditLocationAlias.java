package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.TextView;

import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;

public class EditLocationAlias extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final Context context;
    private final int locationId;
    private final String newAlias;
    private final TextView locationAliasOutput;
    private String error;


    public EditLocationAlias(Context context, int locationId, String newAlias, TextView locationAliasOutput){
        this.geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.context = context;
        this.locationId = locationId;
        this.newAlias = newAlias;
        this.locationAliasOutput = locationAliasOutput;
    }

    @Override
    public void execute() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean res = false;
                try {
                    res = geoNewsManager.setAliasToLocation(newAlias, locationId);
                    if (!res) error = "No se ha podido asignar el alias " + newAlias + " a esta ubicación";
                } catch (NoLocationRegisteredException e) {
                    error = e.getMessage();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (error != null) showAlertError();
                        else locationAliasOutput.setText(newAlias);
                    }
                });
            }
        }).start();
    }

    private void showAlertError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(error);
        builder.setMessage("Es posible que ya exista este alias para otra ubicación o que el nuevo alias no sea válido ");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
