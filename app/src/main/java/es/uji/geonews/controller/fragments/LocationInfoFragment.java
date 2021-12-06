package es.uji.geonews.controller.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import es.uji.geonews.R;
import es.uji.geonews.controller.tasks.AddLocation;
import es.uji.geonews.controller.tasks.AddLocationByGPS;
import es.uji.geonews.controller.tasks.AddServiceToLocation;
import es.uji.geonews.controller.tasks.DeactivateLocation;
import es.uji.geonews.controller.tasks.EditLocationAlias;
import es.uji.geonews.controller.tasks.RemoveLocation;
import es.uji.geonews.controller.tasks.RemoveServiceFromLocation;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceName;

public class LocationInfoFragment extends Fragment {
    private GeoNewsManager geoNewsManager;
    private Button deactivateLocation;
    private Button deleteLocation;
    private ImageButton editAliasButton;
    private TextView locationAliasOutput;
    private int locationId;
    private @SuppressLint("UseSwitchCompatOrMaterialCode") Switch weatherServiceSwitch;
    private @SuppressLint("UseSwitchCompatOrMaterialCode") Switch airServiceSwitch;
    private @SuppressLint("UseSwitchCompatOrMaterialCode") Switch currentsServiceSwitch;
    List<ServiceName> activeServices;


    public LocationInfoFragment() {
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

        View view = inflater.inflate(R.layout.fragment_location_settings, container, false);

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
        weatherServiceSwitch = view.findViewById(R.id.open_weather_service_switch);
        airServiceSwitch = view.findViewById(R.id.air_visual_service_switch);
        currentsServiceSwitch = view.findViewById(R.id.currents_service_switch);
        deactivateLocation = view.findViewById(R.id.deactivate_location);
        deleteLocation = view.findViewById(R.id.delete_location);
        editAliasButton = view.findViewById(R.id.location_alias_button);

        if (!location.getAlias().equals("")) locationAliasOutput.setText(location.getAlias());
        if (location.getPlaceName() != null) locationPlaceNameOutput.setText(location.getPlaceName());
        locationCoordsOutput.setText(location.getGeographCoords().toString());
        activeServices = geoNewsManager.getServicesOfLocation(locationId);
        if (activeServices.contains(ServiceName.OPEN_WEATHER)) weatherServiceSwitch.setChecked(true);
        if (activeServices.contains(ServiceName.AIR_VISUAL)) airServiceSwitch.setChecked(true);
        if (activeServices.contains(ServiceName.CURRENTS)) currentsServiceSwitch.setChecked(true);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        weatherServiceSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeServices.contains(ServiceName.OPEN_WEATHER)){
                    new RemoveServiceFromLocation(getContext(), ServiceName.OPEN_WEATHER, locationId)
                            .execute();
                } else {
                    new AddServiceToLocation(getContext(), ServiceName.OPEN_WEATHER, locationId, weatherServiceSwitch)
                            .execute();
                }
            }
        });

        airServiceSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeServices.contains(ServiceName.AIR_VISUAL)){
                    new RemoveServiceFromLocation(getContext(), ServiceName.AIR_VISUAL, locationId)
                            .execute();
                } else {
                    new AddServiceToLocation(getContext(), ServiceName.AIR_VISUAL, locationId, airServiceSwitch)
                            .execute();
                }
            }
        });

        currentsServiceSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeServices.contains(ServiceName.CURRENTS)){
                    new RemoveServiceFromLocation(getContext(), ServiceName.CURRENTS, locationId)
                            .execute();
                } else {
                    new AddServiceToLocation(getContext(), ServiceName.CURRENTS, locationId, currentsServiceSwitch)
                            .execute();
                }
            }
        });

        deactivateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeactivateLocation(getContext(), locationId).execute();
            }
        });

        deleteLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RemoveLocation(getContext(), locationId, view).execute();
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