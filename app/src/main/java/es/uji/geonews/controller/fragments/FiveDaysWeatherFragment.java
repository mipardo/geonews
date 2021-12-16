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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import es.uji.geonews.R;
import es.uji.geonews.controller.adapters.FiveDaysForecastAdapter;
import es.uji.geonews.controller.tasks.GetOpenWeatherFiveDaysData;
import es.uji.geonews.model.data.ServiceData;

public class FiveDaysWeatherFragment extends Fragment {
    private final int locationId;

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
        RecyclerView recyclerView = view.findViewById(R.id.five_days_recycler_view);
        ProgressBar progressBar = view.findViewById(R.id.my_progress_bar);

        recyclerView.setAdapter(new FiveDaysForecastAdapter(new ArrayList<>()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        new GetOpenWeatherFiveDaysData(locationId, recyclerView, progressBar, getContext()).execute();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}