package es.uji.geonews.controller.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import es.uji.geonews.R;
import es.uji.geonews.controller.tasks.ActivateLocation;
import es.uji.geonews.controller.tasks.AddServiceToLocation;
import es.uji.geonews.controller.tasks.DeactivateLocation;
import es.uji.geonews.controller.tasks.EditLocationAlias;
import es.uji.geonews.controller.tasks.RemoveLocation;
import es.uji.geonews.controller.tasks.RemoveServiceFromLocation;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class NonActiveLocationInfoFragment extends Fragment {
    private GeoNewsManager geoNewsManager;
    private Button activateLocation;
    private Button deleteLocation;
    private ImageView editAliasButton;
    private TextView locationAliasOutput;
    private ProgressBar progressBar;
    private int locationId;
    List<ServiceName> activeServices;


    public NonActiveLocationInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        geoNewsManager = GeoNewsManagerSingleton.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_non_active_location_settings, container, false);

        locationId = getArguments().getInt("locationId");
        Location location  = null;
        try {
            location = geoNewsManager.getLocation(locationId);
        } catch (NoLocationRegisteredException e) {
            e.printStackTrace();
        }

        TextView locationPlaceNameOutput = view.findViewById(R.id.location_placename_output);
        TextView locationCoordsOutput = view.findViewById(R.id.location_coords_output);
        locationAliasOutput = view.findViewById(R.id.location_alias_output);
        activateLocation = view.findViewById(R.id.activate_location);
        deleteLocation = view.findViewById(R.id.delete_location);
        editAliasButton = view.findViewById(R.id.location_alias_button);
        progressBar = view.findViewById(R.id.activate_location_progress_bar);

        if (!location.getAlias().equals("")) locationAliasOutput.setText(location.getAlias());
        if (location.getPlaceName() != null) locationPlaceNameOutput.setText(location.getPlaceName());
        locationCoordsOutput.setText(location.getGeographCoords().toString());
        activeServices = geoNewsManager.getServicesOfLocation(locationId);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                new ActivateLocation(getContext(), locationId, progressBar,view).execute();
            }
        });

        deleteLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("¿Seguro que desea eliminar la ubicación?");
                builder.setMessage("No la podrá recuperar");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new RemoveLocation(getContext(), locationId, view).execute();
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        editAliasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Asigna o edita el alias para esta ubicación ");
                View viewInflated = LayoutInflater.from(view.getContext()).inflate(R.layout.set_alias_alert, view.findViewById(R.id.new_alias_input),false);
                EditText newAlias = viewInflated.findViewById(R.id.new_alias_input);
                builder.setView(viewInflated);

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new EditLocationAlias(getContext(), locationId,
                                newAlias.getText().toString(), locationAliasOutput).execute();
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }
}