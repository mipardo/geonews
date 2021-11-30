package es.uji.geonews.controller.tasks;

import static androidx.test.InstrumentationRegistry.getContext;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uji.geonews.controller.LocationListAdapter;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;

public class AddLocationByGPS extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final Context context;
    private final RecyclerView recyclerView;
    private final ProgressBar progressBar;
    private String error;

    public AddLocationByGPS(GeoNewsManager geoNewsManager, ProgressBar progressBar, Context context, RecyclerView recyclerView){
        this.geoNewsManager = geoNewsManager;
        this.context = context;
        this.recyclerView = recyclerView;
        this.progressBar = progressBar;
    }

    @Override
    public void execute() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Location location = geoNewsManager.addLocationByGps();
                } catch (UnrecognizedPlaceNameException | ServiceNotAvailableException | NotValidCoordinatesException | GPSNotAvailableException e) {
                    error = e.getMessage();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (error != null) showAlertError();
                        else{
                            List<Location> locations = geoNewsManager.getNonActiveLocations();
                            LocationListAdapter adapter = ((LocationListAdapter) recyclerView.getAdapter());
                            if (adapter != null) adapter.updateLocations(locations);
                        }
                        progressBar.setVisibility(View.INVISIBLE);
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
