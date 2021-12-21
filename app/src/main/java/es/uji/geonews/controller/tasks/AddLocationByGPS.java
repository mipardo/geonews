package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;

import es.uji.geonews.R;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;

public class AddLocationByGPS extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final Context context;
    private final ConstraintLayout loadingLayout;
    private final View view;
    private Location newLocation;
    private String error;

    public AddLocationByGPS(ConstraintLayout loadingLayout, Context context, View view){
        this.geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.context = context;
        this.view = view;
        this.loadingLayout = loadingLayout;
    }

    @Override
    public void execute() {
        lockUI(context, loadingLayout);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    newLocation = geoNewsManager.addLocationByGps();
                    geoNewsManager.activateLocation(newLocation.getId());
                    if (newLocation == null) error = "No se ha podido dar de alta la ubicación. Pruebe más tarde";
                } catch (UnrecognizedPlaceNameException | ServiceNotAvailableException |
                        NotValidCoordinatesException | GPSNotAvailableException | NoLocationRegisteredException e) {
                    error = e.getMessage();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        unlockUI(context, loadingLayout);
                        if (error != null) showAlertError();
                        else{
                            Bundle bundle = new Bundle();
                            bundle.putInt("locationId", newLocation.getId());
                            Navigation.findNavController(view).navigate(R.id.activeLocationInfoFragment, bundle);
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
