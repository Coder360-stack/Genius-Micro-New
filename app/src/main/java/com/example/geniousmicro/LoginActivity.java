package com.example.geniousmicro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.geniousmicro.Data.PostDataParserObjectResponse;
import com.example.geniousmicro.Data.VolleyCallback;
import com.example.geniousmicro.Models.UserDataModel.AdminDataModel;
import com.example.geniousmicro.Models.UserDataModel.EmployeeDataModel;
import com.example.geniousmicro.Models.UserDataModel.MemberDataModel;
import com.example.geniousmicro.Others.ApiLinks;
import com.example.geniousmicro.UserData.GlobalUserData;
import com.example.geniousmicro.activities.AdminDashboardActivity;
import com.example.geniousmicro.activities.MemberDashboardActivity;
import com.example.geniousmicro.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding binding;
    private String username = "", password = "";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private static final String SHARED_PREF_NAME = "UserDetails";
    private static final String USERNAME = "UserName";
    private static final String PASSWORD = "Password";
    private boolean isPasswordVisible = false;
    private ExecutorService executorService;
    private boolean isLoggingIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize executor service for background tasks
        executorService = Executors.newSingleThreadExecutor();

        sp = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();

        // Check for saved credentials and auto-login
        if (sp.contains(USERNAME)) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnlogin.setEnabled(false);

            final String savedUsername = sp.getString(USERNAME, "");
            final String savedPassword = sp.getString(PASSWORD, "");

            if (!isNetworkAvailable()) {
                showNetworkError();
                binding.progressBar.setVisibility(View.GONE);
                binding.btnlogin.setEnabled(true);
                return;
            }

            // Pre-fill the fields for better UX
            binding.loginUserName.setText(savedUsername);
            binding.loginPassword.setText(savedPassword);
            binding.rememberMe.setChecked(true);

            login(savedUsername, savedPassword);
        }

        binding.btnlogin.setOnClickListener(v -> {
            if (isLoggingIn) {
                return; // Prevent multiple login attempts
            }

            username = binding.loginUserName.getText().toString().trim();
            password = binding.loginPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                showValidationError("Please enter both username and password");
                return;
            }

            if (!isNetworkAvailable()) {
                showNetworkError();
                return;
            }

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnlogin.setEnabled(false);
            isLoggingIn = true;

            login(username, password);
        });

        binding.ivTogglePassword.setOnClickListener(v -> togglePasswordVisibility());

     /*   // Add forgot password functionality if needed
        binding.forgotPassword.setOnClickListener(v -> {
            // Implement forgot password logic here
            Toast.makeText(LoginActivity.this, "Forgot password feature coming soon", Toast.LENGTH_SHORT).show();
        });*/
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide Password
            binding.loginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            binding.ivTogglePassword.setImageResource(R.drawable.visibility_off);
        } else {
            // Show Password
            binding.loginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            binding.ivTogglePassword.setImageResource(R.drawable.visibility_on);
        }
        binding.loginPassword.setSelection(binding.loginPassword.length()); // Move cursor to the end
        isPasswordVisible = !isPasswordVisible;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }



    private void showNetworkError() {
        new AlertDialog.Builder(this)
                .setTitle("Network Error")
                .setMessage("No internet connection available. Please check your network settings and try again.")
                .setPositiveButton("Retry", (dialog, which) -> {
                    if (isNetworkAvailable()) {
                        if (!username.isEmpty() && !password.isEmpty()) {
                            login(username, password);
                        }
                    } else {
                        showNetworkError();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showValidationError(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void showServerError(String errorMessage) {
        new AlertDialog.Builder(this)
                .setTitle("Server Error")
                .setMessage(errorMessage)
                .setPositiveButton("OK", null)
                .show();
    }

    private void login(String username, String password) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("username", username);
        hashMap.put("password", password);

        new PostDataParserObjectResponse(LoginActivity.this, ApiLinks.LOGIN, hashMap, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnlogin.setEnabled(true);
                isLoggingIn = false;

                try {
                    if (!response.has("Login")) {
                        showServerError("Invalid server response. Please try again later.");
                        return;
                    }

                    JSONObject userDetails = response.getJSONObject("Login");
                    Log.d(TAG, "User details: " + userDetails);

                    if (!userDetails.has("status") || userDetails.getString("status").isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Invalid login response", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String status = userDetails.getString("status");

                    // Process login based on user type
                    switch (status) {
                        case "arranger":
                            handleArrangerLogin(userDetails);
                            break;
                        case "member":
                            handleMemberLogin(userDetails);
                            break;
                        case "admin":
                            handleAdminLogin(userDetails);
                            break;
                        default:
                            Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "JSON parsing error: " + e.getMessage(), e);
                    Toast.makeText(LoginActivity.this, "Error processing login data", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e(TAG, "Unexpected error: " + e.getMessage(), e);
                    Toast.makeText(LoginActivity.this, "An unexpected error occurred", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(String error) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnlogin.setEnabled(true);
                isLoggingIn = false;

                Log.e(TAG, "Login error: " + error);

                if (error.contains("timeout")) {
                    showServerError("Server connection timed out. Please try again later.");
                } else if (error.contains("NoConnectionError")) {
                    showNetworkError();
                } else if (error.contains("AuthFailureError")) {
                    Toast.makeText(LoginActivity.this, "Authentication failed. Check your credentials.", Toast.LENGTH_SHORT).show();
                } else if (error.contains("ServerError")) {
                    showServerError("Server error. Please try again later.");
                } else {
                    showServerError("Login failed: " + error);
                }
            }
        });
    }

    private void handleArrangerLogin(JSONObject userDetails) throws JSONException {
        EmployeeDataModel ed = new EmployeeDataModel(
                userDetails.getString("usercode"),
                userDetails.getString("username"),
                userDetails.getString("PassWord"),
                userDetails.getString("Officeid"),
                userDetails.getBoolean("isActive")
        );

        // Check if user is active
        if (!ed.isActive()) {
            Toast.makeText(LoginActivity.this, "Your account is inactive. Please contact administrator.", Toast.LENGTH_LONG).show();
            return;
        }

        GlobalUserData.employeeDataModel = ed;
        saveCredentialsIfNeeded(userDetails.getString("usercode"), userDetails.getString("PassWord"));

        // Start activity
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void handleMemberLogin(JSONObject userDetails) throws JSONException {
        MemberDataModel ed = new MemberDataModel(
                userDetails.getString("usercode"),
                userDetails.getString("PassWord")
        );

        GlobalUserData.memberDataModel = ed;
        saveCredentialsIfNeeded(userDetails.getString("usercode"), userDetails.getString("PassWord"));

        // Start activity
        startActivity(new Intent(LoginActivity.this, MemberDashboardActivity.class));
        finish();
    }

    private void handleAdminLogin(JSONObject userDetails) throws JSONException {
        AdminDataModel ed = new AdminDataModel(
                userDetails.getString("usercode"),
                userDetails.getString("PassWord")
        );

        GlobalUserData.adminDataModel = ed;
        saveCredentialsIfNeeded(userDetails.getString("usercode"), userDetails.getString("PassWord"));

        // Start activity
        startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
        finish();
    }

    private void saveCredentialsIfNeeded(String username, String password) {
        if (binding.rememberMe.isChecked()) {
            editor.putString(USERNAME, username);
            editor.putString(PASSWORD, password);
            editor.apply();
        } else {
            // Clear saved credentials if remember me is not checked
            editor.remove(USERNAME);
            editor.remove(PASSWORD);
            editor.apply();
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}