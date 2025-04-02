package com.example.geniousmicro.activities;


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
import com.example.geniousmicro.Data.TempData;
import com.example.geniousmicro.Data.VolleyCallback;
import com.example.geniousmicro.MainActivity;
import com.example.geniousmicro.Models.UtilityModels.LateFineModel;
import com.example.geniousmicro.Models.UtilityModels.LoanEMIBreakUpModel;
import com.example.geniousmicro.Models.UtilityModels.SBDataModel;
import com.example.geniousmicro.Others.ApiLinks;
import com.example.geniousmicro.R;
import com.example.geniousmicro.UserData.GlobalUserData;
import com.example.geniousmicro.databinding.ActivityLoanCollectionBinding;
import com.example.geniousmicro.databinding.ActivityLoanCollectionManualBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class LoanCollectionManualActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityLoanCollectionManualBinding binding;
    String searchValue = "";
    ArrayList<SBDataModel> accDetailsList;
    ArrayList<LateFineModel> arrLateFine;
    ArrayList<LoanEMIBreakUpModel> arrLoanBreakUpDetails = new ArrayList<>();
    ArrayList<String> accList = new ArrayList<>();
    String memberCode = "", prevDeposit = "";
    int lastEMINo;
    Double totalLateFine;
    private boolean isReducingEMI = false;
    ArrayList<LoanEMIBreakUpModel> arrayListBreakUp;
    int LateFineSummary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoanCollectionManualBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindEventHandlers();

        Intent intent = getIntent();
        if (intent.hasExtra("loanCode")) {
            String loancode = intent.getStringExtra("loanCode");
            binding.etSearchtxt.setText(loancode);
            accList.clear();
            searchValue = binding.etSearchtxt.getText().toString().trim();
            if (!searchValue.isEmpty()) {
                accDetailsList = new ArrayList<>();
                accList.add("~ Select Account ~");
            }
            getLoanAccountDetails(ApiLinks.GET_LOAN_ACCOUNT_DETAILS, searchValue);
            Log.e("LoanCode", loancode);
        }


        binding.cbPrintRecipt.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                binding.cBtnPrint.setVisibility(View.VISIBLE);
            } else {
                binding.cBtnPrint.setVisibility(View.GONE);
            }
        });

        binding.spSearchtxt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
//                    setAccountDetails(i - 1);
                    String selectedCode = accList.get(i);
                    String loanCode = selectedCode.split(" - ")[0];
                    getLoanHolderDetails(ApiLinks.GET_LOAN_HOLDER_DETAILS, loanCode, GlobalUserData.employeeDataModel.getEmployeeID());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getLoanHolderDetails(String url, String selectedLoanCode, String employeeID) {
        HashMap<String, String> hashMap_loan_details = new HashMap<>();
        hashMap_loan_details.put("LoanCode", selectedLoanCode);
        hashMap_loan_details.put("ArrangerCode", GlobalUserData.employeeDataModel.getEmployeeID());

        new PostDataParserObjectResponse(LoanCollectionManualActivity.this, url, hashMap_loan_details, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    if (result != null) {

                        JSONObject loanDetails = result.getJSONObject("ld");
//                        Log.d("Leng",""+loanDetails.getString("LoanCode").length());
                        if (!loanDetails.getString("LoanCode").equals("null")) {

                            binding.tilAccountNumber.setText(loanDetails.getString("LoanCode"));
                            binding.tilName.setText(loanDetails.getString("HolderName"));
                            binding.tilEmiAmount.setText(loanDetails.getString("EMIAmount"));
                            binding.tilLoanDate.setText(loanDetails.getString("LoanDate"));
                            binding.tilLoanAmount.setText(loanDetails.getString("NetLoanAmount"));
                            binding.tilMode.setText(loanDetails.getString("EMIMode"));
                            binding.tvTotalAmount.setText(loanDetails.getString("TotalDepo"));
                            binding.tvTerm.setText(loanDetails.getString("LoanTerm"));
                            memberCode = (loanDetails.getString("GenericCode"));
                            prevDeposit = (loanDetails.getString("TotalDepo"));
                            lastEMINo = loanDetails.getInt("EMINo");
                            isReducingEMI = loanDetails.getString("IsReducingEMI").equals("False") ? false : true;
                            Log.d("isReducingEMI", isReducingEMI + "");



                        /*JSONArray jsa = result.getJSONArray("accountDetails");
                        for (int i = 0; i < jsa.length(); i++) {
                            JSONObject jso = jsa.getJSONObject(i);
                            accDetailsList.add(new SBDataModel(jso.getString("AccountCode"),
                                    jso.getString("Name"),
                                    jso.getString("Father"),
                                    jso.getString("AccountType"),
                                    jso.getString("AccountBalance")));
                        }*/
                        } else {
                            Toast.makeText(LoanCollectionManualActivity.this, "LoanCode is not Associated with this arranger", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception ex) {
                    Log.d("LoanEX", ex.toString());
                    Toast.makeText(LoanCollectionManualActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(String error) {
                Toast.makeText(LoanCollectionManualActivity.this, "Unable to Fetch Accounts Details", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void bindEventHandlers() {
        binding.btnSearchPolicy.setOnClickListener(this);
        binding.cBtnView.setOnClickListener(this);
        binding.cBtnSave.setOnClickListener(this);
        binding.cBtnPrint.setOnClickListener(this);
        binding.btnView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.btnSearchPolicy) {
            accList.clear();
            searchValue = binding.etSearchtxt.getText().toString().trim();
            if (!searchValue.isEmpty()) {
                accDetailsList = new ArrayList<>();
                accList.add("~ Select Account ~");
            }
            getLoanAccountDetails(ApiLinks.GET_LOAN_ACCOUNT_DETAILS, searchValue);
        }
        if (view == binding.cBtnView) {
            Double enteredAmount = Double.parseDouble(binding.tilAmount.getEditableText().toString().trim());
            Double emiAmount = Double.parseDouble(binding.tilEmiAmount.getText().toString());
            int rem = (int) (enteredAmount % emiAmount);
            int div = (int) (enteredAmount / emiAmount);

            if (!(rem == 0)) {
                binding.tilNetAmount.setText("");
                binding.tilFromInst.setText("");
                binding.tilToInst.setText("");
                Toast.makeText(this, "Please Enter Valid Amount", Toast.LENGTH_SHORT).show();
            } else {
                binding.tilFromInst.setText(String.valueOf(lastEMINo + 1));
                binding.tilToInst.setText(String.valueOf(lastEMINo + div));
                binding.tilNetAmount.setText(String.valueOf(emiAmount * div));
            }

            getLoanLateFine(ApiLinks.GET_LOAN_LATE_FINE, binding.tilAccountNumber.getText().toString(), binding.tilFromInst.getText().toString(), binding.tilToInst.getText().toString());
            getEMIBreakUpData(ApiLinks.GET_LOAN_EMI_BREAKUP_DETAILS, binding.tilAccountNumber.getText().toString(), isReducingEMI);

        }
        if (view == binding.btnView) {
//            Double enteredAmount = Double.parseDouble(binding.tilAmount.getEditableText().toString().trim());
//            Double emiAmount = Double.parseDouble(binding.tilEmiAmount.getText().toString());
//            int rem = (int) (enteredAmount % emiAmount);
//            int div = (int) (enteredAmount / emiAmount);
            if (!binding.insNumber.getText().toString().isEmpty()) {
                int noofemi = Integer.parseInt(binding.insNumber.getText().toString());
                Double emiAmount = Double.parseDouble(binding.tilEmiAmount.getText().toString());

                if( noofemi<=Double.parseDouble(binding.tvTerm.getText().toString())) {
                    binding.tilFromInst.setText(String.valueOf(lastEMINo + 1));
                    binding.tilToInst.setText(String.valueOf(lastEMINo + noofemi));
                    binding.tilNetAmount.setText(String.valueOf(emiAmount * noofemi));
                }else {
                    Toast.makeText(this, "Please Enter Valid Emi", Toast.LENGTH_SHORT).show();
                }

//            if (!(rem == 0)) {
//                binding.tilNetAmount.setText("");
//                binding.tilFromInst.setText("");
//                binding.tilToInst.setText("");
//                Toast.makeText(this, "Please Enter Valid Amount", Toast.LENGTH_SHORT).show();
//            } else {
//                binding.tilFromInst.setText(String.valueOf(lastEMINo + 1));
//                binding.tilToInst.setText(String.valueOf(lastEMINo + div));
//                binding.tilNetAmount.setText(String.valueOf(emiAmount * div));
//            }

                getLoanLateFine(ApiLinks.GET_LOAN_LATE_FINE, binding.tilAccountNumber.getText().toString(), binding.tilFromInst.getText().toString(), binding.tilToInst.getText().toString());
                getEMIBreakUpData(ApiLinks.GET_LOAN_EMI_BREAKUP_DETAILS, binding.tilAccountNumber.getText().toString(), isReducingEMI);
            }

        }
        if (view == binding.cBtnSave) {

           /* for (LoanEMIBreakUpModel l : arrLoanBreakUpDetails) {
                if (Integer.parseInt(l.getPeriod()) >= Integer.parseInt(binding.tilFromInst.getText().toString())
                        && (Integer.parseInt(l.getPeriod()) <= Integer.parseInt(binding.tilToInst.getText().toString()))) {
                    EMIDetails += l.getPeriod() + "," + l.getPayDate() + "," +
                            l.getPrinciple() + "," + l.getInterest() + "," +
                            l.getCurrentBalance() + "," + l.getLateFine() + ";";
                    LateFineSummary += l.getLateFine();
                }

                Log.d("EMIDetails", EMIDetails);

//                EMIDetails = 1,20240915,8333.33,9999.67,491666.67,0;
til_accountNumber
            }*/
          //  if(!binding.tilAccountNumber.getText().toString().isEmpty()) {

                if (!binding.insNumber.getText().toString().isEmpty()) {
                    if (!binding.tilAccountNumber.getText().toString().isEmpty()) {
                        insertLoanMEI(ApiLinks.INSERT_LOAN_AMOUNT_MANUAL, binding.tilAccountNumber.getText().toString(), GlobalUserData.employeeDataModel.getEmployeeID(), binding.insNumber.getText().toString(), Types.INTEGER);
                    }else {
                        Toast.makeText(this, "LoanCode Missing", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Please Enter Valid Amount", Toast.LENGTH_SHORT).show();
                }


       }
        if (view == binding.cBtnPrint) {
            Toast.makeText(this, "Faild To Print", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertLoanMEI(String url, String Pcode, String employeeID,String amount, int error) {
       // binding.cBtnSave.setVisibility(View.GONE);
        Log.e("url", "url"+url);


        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("loanCode", Pcode);
        hashMap.put("amount", amount);
        hashMap.put("userName", employeeID);
        hashMap.put("IsError", "1");

        new PostDataParserObjectResponse(LoanCollectionManualActivity.this, url, hashMap, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    if (result != null) {
                        if (result.length() > 0) {

                            Log.e("result", result.toString());
                            JSONObject jsa = result.getJSONObject("ReturnData");
//                            for (int i = 0; i < jsa.length(); i++) {
//                                JSONObject jso = jsa.getJSONObject(i);
                            if (jsa.getString("ErrorCode").equals("0")) {


                                Toast.makeText(LoanCollectionManualActivity.this, "Entry Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), LoanCollectionManualActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoanCollectionManualActivity.this, "Entry Failed!", Toast.LENGTH_SHORT).show();
//                                    startActivity(new Intent(getApplicationContext(),ArrangerSBCollectionActivity.class));
//                                    finish();
                            }
//                            }


                        }

                    }
                } catch (Exception e) {
                    e.toString();
                    Log.d("Exception", e.toString());
                    Toast.makeText(LoanCollectionManualActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(String error) {
                Toast.makeText(LoanCollectionManualActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });
        binding.cBtnSave.setVisibility(View.VISIBLE);
    }

    private void getLoanLateFine(String url, String loanCode, String fromInst, String toInst) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("loanCode", loanCode);
        hashMap.put("fromInstNo", fromInst);
        hashMap.put("toInstNo", toInst);

        new PostDataParserObjectResponse(LoanCollectionManualActivity.this, url, hashMap, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    if (result != null) {
                        if (result.length() > 0) {
                            JSONArray jsa = result.getJSONArray("latefinedetails");
                            for (int i = 0; i < jsa.length(); i++) {
                                JSONObject jso = jsa.getJSONObject(i);
                                arrLateFine = new ArrayList<>();
                                arrLateFine.add(new LateFineModel(jso.getString("LateFine"),
                                        jso.getString("EmiNo"),
                                        jso.getString("LoanCode")));

                                totalLateFine = jso.getDouble("LateFine");
                                binding.lateFine.setText(String.valueOf(totalLateFine));

//                                getEMIBreakUpData(ApiLinks.GET_LOAN_EMI_BREAKUP_DETAILS, binding.tilAccountNumber.getText().toString(), isReducingEMI);
                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(LoanCollectionManualActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(String error) {
                Toast.makeText(LoanCollectionManualActivity.this, "Unable to Fetch Accounts Details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getEMIBreakUpData(String url, String loanCode, boolean isReducingEMI) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("LoanCode", loanCode);
        hashMap.put("isReducing", String.valueOf(isReducingEMI ? 1 : 0));

        new PostDataParserObjectResponse(LoanCollectionManualActivity.this, url, hashMap, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    if (result != null) {
                        if (result.length() > 0) {
                            JSONArray jsa = result.getJSONArray("BreakUpDetails");
                            for (int i = 0; i < jsa.length(); i++) {
                                JSONObject jso = jsa.getJSONObject(i);

                                if (jso.getString("PERIOD") == null) {
                                    arrLoanBreakUpDetails.add(new LoanEMIBreakUpModel(jso.getString("PERIOD"),
                                            jso.getString("PAYDATE"),
                                            jso.getString("PAYMENT"),
                                            jso.getString("INTEREST"),
                                            jso.getString("PRINCIPAL"),
                                            jso.getString("CURRENT_BALANCE"),
                                            jso.getInt("0")));
                                } else {
                                    arrLoanBreakUpDetails.add(new LoanEMIBreakUpModel(jso.getString("PERIOD"),
                                            jso.getString("PAYDATE"),
                                            jso.getString("PAYMENT"),
                                            jso.getString("INTEREST"),
                                            jso.getString("PRINCIPAL"),
                                            jso.getString("CURRENT_BALANCE"),
                                            jso.getInt("PERIOD")));
                                }


                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(LoanCollectionManualActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(String error) {
                Toast.makeText(LoanCollectionManualActivity.this, "Unable to Fetch Accounts Details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getLoanAccountDetails(String url, String searchValue) {
        HashMap<String, String> hashMap_loan_details = new HashMap<>();
        hashMap_loan_details.put("SearchValue", searchValue);
        hashMap_loan_details.put("SearchTag", "Loan");

        new PostDataParserObjectResponse(LoanCollectionManualActivity.this, url, hashMap_loan_details, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    if (result != null) {
                        JSONArray jsa = result.getJSONArray("accountDetails");
                        for (int i = 0; i < jsa.length(); i++) {
                            JSONObject jso = jsa.getJSONObject(i);
                            accDetailsList.add(new SBDataModel(jso.getString("AccountCode"),
                                    jso.getString("Name"),
                                    jso.getString("Father"),
                                    jso.getString("AccountType"),
                                    jso.getString("AccountBalance")));
                            accList.add(jso.getString("AccountCode") + " - " + jso.getString("Name"));
                        }
                        if (accList.size() > 0) {
                            ArrayAdapter ad
                                    = new ArrayAdapter(
                                    LoanCollectionManualActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    accList);
                            ad.setDropDownViewResource(
                                    android.R.layout
                                            .simple_spinner_dropdown_item);

                            binding.spSearchtxt.setAdapter(ad);
                            binding.laySearch.getLayoutParams().height = 250;
                            binding.spSearchtxt.setVisibility(View.VISIBLE);
                        } else {
                            binding.spSearchtxt.setVisibility(View.GONE);
                            Toast.makeText(LoanCollectionManualActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    binding.spSearchtxt.setVisibility(View.GONE);
                    Toast.makeText(LoanCollectionManualActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(String error) {
                binding.spSearchtxt.setVisibility(View.GONE);
                Toast.makeText(LoanCollectionManualActivity.this, "Unable to Fetch Accounts", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = getIntent();
        if (intent.hasExtra("loanCode")) {
            String loancode = intent.getStringExtra("loanCode");
            startActivity(new Intent(LoanCollectionManualActivity.this, AgentLoanDueReportDaywise.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();

        } else {
            startActivity(new Intent(LoanCollectionManualActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }

    }
}