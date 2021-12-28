package es.uji.geonews.controller.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
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
    private List<Location> locations2;
    private List<Location> locations1;

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

        RecyclerView recyclerView = view.findViewById(R.id.my_recycler_view);
        ImageView addLocationButton = view.findViewById(R.id.add_location_button);
        ConstraintLayout loadingLayout = getActivity().findViewById(R.id.greyLayout);
        TextView loadingTextview = getActivity().findViewById(R.id.loadingDescription);
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
                        locations1 = geoNewsManager.getFavouriteLocations();
                        locations2 =geoNewsManager.getNoFavouriteLocations();
                        (locations = new ArrayList<>(locations1)).addAll(locations2);
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
                View viewInflated = LayoutInflater.from(view.getContext()).inflate(R.layout.alert_add_location, view.findViewById(R.id.location_input),false);
                AutoCompleteTextView locationInput = viewInflated.findViewById(R.id.location_input);
                ArrayAdapter<String> autoCompletePlaceNamesAdapter = new ArrayAdapter<>(v.getContext(),
                        android.R.layout.simple_list_item_1, getPlaceNames());
                locationInput.setAdapter(autoCompletePlaceNamesAdapter);
                CheckBox byGpsInput = viewInflated.findViewById(R.id.by_coords_input);
                builder.setView(viewInflated);

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (byGpsInput.isChecked()){
                            new AddLocationByGPS(loadingLayout, loadingTextview, getContext(), view).execute();
                        } else {
                            String location = locationInput.getText().toString();
                            new AddLocation(location, loadingLayout, loadingTextview, getContext(), view).execute();
                        }
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private String[] getPlaceNames(){
        return new String[]{
            "Álava", "Albacete", "Alicante", "Almería", "Ávila", "Badajoz", "Illes Balears", "Barcelona", "Burgos", "Cáceres", "Cádiz",
            "Castellón", "Ciudad Real", "Córdoba", "A Coruña", "Cuenca", "Girona", "Granada", "Guadalajara", "Gipuzkoa", "Huelva", "Huesca",
            "Jaén", "León", "Lleida", "La Rioja ", "Lugo", "Madrid", "Málaga", "Murcia", "Navarra", "Ourense", "Asturias",
            "Palencia", "Las Palmas ", "Pontevedra", "Salamanca", "Santa Cruz de Tenerife", "Cantabria", "Segovia", "Sevilla", "Soria", "Tarragona", "Teruel",
            "Toledo", "Valencia/València", "Valladolid", "Bizkaia", "Zamora", "Zaragoza", "Ceuta", "Melilla"
        };
    }
}