package com.example.geniousmicro.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.geniousmicro.MainActivity;
import com.example.geniousmicro.R;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminAnalyticsActivity extends AppCompatActivity {
    private BarChart barChart;
    private PieChart pieChart;

    // Financial data
    private final float totalShare = 1200f;
    private final float totalPolicy = 1233f;
    private final float loanRec = 5688f;
    private final float sbDeposit = 7452f;
    private final float serviceTax = 6565f;
    private final float processingFees = 3458f;

    // Labels for the financial data
    private final String[] labels = new String[] {
            "Total Share",
            "Total Policy",
            "Loan Rec",
            "SB Deposit",
            "Service Tax",
            "Processing Fees"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_analytics);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize charts
        barChart = findViewById(R.id.barChart);
        pieChart = findViewById(R.id.pieChart);

        // Set up the bar chart
        setupBarChart();

        // Set up the pie chart
        setupPieChart();
    }

    private void setupBarChart() {
        // Create bar entries
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, totalShare));
        entries.add(new BarEntry(1f, totalPolicy));
        entries.add(new BarEntry(2f, loanRec));
        entries.add(new BarEntry(3f, sbDeposit));
        entries.add(new BarEntry(4f, serviceTax));
        entries.add(new BarEntry(5f, processingFees));

        // Create a data set and customize it
        BarDataSet dataSet = new BarDataSet(entries, "Financial Data");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);

        // Create bar data
        BarData barData = new BarData(dataSet);

        // Customize the bar chart
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true);

        // Customize X axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(false);
        xAxis.setLabelRotationAngle(-45f);

        // Customize Y axis
        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getAxisRight().setEnabled(false);

        // Animation and legend
        barChart.animateY(1500);
        barChart.getLegend().setEnabled(true);

        barChart.invalidate();
    }

    private void setupPieChart() {
        // Create pie entries
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(totalShare, "Total Share"));
        entries.add(new PieEntry(totalPolicy, "Total Policy"));
        entries.add(new PieEntry(loanRec, "Loan Rec"));
        entries.add(new PieEntry(sbDeposit, "SB Deposit"));
        entries.add(new PieEntry(serviceTax, "Service Tax"));
        entries.add(new PieEntry(processingFees, "Processing Fees"));

        // Create a data set and customize it
        PieDataSet dataSet = new PieDataSet(entries, "Financial Data");

        // Create custom color list
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(64, 89, 128));
        colors.add(Color.rgb(149, 165, 124));
        colors.add(Color.rgb(217, 184, 162));
        colors.add(Color.rgb(191, 134, 134));
        colors.add(Color.rgb(179, 48, 80));
        colors.add(Color.rgb(93, 35, 71));

        dataSet.setColors(colors);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setValueTextSize(12f);

        // Create pie data
        PieData pieData = new PieData(dataSet);

        // Customize the pie chart
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleRadius(35f);
        pieChart.setDrawEntryLabels(false);

        // Animation and legend
        pieChart.animateY(1500);
        pieChart.getLegend().setEnabled(true);

        pieChart.invalidate();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        startActivity(new Intent(AdminAnalyticsActivity.this, AdminDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();


    }

}