package com.example.geniousmicro.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.geniousmicro.Adapter.TodayPolicyCollAdapter;
import com.example.geniousmicro.MainActivity;
import com.example.geniousmicro.Models.UtilityModels.PolicyCollModel;
import com.example.geniousmicro.R;
import com.example.geniousmicro.UserData.GlobalUserData;
import com.example.geniousmicro.databinding.ActivityArrPolicyManualCollectionBinding;
import com.example.geniousmicro.mssql.SqlManager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;

public class ArrPolicyManualCollection extends AppCompatActivity {
    ActivityArrPolicyManualCollectionBinding binding;
    private int fdate = 0;
    private int tdate = 0;
    private String isApproved = "0";
    int mYear, mMonth, mDay;
    private int currentDay, currentMonth, currentYear;
    ArrayList<String> monthlist;
    private Calendar mCalendar;
    String status = "Approved";
    double TotalAmt=0;
    PolicyCollModel policyCollModel;
    ArrayList<PolicyCollModel> list = new ArrayList<>();
    TodayPolicyCollAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityArrPolicyManualCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
                list.clear();
                if (fdate != 0 && tdate != 0) {
                    TotalAmt=0;
                    // withdrawAmt=0;
                    getPolicyManualCollection(fdate,tdate, GlobalUserData.employeeDataModel.getEmployeeID(),status);
                } else {
                    Toast.makeText(ArrPolicyManualCollection.this, "Please Select Dates", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getPolicyManualCollection(int fdate, int tdate, String employeeID, String status) {
        Connection cn = new SqlManager().getSQLConnection();
        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GetPolicyManualCollection(?,?,?,?)}");
                smt.setString("@ArrangerCode",employeeID);
                smt.setString("@Status",status);
                smt.setInt("@FDate",fdate);
                smt.setInt("@TDate",tdate);
                smt.execute();
                ResultSet rs = smt.getResultSet();
                if (rs.isBeforeFirst()){
                    while (rs.next()) {
                        policyCollModel = new PolicyCollModel();
                        policyCollModel.setPolicyCode(rs.getString("PolicyCode"));
                        policyCollModel.setName(rs.getString("ApplicantName"));
                        policyCollModel.setDate(rs.getString("DateofEntry"));
                        policyCollModel.setAmount(rs.getString("Amount"));
                        if(rs.getString("Status").equals("0")){
                            policyCollModel.setStatus("UnApproved");
                        }else{
                            policyCollModel.setStatus("Approved");
                        }

                        list.add(policyCollModel);
                        TotalAmt+=Double.parseDouble(rs.getString("Amount"));
                    }
                    binding.transRec.setVisibility(View.VISIBLE);
                    adapter = new TodayPolicyCollAdapter( list,ArrPolicyManualCollection.this);
                    LinearLayoutManager llm=new LinearLayoutManager(ArrPolicyManualCollection.this);
                    llm.setOrientation(RecyclerView.VERTICAL);
                    binding.transRec.setLayoutManager(llm);
                    binding.transRec.setAdapter(adapter);
                    binding.totalDepo.setText(""+TotalAmt);
                }else{
                    binding.transRec.setVisibility(View.GONE);
                    binding.totalDepo.setText("");
                    Toast.makeText(ArrPolicyManualCollection.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }

            }else{

                Toast.makeText(ArrPolicyManualCollection.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(ArrPolicyManualCollection.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
        }



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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(ArrPolicyManualCollection.this, MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }


}