package es.uji.geonews.controller.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Igual  esto tiene que ir en el onCreateView?
        int locationId = getArguments().getInt("locationId");
        Location location  = null;
        try {
            location = geoNewsManager.getLocation(locationId);
        } catch (NoLocationRegisteredException e) {
            e.printStackTrace();
        }

        TextView locationAliasOutput = view.findViewById(R.id.location_alias_output);
        TextView locationPlaceNameOutput = view.findViewById(R.id.location_placename_output);
        TextView locationCoordsOutput = view.findViewById(R.id.location_coords_output);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch weatherServiceSwitch = view.findViewById(R.id.open_weather_service_switch);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch airServiceSwitch = view.findViewById(R.id.air_visual_service_switch);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch currentsServiceSwitch = view.findViewById(R.id.currents_service_switch);
        Button deactivateLocation = view.findViewById(R.id.deactivate_location);
        Button deleteLocation = view.findViewById(R.id.delete_location);
        ImageButton editAliasButton = view.findViewById(R.id.location_alias_button);

        if (!location.getAlias().equals("")) locationAliasOutput.setText(location.getAlias());
        if (location.getPlaceName() != null) locationPlaceNameOutput.setText(location.getPlaceName());
        locationCoordsOutput.setText(location.getGeographCoords().toString());
        List<ServiceName> activeServices = geoNewsManager.getServicesOfLocation(locationId);
        if (activeServices.contains(ServiceName.OPEN_WEATHER)) weatherServiceSwitch.setChecked(true);
        if (activeServices.contains(ServiceName.AIR_VISUAL)) airServiceSwitch.setChecked(true);
        if (activeServices.contains(ServiceName.CURRENTS)) currentsServiceSwitch.setChecked(true);

        weatherServiceSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeServices.contains(ServiceName.OPEN_WEATHER)){
                    new RemoveServiceFromLocation(geoNewsManager, getContext(), ServiceName.OPEN_WEATHER, locationId)
                            .execute();
                } else {
                    new AddServiceToLocation(geoNewsManager, getContext(), ServiceName.OPEN_WEATHER, locationId)
                            .execute();
                }
            }
        });

        airServiceSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeServices.contains(ServiceName.AIR_VISUAL)){
                    new RemoveServiceFromLocation(geoNewsManager, getContext(), ServiceName.AIR_VISUAL, locationId)
                            .execute();
                } else {
                    new AddServiceToLocation(geoNewsManager, getContext(), ServiceName.AIR_VISUAL, locationId)
                            .execute();
                }
            }
        });

        currentsServiceSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeServices.contains(ServiceName.CURRENTS)){
                    new RemoveServiceFromLocation(geoNewsManager, getContext(), ServiceName.CURRENTS, locationId)
                            .execute();
                } else {
                    new AddServiceToLocation(geoNewsManager, getContext(), ServiceName.CURRENTS, locationId)
                            .execute();
                }
            }
        });

        deactivateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeactivateLocation(geoNewsManager, getContext(), locationId).execute();
            }
        });

        deleteLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RemoveLocation(geoNewsManager, getContext(), locationId).execute();
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
                        new EditLocationAlias(geoNewsManager, getContext(),
                                locationId, newAlias.getText().toString(), locationAliasOutput).execute();
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }
}