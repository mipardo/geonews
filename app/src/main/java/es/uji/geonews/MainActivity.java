package es.uji.geonews;

import android.os.AsyncTask;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.services.GeocodeService;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;
    private EditText editText;
    private GeocodeService service;
    private ProgressBar progressBar;
    private GeographCoords geographCoords;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        service = new GeocodeService();

        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView2);
        editText = findViewById(R.id.locationInput);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
                String location = editText.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            geographCoords = service.getCoords(location);
                        } catch (UnrecognizedPlaceNameException | ServiceNotAvailableException e) {
                            result = e.getMessage();
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (result == null) textView.setText(geographCoords.toString());
                                else textView.setText(result);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }).start();
            }
        });



//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                progressBar = findViewById(R.id.progressBar);
//                String location = editText.getText().toString();
//                progressBar.setVisibility(View.VISIBLE);
//                try {
//                    geographCoords = service.getCoordsAsync(location);
//                } catch (Throwable e) {
//                    e.printStackTrace();
//                }
//                textView.setText(geographCoords.toString());
//                progressBar.setVisibility(View.INVISIBLE);
//            }
//        });
    }
}