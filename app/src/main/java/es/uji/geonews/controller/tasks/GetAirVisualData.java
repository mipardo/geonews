package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;

import es.uji.geonews.controller.charts.AirVisualPieChart;
import es.uji.geonews.controller.template.AirTemplate;
import es.uji.geonews.model.data.AirVisualData;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class GetAirVisualData extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final ViewGroup loadingLayout;
    private final Context context;
    private final AirTemplate airTemplate;
    private final PieChart pieChart;
    private final int locationId;
    private AirVisualData airVisualData;
    private String error;

    public GetAirVisualData(int locationId, AirTemplate airTemplate, ViewGroup loadingLayout, PieChart pieChart, Context context){
        this.geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.loadingLayout = loadingLayout;
        this.pieChart = pieChart;
        this.context = context;
        this.locationId = locationId;
        this.airTemplate = airTemplate;
    }

    @Override
    public void execute() {
        showLoadingAnimation(loadingLayout);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    airVisualData = (AirVisualData) geoNewsManager.getData(ServiceName.AIR_VISUAL, locationId);
                    if (airVisualData == null) error = "Esta ubicación no esta suscrita al servicio de calidad del aire";
                } catch (ServiceNotAvailableException | NoLocationRegisteredException e) {
                    error = e.getMessage();
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        hideLoadingAnimation(loadingLayout);
                        if (error != null) showAlertError();
                        else {
                            AirVisualPieChart airVisualPieChart = new AirVisualPieChart(pieChart);
                            airVisualPieChart.fillChart(airVisualData.getAqiUs(), false);
                            airTemplate.getTempertaureOutput().setText(airVisualData.getTemperature() + "º C");
                            airTemplate.getPreassureOutput().setText(airVisualData.getPressure() + " hPa");
                            airTemplate.getHumidityOutput().setText(airVisualData.getHumidity() + " %");
                            airTemplate.getWindSpeedOutput().setText(airVisualData.getWindSpeed() + " m/s");
                            airTemplate.getWindDirectionOuptut().setRotation(airVisualData.getWindDirection());
                            airTemplate.getAqiMainTextOutput().setText(airVisualData.getAqiMainText());
                            airTemplate.getAqiTextOutput().setText(airVisualData.getAqiText());
                            airTemplate.getAqiImageOutput().setImageResource(airVisualData.getAqiImage());
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
