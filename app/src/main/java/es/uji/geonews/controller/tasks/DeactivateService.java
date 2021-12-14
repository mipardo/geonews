package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;

import androidx.appcompat.widget.SwitchCompat;

import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class DeactivateService extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final ServiceName serviceName;
    private final SwitchCompat switchCompat;
    private final Context context;
    private String error;

    public DeactivateService (ServiceName serviceName, SwitchCompat switchCompat, Context context) {
        this.serviceName = serviceName;
        this.switchCompat = switchCompat;
        geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.context = context;
    }

    @Override
    public void execute() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean res = geoNewsManager.deactivateService(serviceName);
                if (!res) error = "Esta servicio ya está desactivado";
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (error != null) {
                            showAlertError();
                            switchCompat.setChecked(true);
                        }
                        switchCompat.setChecked(false);
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
