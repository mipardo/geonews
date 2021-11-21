package es.uji.geonews.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.uji.geonews.R;
import es.uji.geonews.controller.tasks.AddLocation;
import es.uji.geonews.controller.tasks.UserTask;
import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.managers.GeoNewsManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GeoNewsManager geoNewsManager;
    private List<Location> locations;
    private EditText locationInput;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_locations);
        context = this;

        geoNewsManager = new GeoNewsManager(this);

        FloatingActionButton addLocationButton = findViewById(R.id.add_location_floating_button);
        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        ProgressBar progressBar = findViewById(R.id.my_progress_bar);

        locations = null;
        try {
            locations = geoNewsManager.getNonActiveLocations();
        } catch (NoLocationRegisteredException e) {
            locations = new ArrayList<>();
        }
        recyclerView.setAdapter(new LocationListAdapter(locations));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Añade una nueva ubicación ");
                builder.setMessage("Introduzca un topónimo o unas coordenadas");
                View viewInflated = LayoutInflater.from(context).inflate(R.layout.add_location_alert, findViewById(R.id.location_input),false);
                locationInput = viewInflated.findViewById(R.id.location_input);
                builder.setView(viewInflated);
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String location = locationInput.getText().toString();
                        UserTask addLocation = new AddLocation(geoNewsManager, location, progressBar, context, recyclerView);
                        addLocation.execute();
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    private void addRandomLocations(List<Location> locations){
        locations.add(new Location(1, "Castellón", new GeographCoords(39.97990, -0.03304), LocalDate.now()));
        locations.add(new Location(2, "Valencia", new GeographCoords(39.50337, -0.40466), LocalDate.now()));
        locations.add(new Location(3, "Bilbao", new GeographCoords(43.26270, -2.92530), LocalDate.now()));
    }
}