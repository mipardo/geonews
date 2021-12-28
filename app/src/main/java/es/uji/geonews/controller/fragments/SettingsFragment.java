package es.uji.geonews.controller.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import es.uji.geonews.R;
import es.uji.geonews.controller.tasks.ActivateService;
import es.uji.geonews.controller.tasks.DeactivateService;
import es.uji.geonews.controller.tasks.ImportConfiguration;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class SettingsFragment extends Fragment {

    private GeoNewsManager geoNewsManager;

    private Button buttonMasInfoAir;
    private Button buttonMasInfoOpen;
    private Button buttonMasInfoCurrents;
    private Button exportButton;
    private Button importButton;

    private SwitchCompat switchAir;
    private SwitchCompat switchOpen;
    private SwitchCompat switchCurrents;

    private ConstraintLayout loadingLayout;

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
        loadingLayout = getActivity().findViewById(R.id.greyLayout);
        List<ServiceName> activeServices = geoNewsManager.getActiveServices();

        if (activeServices.contains(ServiceName.OPEN_WEATHER)) switchOpen.setChecked(true);
        if (activeServices.contains(ServiceName.AIR_VISUAL)) switchAir.setChecked(true);
        if (activeServices.contains(ServiceName.CURRENTS)) switchCurrents.setChecked(true);

        buttonMasInfoAir = view.findViewById(R.id.buttonMasinfoAir);
        buttonMasInfoOpen = view.findViewById(R.id.buttonMasinfoOpen);
        buttonMasInfoCurrents = view.findViewById(R.id.buttonMasinfoCurrents);
        exportButton = view.findViewById(R.id.exportButton);
        importButton = view.findViewById(R.id.importButton);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout settings = getActivity().findViewById(R.id.settings);
        settings.setVisibility(View.GONE);

        switchAir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchAir.isChecked()) {
                    new ActivateService(ServiceName.AIR_VISUAL, switchAir, getContext(), loadingLayout).execute();
                } else {
                    new DeactivateService(ServiceName.AIR_VISUAL, switchAir, getContext()).execute();
                }
            }
        });

        switchOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchOpen.isChecked()) {
                    new ActivateService(ServiceName.OPEN_WEATHER, switchOpen, getContext(), loadingLayout).execute();
                } else {
                    new DeactivateService(ServiceName.OPEN_WEATHER, switchOpen, getContext()).execute();
                }
            }
        });

        switchCurrents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchCurrents.isChecked()) {
                    new ActivateService(ServiceName.CURRENTS, switchCurrents, getContext(), loadingLayout).execute();
                } else {
                    new DeactivateService(ServiceName.CURRENTS, switchCurrents, getContext()).execute();
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

        exportButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                LinearLayoutCompat layout = view.findViewById(R.id.export_description);
                TextView description = view.findViewById(R.id.export_textview);
                expandAndShrinkExportView(layout, exportButton);
                description.setText("Su código de exportación es: " + geoNewsManager.generateExportCode());
            }
        });

        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geoNewsManager.loadAllSharedCodes();
                showImportDialog(view);
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

    public void expandAndShrinkExportView(LinearLayoutCompat layout, Button button) {
        if (layout.getVisibility() == View.VISIBLE) {
            layout.animate()
                    .alpha(0.0f)
                    .setDuration(500);
            layout.setVisibility(View.GONE);
        } else {
            layout.animate()
                    .alpha(1.0f)
                    .setDuration(500);
            layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        RelativeLayout settings = getActivity().findViewById(R.id.settings);
        settings.setVisibility(View.VISIBLE);
    }

    private void showExportDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Código de exportación ");
        builder.setMessage("Su código de exportación es: \n\n" + geoNewsManager.loadUserId(view.getContext()));
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showImportDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Importar configuración");
        builder.setMessage("Introduzca el código de la configuración que desea importar");
        EditText codeInput = new EditText(view.getContext());
        builder.setView(codeInput);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String code = codeInput.getText().toString();
                if (code.length() > 0){
                    new ImportConfiguration(code, loadingLayout, getContext()).execute();
                }
            }
        });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}