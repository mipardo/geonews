package es.uji.geonews.controller.charts;

import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class AirVisualPieChart {
    private final PieChart pieChart;

    public AirVisualPieChart(PieChart pieChart){
        this.pieChart = pieChart;
    }

    public void fillChart(int aqiUs, boolean animate){
        // Generate dataset
        ArrayList<PieEntry> data = new ArrayList<>();
        data.add(new PieEntry(aqiUs, ""));
        data.add(new PieEntry(Math.max((360 - aqiUs), 0), ""));

        // Assign colors:
        int[] colorData = new int[] {
                getAqiColor(aqiUs),
                Color.rgb(245, 245, 245)
        };

        PieDataSet pieDataSet = new PieDataSet(data, "");
        pieDataSet.setColors(colorData);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setCenterText(String.valueOf(aqiUs));
        pieChart.setSaveEnabled(true);
        pieChart.setCenterTextSize(40);
        pieChart.setHoleRadius(70);
        pieChart.setTransparentCircleRadius(60);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.getData().setDrawValues(false);
        pieChart.setEnabled(false);
        pieChart.setDrawRoundedSlices(true);
        pieChart.setRotationEnabled(false);
        if (animate) pieChart.animateY(600);
        pieChart.invalidate();
    }

    private int getAqiColor(int aqiUs){
        int color;
        if (aqiUs < 51) color = Color.rgb(170, 210, 95);
        else if (aqiUs < 101) color = Color.rgb(253, 215, 70);
        else if (aqiUs < 151) color = Color.rgb(245, 157, 86);
        else if (aqiUs < 201) color = Color.rgb(241, 107, 104);
        else if (aqiUs < 251) color = Color.rgb(161, 126, 184);
        else color = Color.rgb(159, 119, 130);
        return color;
    }
}
