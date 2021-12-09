package es.uji.geonews.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.uji.geonews.R;
import es.uji.geonews.controller.tasks.UserTask;
import es.uji.geonews.controller.template.CurrentsTemplate;

public class CurrentsFragment extends Fragment {
    private int locationId;
    private CurrentsTemplate currentsTemplate;
    private CurrentsTemplate currentsTemplate2;
    private CurrentsTemplate currentsTemplate3;

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
        return inflater.inflate(R.layout.fragment_currents_v2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentsTemplate = new CurrentsTemplate();
        currentsTemplate.setCardview(view.findViewById(R.id.cardView1));
        currentsTemplate.setLugerTextview(view.findViewById(R.id.textviewLugar));
        currentsTemplate.setDateTextview(view.findViewById(R.id.dateTextview));
        currentsTemplate.setTituloTextview(view.findViewById(R.id.textTitular1));
        currentsTemplate.setCategoryTextview(view.findViewById(R.id.textviewCategory1));
        currentsTemplate.setAuthorTextview(view.findViewById(R.id.textAuthor1));
        currentsTemplate.setDescripcionTextview(view.findViewById(R.id.textDescripcion1));
        currentsTemplate.setImageView(view.findViewById(R.id.imageView1));
        currentsTemplate.setProgressBar(view.findViewById(R.id.progress_bar2));
        currentsTemplate.setAviso(view.findViewById(R.id.textViewAviso));

        currentsTemplate2 = new CurrentsTemplate();
        currentsTemplate2.setCardview(view.findViewById(R.id.cardView2));
        currentsTemplate2.setTituloTextview(view.findViewById(R.id.textTitular2));
        currentsTemplate2.setCategoryTextview(view.findViewById(R.id.textviewCategory2));
        currentsTemplate2.setAuthorTextview(view.findViewById(R.id.textAuthor2));
        currentsTemplate2.setDescripcionTextview(view.findViewById(R.id.textDescripcion2));
        currentsTemplate2.setImageView(view.findViewById(R.id.imageView2));

        currentsTemplate3 = new CurrentsTemplate();
        currentsTemplate3.setCardview(view.findViewById(R.id.cardView3));
        currentsTemplate3.setTituloTextview(view.findViewById(R.id.textTitular3));
        currentsTemplate3.setCategoryTextview(view.findViewById(R.id.textviewCategory3));
        currentsTemplate3.setAuthorTextview(view.findViewById(R.id.textAuthor3));
        currentsTemplate3.setDescripcionTextview(view.findViewById(R.id.textDescripcion3));
        currentsTemplate3.setImageView(view.findViewById(R.id.imageView3));


        UserTask getActualCurrentsData = new GetActualCurrentsData(locationId, currentsTemplate,currentsTemplate2, currentsTemplate3, getContext());
        //TODO: PROPONER
        //getActualCurrentsData.actualiza();
        getActualCurrentsData.execute();
    }
}