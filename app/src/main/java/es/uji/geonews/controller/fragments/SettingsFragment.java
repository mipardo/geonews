package es.uji.geonews.controller.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import es.uji.geonews.R;
import es.uji.geonews.controller.tasks.AddLocationByGPS;
import es.uji.geonews.controller.tasks.GetAirVisualData;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceName;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private GeoNewsManager geoNewsManager;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout settings = getActivity().findViewById(R.id.settings);
        settings.setVisibility(View.GONE);


        Switch switchAir = view.findViewById(R.id.switch1);
        Switch switchOpen = view.findViewById(R.id.switch2);
        Switch switchCurrents = view.findViewById(R.id.switch3);

        Button buttonMostrar = view.findViewById(R.id.buttonMostrar);
        Button buttonImportar = view.findViewById(R.id.buttonImportar);
        Button buttonMasInfoAir = view.findViewById(R.id.buttonMasinfoAir);
        Button buttonMasInfoOpen = view.findViewById(R.id.buttonMasinfoOpen);
        Button buttonMasInfoCurrents = view.findViewById(R.id.buttonMasinfoCurrents);
        TextView textViewIDDB = view.findViewById(R.id.id_DataBase);


        switchAir.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // Show the dialog
                if (switchAir.isChecked()) {

                    geoNewsManager.getService(ServiceName.AIR_VISUAL).activate();

                } else {

                    geoNewsManager.getService(ServiceName.AIR_VISUAL).deactivate();


                }
            }
        });

        switchOpen.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // Show the dialog
                if (switchOpen.isChecked()) {

                    geoNewsManager.getService(ServiceName.OPEN_WEATHER).activate();
                } else {

                    geoNewsManager.getService(ServiceName.OPEN_WEATHER).deactivate();
                }
            }
        });

        switchCurrents.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // Show the dialog
                if (switchCurrents.isChecked()) {

                    geoNewsManager.getService(ServiceName.CURRENTS).activate();
                } else {

                    geoNewsManager.getService(ServiceName.CURRENTS).deactivate();
                }
            }
        });

        buttonMostrar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // Show the dialog
                //String idCliente = geoNewsManager.getIdDataBase();
                textViewIDDB.setVisibility(View.VISIBLE);
                textViewIDDB.setText("idCliente");
            }
        });

        buttonImportar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Cambia tu Configuracion ");
                builder.setMessage("Introduzca un ID de configuracion");
                //TODO Mirar esto
                View viewInflated = LayoutInflater.from(view.getContext()).inflate(R.layout.add_location_alert, view.findViewById(R.id.location_input), false);
                //idInput = viewInflated.findViewById(R.id.location_input);
                builder.setView(viewInflated);
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //new SetIdDataBase(geoNewsManager, progressBar, getContext(), recyclerView).execute();

                    }
                });
                builder.setNegativeButton("Cancelar", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        buttonMasInfoAir.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("AirVisual ");
                builder.setMessage("La descripcion de Air Visual");
                //View viewInflated = LayoutInflater.from(view.getContext()).inflate(R.layout.add_location_alert, view.findViewById(R.id.location_input), false);
                //builder.setView(viewInflated);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        buttonMasInfoOpen.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Open Weather ");
                builder.setMessage("La descripcion de Open Weather");
                //View viewInflated = LayoutInflater.from(view.getContext()).inflate(R.layout.add_location_alert, view.findViewById(R.id.location_input), false);
                //builder.setView(viewInflated);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        buttonMasInfoCurrents.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Currents ");
                builder.setMessage("La descripcion de Currents");
                //View viewInflated = LayoutInflater.from(view.getContext()).inflate(R.layout.add_location_alert, view.findViewById(R.id.location_input), false);
                //builder.setView(viewInflated);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}