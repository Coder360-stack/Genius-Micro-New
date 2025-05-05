package com.example.geniousmicro.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.geniousmicro.UserData.GlobalUserData;
import com.example.geniousmicro.mssql.SqlManager;
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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.geniousmicro.LoginActivity;
import com.example.geniousmicro.R;
import com.example.geniousmicro.databinding.ActivityAdminDashboardBinding;
import com.google.android.material.snackbar.Snackbar;

public class AdminDashboardActivity extends AppCompatActivity implements View.OnClickListener {
    private BarChart barChart;
    private PieChart pieChart;
    private String St_OfficeCode="";

    // Financial data
    private final float totalShare = 1200f;
    private final float totalPolicy = 1233f;
    private final float loanRec = 5688f;
    private final float sbDeposit = 7452f;
    private final float serviceTax = 6565f;
    private final float processingFees = 3458f;
    private  ArrayList<String> officecode=new ArrayList<>();
    private  ArrayList<String> officnamewithcode=new ArrayList<>();


    // Labels for the financial data
    private final String[] labels = new String[] {
            "Total Share",
            "Total Policy",
            "Loan Rec",
            "SB Deposit",
            "Service Tax",
            "Processing Fees"
    };
    ActivityAdminDashboardBinding binding;

    private int backPressCounter = 0;
    private static final int REQUIRED_BACK_PRESS_COUNT = 2;
    private static final int BACK_PRESS_INTERVAL = 2000;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME="UserDetails";
    String USERNAME="UserName";
    String PASSWORD="Password";
    RadioGroup chartTypeGroup;
    CardView BarChartCard,PieChartCard;
    LinearLayout Fromdate,Todate;
    RadioButton barChartButton,pieChartButton;




    private int fdate = 0;
    private int tdate = 0;
    private String isApproved = "0";
    int mYear, mMonth, mDay;
    private int currentDay, currentMonth, currentYear;
    ArrayList<String> monthlist;
    private Calendar mCalendar;
    String status = "Approved";

    double TotalAmt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         chartTypeGroup = findViewById(R.id.chartTypeGroup);  // Assuming you've assigned an ID to your RadioGroup
         barChartButton = findViewById(R.id.Btn_BarChart);
         pieChartButton = findViewById(R.id.Btn_PieChart);
        BarChartCard=findViewById(R.id.BarChartCard);
        PieChartCard=findViewById(R.id.PieChartCard);
        Fromdate=findViewById(R.id.fromDateLinear);
        Todate=findViewById(R.id.toDateLinear);
        monthlist = new ArrayList<>();
        monthlist.add("Jan");
        monthlist.add("Feb");
        monthlist.add("Mar");
        monthlist.add("Apr");
        monthlist.add("May");
        monthlist.add("Jun");
        monthlist.add("Jul");
        monthlist.add("Aug");
        monthlist.add("Sep");
        monthlist.add("Oct");
        monthlist.add("Nov");
        monthlist.add("Dec");


        sp=getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor=sp.edit();
        layoutManagers();


        // Initialize charts
        barChart = findViewById(R.id.barChart);
        pieChart = findViewById(R.id.pieChart);

        binding.userCode.setText(""+ GlobalUserData.adminDataModel.getAdminID());
        getOfficeDetails();
        CurrentDate();
        Fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFDate();

            }
        });

        Todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTDate();

            }
        });


        binding.logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(AdminDashboardActivity.this);
                builder.setTitle("Are You Want To Logout");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    editor.clear();
                    editor.apply();
                    startActivity(new Intent(AdminDashboardActivity.this, LoginActivity.class));
                    finishAffinity();
                });
                builder.setNegativeButton("No", (dialog, which) -> {

                });
                builder.create().show();
            }
        });


        binding.applyFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetBarChartDetails(fdate,tdate,St_OfficeCode);

            }
        });

        StatusbarColor();


        chartTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId==R.id.Btn_BarChart){
                    BarChartCard.setVisibility(VISIBLE);
                    PieChartCard.setVisibility(GONE);
                   // Toast.makeText(AdminDashboardActivity.this, "Bar Chart selected1", Toast.LENGTH_SHORT).show();
                } else if (checkedId==R.id.Btn_PieChart) {
                     PieChartCard.setVisibility(VISIBLE);
                    BarChartCard.setVisibility(GONE);
                    //Toast.makeText(AdminDashboardActivity.this, "Bar Chart selected12", Toast.LENGTH_SHORT).show();
                }
            }
        });






        ///nomine type



        ArrayAdapter<String> mnonie_type_adpater = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, officnamewithcode);
        mnonie_type_adpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.officeSpinner.setAdapter(mnonie_type_adpater);
        binding.officeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                St_OfficeCode = officecode.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private void CurrentDate() {

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Set tdate as today's date by default

      String TodayDate= String.valueOf(Integer.toString(mYear) +"/"+ String.format("%02d", mMonth + 1) +"/"+ String.format("%02d", mDay)) ;


        tdate = Integer.parseInt(
                Integer.toString(mYear) +
                        String.format("%02d", mMonth + 1) +
                        String.format("%02d", mDay)
        );
        fdate=tdate;
        binding.fromDateText.setText(""+TodayDate);
        binding.toDateText.setText(""+TodayDate);

         GetBarChartDetails(fdate,tdate,"0");


    }

    private void GetBarChartDetails(int fromdate,int todate,String Officedate ) {

        Connection cn = new SqlManager().getSQLConnection();
        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GET_BUSINESS_SUMMARY(?,?,?,?)}");
                smt.setString("@SEARCH_TYPE", "TILLDATE");
                smt.setString("@OFFICEID", Officedate);
                smt.setInt("@FROMDATE", fromdate);
                smt.setInt("@TODATE", todate);

                smt.execute();
                ResultSet rs = smt.getResultSet();
                if (rs.isBeforeFirst()){
                    while (rs.next()) {

                        Float TOTALSHARE = Float.valueOf((rs.getString("TOTALSHARE")));
                        Float TOTALPOLICY = Float.valueOf((rs.getString("TOTALPOLICY")));
                        Float LOANREC = Float.valueOf((rs.getString("LOANREC")));
                        Float SBDEPOSIT = Float.valueOf(rs.getString("SBDEPOSIT"));
                        Float SERVICETAX = Float.valueOf(rs.getString("SERVICETAX"));
                        Float PROCESSINGFEES = Float.valueOf(rs.getString("PROCESSINGFEES"));
                        Log.e("PROCESSINGFEES",""+PROCESSINGFEES);
                        // Set up the bar chart
                        setupBarChart(TOTALSHARE,TOTALPOLICY,LOANREC,SBDEPOSIT,SERVICETAX,PROCESSINGFEES);


                        // Set up the pie chart
                        setupPieChart(TOTALSHARE,TOTALPOLICY,LOANREC,SBDEPOSIT,SERVICETAX,PROCESSINGFEES);

                    }
                }else{
                    Toast.makeText(AdminDashboardActivity.this,"No Data Found",Toast.LENGTH_LONG).show();
                }

            }else{
                Log.d("bbc", "Error ");
            }
        } catch (Exception e) {

            Log.d("bbc", ""+e);
        }

    }

    private void StatusbarColor() {

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.background_blue));

    }


    private void getOfficeDetails() {
        officnamewithcode.add("All");
        officecode.add("0");

        Connection cn = new SqlManager().getSQLConnection();
        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GET_Office()}");
                smt.execute();
                ResultSet rs = smt.getResultSet();
                if (rs.isBeforeFirst()){
                    while (rs.next()) {

                        officecode.add(rs.getString("OfficeID"));
                        officnamewithcode.add(rs.getString("OfficeID")+"-"+rs.getString("OfficeName"));



                    }
                }else{
                    //Toast.makeText(AdminDashboardActivity.this,"No Data Found",Toast.LENGTH_LONG).show();
                }

            }else{
                Log.d("bbc", "Error ");
            }
        } catch (Exception e) {

            Log.d("bbc", ""+e);
        }


    }


    @Override
    public void onClick(View view) {

        if (view == binding.tvAdminDashChainReport) {
            navigateToNextActivity(AdminAnalyticsActivity.class,"");

         /*   navigateToNextActivity(AdminDashChainReportActivity.class,"");*/
        }
        else if (view== binding.tvAdminDashNewRenewReport) {
            navigateToNextActivity(AdminDashNewRenewReportActivity.class,"");
        }
        else if (view == binding.tvAdminDashExecutiveCollectionReport) {
            navigateToNextActivity(AdminDashExecutiveCollectionReportActivity.class,"");
        }
        else if (view==binding.tvactivityAdminLoanDueReport) {
           // navigateToNextActivity(AdminLoanDueReportActivity.class,"AdminArangerWiseReport");
            navigateToNextActivityWithIntentValue(AdminLoanDueReportActivity.class,"AdminArangerWiseReport");

        }
        else if (view==binding.tvAdminDashAccountSavingsTransReport) {
            Intent intent=new Intent(getApplicationContext(),SavingsCollectionReportActivity.class);
            intent.putExtra("user","Admin");
            startActivity(intent);


        }
      /*  else if (view==binding.tvAdminDashMobileCollectionReport) {
            navigateToNextActivity(AdminDashMobileCollectionReportActivity.class,"");
        }
        else if (view == binding.AdminChainReport) {
            navigateToNextActivity(AdminChainReportActivity.class,"");
        }
        else if (view == binding.tvAdminDashExecutiveCashSheet) {
            navigateToNextActivity(AdminDashExecutiveCashSheetActivity.class,"");
        }
        else if (view==binding.tvAdminDashExecutiveBankTransReport) {
            navigateToNextActivity(AdminDashExecutiveBankTransReportActivity.class,"");
        }*/

        else if (view==binding.tvDailyPolicyCollectionRpt) {



            navigateToNextActivity(AdminPolicyCollReportActivity.class,"");
        }
        else if (view== binding.tvAdminApproveLoan) {
            Intent intent=new Intent(getApplicationContext(),SavingsCollectionReportActivity.class);
            intent.putExtra("user","Admin");
            startActivity(intent);
        } else if (view==binding.tvDailyCollectionRpt) {
            navigateToNextActivityWithIntentValue(ArrLoanCollReportActivity.class,"AdminArangerWiseReport"); //----use this
        }else if (view==binding.tdaysavings) {

            Intent intent=new Intent(getApplicationContext(),AdminTodaySavingsCollectionReportActivity.class);
            intent.putExtra("user","Admin");
            startActivity(intent);

        } else if (view==binding.TodayLoanColl) {
            navigateToNextActivityWithIntentValue(AdminTodayLoanCollReportActivity.class,"AdminArangerWiseReport"); //----use this
        }else if (view==binding.empdetails) {
            navigateToNextActivityWithIntentValue(AdminEmpDetailsActivity.class,""); //----use this
        }
        else if (view==binding.TodayPolicyColl) {
            Intent intent=new Intent(getApplicationContext(),AdminTodayPolicyCollReportActivity.class);
            startActivity(intent);
        }

        else if (view==binding.policyCollReport) {
            Intent intent=new Intent(getApplicationContext(), PolicyCollectionReportAdminActivity.class);
            startActivity(intent);
        }


    }


    private void navigateToNextActivity(Class<?> ActivityClass ,String user) {
        if (ActivityClass != null) {
            startActivity(new android.content.Intent(this, ActivityClass));
            finish();
        }

    }
    private void navigateToNextActivityWithIntentValue(Class<?> ActivityClass, String user) {
        if (ActivityClass != null) {
            Intent intent = new Intent(this, ActivityClass);
            intent.putExtra("LoanCollectionType", user); // Add the 'user' parameter as an extra
            startActivity(intent);
            finish();
        }
    }



    @Override
    public void onBackPressed() {
        backPressCounter++;

        if (backPressCounter == REQUIRED_BACK_PRESS_COUNT) {
            super.onBackPressed(); // Exit the app
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressCounter = 0;
                }
            }, BACK_PRESS_INTERVAL);
        }


    }
          private void layoutManagers(){
              binding.tvAdminDashChainReport.setOnClickListener(this);
              binding.tvAdminDashNewRenewReport.setOnClickListener(this);
              binding.tvAdminDashExecutiveCollectionReport.setOnClickListener(this);
              binding.tvactivityAdminLoanDueReport.setOnClickListener(this);
              binding.tvAdminDashAccountSavingsTransReport.setOnClickListener(this);
             // binding.tvAdminDashMobileCollectionReport.setOnClickListener(this);
              binding.AdminChainReport.setOnClickListener(this);
             // binding.tvAdminDashExecutiveCashSheet.setOnClickListener(this);
            //  binding.tvAdminDashExecutiveBankTransReport.setOnClickListener(this);
              binding.tvDailyCollectionRpt.setOnClickListener(this);
              binding.tvDailyPolicyCollectionRpt.setOnClickListener(this);
              binding.tvAdminApproveLoan.setOnClickListener(this);
              binding.tdaysavings.setOnClickListener(this);
              binding.TodayLoanColl.setOnClickListener(this);
              Fromdate.setOnClickListener(this);
              Todate.setOnClickListener(this);
              binding.applyFilterButton.setOnClickListener(this);
              binding.empdetails.setOnClickListener(this);
              binding.TodayPolicyColl.setOnClickListener(this);
              binding.policyCollReport.setOnClickListener(this);




          }

    private void setupBarChart(Float TOTALSHARE,Float TOTALPOLICY,Float LOANREC,Float SBDEPOSIT,Float SERVICETAX,Float PROCESSINGFEES) {
        // Create bar entries
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, TOTALSHARE));
        entries.add(new BarEntry(1f, TOTALPOLICY));
        entries.add(new BarEntry(2f, LOANREC));
        entries.add(new BarEntry(3f, SBDEPOSIT));
        entries.add(new BarEntry(4f, SERVICETAX));
        entries.add(new BarEntry(5f, PROCESSINGFEES));

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

    private void setupPieChart(Float TOTALSHARE,Float TOTALPOLICY,Float LOANREC,Float SBDEPOSIT,Float SERVICETAX,Float PROCESSINGFEES)  {
        // Create pie entries
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(TOTALSHARE, "Total Share"));
        entries.add(new PieEntry(TOTALPOLICY, "Total Policy"));
        entries.add(new PieEntry(LOANREC, "Loan Rec"));
        entries.add(new PieEntry(SBDEPOSIT, "SB Deposit"));
        entries.add(new PieEntry(SERVICETAX, "Service Tax"));
        entries.add(new PieEntry(PROCESSINGFEES, "Processing Fees"));

        // Create a data set and customize it
        PieDataSet dataSet = new PieDataSet(entries, "Financial Data");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(12f);





      //  dataSet.setColors(colors);
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



    private void selectTDate() {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Calendar
        mCalendar = Calendar.getInstance();
        currentDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = mCalendar.get(Calendar.MONTH);
        currentYear = mCalendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;

                binding.toDateText.setText(formatdate(dayOfMonth) + "-" + monthlist.get(month - 1) + "\n" + year);
                tdate = Integer.parseInt(Integer.toString(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth));
//                getMemberDetails("all", "k");
            }
        }, currentYear, currentMonth, currentDay);
        mCalendar.set(currentYear, currentMonth, currentDay);

        datePickerDialog.getDatePicker().setMaxDate(mCalendar.getTimeInMillis());
        datePickerDialog.show();

    }
    private void showSuccess(String message) {
        Snackbar.make(findViewById(R.id.main), message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.success_color))
                .setTextColor(Color.WHITE)
                .show();
    }

    private void showError(String message) {
        Snackbar.make(findViewById(R.id.main), message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.error_color))
                .setTextColor(Color.WHITE)
                .show();
    }

    private void selectFDate() {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Calendar
        mCalendar = Calendar.getInstance();
        currentDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = mCalendar.get(Calendar.MONTH);
        currentYear = mCalendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;

                binding.fromDateText.setText(formatdate(dayOfMonth) + "-" + monthlist.get(month - 1) + "\n" + year);
                fdate = Integer.parseInt(Integer.toString(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth));
//                getMemberDetails("all", "k");
            }
        }, currentYear, currentMonth, currentDay);
        mCalendar.set(currentYear, currentMonth, currentDay);

        datePickerDialog.getDatePicker().setMaxDate(mCalendar.getTimeInMillis());
        datePickerDialog.show();

    }

    private String formatdate(int dayOfMonth) {
        return dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
    }



    }



