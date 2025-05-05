package com.example.geniousmicro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.geniousmicro.Data.PostDataParserObjectResponse;
import com.example.geniousmicro.Data.VolleyCallback;
import com.example.geniousmicro.Models.UtilityModels.PolicyDetailsModel;
import com.example.geniousmicro.Others.ApiLinks;
import com.example.geniousmicro.UserData.GlobalUserData;
import com.example.geniousmicro.activities.AgentPolicyDueReportDaywise;


import com.example.geniousmicro.databinding.ActivityRenewalCollectionBinding;
import com.example.geniousmicro.util.NetworkUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RenewalCollectionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RenewalCollection";
    private static final int API_TIMEOUT = 30000; // 30 seconds timeout
    private static final int MAX_RETRY_COUNT = 3;

    ActivityRenewalCollectionBinding binding;
    String status = "Approved";
    String IsLatePaid = "";
    PolicyDetailsModel policyDetailsModel;
    private ArrayList<String> arrayList_policyCode = new ArrayList<>();
    private ArrayList<String> arrayList_policyCodeName = new ArrayList<>();
    private double actualLateFine = 0.0;
    private int IsLateFine = 0;
    private String NAME;
    private int currentRetryCount = 0;
    private AlertDialog progressDialog;
    private Handler timeoutHandler = new Handler();

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

        initUI();
        setupListeners();
        handleIntent();
    }

    private void initUI() {
        binding.cbPrintRecipt.setChecked(true);
        createProgressDialog();
    }

    private void createProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.progress_dialog);
        builder.setCancelable(false);
        progressDialog = builder.create();
    }

    private void setupListeners() {
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
                // Do nothing
            }
        });

        binding.isLatePaid.setOnClickListener(v -> {
            IsLatePaid = binding.isLatePaid.isChecked() ? "1" : "0";
        });
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("policyCode")) {
            String loancode = intent.getStringExtra("policyCode");
            binding.etSearchtxt.setText(loancode);
            if (!binding.etSearchtxt.getText().toString().isEmpty()) {
                resetPolicyLists();
                searchPolicyByNameOrCode(binding.etSearchtxt.getText().toString());
            } else {
                showToast("Please Enter Policy Code OR Name");
            }
        }
    }

    private void resetPolicyLists() {
        arrayList_policyCode.clear();
        arrayList_policyCodeName.clear();
        arrayList_policyCode.add("");
        arrayList_policyCodeName.add("--Select An Item--");
    }

    @Override
    public void onClick(View view) {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showToast("No internet connection. Please check your network settings.");
            return;
        }

        if (view == binding.btnSearchPolicy) {
            handleSearchPolicyClick();
        } else if (view == binding.btnView) {
            handleViewClick();
        } else if (view == binding.cBtnView) {
            handleCustomViewClick();
        } else if (view == binding.btnSave) {
            handleSaveClick();
        }
    }

    private void handleSearchPolicyClick() {
        if (!binding.etSearchtxt.getText().toString().isEmpty()) {
            resetPolicyLists();
            searchPolicyByNameOrCode(binding.etSearchtxt.getText().toString());
        } else {
            showToast("Please Enter Policy Code OR Name");
        }
    }

    private void handleViewClick() {
        if (!binding.insNumber.getText().toString().isEmpty()) {
            actualLateFine = 0.0;
            IsLateFine = 0;
            getInstChange();
        } else {
            showToast("Please Enter Installment Number");
        }
    }

    private void handleCustomViewClick() {
        if (!binding.tilAmount.getText().toString().isEmpty()) {
            try {
                Double enteredAmount = Double.parseDouble(binding.tilAmount.getEditableText().toString().trim());
                Double emiAmount = Double.parseDouble(binding.emiAmount.getText().toString());
                int rem = (int) (enteredAmount % emiAmount);
                int div = (int) (enteredAmount / emiAmount);

                if (rem != 0) {
                    clearInstallmentFields();
                    showToast("Please Enter Valid Amount");
                } else {
                    if (div <= Integer.parseInt(binding.term.getText().toString())) {
                        binding.insNumber.setText("" + div);
                        actualLateFine = 0.0;
                        IsLateFine = 0;
                        getInstChange();
                    } else {
                        clearInstallmentFields();
                        showToast("Please Enter Valid Amount");
                    }
                }
            } catch (NumberFormatException e) {
                Log.e(TAG, "Error parsing amounts: " + e.getMessage());
                showToast("Invalid amount format");
            }
        } else {
            showToast("Enter Amount");
        }
    }

    private void clearInstallmentFields() {
        binding.fromInsNumber.setText("");
        binding.toInsNumber.setText("");
        binding.netAmount.setText("");
    }

    private void handleSaveClick() {
        if (!binding.txtPolicyCode.getText().toString().isEmpty()) {
            if (!binding.totalAmount.getText().toString().isEmpty()) {
                showProgressDialog("Checking renewal status...");
                checkRenewalPending(binding.txtPolicyCode.getText().toString());
            } else {
                showToast("Please Select the Installment Number");
            }
        } else {
            showToast("Please Select Valid policy Code");
        }
    }

    private void getPolicyDetails(String PolicyCode) {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showToast("No internet connection. Please check your network settings.");
            return;
        }

        showProgressDialog("Loading policy details...");
        HashMap<String, String> map = new HashMap<>();
        map.put("PolicyCode", PolicyCode.trim());
        map.put("ACode", GlobalUserData.employeeDataModel.getEmployeeID());

        startApiTimeout("Get Policy Details");

        new PostDataParserObjectResponse(RenewalCollectionActivity.this, ApiLinks.GET_POLICYDETAILS, map, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                cancelApiTimeout();
                hideProgressDialog();
                try {
                    if (result.has("rd")) {
                        JSONObject jsa = result.getJSONObject("rd");
                        if (jsa.length() > 0) {
                            Gson gson = new Gson();
                            policyDetailsModel = gson.fromJson(String.valueOf(result.getJSONObject("rd")), PolicyDetailsModel.class);
                            if (policyDetailsModel != null && policyDetailsModel.getApplicantName() != null) {
                                updatePolicyDetailsUI();
                            } else {
                                clearPolicyDetailsUI();
                                showToast("PolicyCode is Not Associated with this arranger");
                            }
                        } else {
                            clearPolicyDetailsUI();
                            showToast("No policy details found");
                        }
                    } else {
                        showToast("Invalid response format");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing policy details: " + e.getMessage(), e);
                    showToast("Error retrieving policy details");
                }
            }

            @Override
            public void onErrorResponse(String error) {
                cancelApiTimeout();
                hideProgressDialog();
                Log.e(TAG, "API Error: " + error);
                showToast("Failed to get policy details. Please try again.");
            }
        });
    }

    private void updatePolicyDetailsUI() {
        binding.name.setText("Hi " + policyDetailsModel.getApplicantName());
        NAME = policyDetailsModel.getApplicantName();
        binding.txtPolicyCode.setText(policyDetailsModel.getPolicyCode());
        binding.DOC.setText(policyDetailsModel.getDateofCom());
        binding.plan.setText(policyDetailsModel.getPTable());
        binding.term.setText(policyDetailsModel.getTerm());
        binding.lateFine.setText("Yes");
        binding.mode.setText(policyDetailsModel.getMode());
        binding.totalAmount.setText(policyDetailsModel.getTillAmount());
        binding.memberCode.setText(policyDetailsModel.getMemberCode());
        binding.PhoneNo.setText(policyDetailsModel.getPhoneNo());
        binding.emiAmount.setText(policyDetailsModel.getAmount());
        binding.depositeDate.setText(policyDetailsModel.getCDate());
    }

    private void clearPolicyDetailsUI() {
        binding.name.setText("Hi ");
        binding.txtPolicyCode.setText("");
        binding.DOC.setText("");
        binding.plan.setText("");
        binding.term.setText("");
        binding.lateFine.setText("");
        binding.mode.setText("");
        binding.totalAmount.setText("");
        binding.memberCode.setText("");
        binding.PhoneNo.setText("");
        binding.emiAmount.setText("");
        binding.depositeDate.setText("");
    }

    private void checkRenewalPending(String policyCode) {
        binding.btnSave.setEnabled(false);
        HashMap<String, String> map = new HashMap<>();
        map.put("Code", policyCode.trim());
        map.put("SearchTag", "policy");

        startApiTimeout("Check Renewal Pending");

        new PostDataParserObjectResponse(RenewalCollectionActivity.this, ApiLinks.CHECK_IFRENEWALPENDING, map, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                cancelApiTimeout();
                try {
                    JSONObject jsa = result.getJSONObject("status");
                    if (jsa.getString("Status").equals("0")) {
                        updateProgressDialog("Saving renewal...");
                        SaveRenewal();
                    } else {
                        hideProgressDialog();
                        binding.btnSave.setEnabled(true);
                        showToast("Previous Renewal is Pending");
                    }
                } catch (Exception e) {
                    hideProgressDialog();
                    binding.btnSave.setEnabled(true);
                    Log.e(TAG, "Error checking renewal pending: " + e.getMessage(), e);
                    showToast("Error checking renewal status");
                }
            }

            @Override
            public void onErrorResponse(String error) {
                cancelApiTimeout();
                hideProgressDialog();
                binding.btnSave.setEnabled(true);
                Log.e(TAG, "API Error: " + error);
                showToast("Failed to check renewal status. Please try again.");
            }
        });
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

        startApiTimeout("Save Renewal");

        new PostDataParserObjectResponse(RenewalCollectionActivity.this, ApiLinks.INSERT_RENEWAL, map, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                cancelApiTimeout();
                hideProgressDialog();
                binding.btnSave.setEnabled(true);
                try {
                    JSONObject jsa = result.getJSONObject("response");
                    if (jsa.getString("ErrorCode").equals("0")) {
                        showToast("Saved Successfully");
                       /* if (binding.cbPrintRecipt.isChecked()) {
                            Print();
                        } else {*/
                            startActivity(new Intent(RenewalCollectionActivity.this, RenewalCollectionActivity.class));
                            finish();
                       // }
                    } else {
                        String errorMessage = jsa.has("Message") ? jsa.getString("Message") : "Unable To Save";
                        showToast(errorMessage);
                        // Reset retry counter when we get an error response
                        currentRetryCount = 0;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error saving renewal: " + e.getMessage(), e);
                    showToast("Error processing the save request");
                }
            }

            @Override
            public void onErrorResponse(String error) {
                cancelApiTimeout();
                hideProgressDialog();
                binding.btnSave.setEnabled(true);
                Log.e(TAG, "API Error: " + error);

                if (currentRetryCount < MAX_RETRY_COUNT) {
                    currentRetryCount++;
                    showToast("Save failed. Retrying... (" + currentRetryCount + "/" + MAX_RETRY_COUNT + ")");
                    new Handler().postDelayed(() -> SaveRenewal(), 1000);
                } else {
                    showToast("Failed to save renewal after multiple attempts. Please try again later.");
                    currentRetryCount = 0;
                }
            }
        });
    }

    private void Print() {
        try {
            LocalDateTime currentDateTime = null;
            DateTimeFormatter formatter = null;
            String formattedDateTime = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                currentDateTime = LocalDateTime.now();
                formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                formattedDateTime = currentDateTime.format(formatter);
            }

            int CLAMT = Integer.parseInt(binding.totalAmount.getText().toString()) + Integer.parseInt(binding.netAmount.getText().toString());
            String Employcode = GlobalUserData.employeeDataModel.getEmployeeID();
            String Officeid = GlobalUserData.employeeDataModel.getOfficeid();
            //String Phone = GlobalUserData.employeeDataModel.get();

            String customMessage =
                    "-------------------------------\n" +
                            "| " + getResources().getString(R.string.app_name) + ". |\n" +
                            "-------------------------------\n" +
                            String.format("| Open Date:  %s \n", binding.DOC.getText().toString()) +
                            String.format("| Name :  %s \n", NAME) +
                            String.format("| Policy code:  %s \n", binding.txtPolicyCode.getText().toString()) +
                            String.format("| Tr. Date :  %s \n", "" + formattedDateTime) +
                            String.format("| Inst No. :  %s \n", binding.fromInsNumber.getText().toString() + " FROM " + binding.toInsNumber.getText().toString()) +
                            String.format("| OPN.BAL. :  %s \n", binding.totalAmount.getText().toString()) +
                            String.format("| COLL.AMT :  %s \n", binding.netAmount.getText().toString()) +
                            String.format("| CL.BAL :  %s ", CLAMT) +
                            String.format(" Mode :  %s\n", "CASH") +
                            String.format("| EMP. :  %s", Employcode) +
                            String.format(" OFFICE. :  %s\n ", Officeid) +
                            String.format("| This is system generated & does not require signature") +
                            "\n\n\n\n\n";

          /*  Intent intent;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                intent = new Intent(RenewalCollectionActivity.this, PrintSecondActivity.class);
            } else {
                intent = new Intent(RenewalCollectionActivity.this, PrintActivity.class);
            }
            intent.putExtra("printtxt", customMessage);
            intent.putExtra("Page", "POLICY_PRINT");
            startActivity(intent);*/

        } catch (Exception e) {
            Log.e(TAG, "Error in print: " + e.getMessage(), e);
            showToast("Error generating print receipt");
        }
    }

    private void getInstChange() {
        try {
            int toinstno = Integer.parseInt(binding.insNumber.getText().toString());
            if (Integer.parseInt(policyDetailsModel.getLastInstNo()) + toinstno <= Integer.parseInt(binding.term.getText().toString())) {
                binding.fromInsNumber.setText(String.valueOf(Integer.parseInt(policyDetailsModel.getLastInstNo()) + 1));
                binding.toInsNumber.setText(String.valueOf(Integer.parseInt(policyDetailsModel.getLastInstNo()) + toinstno));
                binding.netAmount.setText(String.valueOf(Integer.parseInt(binding.emiAmount.getText().toString()) * toinstno));
                getRenewalLatefine();
            } else {
                showToast("Enter Valid Installment Number");
            }
        } catch (NumberFormatException ex) {
            Log.e(TAG, "Error parsing installment numbers: " + ex.getMessage());
            showToast("Invalid installment number");
        } catch (Exception ex) {
            Log.e(TAG, "Unexpected error: " + ex.getMessage(), ex);
            showToast("Error calculating installments");
        }
    }

    private void getRenewalLatefine() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showToast("No internet connection. Please check your network settings.");
            return;
        }

        showProgressDialog("Calculating late fine...");
        HashMap<String, String> map = new HashMap<>();
        map.put("PolicyCode", Objects.requireNonNull(binding.txtPolicyCode.getText()).toString().trim());
        map.put("FromInstNo", Objects.requireNonNull(binding.fromInsNumber.getText()).toString().trim());
        map.put("ToInstNo", Objects.requireNonNull(binding.toInsNumber.getText()).toString().trim());

        startApiTimeout("Get Late Fine");

        new PostDataParserObjectResponse(RenewalCollectionActivity.this, ApiLinks.GET_RENEWALLATEFINE, map, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                cancelApiTimeout();
                hideProgressDialog();
                try {
                    if (result.has("latefinedetails")) {
                        JSONArray jsa = result.getJSONArray("latefinedetails");
                        actualLateFine = 0.0;

                        if (jsa.length() > 0) {
                            binding.lateFineLayout.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsa.length(); i++) {
                                actualLateFine += Double.parseDouble(jsa.getJSONObject(i).getString("LateFine"));
                            }
                            IsLateFine = 1;
                            binding.latefineAmount.setText(String.valueOf(actualLateFine));
                        } else {
                            binding.lateFineLayout.setVisibility(View.GONE);
                            binding.latefineAmount.setText("0");
                            IsLateFine = 0;
                            showToast("Late fine not applicable");
                        }
                    } else {
                        showToast("Invalid response format");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing late fine: " + e.getMessage(), e);
                    showToast("Error calculating late fine");
                }
            }

            @Override
            public void onErrorResponse(String error) {
                cancelApiTimeout();
                hideProgressDialog();
                Log.e(TAG, "API Error: " + error);
                showToast("Failed to calculate late fine. Please try again.");
            }
        });
    }

    private void searchPolicyByNameOrCode(String searchValue) {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showToast("No internet connection. Please check your network settings.");
            return;
        }

        showProgressDialog("Searching policies...");
        HashMap<String, String> map = new HashMap<>();
        map.put("SearchValue", searchValue.trim());
        map.put("SearchTag", "Policy");
        // map.put("ArrangerCode", GlobalUserData.employeeDataModel.getEmployeeID());

        startApiTimeout("Search Policy");

        new PostDataParserObjectResponse(RenewalCollectionActivity.this, ApiLinks.GET_ACCOUNTDETAILS, map, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                cancelApiTimeout();
                hideProgressDialog();
                try {
                    if (result.has("accountDetails")) {
                        JSONArray jsa = result.getJSONArray("accountDetails");
                        if (jsa.length() > 0) {
                            binding.spPolicyNameAndCodeList.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsa.length(); i++) {
                                arrayList_policyCode.add(jsa.getJSONObject(i).getString("AccountCode"));
                                arrayList_policyCodeName.add(jsa.getJSONObject(i).getString("Name") + " - " + jsa.getJSONObject(i).getString("AccountCode"));
                            }
                            ArrayAdapter arrayAdapter = new ArrayAdapter(RenewalCollectionActivity.this, R.layout.row_savings_acc_hint, arrayList_policyCodeName);
                            arrayAdapter.setDropDownViewResource(R.layout.row_savings_acc_hint);
                            binding.spPolicyNameAndCodeList.setAdapter(arrayAdapter);
                        } else {
                            showToast("No policies found with the provided search term");
                        }
                    } else {
                        showToast("Invalid response format");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing policy search: " + e.getMessage(), e);
                    showToast("Error retrieving policies");
                }
            }

            @Override
            public void onErrorResponse(String error) {
                cancelApiTimeout();
                hideProgressDialog();
                Log.e(TAG, "API Error: " + error);
                showToast("Failed to search policies. Please try again.");
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        if (intent.hasExtra("policyCode")) {
            startActivity(new Intent(RenewalCollectionActivity.this, AgentPolicyDueReportDaywise.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else {
            startActivity(new Intent(RenewalCollectionActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }

    // Helper methods for UI feedback and API timeout management

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showProgressDialog(String message) {
        if (progressDialog == null) {
            createProgressDialog();
        }
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void updateProgressDialog(String message) {
        if (progressDialog != null && progressDialog.isShowing()) {
            // Could update text if dialog has a TextView
        }
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void startApiTimeout(String apiName) {
        timeoutHandler.postDelayed(() -> {
            Log.e(TAG, apiName + " API timeout");
            hideProgressDialog();
            showToast(apiName + " request timed out. Please try again.");
            // Re-enable any disabled buttons
            binding.btnSave.setEnabled(true);
        }, API_TIMEOUT);
    }

    private void cancelApiTimeout() {
        timeoutHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelApiTimeout();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}