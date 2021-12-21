package es.uji.geonews.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import es.uji.geonews.R;
import es.uji.geonews.controller.tasks.GetAirVisualData;
import es.uji.geonews.controller.tasks.GetAirVisualOfflineData;
import es.uji.geonews.controller.template.AirTemplate;


public class AirVisualFragment extends Fragment {
    private final int locationId;

    public AirVisualFragment(int locationId) {
        this.locationId = locationId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_air_visual, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = getActivity().findViewById(R.id.my_toolbar);
        toolbar.setTitle("Calidad del aire");

        RelativeLayout settings =  getActivity().findViewById(R.id.settings);
        settings.setVisibility(View.VISIBLE);

        ProgressBar progressBar = view.findViewById(R.id.my_progress_bar);
        TextView temperatureOutput = view.findViewById(R.id.temperature_output);
        TextView humidityOutput = view.findViewById(R.id.humidity_output);
        TextView pressureOutput = view.findViewById(R.id.pressure_output);
        TextView windSpeedOutput = view.findViewById(R.id.wind_speed_output);
        ImageView windDirectionOutput = view.findViewById(R.id.wind_direction_output);
        TextView aqiMainTextOutput = view.findViewById(R.id.aqi_main_text_output);
        TextView aqiTextOutput = view.findViewById(R.id.aqi_text_output);
        ImageView aqiImageOutput = view.findViewById(R.id.aqi_image_output);
        PieChart pieChart = view.findViewById(R.id.air_visual_chart);

        AirTemplate airTemplate = new AirTemplate();
        airTemplate.setTempertaureOutput(temperatureOutput);
        airTemplate.setHumidityOutput(humidityOutput);
        airTemplate.setPreassureOutput(pressureOutput);
        airTemplate.setWindDirectionOuptut(windDirectionOutput);
        airTemplate.setWindSpeedOutput(windSpeedOutput);
        airTemplate.setAqiMainTextOutput(aqiMainTextOutput);
        airTemplate.setAqiTextOutput(aqiTextOutput);
        airTemplate.setAqiImageOutput(aqiImageOutput);

        new GetAirVisualOfflineData(locationId, airTemplate, progressBar, pieChart, getContext()).execute();
        new GetAirVisualData(locationId, airTemplate, progressBar, pieChart, getContext()).execute();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Toolbar toolbar = getActivity().findViewById(R.id.my_toolbar);
            toolbar.setTitle("Calidad del aire");
        }
        Log.e("dsadsa", "VISIBLE");
    }
}