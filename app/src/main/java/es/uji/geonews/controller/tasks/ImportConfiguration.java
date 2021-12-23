package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.view.ViewGroup;

import es.uji.geonews.model.exceptions.DatabaseNotAvailableException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;

public class ImportConfiguration extends UserTask {
    private String importCode;
    private String originalCode;
    private GeoNewsManager geoNewsManager;
    private ViewGroup loadingLayout;
    private Context context;
    private String error;

    public ImportConfiguration(String importCode, ViewGroup loadingLayout, Context context) {
        this.importCode = importCode;
        this.loadingLayout = loadingLayout;
        geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.originalCode = geoNewsManager.loadUserId(context);
        this.context = context;
    }

    @Override
    public void execute() {
        showLoadingAnimation(loadingLayout);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    geoNewsManager.setUserId(importCode);
                    geoNewsManager.loadAll();
                    geoNewsManager.setUserId(originalCode);
                } catch (DatabaseNotAvailableException e) {
                    error = e.getMessage();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        hideLoadingAnimation(loadingLayout);
                        if (error != null) showAlertError();
                        else
                        {
                            showConfirmation();
                        }
                    }

                });
            }

        }).start();
    }

    private void showAlertError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error al importar configuración");
        builder.setMessage("El código es incorrecto. Vuelva a intentarlo.");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showConfirmation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Éxito al importar");
        builder.setMessage("Importación realizada con éxito.");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
