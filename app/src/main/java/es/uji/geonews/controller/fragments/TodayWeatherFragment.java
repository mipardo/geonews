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
import es.uji.geonews.controller.tasks.AddLocation;
import es.uji.geonews.controller.tasks.GetActualForecastData;
import es.uji.geonews.controller.tasks.GetActualWeatherData;
import es.uji.geonews.controller.tasks.UserTask;
import es.uji.geonews.controller.template.WeatherTemplate;

public class TodayWeatherFragment extends Fragment {
    private int locationId;
    private WeatherTemplate weatherTemplate;
    private LineChart lineChart;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TodayWeatherFragment(int locationId) {
        // Required empty public constructor
        this.locationId = locationId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_weather, container, false);

        lineChart = view.findViewById(R.id.todayChart);
        weatherTemplate = new WeatherTemplate();
        weatherTemplate.setDateTextview(view.findViewById(R.id.dateTextview));
        weatherTemplate.setMaxTempTextview(view.findViewById(R.id.maxTempTextview));
        weatherTemplate.setMinTempTextview(view.findViewById(R.id.minTempTextview));
        weatherTemplate.setActualTempTextview(view.findViewById(R.id.actualTempTextview));
        weatherTemplate.setWeatherDescriptionTextview(view.findViewById(R.id.actualWeatherDescriptionTextview));
        weatherTemplate.setWeatherIcon(view.findViewById(R.id.actualWeatherIconTextview));
        weatherTemplate.setProgressBar(view.findViewById(R.id.today_progress_bar));

        new GetActualWeatherData(locationId, weatherTemplate, getContext()).execute();
        new GetActualForecastData(locationId, lineChart, getContext(), weatherTemplate.getProgressBar()).execute();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}