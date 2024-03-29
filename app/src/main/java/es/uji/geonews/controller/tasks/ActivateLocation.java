package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;

import es.uji.geonews.R;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;

public class ActivateLocation extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final Context context;
    private final int locationId;
    private final View view;
    private final ConstraintLayout loadingLayout;
    private String error;


    public ActivateLocation(Context context, int locationId, ConstraintLayout loadingLayout, View view){
        this.geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.context = context;
        this.locationId = locationId;
        this.loadingLayout = loadingLayout;
        this.view = view;
    }

    @Override
    public void execute() {
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
                        unlockUI(context, loadingLayout);
                        if (error != null) showAlertError();
                        else{
                            Navigation.findNavController(view).navigate(R.id.locationListFragment);
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
