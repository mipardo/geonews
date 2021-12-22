package es.uji.geonews.controller.tasks;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.widget.Switch;

import androidx.constraintlayout.widget.ConstraintLayout;

import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class AddServiceToLocation extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final Context context;
    private final ServiceName serviceName;
    private final @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchButton;
    private final int locationId;
    private String error;
    private ConstraintLayout loadingLayout;


    public AddServiceToLocation(Context context, ServiceName serviceName, int locationId, Switch switchButton, ConstraintLayout loadingLayout){
        this.geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.context = context;
        this.serviceName = serviceName;
        this.locationId = locationId;
        this.switchButton = switchButton;
        this.loadingLayout = loadingLayout;
    }

    @Override
    public void execute() {
        lockUI(context, loadingLayout);
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
                        if (error != null){
                            switchButton.setChecked(false);
                            showAlertError();
                        }
                        unlockUI(context, loadingLayout);
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
