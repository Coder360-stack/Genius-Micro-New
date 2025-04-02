package com.example.geniousmicro.activities;

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
import com.example.geniousmicro.Others.ApiLinks;
import com.example.geniousmicro.R;
import com.example.geniousmicro.UserData.GlobalUserData;
import com.example.geniousmicro.databinding.ActivityRenewalCollectionBinding;
import com.example.geniousmicro.databinding.ActivityRenewalDetailsBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RenewalDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityRenewalDetailsBinding binding;
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
        binding=ActivityRenewalDetailsBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        binding.etSearchtxt.setOnClickListener(this);
        binding.btnSearchPolicy.setOnClickListener(this);
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
       /* binding.btnSearchPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.etSearchtxt.getText().toString().isEmpty()) {
                    arrayList_policyCode.clear();
                    arrayList_policyCodeName.clear();
                    arrayList_policyCode.add("");
                    arrayList_policyCodeName.add("--Select An Item--");
                    searchPolicyByNameOrCode(binding.etSearchtxt.getText().toString());
                    Log.e("clcick","clcick");
                }else {
                    // Toast.makeText(Context, "Please Enter Policy Code OR Name", Toast.LENGTH_LONG).show();
                    Toast.makeText(RenewalDetailsActivity.this, "Please Enter Policy Code OR Name", Toast.LENGTH_LONG).show();
                }

            }
        });*/





    }

    private void getPolicyDetails(String PolicyCode) {
        HashMap<String, String> map = new HashMap<>();
        map.put("PolicyCode", PolicyCode.trim());
        map.put("ACode",  GlobalUserData.employeeDataModel.getEmployeeID());
        new PostDataParserObjectResponse(RenewalDetailsActivity.this, ApiLinks.GET_POLICYDETAILS, map, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    JSONObject jsa = result.getJSONObject("rd");
                    if (jsa.length() > 0) {
                        Gson gson = new Gson();
                        policyDetailsModel = gson.fromJson(String.valueOf(result.getJSONObject("rd")), PolicyDetailsModel.class);
                        if (policyDetailsModel != null) {
                           // binding.name.setText("Hi " + policyDetailsModel.getApplicantName());
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
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(RenewalDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                searchPolicyByNameOrCode(binding.etSearchtxt.getText().toString());
                Log.e("clcick","clcick");
            }else{
                Toast.makeText(this, "Please Enter Policy Code OR Name", Toast.LENGTH_LONG).show();
            }

        }


    }





    private void searchPolicyByNameOrCode(String searchValue) {
        HashMap<String, String> map = new HashMap<>();
        map.put("SearchValue", searchValue.trim());
        map.put("SearchTag", "Policy");
        new PostDataParserObjectResponse(RenewalDetailsActivity.this, ApiLinks.GET_ACCOUNTDETAILS, map, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    binding.spPolicyNameAndCodeList.setVisibility(View.VISIBLE);
                    JSONArray jsa = result.getJSONArray("accountDetails");
                    Log.e("","");
                    for (int i = 0; i < jsa.length(); i++) {

                        arrayList_policyCode.add(jsa.getJSONObject(i).getString("AccountCode"));
                        arrayList_policyCodeName.add(jsa.getJSONObject(i).getString("Name") + " - "+jsa.getJSONObject(i).getString("AccountCode"));
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(RenewalDetailsActivity.this, R.layout.row_savings_acc_hint, arrayList_policyCodeName);
                    arrayAdapter.setDropDownViewResource(R.layout.row_savings_acc_hint);
                    binding.spPolicyNameAndCodeList.setAdapter(arrayAdapter);

                } catch (Exception e) {
                    Toast.makeText(RenewalDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.d("EX", e.toString());
                }
            }

            @Override
            public void onErrorResponse(String error) {
                Log.e("Callback Error", error);
            }
        });

    }
}