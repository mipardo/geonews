package es.uji.geonews.controller;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import es.uji.geonews.R;
import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.GeoNewsManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_locations);

        List<Location> activeLocations = new ArrayList<>();
        activeLocations.add(new Location(1, "Castellón", new GeographCoords(39.97990, -0.03304), LocalDate.now()));
        activeLocations.add(new Location(2, "Valencia", new GeographCoords(39.50337, -0.40466), LocalDate.now()));
        activeLocations.add(new Location(3, "Bilbao", new GeographCoords(43.26270, -2.92530), LocalDate.now()));
        activeLocations.add(new Location(1, "Castellón", new GeographCoords(39.97990, -0.03304), LocalDate.now()));
        activeLocations.add(new Location(3, "Bilbao", new GeographCoords(43.26270, -2.92530), LocalDate.now()));
        activeLocations.add(new Location(2, "Valencia", new GeographCoords(39.50337, -0.40466), LocalDate.now()));
        activeLocations.add(new Location(3, "Bilbao", new GeographCoords(43.26270, -2.92530), LocalDate.now()));

        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setAdapter(new LocationListAdapter(activeLocations));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}