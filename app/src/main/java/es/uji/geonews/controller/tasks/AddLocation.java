package es.uji.geonews.controller.tasks;

import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;

public class AddLocation extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final ProgressBar progressBar;
    private final EditText locationInput;
    private final TextView placeNameOutput;
    private final TextView coordsOutput;
    private Location newLocation;
    private String result;

    public AddLocation(GeoNewsManager geoNewsManager, ProgressBar progressBar,
                       EditText locationInput, TextView placeNameOutput, TextView coordsOutput){
        this.geoNewsManager = geoNewsManager;
        this.progressBar = progressBar;
        this.locationInput = locationInput;
        this.placeNameOutput = placeNameOutput;
        this.coordsOutput = coordsOutput;
    }

    @Override
    public void execute() {
        progressBar.setVisibility(View.VISIBLE);
        String location = locationInput.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    newLocation = geoNewsManager.addLocation(location);
                } catch (UnrecognizedPlaceNameException | ServiceNotAvailableException | NotValidCoordinatesException e) {
                    result = e.getMessage();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (result == null) {
                            placeNameOutput.setText(newLocation.getPlaceName());
                            coordsOutput.setText(newLocation.getGeographCoords().toString());
                        }
                        else placeNameOutput.setText(result);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).start();
    }
}
