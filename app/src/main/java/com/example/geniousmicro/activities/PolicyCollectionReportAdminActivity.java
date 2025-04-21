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

import com.example.geniousmicro.Adapter.ArrMemPolicyStatementAdapter;
import com.example.geniousmicro.Data.PostDataParserObjectResponse;
import com.example.geniousmicro.Data.VolleyCallback;
import com.example.geniousmicro.Models.UtilityModels.PolicyStatementModel;
import com.example.geniousmicro.Others.ApiLinks;
import com.example.geniousmicro.R;
import com.example.geniousmicro.UserData.GlobalUserData;
import com.example.geniousmicro.databinding.ActivityAdminPolicyCollectionReport2Binding;
import com.example.geniousmicro.databinding.ActivityAdminPolicyCollectionReportBinding;
import com.example.geniousmicro.databinding.ActivityPolicyCollectionReportBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class  PolicyCollectionReportAdminActivity extends AppCompatActivity {
    ActivityAdminPolicyCollectionReport2Binding binding;
    private int fdate = 0;
    private int tdate = 0;
    private String isApproved = "0";
    int mYear, mMonth, mDay;
    private int currentDay, currentMonth, currentYear;
    ArrayList<String> monthlist;
    private Calendar mCalendar;
    String status = "Approved";
    ArrayList<PolicyStatementModel> list;
    ArrMemPolicyStatementAdapter adapter;
    double TotalAmt=0;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAdminPolicyCollectionReport2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
                    if (binding.etSearchtxt.getText().toString().isEmpty()) {
                        Toast.makeText(PolicyCollectionReportAdminActivity.this, "Please Enter Employee Code", Toast.LENGTH_SHORT).show();
                        binding.etSearchtxt.requestFocus();
                        binding.etSearchtxt.setError("Please Enter Employee Code");
                    }else{
                        TotalAmt=0;
                        // withdrawAmt=0;
                        getSBTrasactionDetails();
                    }

                } else {
                    Toast.makeText(PolicyCollectionReportAdminActivity.this, "Please Select Dates", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
    }
    private void getSBTrasactionDetails() {
        HashMap<String, String> map = new HashMap<>();
        map.put("ArrangerCode", binding.etSearchtxt.getText().toString());
        map.put("Fdate", "" + fdate);
        map.put("Tdate", "" + tdate);
        Log.e("TEST1",""+ ApiLinks.GET_POLICYSUMMARYDATEWISE+binding.etSearchtxt.getText().toString()+fdate+tdate);
        new PostDataParserObjectResponse(PolicyCollectionReportAdminActivity.this, ApiLinks.GET_POLICYSUMMARYDATEWISE, map, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    JSONArray jsa = result.getJSONArray("RenewalSummary");
                    Log.e("TEST2",""+jsa);
                    for (int i = 0; i < jsa.length(); i++) {
                        if(jsa.getJSONObject(i).getString("Status").equals(status)) {
                            Gson gson = new Gson();
                            PolicyStatementModel model = gson.fromJson(String.valueOf(jsa.getJSONObject(i)), PolicyStatementModel.class);
                            list.add(model);
                            TotalAmt+=jsa.getJSONObject(i).getDouble("PaidAmount");
                            Log.e("TotalAmt1",""+jsa.getJSONObject(i).getDouble("PaidAmount"));

                        }

                    }
                    if (list.size()>0){
                        adapter=new ArrMemPolicyStatementAdapter(list, PolicyCollectionReportAdminActivity.this);
                        LinearLayoutManager llm=new LinearLayoutManager(PolicyCollectionReportAdminActivity.this);
                        llm.setOrientation(RecyclerView.VERTICAL);
                        binding.transRec.setLayoutManager(llm);
                        binding.transRec.setAdapter(adapter);
                        binding.transRec.setVisibility(View.VISIBLE);
                        binding.totalDepo.setText(""+TotalAmt);
                        // Log.e("TotalAmt1",""+TotalAmt);
                        binding.totalDepo.setText(""+TotalAmt);
                    }else{
                        binding.transRec.setVisibility(View.GONE);
                        binding.totalDepo.setText("0");
                        //  binding.totalWith.setText("0");
                        Toast.makeText(PolicyCollectionReportAdminActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(PolicyCollectionReportAdminActivity.this, "Error", Toast.LENGTH_SHORT).show();
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



        }


    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(PolicyCollectionReportAdminActivity.this, AdminDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}