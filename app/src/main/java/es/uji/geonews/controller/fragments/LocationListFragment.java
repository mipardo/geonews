package es.uji.geonews.controller.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import es.uji.geonews.R;
import es.uji.geonews.controller.adapters.LocationListAdapter;
import es.uji.geonews.controller.tasks.AddLocation;
import es.uji.geonews.controller.tasks.AddLocationByGPS;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;

public class LocationListFragment extends Fragment {
    private GeoNewsManager geoNewsManager;
    private List<Location> locations;

    public LocationListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        geoNewsManager = GeoNewsManagerSingleton.getInstance(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout settings =  getActivity().findViewById(R.id.settings);
        settings.setVisibility(View.VISIBLE);

        RecyclerView recyclerView = view.findViewById(R.id.my_recycler_view);
        ImageView addLocationButton = view.findViewById(R.id.add_location_button);
        ProgressBar progressBar = view.findViewById(R.id.my_progress_bar);
        Spinner listSelector = view.findViewById(R.id.list_selector_input);
        String[] listSelections = new String[] {"Activas", "Favoritas", "No activas"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listSelections);
        listSelector.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        locations = geoNewsManager.getActiveLocations();

        listSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        locations = geoNewsManager.getActiveLocations();
                        break;
                    case 1:
                        locations = geoNewsManager.getFavouriteLocations();
                        break;
                    case 2:
                        locations = geoNewsManager.getNonActiveLocations();
                        break;
                }
                LocationListAdapter adapter = ((LocationListAdapter) recyclerView.getAdapter());
                if (adapter != null) adapter.updateLocations(locations);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        recyclerView.setAdapter(new LocationListAdapter(locations, new OnItemClickListener() {
            @Override
            public void onItemClick(Location location) {
                Bundle bundle = new Bundle();
                bundle.putInt("locationId", location.getId());
                NavController navController = Navigation.findNavController(view);
                try {
                    if(geoNewsManager.getLocation(location.getId()).isActive()){
                        navController.navigate(R.id.action_locationListFragment_to_locationFragment, bundle);
                    }
                } catch (NoLocationRegisteredException e) {
                    e.printStackTrace();
                }
            }
        }));


        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Compute the current gps coords
                geoNewsManager.updateGpsCoords();

                // Show the dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Añade una nueva ubicación ");
                builder.setMessage("Introduzca un topónimo o unas coordenadas");
                View viewInflated = LayoutInflater.from(view.getContext()).inflate(R.layout.add_location_alert, view.findViewById(R.id.location_input),false);
                EditText locationInput = viewInflated.findViewById(R.id.location_input);
                CheckBox byGpsInput = viewInflated.findViewById(R.id.by_coords_input);
                builder.setView(viewInflated);

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (byGpsInput.isChecked()){
                            new AddLocationByGPS(progressBar, getContext(), view).execute();
                        } else {
                            String location = locationInput.getText().toString();
                            new AddLocation(location, progressBar, getContext(), view).execute();
                        }
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


    }
}