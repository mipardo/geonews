package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;

import es.uji.geonews.R;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;

public class ActivateAndOpenLocationInfo extends UserTask{
    private final GeoNewsManager geoNewsManager;
    private final Context context;
    private final int locationId;
    private final View view;
    private final ConstraintLayout loadingLayout;
    private final TextView loadingTextview;
    private String error;


    public ActivateAndOpenLocationInfo(Context context, int locationId, ConstraintLayout loadingLayout, TextView loadingTextview, View view){
        this.geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.context = context;
        this.locationId = locationId;
        this.loadingLayout = loadingLayout;
        this.loadingTextview = loadingTextview;
        this.view = view;
    }

    @Override
    public void execute() {
        loadingTextview.setText("Validando servicios...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean res = geoNewsManager.activateLocation(locationId);
                    if (!res) error = "Esta ubicación ya está activada";
                } catch (NoLocationRegisteredException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        loadingTextview.setVisibility(View.GONE);
                        unlockUI(context, loadingLayout);
                        if (error != null) showAlertError();
                        else{
                            Bundle bundle = new Bundle();
                            bundle.putInt("locationId", locationId);
                            Navigation.findNavController(view).navigate(R.id.activeLocationInfoFragment, bundle);
                        }
                    }
                });
            }
        }).start();
    }

    private void showAlertError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(error);
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
