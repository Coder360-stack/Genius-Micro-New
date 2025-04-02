package com.example.geniousmicro.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.geniousmicro.Data.PostDataParserObjectResponse;
import com.example.geniousmicro.Data.VolleyCallback;
import com.example.geniousmicro.Models.UtilityModels.SBDataModel;
import com.example.geniousmicro.Others.ApiLinks;
import com.example.geniousmicro.R;
import com.example.geniousmicro.UserData.GlobalUserData;
import com.example.geniousmicro.databinding.ActivityArrangerSbcollectionBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class ArrangerSBCollectionActivity extends AppCompatActivity {
    ActivityArrangerSbcollectionBinding binding;
    ArrayList<String> accList;
  //  ArrayList<String> accList;
    private ProgressDialog loadingDialog;
    ArrayList<SBDataModel> accDetailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityArrangerSbcollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        accList = new ArrayList<>();
        binding.spSearchtxt.setVisibility(View.GONE);

        binding.spSearchtxt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    setAccountDetails(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.btnDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.edBal.getText().toString().length() > 0 && binding.accno.getText().toString().length() > 0) {
                    insertSBTransaction("d", binding.edBal.getText().toString(), binding.edRemarks.getText().toString());
                } else {
                    Toast.makeText(ArrangerSBCollectionActivity.this, "Please Enter Valid Details", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.edBal.getText().toString().length() > 0 && binding.accno.getText().toString().length() > 0) {
                    insertSBTransaction("w", binding.edBal.getText().toString(), binding.edRemarks.getText().toString());
                } else {
                    Toast.makeText(ArrangerSBCollectionActivity.this, "Please Enter Valid Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Immediately validate input
                String searchValue = binding.edSearchtxt.getText().toString().trim();
                if (searchValue.isEmpty()) {
                    Toast.makeText(ArrangerSBCollectionActivity.this, "Enter Value to Search", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Use a Handler to show dialog with a slight delay
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // Prepare UI for search
                        binding.spSearchtxt.setVisibility(View.GONE);
                        accList = new ArrayList<>();
                        accDetailsList = new ArrayList<>();
                        accList.add("~ Select Account ~");

                        // Show dialog immediately
                        showLoadingDialog();

                        // Perform search in a slightly delayed manner to ensure UI is responsive
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getSBAccounts(searchValue);
                            }
                        }, 100); // Minimal delay to allow UI to update
                    }
                });
            }
        });
    }
    private void dismissLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                loadingDialog = null;
            }
        });
    }

    private JsonObjectRequest jsonObjectRequest; // Make this a class member to allow cancellation


    private void showLoadingDialog() {
        // Use WeakReference to prevent potential memory leaks
        final WeakReference<ArrangerSBCollectionActivity> activityRef = new WeakReference<>(this);

        // Ensure dialog creation happens on UI thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrangerSBCollectionActivity activity = activityRef.get();
                if (activity == null || activity.isFinishing()) return;

                // Dismiss existing dialog if any
                dismissLoadingDialog();

                // Create dialog
                loadingDialog = new ProgressDialog(ArrangerSBCollectionActivity.this);
                loadingDialog.setMessage("Searching Accounts...");
                loadingDialog.setCancelable(true);
                loadingDialog.setIndeterminate(true);

                // Add cancel button
                loadingDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel the Volley request if it's still running
                        if (jsonObjectRequest != null) {
                            jsonObjectRequest.cancel();
                        }
                        dialog.dismiss();
                    }
                });

                // Show dialog with a fade-in effect
                Window window = loadingDialog.getWindow();
                if (window != null) {
                    window.setWindowAnimations(android.R.style.Animation_Dialog);
                }

                loadingDialog.show();
            }
        });
    }

    private void insertSBTransaction(String mode, String amount, String remarks) {
        String Remarks="";
          if (!binding.edRemarks.getText().toString().isEmpty()) {
               Remarks=remarks;
          }else {
               Remarks="Savings Trnsaction";
          }

        HashMap<String, String> map = new HashMap<>();
        map.put("SBAccountCode", binding.accno.getText().toString());
        map.put("Amount", amount);
        map.put("Remarks", Remarks);
        map.put("ArrangerCode", GlobalUserData.employeeDataModel.getEmployeeID());
        map.put("Type", mode);
        map.put("IsMemberRequest", "0");
        map.put("ErrorCode", "0");
        new PostDataParserObjectResponse(ArrangerSBCollectionActivity.this, ApiLinks.INSERT_SBTRANSACTION, map, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    JSONObject jso=result.getJSONObject("ReturnData");
                    if(jso.getString("ErrorCode").equals("0")){
                        Toast.makeText(ArrangerSBCollectionActivity.this, "Successfully Done", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),ArrangerSBCollectionActivity.class));
                        finish();
                    }else if(jso.getString("ErrorCode").equals("2")){
                        Toast.makeText(ArrangerSBCollectionActivity.this, "Collection Already Pending", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(ArrangerSBCollectionActivity.this, "Unable to Complete Collection", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.d("InsertSBError", e.toString());
                    Toast.makeText(ArrangerSBCollectionActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(String error) {
                Toast.makeText(ArrangerSBCollectionActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAccountDetails(int i) {
        SBDataModel model = accDetailsList.get(i);
        binding.name.setText(model.getName());
        binding.bal.setText(model.getBalance());
        binding.accno.setText(model.getAccountCode());
    }

    private void getSBAccounts(String accCode) {
        // Validate input
        if (TextUtils.isEmpty(accCode)) {
            Toast.makeText(ArrangerSBCollectionActivity.this, "Please enter a valid search value", Toast.LENGTH_SHORT).show();
            return;
        }

        // Clear previous results
        accList.clear();
        accDetailsList.clear();
        accList.add("~ Select Account ~");

        // Show loading dialog
        showLoadingDialog();

        // Create request parameters
        HashMap<String, String> map = new HashMap<>();
        map.put("SearchTag", "Savings");
        map.put("SearchValue", accCode);
        map.put("ArrangerCode", GlobalUserData.employeeDataModel.getEmployeeID());

        // Configure request with timeout and retry policy
        int socketTimeout = 30000; // 30 seconds
        RetryPolicy policy = new DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );

        // Create the request with error handling
        jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                ApiLinks.GET_ACCOUNTDETAILS,
                new JSONObject(map),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {
                        // Dismiss loading dialog
                        dismissLoadingDialog();

                        try {
                            // Null check and validate response
                            if (result == null) {
                                showErrorMessage("No data received");
                                return;
                            }

                            // Check if accounts array exists
                            if (!result.has("accountDetails")) {
                                showErrorMessage("Invalid response format");
                                return;
                            }

                            JSONArray jsa = result.getJSONArray("accountDetails");

                            // Check if array is empty
                            if (jsa.length() == 0) {
                                showErrorMessage("No accounts found");
                                return;
                            }

                            // Process accounts with error checking
                            for (int i = 0; i < jsa.length(); i++) {
                                try {
                                    JSONObject jso = jsa.getJSONObject(i);

                                    // Validate required fields
                                    if (!jso.has("AccountCode") || !jso.has("Name")) {
                                        Log.w("AccountProcessing", "Skipping incomplete account entry");
                                        continue;
                                    }

                                    SBDataModel sbModel = new SBDataModel(
                                            jso.optString("AccountCode", "N/A"),
                                            jso.optString("Name", "Unknown"),
                                            jso.optString("Father", ""),
                                            jso.optString("AccountType", ""),
                                            jso.optString("AccountBalance", "0")
                                    );

                                    accDetailsList.add(sbModel);
                                    accList.add(sbModel.getAccountCode() + " - " + sbModel.getName());
                                } catch (JSONException e) {
                                    Log.e("AccountProcessingError", "Error processing account: " + e.getMessage());
                                }
                            }

                            // Update UI with results
                            if (!accList.isEmpty()) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                        ArrangerSBCollectionActivity.this,
                                        android.R.layout.simple_spinner_item,
                                        accList
                                );
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                binding.spSearchtxt.setAdapter(adapter);

                              //  binding.laySearch.getLayoutParams().height = 250;
                                binding.spSearchtxt.setVisibility(View.VISIBLE);
                            } else {
                                showErrorMessage("No accounts found");
                            }

                        } catch (JSONException e) {
                            Log.e("ResponseParsingError", "Error parsing response", e);
                            showErrorMessage("Error processing account data");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Dismiss loading dialog
                        dismissLoadingDialog();

                        // Detailed error handling
                        String errorMessage = "Network error";
                        if (error instanceof TimeoutError) {
                            errorMessage = "Request timed out. Please check your connection.";
                        } else if (error instanceof NoConnectionError) {
                            errorMessage = "No internet connection available.";
                        } else if (error instanceof AuthFailureError) {
                            errorMessage = "Authentication failed. Please login again.";
                        } else if (error instanceof ServerError) {
                            errorMessage = "Server error. Please try again later.";
                        } else if (error instanceof NetworkError) {
                            errorMessage = "Network error. Please check your connection.";
                        } else if (error instanceof ParseError) {
                            errorMessage = "Error parsing server response.";
                        }

                        showErrorMessage(errorMessage);
                        Log.e("VolleyError", error.toString());
                    }
                }
        );

        // Set retry policy
        jsonObjectRequest.setRetryPolicy(policy);

        // Add to request queue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
    private void showErrorMessage(String message) {
        Toast.makeText(ArrangerSBCollectionActivity.this, message, Toast.LENGTH_SHORT).show();
        binding.spSearchtxt.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        // Ensure dialog is dismissed to prevent window leaks
        dismissLoadingDialog();
        super.onDestroy();
    }

}