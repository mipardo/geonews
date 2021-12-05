package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import es.uji.geonews.R;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.services.ServiceName;

public class RemoveLocation extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final Context context;
    private final int locationId;
    private final View view;
    private String error;


    public RemoveLocation(GeoNewsManager geoNewsManager, Context context, int locationId, View view){
        this.geoNewsManager = geoNewsManager;
        this.context = context;
        this.locationId = locationId;
        this.view = view;
    }

    @Override
    public void execute() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean res = geoNewsManager.removeLocation(locationId);
                if (!res) error = "No se puede dar de baja un ubicación activa";
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
        builder.setMessage("Si realmente desea eliminar la ubicación primero desactivela");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
