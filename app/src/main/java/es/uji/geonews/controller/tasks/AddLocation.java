package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import es.uji.geonews.R;
import es.uji.geonews.controller.LocationListAdapter;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;

public class AddLocation extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final ProgressBar progressBar;
    private final String location;
    private final Context context;
    private final RecyclerView recyclerView;
    private final View view;
    private Location newLocation;
    private String error;

    public AddLocation(String location, ProgressBar progressBar, Context context, RecyclerView recyclerView, View view){
        this.geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.location = location;
        this.progressBar = progressBar;
        this.context = context;
        this.recyclerView = recyclerView;
        this.view = view;
    }

    @Override
    public void execute() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    newLocation = geoNewsManager.addLocation(location);
                    geoNewsManager.activateLocation(newLocation.getId());
                    if (newLocation == null) error = "No se ha podido dar de alta la ubicación. Pruebe más tarde";
                } catch (UnrecognizedPlaceNameException | ServiceNotAvailableException |
                        NotValidCoordinatesException | NoLocationRegisteredException e) {
                    error = e.getMessage();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (error != null) showAlertError();
                        else{
                            Bundle bundle = new Bundle();
                            bundle.putInt("locationId", newLocation.getId());
                            Navigation.findNavController(view).navigate(R.id.locationInfo, bundle);
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
