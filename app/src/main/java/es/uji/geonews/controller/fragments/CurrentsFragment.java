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
import es.uji.geonews.controller.adapters.CurrentsAdapter;
import es.uji.geonews.controller.tasks.GetActualCurrentsData;
import es.uji.geonews.controller.tasks.GetOfflineCurrentsData;
import es.uji.geonews.model.data.News;

public class CurrentsFragment extends Fragment {
    private final int locationId;

    public CurrentsFragment(int locationId) {
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

        View view = inflater.inflate(R.layout.fragment_currents, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.currents_recycler_view);
        ProgressBar progressBar = view.findViewById(R.id.progress_bar);

        List<News> news = new ArrayList<>();
        recyclerView.setAdapter(new CurrentsAdapter(news));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        new GetOfflineCurrentsData(locationId, recyclerView, progressBar, getContext()).execute();
        new GetActualCurrentsData(locationId, recyclerView, progressBar, getContext()).execute();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}