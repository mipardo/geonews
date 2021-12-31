package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;

public class AddLocationByGPS extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final Context context;
    private final ConstraintLayout loadingLayout;
    private final TextView loadingTextview;
    private final View view;
    private Location newLocation;
    private String error;

    public AddLocationByGPS(ConstraintLayout loadingLayout, TextView loadingTextview, Context context, View view){
        this.geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.context = context;
        this.view = view;
        this.loadingLayout = loadingLayout;
        this.loadingTextview = loadingTextview;
    }

    @Override
    public void execute() {
        loadingTextview.setText("Añadiendo ubicación...");
        loadingTextview.setVisibility(View.VISIBLE);
        lockUI(context, loadingLayout);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    newLocation = geoNewsManager.addLocationByGps();
                    if (newLocation == null) error = "No se ha podido dar de alta la ubicación. Pruebe más tarde";
                } catch (UnrecognizedPlaceNameException | ServiceNotAvailableException |
                        NotValidCoordinatesException | GPSNotAvailableException e) {
                    error = e.getMessage();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (error != null) {
                            unlockUI(context, loadingLayout);
                            showAlertError();
                        }
                        else{
                            new ActivateAndOpenLocationInfo(context, newLocation.getId(), loadingLayout, loadingTextview, view).execute();
                        }
                    }
                });
            }
        }).start();
    }

    private void showAlertError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error al añadir una nueva ubicación ");
        builder.setMessage(error);
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
