package com.example.geniousmicro.activities;

import android.app.DatePickerDialog;
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

import com.example.geniousmicro.Adapter.SBSummaryDatewiseAdapter;
import com.example.geniousmicro.Data.PostDataParserObjectResponse;
import com.example.geniousmicro.Data.VolleyCallback;
import com.example.geniousmicro.Models.UtilityModels.SBTransactionModel;
import com.example.geniousmicro.Others.ApiLinks;
import com.example.geniousmicro.R;
import com.example.geniousmicro.UserData.GlobalUserData;
import com.example.geniousmicro.databinding.ActivityGroupCollectionSummaryBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class GroupCollectionSummaryActivity extends AppCompatActivity {

    ActivityGroupCollectionSummaryBinding binding;
    private int fdate = 0;
    private int tdate = 0;
    private String isApproved = "0";
    int mYear, mMonth, mDay;
    private int currentDay, currentMonth, currentYear;
    ArrayList<String> monthlist;
    private Calendar mCalendar;
    String status = "1";
    ArrayList<SBTransactionModel> list;
    SBSummaryDatewiseAdapter adapter;
    double emiamt = 0, lsamt = 0;
    String userStatus = "Arranger";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityGroupCollectionSummaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userStatus = getIntent().getStringExtra("user");
        if (userStatus.equals("Admin")) {
            binding.edSearchtxt.setVisibility(View.VISIBLE);
        } else {
            binding.edSearchtxt.setVisibility(View.GONE);
        }
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
                    status = "1";
                } else if (binding.unapprbtn.isChecked()) {
                    status = "0";
                }
            }
        });
        binding.searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = new ArrayList<>();
                if (fdate != 0 && tdate != 0) {
                    emiamt = 0;
                    lsamt = 0;
                    if ((userStatus.equals("Admin") && binding.edSearchtxt.getText().toString().length() > 0) ||
                            userStatus.equals("Arranger")) {
                        getSBTrasactionDetails();
                    } else {
                        Toast.makeText(GroupCollectionSummaryActivity.this, "Enter Details Properly", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(GroupCollectionSummaryActivity.this, "Please Select Dates", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getSBTrasactionDetails() {
        HashMap<String, String> map = new HashMap<>();
        map.put("ArrangerCode", GlobalUserData.employeeDataModel.getEmployeeID());
        map.put("FDate", "" + fdate);
        map.put("TDate", "" + tdate);
        map.put("isApprove", status);
        new PostDataParserObjectResponse(GroupCollectionSummaryActivity.this, ApiLinks.GET_GROUPCOLLDETAILSDATEWISE, map, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    JSONArray jsa = result.getJSONArray("CollDetails");
                    for (int i = 0; i < jsa.length(); i++) {
//                        Gson gson = new Gson();
//                        SBTransactionModel model = gson.fromJson(String.valueOf(jsa.getJSONObject(i)), SBTransactionModel.class);
                        JSONObject jso = jsa.getJSONObject(i);

                        list.add(new SBTransactionModel(jso.getString("MemberCode"),
                                jso.getString("MemberName"),
                                jso.getString("PaymentDate"),
                                jso.getString("CollectedEMIAmount"),
                                jso.getString("CollectedLSAmount"),
                                "",
                                jso.getString("Status")));
                        emiamt += jso.getDouble("CollectedEMIAmount");
                        lsamt += jso.getDouble("CollectedLSAmount");

                    }
                    if (list.size() > 0) {
                        adapter = new SBSummaryDatewiseAdapter(list, GroupCollectionSummaryActivity.this);
                        LinearLayoutManager llm = new LinearLayoutManager(GroupCollectionSummaryActivity.this);
                        llm.setOrientation(RecyclerView.VERTICAL);
                        binding.transRec.setLayoutManager(llm);
                        binding.transRec.setAdapter(adapter);
                        binding.transRec.setVisibility(View.VISIBLE);
                        binding.totalDepo.setText("" + emiamt);
                        binding.totalWith.setText("" + lsamt);
                    } else {
                        binding.transRec.setVisibility(View.GONE);
                        binding.totalDepo.setText("0");
                        binding.totalWith.setText("0");
                        Toast.makeText(GroupCollectionSummaryActivity.this, "No Data Fount", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(GroupCollectionSummaryActivity.this, "Error", Toast.LENGTH_SHORT).show();
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


}