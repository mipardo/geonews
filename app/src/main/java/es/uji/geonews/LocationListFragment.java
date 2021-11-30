package es.uji.geonews;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import es.uji.geonews.controller.LocationListAdapter;
import es.uji.geonews.controller.OnItemClickListener;
import es.uji.geonews.controller.tasks.AddLocation;
import es.uji.geonews.controller.tasks.UserTask;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.GeoNewsManager;

public class LocationListFragment extends Fragment {
    private GeoNewsManager geoNewsManager;
    private List<Location> locations;
    private EditText locationInput;

    public LocationListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        geoNewsManager = new GeoNewsManager(getContext());

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

        FloatingActionButton addLocationButton = view.findViewById(R.id.add_location_floating_button);
        RecyclerView recyclerView = view.findViewById(R.id.my_recycler_view);
        ProgressBar progressBar = view.findViewById(R.id.my_progress_bar);

        locations = geoNewsManager.getNonActiveLocations();
        recyclerView.setAdapter(new LocationListAdapter(locations, new OnItemClickListener() {
            @Override
            public void onItemClick(Location location) {
                Bundle bundle = new Bundle();
                bundle.putInt("locationId", location.getId());
                Navigation.findNavController(view).navigate(R.id.action_locationListFragment_to_locationFragment, bundle);
            }
        }));
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Añade una nueva ubicación ");
                builder.setMessage("Introduzca un topónimo o unas coordenadas");
                View viewInflated = LayoutInflater.from(view.getContext()).inflate(R.layout.add_location_alert, view.findViewById(R.id.location_input),false);
                locationInput = viewInflated.findViewById(R.id.location_input);
                builder.setView(viewInflated);
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String location = locationInput.getText().toString();
                        UserTask addLocation = new AddLocation(geoNewsManager, location, progressBar, view.getContext(), recyclerView);
                        addLocation.execute();
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}