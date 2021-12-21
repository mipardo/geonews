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

    public void fillChart(int aqiUs){
        // Generate dataset
        ArrayList<PieEntry> data = new ArrayList<>();
        data.add(new PieEntry(aqiUs, ""));
        data.add(new PieEntry(300 - aqiUs , ""));

        // Assign colors:
        int[] colorData = new int[] {
                getAqiColor(aqiUs),
                Color.rgb(143, 180, 189)
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
        pieChart.invalidate();
    }

    private int getAqiColor(int aqiUs){
        int color;
        if (aqiUs < 51) color = Color.rgb(98, 231, 19);
        else if (aqiUs < 101) color = Color.rgb(179, 231, 19);
        else if (aqiUs < 151) color = Color.rgb(245, 226, 0);
        else if (aqiUs < 201) color = Color.rgb(231, 168, 19);
        else if (aqiUs < 251) color = Color.rgb(231, 119, 19);
        else color = Color.rgb(231, 19, 19);
        return color;
    }
}
