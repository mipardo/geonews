package es.uji.geonews.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;

import es.uji.geonews.R;
import es.uji.geonews.controller.tasks.GetOpenWeatherChartData;
import es.uji.geonews.controller.tasks.GetOpenWeatherChartOfflineData;
import es.uji.geonews.controller.tasks.GetOpenWeatherTomorrowData;
import es.uji.geonews.controller.tasks.GetOpenWeatherTomorrowOfflineData;
import es.uji.geonews.controller.template.WeatherTemplate;


public class TomorrowWeatherFragment extends Fragment {
    private final int locationId;
    private LineChart lineChart;
    private WeatherTemplate weatherTemplate;

    public TomorrowWeatherFragment(int locationId) {
        this.locationId = locationId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tomorrow_weather, container, false);

        lineChart = view.findViewById(R.id.tomorrowChart);
        weatherTemplate = new WeatherTemplate();
        weatherTemplate.setDateTextview(view.findViewById(R.id.dateTextview));
        weatherTemplate.setMaxTempTextview(view.findViewById(R.id.maxTempTextview));
        weatherTemplate.setMinTempTextview(view.findViewById(R.id.minTempTextview));
        weatherTemplate.setActualTempTextview(view.findViewById(R.id.actualTempTextview));
        weatherTemplate.setWeatherDescriptionTextview(view.findViewById(R.id.actualWeatherDescriptionTextview));
        weatherTemplate.setWeatherIcon(view.findViewById(R.id.actualWeatherIconTextview));

        new GetOpenWeatherTomorrowOfflineData(locationId, weatherTemplate, getContext()).execute();
        new GetOpenWeatherTomorrowData(locationId, weatherTemplate, getContext()).execute();
        new GetOpenWeatherChartOfflineData(locationId, lineChart, getContext()).execute();
        new GetOpenWeatherChartData(locationId, lineChart, getContext(), weatherTemplate.getLoadingLayout()).execute();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}