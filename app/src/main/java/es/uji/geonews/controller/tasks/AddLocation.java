package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import es.uji.geonews.controller.LocationListAdapter;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;

public class AddLocation extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final ProgressBar progressBar;
    private final String location;
    private final Context context;
    private final RecyclerView recyclerView;
    private String error;

    public AddLocation(GeoNewsManager geoNewsManager, String location, ProgressBar progressBar, Context context, RecyclerView recyclerView){
        this.geoNewsManager = geoNewsManager;
        this.location = location;
        this.progressBar = progressBar;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    public void execute() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    geoNewsManager.addLocation(location);
                } catch (UnrecognizedPlaceNameException | ServiceNotAvailableException | NotValidCoordinatesException e) {
                    error = e.getMessage();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (error != null) showAlertError();
                        else{
                            List<Location> locations = geoNewsManager.getNonActiveLocations();
                            recyclerView.setAdapter(new LocationListAdapter(locations));
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
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
