package es.uji.geonews.controller.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import es.uji.geonews.R;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class SettingsFragment extends Fragment {

    private GeoNewsManager geoNewsManager;

    private Button buttonMasInfoAir;
    private Button buttonMasInfoOpen;
    private Button buttonMasInfoCurrents;

    SwitchCompat switchAir;
    SwitchCompat switchOpen;
    SwitchCompat switchCurrents;

    public SettingsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        switchAir = view.findViewById(R.id.switch1);
        switchOpen = view.findViewById(R.id.switch2);
        switchCurrents = view.findViewById(R.id.switch3);
        List<ServiceName> activeServices = geoNewsManager.getActiveServices();

        if (activeServices.contains(ServiceName.OPEN_WEATHER)) switchOpen.setChecked(true);
        if (activeServices.contains(ServiceName.AIR_VISUAL)) switchAir.setChecked(true);
        if (activeServices.contains(ServiceName.CURRENTS)) switchCurrents.setChecked(true);

        buttonMasInfoAir = view.findViewById(R.id.buttonMasinfoAir);
        buttonMasInfoOpen = view.findViewById(R.id.buttonMasinfoOpen);
        buttonMasInfoCurrents = view.findViewById(R.id.buttonMasinfoCurrents);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout settings = getActivity().findViewById(R.id.settings);
        settings.setVisibility(View.GONE);


        switchAir.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // Show the dialog
                if (switchAir.isChecked()) {
                    geoNewsManager.activateService(ServiceName.AIR_VISUAL);
                } else {
                    geoNewsManager.deactivateService(ServiceName.AIR_VISUAL);
                }
            }
        });

        switchOpen.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // Show the dialog
                if (switchOpen.isChecked()) {
                    geoNewsManager.activateService(ServiceName.OPEN_WEATHER);
                } else {
                    geoNewsManager.deactivateService(ServiceName.OPEN_WEATHER);
                }
            }
        });

        switchCurrents.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // Show the dialog
                if (switchCurrents.isChecked()) {
                    geoNewsManager.activateService(ServiceName.CURRENTS);
                } else {
                    geoNewsManager.deactivateService(ServiceName.CURRENTS);
                }
            }
        });

        buttonMasInfoAir.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                LinearLayoutCompat layout = view.findViewById(R.id.air_visual_description);
                TextView description = view.findViewById(R.id.air_description_textview);
                expandAndShrinkView(layout, buttonMasInfoAir);
                description.setText(geoNewsManager.getService(ServiceName.AIR_VISUAL).getDescription());
            }
        });
        buttonMasInfoOpen.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                LinearLayoutCompat layout = view.findViewById(R.id.open_weather_description);
                TextView description = view.findViewById(R.id.open_description_textview);
                expandAndShrinkView(layout, buttonMasInfoOpen);
                description.setText(geoNewsManager.getService(ServiceName.OPEN_WEATHER).getDescription());
            }
        });

        buttonMasInfoCurrents.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                LinearLayoutCompat layout = view.findViewById(R.id.currents_description);
                TextView description = view.findViewById(R.id.currents_description_textview);
                expandAndShrinkView(layout, buttonMasInfoCurrents);
                description.setText(geoNewsManager.getService(ServiceName.CURRENTS).getDescription());
            }
        });
    }

    public void expandAndShrinkView(LinearLayoutCompat layout, Button button) {
        if (layout.getVisibility() == View.VISIBLE) {
            layout.animate()
                    .alpha(0.0f)
                    .setDuration(500);
            layout.setVisibility(View.GONE);
            button.setText(R.string.more_info);
        } else {
            layout.animate()
                    .alpha(1.0f)
                    .setDuration(500);
            layout.setVisibility(View.VISIBLE);
            button.setText(R.string.less_info);
        }
    }
}