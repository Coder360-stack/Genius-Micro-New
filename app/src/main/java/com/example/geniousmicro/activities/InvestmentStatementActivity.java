package com.example.geniousmicro.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import com.example.geniousmicro.Adapter.InvestStatementAdapter;
import com.example.geniousmicro.Data.PostDataParserObjectResponse;
import com.example.geniousmicro.Data.VolleyCallback;
import com.example.geniousmicro.Models.UtilityModels.InvestStatementModel;
import com.example.geniousmicro.Others.ApiLinks;
import com.example.geniousmicro.R;
import com.example.geniousmicro.UserData.GlobalUserData;
import com.example.geniousmicro.databinding.ActivityInvestmentStatementBinding;
import com.example.geniousmicro.databinding.ActivityLoanStatementBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class InvestmentStatementActivity extends AppCompatActivity {
    ActivityInvestmentStatementBinding binding;
    private int fdate = 0;
    private int tdate = 0;
    private String isApproved = "0";
    int mYear, mMonth, mDay;
    private int currentDay, currentMonth, currentYear;
    ArrayList<String> monthlist;
    private Calendar mCalendar;
    String status = "Approved";
    ArrayList<InvestStatementModel> list;
    InvestStatementAdapter adapter;
    double depositAmt=0,withdrawAmt=0;
    private ArrayList<String> arrayList_policyCode = new ArrayList<>();
    private ArrayList<String> arrayList_policyCodeName = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInvestmentStatementBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
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
                    depositAmt=0;
                    withdrawAmt=0;
                  //  getSBTrasactionDetails();
                } else {
                    Toast.makeText(InvestmentStatementActivity.this, "Please Select Dates", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.spPolicyNameAndCodeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    // getPolicyDetails(arrayList_policyCode.get(position));
                    Log.e("responce",""+arrayList_policyCode.get(position));

                    list = new ArrayList<>();
                    if (fdate != 0 && tdate != 0) {
                        depositAmt=0;
                        withdrawAmt=0;
                        getSBTrasactionDetails(arrayList_policyCode.get(position));

                    } else {
                        Toast.makeText(InvestmentStatementActivity.this, "Please Select Dates", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        arrayList_policyCode.add("");
        arrayList_policyCodeName.add("--Select Account --");
        searchPolicyByNameOrCode(   GlobalUserData.memberDataModel.getMemberID());



    }
    private void searchPolicyByNameOrCode(String searchValue) {
        HashMap<String, String> map = new HashMap<>();
        map.put("SearchValue", searchValue.trim());
        map.put("SearchTag", "Policy");
        new PostDataParserObjectResponse(InvestmentStatementActivity.this, ApiLinks.GET_ACCOUNTDETAILS, map, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    binding.spPolicyNameAndCodeList.setVisibility(View.VISIBLE);
                    JSONArray jsa = result.getJSONArray("accountDetails");
                    for (int i = 0; i < jsa.length(); i++) {
                        arrayList_policyCode.add(jsa.getJSONObject(i).getString("AccountCode"));
                        arrayList_policyCodeName.add(jsa.getJSONObject(i).getString("Name") + " - "+jsa.getJSONObject(i).getString("AccountCode"));
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(InvestmentStatementActivity.this, R.layout.row_savings_acc_hint, arrayList_policyCodeName);
                    arrayAdapter.setDropDownViewResource(R.layout.row_savings_acc_hint);
                    binding.spPolicyNameAndCodeList.setAdapter(arrayAdapter);

                } catch (Exception e) {
                    Toast.makeText(InvestmentStatementActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.d("EX", e.toString());
                }
            }

            @Override
            public void onErrorResponse(String error) {
                Log.e("Callback Error", error);
            }
        });

    }
    private void getSBTrasactionDetails(String Policycode) {
        HashMap<String, String> map = new HashMap<>();
        map.put("Code","RD00001");
        map.put("FDate", "" + fdate);
        map.put("TDate", "" + tdate);


        new PostDataParserObjectResponse(InvestmentStatementActivity.this, ApiLinks.INVESTSTATEMENT, map, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    JSONArray jsa = result.getJSONArray("accountDetails");
                    for (int i = 0; i < jsa.length(); i++) {
                       // if(jsa.getJSONObject(i).getString("Status").equals(status)) {

                        Log.e("","");
                            Gson gson = new Gson();

                        InvestStatementModel model = gson.fromJson(String.valueOf(jsa.getJSONObject(i)), InvestStatementModel.class);
                            list.add(model);
                            depositAmt+=jsa.getJSONObject(i).getDouble("Amount");
                           // withdrawAmt+=jsa.getJSONObject(i).getDouble("WithdrawlAmount");


                       // }
                        
                    }
                    if (list.size()>0){
                        adapter=new InvestStatementAdapter(list, InvestmentStatementActivity.this);
                        LinearLayoutManager llm=new LinearLayoutManager(InvestmentStatementActivity.this);
                        llm.setOrientation(RecyclerView.VERTICAL);
                        binding.transRec.setLayoutManager(llm);
                        binding.transRec.setAdapter(adapter);
                        binding.transRec.setVisibility(View.VISIBLE);
                        binding.totalDepo.setText("₹ "+depositAmt);
                        binding.totalWith.setText("");
                    }else{
                        binding.transRec.setVisibility(View.GONE);
                        binding.totalDepo.setText("0");
                        binding.totalWith.setText("0");
                        Toast.makeText(InvestmentStatementActivity.this, "No Data Fount", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(InvestmentStatementActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
        if (intent.getStringExtra("SBCollectionType") != null &&  Objects.equals(intent.getStringExtra("SBCollectionType"), "Today SBTransaction")){
            binding.LoanCollectionReport.setText("Today SBTransaction Report");
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
        startActivity(new Intent(getApplicationContext(), MemberDashboardActivity.class));
        finishAffinity();
    }
}