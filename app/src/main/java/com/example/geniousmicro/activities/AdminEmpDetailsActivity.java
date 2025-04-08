package com.example.geniousmicro.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.geniousmicro.R;
import com.example.geniousmicro.databinding.ActivityAdminEmpDetailsBinding;
import com.example.geniousmicro.mssql.SqlManager;

import com.example.geniousmicro.util.NetworkUtils;
import com.google.android.material.snackbar.Snackbar;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminEmpDetailsActivity extends AppCompatActivity {

    private static final String TAG = "AdminEmpDetailsActivity";
    private ActivityAdminEmpDetailsBinding binding;
    private ExecutorService executorService;
    private String currentEmployeeCode = "";
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAdminEmpDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        executorService = Executors.newSingleThreadExecutor();

        setupWindowInsets();
        setupListeners();
        setupSwipeRefresh();

        // Check for intent extras that might contain employee code
        handleIncomingIntent();
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupListeners() {
        // Search button click listener
        binding.buttobempcode.setOnClickListener(view -> {
            String employeeCode = binding.empcode.getText().toString().trim();
            if (employeeCode.isEmpty()) {
                binding.empcode.setError("Please enter employee code");
                binding.empcode.requestFocus();
            } else {
                showLoadingState(true);
                fetchEmployeeDetails(employeeCode);
            }
        });

        // Status update button (initially disabled)
        binding.btnUpdateStatus.setEnabled(false);
        binding.btnUpdateStatus.setOnClickListener(v -> showStatusUpdateDialog());

        // Set up toolbar back button if available
        if (binding.toolbar != null) {
            binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }
    }

    private void setupSwipeRefresh() {

    }

    private void handleIncomingIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("employee_code")) {
            String employeeCode = intent.getStringExtra("employee_code");
            binding.empcode.setText(employeeCode);
            fetchEmployeeDetails(employeeCode);
        }
    }

    private void fetchEmployeeDetails(String employeeCode) {
        // First check network connectivity
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showNetworkError();
            showLoadingState(false);
            return;
        }

        currentEmployeeCode = employeeCode;

        executorService.execute(() -> {
            Connection connection = null;
            CallableStatement statement = null;
            ResultSet resultSet = null;

            try {
                connection = new SqlManager().getSQLConnection();
                if (connection == null) {
                    mainHandler.post(() -> showDatabaseConnectionError());
                    return;
                }

                statement = connection.prepareCall("{call ADROID_GET_EMPLOYEEDETAILS(?)}");
                statement.setString("@empcode", employeeCode);
                boolean hasResults = statement.execute();

                if (hasResults) {
                    resultSet = statement.getResultSet();
                    if (resultSet.next()) {
                        // Employee found - process and display data
                        processEmployeeData(resultSet);
                    } else {
                        // No employee found with this code
                        mainHandler.post(() -> showNoDataFound());
                    }
                } else {
                    // Procedure executed but no result set returned
                    mainHandler.post(() -> showNoDataFound());
                }

            } catch (SQLException e) {
                Log.e(TAG, "Database error: " + e.getMessage(), e);
                mainHandler.post(() -> showDatabaseError(e));
            } catch (Exception e) {
                Log.e(TAG, "General error: " + e.getMessage(), e);
                mainHandler.post(() -> showGeneralError(e));
            } finally {
                closeResources(resultSet, statement, connection);
                mainHandler.post(() -> showLoadingState(false));
            }
        });
    }

    private void processEmployeeData(ResultSet rs) {
        try {
            // Extract all the employee data
            final String name = rs.getString("ArrangerName");
            final String code = rs.getString("ArrangerCode");
            final String dob = rs.getString("ArrangerDOB");
            final String doj = rs.getString("DateOfJoin");
            final String address = rs.getString("Address");
            final String bloodGroup = rs.getString("BLOODGR");
            final String gender = rs.getString("Gender");
            final String phone = rs.getString("Phone");
            final String officeId = rs.getString("OfficeID");
            final String fatherName = rs.getString("Father");
            final String idProof = rs.getString("IdentityProof");
            final int isBlocked = rs.getInt("IsBlock");

            // Update UI on main thread
            mainHandler.post(() -> {
                updateUIWithEmployeeData(
                        name, code, dob, doj, address, bloodGroup,
                        gender, phone, officeId, fatherName, idProof, isBlocked
                );
            });
        } catch (SQLException e) {
            Log.e(TAG, "Error processing employee data: " + e.getMessage(), e);
            mainHandler.post(() -> showDatabaseError(e));
        }
    }

    private void updateUIWithEmployeeData(
            String name, String code, String dob, String doj,
            String address, String bloodGroup, String gender,
            String phone, String officeId, String fatherName,
            String idProof, int isBlocked) {

        // Basic info
        binding.name.setText(name);
        binding.mcode.setText(code);

        // Dates
        binding.dob.setText(dob);
        binding.doj.setText(doj);

        // Personal details
        binding.addr.setText(address);
        binding.bloodgr.setText(bloodGroup);
        binding.gender.setText(gender);

        // Contact and identification
        binding.phoneno.setText(phone);
        binding.pincode.setText(officeId);
        binding.gname.setText(fatherName);
        binding.idproofname.setText(idProof);

        // Status
        if (isBlocked == 1) {
            binding.isBloack.setText("Inactive");
            binding.isBloack.setTextColor(Color.RED);
        } else {
            binding.isBloack.setText("Active");
            binding.isBloack.setTextColor(Color.parseColor("#07FF10")); // Green color
        }

        // Enable status update button
        binding.btnUpdateStatus.setEnabled(true);

        // Show data container and employee information
        binding.employeeDataContainer.setVisibility(View.VISIBLE);

        // Show success message
        Snackbar.make(binding.getRoot(), "Employee details loaded successfully", Snackbar.LENGTH_SHORT).show();
    }

    private void showStatusUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Employee Status");

        String employeeCode = binding.mcode.getText().toString();
        String currentStatus = binding.isBloack.getText().toString();

        boolean isCurrentlyActive = "Active".equalsIgnoreCase(currentStatus);

        // Customize button text based on current status
        String blockText = isCurrentlyActive ? "BLOCK EMPLOYEE" : "CONFIRM BLOCK";
        String unblockText = isCurrentlyActive ? "CONFIRM UNBLOCK" : "UNBLOCK EMPLOYEE";

        // Add Block button
        builder.setPositiveButton(blockText, (dialog, which) ->
                updateEmployeeStatus(employeeCode, 1)); // 1 means blocked

        // Add Unblock button
        builder.setNegativeButton(unblockText, (dialog, which) ->
                updateEmployeeStatus(employeeCode, 0)); // 0 means unblocked

        // Add Cancel button
        builder.setNeutralButton("CANCEL", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        builder.create().show();
    }

    private void updateEmployeeStatus(String employeeCode, int isBlocked) {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showNetworkError();
            return;
        }

        showLoadingState(true);

        executorService.execute(() -> {
            Connection connection = null;
            CallableStatement statement = null;

            try {
                connection = new SqlManager().getSQLConnection();
                if (connection == null) {
                    mainHandler.post(() -> showDatabaseConnectionError());
                    return;
                }

                statement = connection.prepareCall("{call ADROID_UPDATE_EMPLOYEE_STATUS(?, ?)}");
                statement.setString("@empcode", employeeCode);
                statement.setInt("@isblock", isBlocked);
                statement.execute();

                // Send success message and refresh data
                mainHandler.post(() -> {
                    String statusText = isBlocked == 1 ? "blocked" : "unblocked";
                    Snackbar.make(
                            binding.getRoot(),
                            "Employee successfully " + statusText,
                            Snackbar.LENGTH_LONG
                    ).show();

                    // Refresh the employee details
                    fetchEmployeeDetails(employeeCode);
                });

            } catch (SQLException e) {
                Log.e(TAG, "Database error during status update: " + e.getMessage(), e);
                mainHandler.post(() -> showDatabaseError(e));
            } catch (Exception e) {
                Log.e(TAG, "General error during status update: " + e.getMessage(), e);
                mainHandler.post(() -> showGeneralError(e));
            } finally {
                closeResources(null, statement, connection);
                mainHandler.post(() -> showLoadingState(false));
            }
        });
    }

    // Error handling methods

    private void showNetworkError() {
        hideProgressDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Network Error")
                .setMessage("Cannot connect to the server. Please check your internet connection and try again.")
                .setIcon(R.drawable.ic_cross) // Make sure you have this drawable
                .setPositiveButton("Retry", (dialog, which) -> {
                    if (!currentEmployeeCode.isEmpty()) {
                        fetchEmployeeDetails(currentEmployeeCode);
                    }
                })
                .setNegativeButton("Settings", (dialog, which) -> {
                    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                })
                .show();
    }

    private void showDatabaseConnectionError() {
        hideProgressDialog();
        showErrorDialog(
                "Database Connection Error",
                "Could not connect to the database. Please try again later or contact IT support."
        );
    }

    private void showDatabaseError(Exception e) {
        hideProgressDialog();
        showErrorDialog(
                "Database Error",
                "An error occurred while accessing the database: " + e.getMessage()
        );
    }

    private void showGeneralError(Exception e) {
        hideProgressDialog();
        showErrorDialog(
                "Error",
                "An unexpected error occurred: " + e.getMessage()
        );
    }

    private void showNoDataFound() {
        hideProgressDialog();
        binding.employeeDataContainer.setVisibility(View.GONE);
        binding.btnUpdateStatus.setEnabled(false);

        Snackbar.make(
                binding.getRoot(),
                "No employee found with this code",
                Snackbar.LENGTH_LONG
        ).show();
    }

    private void showErrorDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    // UI state management

    private void showLoadingState(boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.buttobempcode.setEnabled(false);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.buttobempcode.setEnabled(true);

            // Also end swipe refresh if active

        }
    }

    private void hideProgressDialog() {
        showLoadingState(false);
    }

    // Resource management

    private void closeResources(ResultSet rs, CallableStatement stmt, Connection conn) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error closing ResultSet: " + e.getMessage());
        }

        try {
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error closing Statement: " + e.getMessage());
        }

        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error closing Connection: " + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

    @Override
    public void onBackPressed() {
        navigateToAdminDashboard();
    }

    private void navigateToAdminDashboard() {
        Intent intent = new Intent(AdminEmpDetailsActivity.this, AdminDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the back stack
        startActivity(intent);
        finish();
    }
}