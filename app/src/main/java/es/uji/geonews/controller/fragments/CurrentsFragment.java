package es.uji.geonews.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.auth.User;

import es.uji.geonews.R;
import es.uji.geonews.controller.tasks.UserTask;
import es.uji.geonews.controller.template.CurrentsTemplate;

public class CurrentsFragment extends Fragment {
    private int locationId;
    private CurrentsTemplate currentsTemplate;
    private CurrentsTemplate currentsTemplate2;
    private CurrentsTemplate currentsTemplate3;

    public CurrentsFragment(int locationId) {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_currents_v2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        currentsTemplate = new CurrentsTemplate();
//        currentsTemplate.setCardview(view.findViewById(R.id.cardView1));
//        currentsTemplate.setLugerTextview(view.findViewById(R.id.textviewLugar));
//        currentsTemplate.setDateTextview(view.findViewById(R.id.dateTextview));
//        currentsTemplate.setTituloTextview(view.findViewById(R.id.textTitular1));
//        currentsTemplate.setCategoryTextview(view.findViewById(R.id.textviewCategory1));
//        currentsTemplate.setAuthorTextview(view.findViewById(R.id.textAuthor1));
//        currentsTemplate.setDescripcionTextview(view.findViewById(R.id.textDescripcion1));
//        currentsTemplate.setImageView(view.findViewById(R.id.imageView1));
//        currentsTemplate.setAviso(view.findViewById(R.id.textViewAviso));


    }
}