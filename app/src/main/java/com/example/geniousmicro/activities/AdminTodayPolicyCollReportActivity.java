package com.example.geniousmicro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.geniousmicro.databinding.ActivityAdminTodayPolicyCollReportBinding;
import com.example.geniousmicro.databinding.ActivityArrTodayPolicyCollReportBinding;
import com.example.geniousmicro.mssql.SqlManager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AdminTodayPolicyCollReportActivity extends AppCompatActivity {


    ActivityAdminTodayPolicyCollReportBinding binding;
    String status = "Approved";
    PolicyCollModel policyCollModel;
    ArrayList<PolicyCollModel> list = new ArrayList<>();
    TodayPolicyCollAdapter adapter;
    double TotalAmt=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAdminTodayPolicyCollReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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
                TotalAmt=0;
                if(!binding.etSearchtxt.getText().toString().isEmpty()){
                    getTodayPolicyCollection(binding.etSearchtxt.getText().toString(),status);
                }else{
                    Toast.makeText(AdminTodayPolicyCollReportActivity.this, "Please Enter Employee Code", Toast.LENGTH_SHORT).show();
                    binding.etSearchtxt.requestFocus();
                    binding.etSearchtxt.setError("Please Enter Employee Code");
                }

            }
        });



    }

    private void getTodayPolicyCollection(String employeeID, String status) {
        Connection cn = new SqlManager().getSQLConnection();
        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GetTodayPolicyCollection(?,?)}");
                smt.setString("@ArrangerCode",employeeID);
                smt.setString("@Status",status);
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
                    adapter = new TodayPolicyCollAdapter( list,AdminTodayPolicyCollReportActivity.this);
                    LinearLayoutManager llm=new LinearLayoutManager(AdminTodayPolicyCollReportActivity.this);
                    llm.setOrientation(RecyclerView.VERTICAL);
                    binding.transRec.setLayoutManager(llm);
                    binding.transRec.setAdapter(adapter);
                    binding.totalDepo.setText(""+TotalAmt);
                }else{
                    binding.transRec.setVisibility(View.GONE);
                    binding.totalDepo.setText("");
                    Toast.makeText(AdminTodayPolicyCollReportActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }

            }else{

                Toast.makeText(AdminTodayPolicyCollReportActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.d("ehhe", "getTodayPolicyCollection: "+e);
            Toast.makeText(AdminTodayPolicyCollReportActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(AdminTodayPolicyCollReportActivity.this, AdminDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}