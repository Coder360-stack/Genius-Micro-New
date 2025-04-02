package com.example.geniousmicro.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geniousmicro.Adapter.MemberListAdapter;
import com.example.geniousmicro.Data.PostDataParserObjectResponse;
import com.example.geniousmicro.Data.VolleyCallback;
import com.example.geniousmicro.MainActivity;
import com.example.geniousmicro.Models.UtilityModels.MemberListModels;
import com.example.geniousmicro.Others.ApiLinks;
import com.example.geniousmicro.R;
import com.example.geniousmicro.UserData.GlobalUserData;
import com.example.geniousmicro.databinding.ActivityMemberListBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MemberListActivity extends AppCompatActivity {
    ActivityMemberListBinding binding;
    private int fdate = 0;
    private int tdate = 0;
    private String isApproved = "0";
    int mYear, mMonth, mDay;
    private int currentDay, currentMonth, currentYear;
    private Calendar mCalendar;
    ArrayList<MemberListModels> list;
    ArrayList<MemberListModels> templist;
    ArrayList<String> searchby;
    ArrayList<String> monthlist;
    String searchby_txt = "Member ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMemberListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        isApproved = getIntent().getStringExtra("isApproved");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.MemberList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchby = new ArrayList<>();
        searchby.add("Member ID");
        searchby.add("Name");
        ArrayAdapter<String> NomineeRelationAdpater = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, searchby);
        NomineeRelationAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spSearchby.setAdapter(NomineeRelationAdpater);
        binding.spSearchby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchby_txt = searchby.get(position);
                binding.etSearchtxt.setHint("Enter "+searchby_txt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                templist = new ArrayList<>();
                if (searchby_txt.equals("Member ID")) {
                    for (MemberListModels models : list) {
                        if (models.getMemberCode().toLowerCase()
                                .contains(binding.etSearchtxt.getText().toString().toLowerCase())) {
                            templist.add(models);
                        }
                    }
                } else {
                    for (MemberListModels models : list) {
                        if (models.getMemberName().toLowerCase()
                                .contains(binding.etSearchtxt.getText().toString().toLowerCase())) {
                            templist.add(models);
                        }
                    }
                }
                LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                llm.setOrientation(RecyclerView.VERTICAL);
                binding.memberRec.setLayoutManager(llm);
                MemberListAdapter adapter = new MemberListAdapter(getApplicationContext(), templist);
                binding.memberRec.setAdapter(adapter);

            }
        });

        monthlist=new ArrayList<>();
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

                binding.tvTdate.setText(formatdate(dayOfMonth) + "-" + monthlist.get(month-1) + "-" + year);
                tdate = Integer.parseInt(Integer.toString(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth));
                getMemberDetails("all", "k");
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

                binding.tvFdate.setText(formatdate(dayOfMonth) + "-" + monthlist.get(month-1) + "-" + year);
                fdate = Integer.parseInt(Integer.toString(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth));
                getMemberDetails("all", "k");
            }
        }, currentYear, currentMonth, currentDay);
        mCalendar.set(currentYear, currentMonth, currentDay);

        datePickerDialog.getDatePicker().setMaxDate(mCalendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private String formatdate(int dayOfMonth) {
        return dayOfMonth<10?"0"+dayOfMonth:""+dayOfMonth;
    }


    private void getMemberDetails(String type, String value) {
        list = new ArrayList<>();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("type", type);
        hashMap.put("value", value);
        hashMap.put("fDate", "" + fdate);
        hashMap.put("tDate", "" + tdate);
        hashMap.put("UserName", "" + GlobalUserData.employeeDataModel.getEmployeeID());
        hashMap.put("isApproved", isApproved);
        hashMap.put("extraParams", "test");
        Log.d("Callback Response", hashMap.toString());

        new PostDataParserObjectResponse(MemberListActivity.this, ApiLinks.GET_MEMBER_LIST, hashMap, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                // Handle the successful response
                try {
                    JSONArray userData = response.getJSONArray("MemberData");
                    for (int i = 0; i < userData.length(); i++) {
                        JSONObject userDetails = userData.getJSONObject(i);
                        MemberListModels ed =
                                new MemberListModels(userDetails.getString("MemberCode"),
                                        userDetails.getString("MemberName"),
                                        userDetails.getString("OfcID"),
                                        userDetails.getString("DateOfJoin"),
                                        userDetails.getString("MemberDOB"),
                                        userDetails.getString("Father"),
                                        userDetails.getString("Addr"),
                                        userDetails.getString("Email"),
                                        userDetails.getString("Phone"),
                                        userDetails.getString("NomineeName"),
                                        userDetails.getString("Gender"),
                                        userDetails.getString("IDProof"),
                                        userDetails.getString("IDProofNo"),
                                        userDetails.getString("AddrProof"),
                                        userDetails.getString("AddrProofNo"),
                                        userDetails.getString("PanNo")
                                );
                        list.add(ed);

                    }
                    LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                    llm.setOrientation(RecyclerView.VERTICAL);
                    binding.memberRec.setLayoutManager(llm);
                    MemberListAdapter adapter = new MemberListAdapter(getApplicationContext(), list);
                    binding.memberRec.setAdapter(adapter);
                    Log.d("Callback Response", response.toString());
                } catch (Exception e) {
                    Toast.makeText(MemberListActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.d("EX", e.toString());
                }
            }

            @Override
            public void onErrorResponse(String error) {
                // Handle the error response
                Log.e("Callback Error", error);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finishAffinity();
    }
}