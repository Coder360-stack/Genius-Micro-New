package com.example.geniousmicro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.geniousmicro.Data.PostDataParserObjectResponse;
import com.example.geniousmicro.Data.VolleyCallback;
import com.example.geniousmicro.Models.UtilityModels.PolicyDetailsModel;
import com.example.geniousmicro.Models.UtilityModels.SBDataModel;
import com.example.geniousmicro.Others.ApiLinks;
import com.example.geniousmicro.UserData.GlobalUserData;
import com.example.geniousmicro.activities.AgentPolicyDueReportDaywise;
import com.example.geniousmicro.databinding.ActivityRenewalCollectionBinding;
import com.example.geniousmicro.mssql.SqlManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class RenewalCollectionActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityRenewalCollectionBinding binding;
    String status = "Approved";
    String IsLatePaid = "";
    PolicyDetailsModel policyDetailsModel;
    private ArrayList<String> arrayList_policyCode = new ArrayList<>();
    private ArrayList<String> arrayList_policyCodeName = new ArrayList<>();
    private double actualLateFine = 0.0;
    private int IsLateFine = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRenewalCollectionBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        binding.btnSearchPolicy.setOnClickListener(this);
        binding.btnView.setOnClickListener(this);
        binding.cBtnView.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);
        binding.tilAmount.setOnClickListener(this);

        binding.spPolicyNameAndCodeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    getPolicyDetails(arrayList_policyCode.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.isLatePaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.isLatePaid.isChecked()) {
                    IsLatePaid = "1";
                } else {
                    IsLatePaid = "0";
                }
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra("policyCode")) {
            String loancode = intent.getStringExtra("policyCode");
            binding.etSearchtxt.setText(loancode);
            if (!binding.etSearchtxt.getText().toString().isEmpty()) {
                arrayList_policyCode.clear();
                arrayList_policyCodeName.clear();
                arrayList_policyCode.add("");
                arrayList_policyCodeName.add("--Select An Item--");
                searchPolicyByNameOrCode(binding.etSearchtxt.getText().toString());
            } else {
                Toast.makeText(this, "Please Enter Policy Code OR Name", Toast.LENGTH_LONG).show();
            }

        }


    }

    private void getPolicyDetails(String PolicyCode) {
        HashMap<String, String> map = new HashMap<>();
        map.put("PolicyCode", PolicyCode.trim());
        map.put("ACode", GlobalUserData.employeeDataModel.getEmployeeID());
        new PostDataParserObjectResponse(RenewalCollectionActivity.this, ApiLinks.GET_POLICYDETAILS, map, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    JSONObject jsa = result.getJSONObject("rd");
                    if (jsa.length() > 0) {
                        Gson gson = new Gson();
                        policyDetailsModel = gson.fromJson(String.valueOf(result.getJSONObject("rd")), PolicyDetailsModel.class);
                        if (policyDetailsModel != null && policyDetailsModel.getApplicantName() != null) {
                            binding.name.setText("Hi " + policyDetailsModel.getApplicantName());
                            binding.txtPolicyCode.setText(policyDetailsModel.getPolicyCode());
                            binding.DOC.setText(policyDetailsModel.getDateofCom());
                            binding.plan.setText(policyDetailsModel.getPTable());
                            binding.term.setText(policyDetailsModel.getTerm());
                            //binding.lateFine.setText(policyDetailsModel.getIsLateFineApplicable());
                            binding.lateFine.setText("Yes");
                            binding.mode.setText(policyDetailsModel.getMode());
                            binding.totalAmount.setText(policyDetailsModel.getTillAmount());
                            binding.memberCode.setText(policyDetailsModel.getMemberCode());
                            binding.PhoneNo.setText(policyDetailsModel.getPhoneNo());
                            binding.emiAmount.setText(policyDetailsModel.getAmount());
                            binding.depositeDate.setText(policyDetailsModel.getCDate());
                        } else {
                            binding.name.setText("Hi ");
                            binding.txtPolicyCode.setText("");
                            binding.DOC.setText("");
                            binding.plan.setText("");
                            binding.term.setText("");
                            //binding.lateFine.setText(policyDetailsModel.getIsLateFineApplicable());
                            binding.lateFine.setText("");
                            binding.mode.setText("");
                            binding.totalAmount.setText("");
                            binding.memberCode.setText("");
                            binding.PhoneNo.setText("");
                            binding.emiAmount.setText("");
                            binding.depositeDate.setText("");
                            Toast.makeText(RenewalCollectionActivity.this, "PolicyCode is Not Associated with this arranger", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(RenewalCollectionActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.d("EX", e.toString());
                }
            }

            @Override
            public void onErrorResponse(String error) {
                Log.e("Callback Error", error);
            }
        });


    }

    @Override
    public void onClick(View view) {
        if (view == binding.btnSearchPolicy) {
            if (!binding.etSearchtxt.getText().toString().isEmpty()) {
                arrayList_policyCode.clear();
                arrayList_policyCodeName.clear();
                arrayList_policyCode.add("");
                arrayList_policyCodeName.add("--Select An Item--");
               // searchPolicyByNameOrCode(binding.etSearchtxt.getText().toString());
                searchPolicyCode(binding.etSearchtxt.getText().toString(),GlobalUserData.employeeDataModel.getEmployeeID());
            } else {
                Toast.makeText(this, "Please Enter Policy Code OR Name", Toast.LENGTH_LONG).show();
            }

        } else if (view == binding.btnView) {
            if (!binding.insNumber.getText().toString().isEmpty()) {
                actualLateFine = 0.0;
                IsLateFine = 0;
                getInstChange();
            } else {
                Toast.makeText(this, "Please Enter Installment Number", Toast.LENGTH_LONG).show();
            }
        } else if (view == binding.cBtnView) {
            if (!binding.tilAmount.getText().toString().isEmpty()) {
                Double enteredAmount = Double.parseDouble(binding.tilAmount.getEditableText().toString().trim());
                Double emiAmount = Double.parseDouble(binding.emiAmount.getText().toString());
                int rem = (int) (enteredAmount % emiAmount);
                int div = (int) (enteredAmount / emiAmount);

                if (!(rem == 0)) {
                    binding.fromInsNumber.setText("");
                    binding.toInsNumber.setText("");
                    binding.netAmount.setText("");
                    Toast.makeText(this, "Please Enter Valid Amount", Toast.LENGTH_SHORT).show();
                } else {
//                    binding.tilFromInst.setText(String.valueOf(lastEMINo + 1));
//                    binding.tilToInst.setText(String.valueOf(lastEMINo + div));
//                    binding.tilNetAmount.setText(String.valueOf(emiAmount * div));
                    if (div <= Integer.parseInt(binding.term.getText().toString())) {
                        binding.insNumber.setText("" + div);
                        actualLateFine = 0.0;
                        IsLateFine = 0;
                        getInstChange();
                    } else {
                        binding.fromInsNumber.setText("");
                        binding.toInsNumber.setText("");
                        binding.netAmount.setText("");
                        Toast.makeText(this, "Please Enter Valid Amount", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, "Enter Amount", Toast.LENGTH_SHORT).show();
            }
        } else if (view == binding.btnSave) {
            if (!binding.txtPolicyCode.getText().toString().isEmpty()) {
                if (!binding.totalAmount.getText().toString().isEmpty()) {
                    checkRenewalPending(binding.txtPolicyCode.getText().toString());
                } else {
                    Toast.makeText(this, "Please Select the Installment Number", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Please Select Valid policy Code", Toast.LENGTH_LONG).show();
            }

        }

    }

    private void searchPolicyCode(String searchValue, String employeeID) {
        Connection cn = new SqlManager().getSQLConnection();
        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GetPolicyCodes_BY_CodeOrName(?,?)}");
                smt.setString("@searchValue",searchValue);
                smt.setString("@ArrangerCode",employeeID);
                smt.execute();
                ResultSet rs = smt.getResultSet();
                if (rs.isBeforeFirst()){
                    binding.spPolicyNameAndCodeList.setVisibility(View.VISIBLE);
                    while (rs.next()) {
                        arrayList_policyCode.add(rs.getString("AccountCode"));
                        arrayList_policyCodeName.add(rs.getString("Name") + " - " + rs.getString("AccountCode"));

                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(RenewalCollectionActivity.this, R.layout.row_savings_acc_hint, arrayList_policyCodeName);
                    arrayAdapter.setDropDownViewResource(R.layout.row_savings_acc_hint);
                    binding.spPolicyNameAndCodeList.setAdapter(arrayAdapter);

                }else{
                    Toast.makeText(RenewalCollectionActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(RenewalCollectionActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(RenewalCollectionActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
        }


    }

    private void checkRenewalPending(String policyCode) {
        binding.btnSave.setVisibility(View.GONE);
        HashMap<String, String> map = new HashMap<>();
        map.put("Code", policyCode.toString().trim());
        map.put("SearchTag", "policy");
        new PostDataParserObjectResponse(RenewalCollectionActivity.this, ApiLinks.CHECK_IFRENEWALPENDING, map, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    JSONObject jsa = result.getJSONObject("status");
                    if (jsa.getString("Status").equals("0")) {
                        SaveRenewal();
                    } else {
                        Toast.makeText(RenewalCollectionActivity.this, "Pevious Renewal is on Pending", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    Toast.makeText(RenewalCollectionActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.d("EX1", e.toString());
                }
            }

            @Override
            public void onErrorResponse(String error) {
                Log.e("Callback Error", error);
            }
        });
        binding.btnSave.setVisibility(View.VISIBLE);
    }

    private void SaveRenewal() {
        HashMap<String, String> map = new HashMap<>();
        map.put("PolicyCode", binding.txtPolicyCode.getText().toString().trim());
        map.put("FromInstNo", binding.fromInsNumber.getText().toString().trim());
        map.put("ToInstNo", binding.toInsNumber.getText().toString().trim());
        map.put("UserName", GlobalUserData.employeeDataModel.getEmployeeID());
        map.put("loginOfficeID", GlobalUserData.employeeDataModel.getOfficeid());
        map.put("lateFine", String.valueOf(actualLateFine));
        map.put("isLateFine", String.valueOf(IsLateFine));
        map.put("CollectionLocation", "0,0");
        map.put("isLatePaid", IsLatePaid);
        map.put("ReturnVoucherNo", "0");
        map.put("IsError", "0");
        new PostDataParserObjectResponse(RenewalCollectionActivity.this, ApiLinks.INSERT_RENEWAL, map, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    JSONObject jsa = result.getJSONObject("response");
                    if (jsa.getString("ErrorCode").equals("0")) {
                        Toast.makeText(RenewalCollectionActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RenewalCollectionActivity.this, RenewalCollectionActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RenewalCollectionActivity.this, "Unable To Save", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(RenewalCollectionActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.d("EX1", e.toString());
                }
            }

            @Override
            public void onErrorResponse(String error) {
                Log.e("Callback Error", error);
            }
        });


    }

    private void getInstChange() {
        try {
            int toinstno = Integer.parseInt(binding.insNumber.getText().toString());
            Log.d("ins", "getInstChange: " + Integer.parseInt(policyDetailsModel.getLastInstNo() + toinstno));
            Log.d("ins", "getInstChange: " + toinstno);
            Log.d("ins", "getInstChange: " + Integer.parseInt(binding.term.getText().toString()));
            if (Integer.parseInt(policyDetailsModel.getLastInstNo()) + toinstno <= Integer.parseInt(binding.term.getText().toString())) {

                binding.fromInsNumber.setText(String.valueOf(Integer.parseInt(policyDetailsModel.getLastInstNo()) + 1));
                binding.toInsNumber.setText(String.valueOf(Integer.parseInt(policyDetailsModel.getLastInstNo()) + toinstno));
                binding.netAmount.setText(String.valueOf(Integer.parseInt(binding.emiAmount.getText().toString()) * toinstno));
                getRenewalLatefine();
            } else {
                Toast.makeText(this, "Enter Valid Installment Number", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {

        }
    }

    private void getRenewalLatefine() {
        HashMap<String, String> map = new HashMap<>();
        map.put("PolicyCode", Objects.requireNonNull(binding.txtPolicyCode.getText()).toString().trim());
        map.put("FromInstNo", Objects.requireNonNull(binding.fromInsNumber.getText()).toString().trim());
        map.put("ToInstNo", Objects.requireNonNull(binding.toInsNumber.getText()).toString().trim());
        new PostDataParserObjectResponse(RenewalCollectionActivity.this, ApiLinks.GET_RENEWALLATEFINE, map, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    JSONArray jsa = result.getJSONArray("latefinedetails");
                    if (jsa.length() > 0) {
                        binding.lateFineLayout.setVisibility(View.VISIBLE);
                        for (int i = 0; i < jsa.length(); i++) {
                            actualLateFine += Double.parseDouble(jsa.getJSONObject(i).getString("LateFine"));
                        }
                        IsLateFine = 1;
                        binding.latefineAmount.setText(String.valueOf(actualLateFine));
                    } else {
                        binding.lateFineLayout.setVisibility(View.GONE);
                        actualLateFine = 0.0;
                        binding.latefineAmount.setText(String.valueOf(0));
                        IsLateFine = 0;
                        Toast.makeText(RenewalCollectionActivity.this, "Late fine not applicable", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    Toast.makeText(RenewalCollectionActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.d("EX1", e.toString());
                }
            }

            @Override
            public void onErrorResponse(String error) {
                Log.e("Callback Error", error);
            }
        });


    }

    private void searchPolicyByNameOrCode(String searchValue) {
        HashMap<String, String> map = new HashMap<>();
        map.put("SearchValue", searchValue.trim());
        map.put("SearchTag", "Policy");
        new PostDataParserObjectResponse(RenewalCollectionActivity.this, ApiLinks.GET_ACCOUNTDETAILS, map, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    binding.spPolicyNameAndCodeList.setVisibility(View.VISIBLE);
                    JSONArray jsa = result.getJSONArray("accountDetails");
                    for (int i = 0; i < jsa.length(); i++) {
                        arrayList_policyCode.add(jsa.getJSONObject(i).getString("AccountCode"));
                        arrayList_policyCodeName.add(jsa.getJSONObject(i).getString("Name") + " - " + jsa.getJSONObject(i).getString("AccountCode"));
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(RenewalCollectionActivity.this, R.layout.row_savings_acc_hint, arrayList_policyCodeName);
                    arrayAdapter.setDropDownViewResource(R.layout.row_savings_acc_hint);
                    binding.spPolicyNameAndCodeList.setAdapter(arrayAdapter);

                } catch (Exception e) {
                    Toast.makeText(RenewalCollectionActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.d("EX", e.toString());
                }
            }

            @Override
            public void onErrorResponse(String error) {
                Log.e("Callback Error", error);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = getIntent();
        if (intent.hasExtra("policyCode")) {
            String loancode = intent.getStringExtra("policyCode");
            startActivity(new Intent(RenewalCollectionActivity.this, AgentPolicyDueReportDaywise.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();

        } else {
            startActivity(new Intent(RenewalCollectionActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }


    }

}