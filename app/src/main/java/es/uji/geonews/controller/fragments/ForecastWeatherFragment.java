package es.uji.geonews.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import es.uji.geonews.R;
import es.uji.geonews.controller.adapters.ForecastAdapter;
import es.uji.geonews.controller.tasks.GetOpenWeatherForecastOfflineData;

public class ForecastWeatherFragment extends Fragment {
    private final int locationId;

    public ForecastWeatherFragment(int locationId) {
        // Required empty public constructor
        this.locationId = locationId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forecast_weather, container, false);

        TextView titleOutput = view.findViewById(R.id.forecast_weather_title);
        RecyclerView recyclerView = view.findViewById(R.id.five_days_recycler_view);

        recyclerView.setAdapter(new ForecastAdapter(new ArrayList<>()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        new GetOpenWeatherForecastOfflineData(locationId, titleOutput, recyclerView, getContext()).execute();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}