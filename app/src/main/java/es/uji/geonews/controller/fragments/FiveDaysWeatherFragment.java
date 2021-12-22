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
import android.widget.RelativeLayout;

import java.util.ArrayList;

import es.uji.geonews.R;
import es.uji.geonews.controller.adapters.FiveDaysForecastAdapter;
import es.uji.geonews.controller.tasks.GetOpenWeatherFiveDaysOfflineData;

public class FiveDaysWeatherFragment extends Fragment {
    private final int locationId;
    private RecyclerView recyclerView;

    public FiveDaysWeatherFragment(int locationId) {
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
        RelativeLayout settings =  getActivity().findViewById(R.id.settings);
        settings.setVisibility(View.VISIBLE);

        View view = inflater.inflate(R.layout.fragment_five_days_weather, container, false);
        recyclerView = view.findViewById(R.id.five_days_recycler_view);

        recyclerView.setAdapter(new FiveDaysForecastAdapter(new ArrayList<>()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        new GetOpenWeatherFiveDaysOfflineData(locationId, recyclerView, getContext()).execute();
        //new GetOpenWeatherFiveDaysData(locationId, recyclerView, getContext()).execute();
        // TODO como cargamos el CHART en Today, squí ya debería de haber datos en el offline
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}