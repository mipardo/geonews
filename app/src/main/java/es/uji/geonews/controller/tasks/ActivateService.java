package es.uji.geonews.controller.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.widget.SwitchCompat;

import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class ActivateService extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final ServiceName serviceName;
    private final SwitchCompat switchCompat;
    private final ProgressBar progressBar;
    private final Context context;
    private String error;

    public ActivateService (ServiceName serviceName, SwitchCompat switchCompat, Context context, ProgressBar progressBar) {
        this.serviceName = serviceName;
        this.switchCompat = switchCompat;
        geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.context = context;
        this.progressBar = progressBar;
    }

    @Override
    public void execute() {
        lockUI(context, progressBar);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean res = geoNewsManager.activateService(serviceName);
                if (!res) error = "Esta servicio ya está activado";
                runOnUiThread(new Runnable() {
                    public void run() {
                        unlockUI(context, progressBar);
                        if (error != null) {
                            showAlertError();
                            switchCompat.setChecked(false);
                        }
                        switchCompat.setChecked(true);
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
