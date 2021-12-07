package es.uji.geonews.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.uji.geonews.R;
import es.uji.geonews.controller.tasks.GetActualWeatherData;
import es.uji.geonews.controller.tasks.UserTask;
import es.uji.geonews.controller.template.CurrentsTemplate;
import es.uji.geonews.controller.template.WeatherTemplate;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;

public class CurrentsFragment extends Fragment {
    private int locationId;
    private CurrentsTemplate currentsTemplate;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CurrentsFragment(int locationId) {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_currents, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentsTemplate = new CurrentsTemplate();
        currentsTemplate.setLugerTextview(view.findViewById(R.id.textviewLugar));
        currentsTemplate.setDateTextview(view.findViewById(R.id.dateTextview));
        currentsTemplate.setAuthorTextview(view.findViewById(R.id.textAuthor1));
        currentsTemplate.setCategoryTextview(view.findViewById(R.id.textviewCategory));
        currentsTemplate.setDescripcionTextview(view.findViewById(R.id.textDescripcion1));
        currentsTemplate.setTituloTextview(view.findViewById(R.id.textTitular1));
        currentsTemplate.setImageView(view.findViewById(R.id.imageView1));
        currentsTemplate.setProgressBar(view.findViewById(R.id.progress_bar));

        UserTask getActualCurrentsData = new GetActualCurrentsData(locationId, currentsTemplate, getContext());
        getActualCurrentsData.execute();
    }
}