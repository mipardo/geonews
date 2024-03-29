package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

import androidx.navigation.Navigation;

import es.uji.geonews.R;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;

public class DeactivateLocation extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final Context context;
    private final int locationId;
    private final View view;
    private String error;


    public DeactivateLocation(Context context, int locationId, View view){
        this.geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.context = context;
        this.locationId = locationId;
        this.view = view;
    }

    @Override
    public void execute() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean res = geoNewsManager.deactivateLocation(locationId);
                if (!res) error = "Esta ubicación ya está desactivada";
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (error != null) showAlertError();
                        Navigation.findNavController(view).navigate(R.id.locationListFragment);
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
