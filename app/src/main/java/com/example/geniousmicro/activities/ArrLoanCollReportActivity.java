package com.example.geniousmicro.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geniousmicro.Adapter.LoanStatementDatewiseAdapter;
import com.example.geniousmicro.Data.PostDataParserObjectResponse;
import com.example.geniousmicro.Data.VolleyCallback;
import com.example.geniousmicro.MainActivity;
import com.example.geniousmicro.Models.UtilityModels.LoanStatementDatewiseModel;
import com.example.geniousmicro.Others.ApiLinks;
import com.example.geniousmicro.R;
import com.example.geniousmicro.UserData.GlobalUserData;
import com.example.geniousmicro.databinding.ActivityLoanCollectionReportBinding;
import com.example.geniousmicro.databinding.ActivitySavingsCollectionReportBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class ArrLoanCollReportActivity extends AppCompatActivity {
    ActivityLoanCollectionReportBinding binding;
    private int fdate = 0;
    private int tdate = 0;
    private String isApproved = "0";
    int mYear, mMonth, mDay;
    private int currentDay, currentMonth, currentYear;
    ArrayList<String> monthlist;
    private Calendar mCalendar;
    String status = "Approved";
    ArrayList<LoanStatementDatewiseModel> list;
    LoanStatementDatewiseAdapter adapter;
    double TotalAmt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoanCollectionReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding.EmplyId.setVisibility(View.GONE);
        getLoanCollectionType();
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

        binding.tvFdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFDate();
            }
        });
        binding.tvTdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTDate();
            }
        });
        binding.apprRDgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (binding.apprbtn.isChecked()) {
                    status = "Approved";
                } else if (binding.unapprbtn.isChecked()) {
                    status = "Pending";
                }
            }
        });
        binding.searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = new ArrayList<>();
                if (fdate != 0 && tdate != 0) {
                    TotalAmt=0;
                    Intent intent = getIntent();
                    if (intent.getStringExtra("LoanCollectionType") != null &&  Objects.equals(intent.getStringExtra("LoanCollectionType"), "AdminArangerWiseReport")) {
                        //  Log.e("LoanCollectionType", intent.getStringExtra("LoanCollectionType"));
                        binding.EmplyId.setVisibility(View.VISIBLE);
                       String EmplyCode= binding.EmplyCode.getText().toString();
                       if (binding.EmplyCode.getText().toString().isEmpty()){
                           binding.EmplyCode.setError("Please Enter Employee Code");
                           Toast.makeText(ArrLoanCollReportActivity.this, "Please Enter Employee Code", Toast.LENGTH_SHORT).show();

                       }else {
                           getSBTrasactionDetails(EmplyCode);
                       }



                    }else {
                        String EmplyCode= GlobalUserData.employeeDataModel.getEmployeeID();
                        getSBTrasactionDetails(EmplyCode);
                    }

                } else {
                    Toast.makeText(ArrLoanCollReportActivity.this, "Please Select Dates", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getSBTrasactionDetails(String EmplyCode) {
        HashMap<String, String> map = new HashMap<>();
        map.put("ArrangerCode", EmplyCode);
        map.put("FDate", "" + fdate);
        map.put("TDate", "" + tdate);
        Log.d("Report", EmplyCode);
        Log.d("Report", String.valueOf(fdate));
        Log.d("Report", String.valueOf(tdate));
        Log.d("Report", ApiLinks.GET_LOANSUMMARYDATEWISE);
    ///  Log.e("TEST1",""+ApiLinks.GET_LOANSUMMARYDATEWISE+GlobalUserData.employeeDataModel.getEmployeeID()+fdate+tdate);
        new PostDataParserObjectResponse(ArrLoanCollReportActivity.this, ApiLinks.GET_LOANSUMMARYDATEWISE, map, new VolleyCallback()
        {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    JSONArray jsa = result.getJSONArray("loanSummary");
                    Log.e("TEST2",""+jsa);
                    for (int i = 0; i < jsa.length(); i++) {
                        if(jsa.getJSONObject(i).getString("Status").equals(status)) {
                            Gson gson = new Gson();
                            LoanStatementDatewiseModel model = gson.fromJson(String.valueOf(jsa.getJSONObject(i)), LoanStatementDatewiseModel.class);
                            list.add(model);
                            TotalAmt+=jsa.getJSONObject(i).getDouble("EMIAmount");
                            Log.e("TotalAmt1",""+jsa.getJSONObject(i).getDouble("EMIAmount"));

                        }
                        
                    }
                    if (list.size()>0){
                        adapter=new LoanStatementDatewiseAdapter(list, ArrLoanCollReportActivity.this);
                        LinearLayoutManager llm=new LinearLayoutManager(ArrLoanCollReportActivity.this);
                        llm.setOrientation(RecyclerView.VERTICAL);
                        binding.transRec.setLayoutManager(llm);
                        binding.transRec.setAdapter(adapter);
                        binding.transRec.setVisibility(View.VISIBLE);
                        binding.totalDepo.setText(""+TotalAmt);
                        Log.e("TotalAmt1",""+TotalAmt);

                        // binding.totalWith.setText(""+withdrawAmt);
                    }else{
                        binding.transRec.setVisibility(View.GONE);
                        binding.totalDepo.setText("0");
                      //  binding.totalWith.setText("0");
                        Toast.makeText(ArrLoanCollReportActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(ArrLoanCollReportActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.d("EX", e.toString());
                }
            }

            @Override
            public void onErrorResponse(String error) {
                Log.e("Callback Error", error);
            }
        });
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

                binding.tvTdate.setText(formatdate(dayOfMonth) + "-" + monthlist.get(month - 1) + "-" + year);
                tdate = Integer.parseInt(Integer.toString(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth));
//                getMemberDetails("all", "k");
            }
        }, currentYear, currentMonth, currentDay);
        mCalendar.set(currentYear, currentMonth, currentDay);

        datePickerDialog.getDatePicker().setMaxDate(mCalendar.getTimeInMillis());
        datePickerDialog.show();

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

                binding.tvFdate.setText(formatdate(dayOfMonth) + "-" + monthlist.get(month - 1) + "-" + year);
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
    private void  getLoanCollectionType() {
        Intent intent = getIntent();
        if (intent.getStringExtra("LoanCollectionType") != null &&  Objects.equals(intent.getStringExtra("LoanCollectionType"), "Today LoanCollection")){
             binding.LoanCollectionReport.setText("Today Loan Collection Report");
             binding.DateRange.setVisibility(View.GONE);

            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            // Set tdate as today's date by default
            tdate = Integer.parseInt(
                    Integer.toString(mYear) +
                            String.format("%02d", mMonth + 1) +
                            String.format("%02d", mDay)
            );
            fdate=tdate;



        } else if (intent.getStringExtra("LoanCollectionType") != null &&  Objects.equals(intent.getStringExtra("LoanCollectionType"), "AdminArangerWiseReport")) {
            Log.e("LoanCollectionType12", intent.getStringExtra("LoanCollectionType"));

            binding.EmplyId.setVisibility(View.VISIBLE);
         //   binding.EmplyId.setVisibility(View.VISIBLE);




        }


    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = getIntent();
      if(  intent.getStringExtra("LoanCollectionType") != null &&  Objects.equals(intent.getStringExtra("LoanCollectionType"), "AdminArangerWiseReport")) {
          startActivity(new Intent(ArrLoanCollReportActivity.this, AdminDashboardActivity.class));
          overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
          finish();

      }else {
          startActivity(new Intent(ArrLoanCollReportActivity.this, MainActivity.class));
          overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
          finish();
      }

    }

}