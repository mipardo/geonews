package es.uji.geonews.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import es.uji.geonews.R;
import es.uji.geonews.controller.tasks.AddLocation;
import es.uji.geonews.controller.tasks.UserTask;
import es.uji.geonews.model.managers.GeoNewsManager;

public class AddLocationActivity extends AppCompatActivity {

    private Button addLocationButton;
    private TextView placeNameOutput;
    private TextView coordsOutput;
    private EditText locationInput;
    private ProgressBar progressBar;
    private GeoNewsManager geoNewsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        geoNewsManager = new GeoNewsManager();

        addLocationButton = findViewById(R.id.add_location_button);
        placeNameOutput = findViewById(R.id.place_name_output);
        coordsOutput = findViewById(R.id.coords_output);
        locationInput = findViewById(R.id.location_input);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserTask addLocation = new AddLocation(geoNewsManager, progressBar, locationInput, placeNameOutput, coordsOutput);
                addLocation.execute();
            }
        });
    }
}