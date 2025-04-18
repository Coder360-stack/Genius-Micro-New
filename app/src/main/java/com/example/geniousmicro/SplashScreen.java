package com.example.geniousmicro;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.geniousmicro.Data.PostDataParserObjectResponse;
import com.example.geniousmicro.Data.VolleyCallback;
import com.example.geniousmicro.Others.ApiLinks;
import com.example.geniousmicro.util.OpenLinks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SplashScreen extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SplashScreen";
    private static final int SPLASH_DELAY = 2000; // 2 seconds

    private TextView mTv_splashVersion;
    private LinearLayout mLl_openGTech;
    private String versionName;
    private int versionCode;

    public static String updated_app_ver;
    private boolean isCheckingUpdate = false;
    private AlertDialog networkErrorDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splash_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        getAppVersionInfo();

        if (isNetworkAvailable()) {
            checkForUpdates();
        } else {
            showNetworkErrorDialog();
        }
    }

    private void initializeViews() {
        mTv_splashVersion = findViewById(R.id.splash_version);
        mLl_openGTech = findViewById(R.id.ll_open_genius_technology_links);

        // Set click listeners
        mTv_splashVersion.setOnClickListener(this);
        mLl_openGTech.setOnClickListener(this);
    }

    private void getAppVersionInfo() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);

            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;

            String versionInfo = "Version Name: " + versionName + "\nVersion Code: " + versionCode;
            mTv_splashVersion.setText(versionInfo);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Error getting package info", e);
            mTv_splashVersion.setText("Version information unavailable");
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showNetworkErrorDialog() {
        if (networkErrorDialog != null && networkErrorDialog.isShowing()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("This app requires an internet connection to function. Please check your connectivity and try again.")
                .setCancelable(false)
                .setPositiveButton("Retry", (dialog, which) -> {
                    if (isNetworkAvailable()) {
                        checkForUpdates();
                    } else {
                        Toast.makeText(this, "Still no internet connection", Toast.LENGTH_SHORT).show();
                        showNetworkErrorDialog();
                    }
                })
                .setNegativeButton("Open Settings", (dialog, which) -> {
                    try {
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    } catch (Exception e) {
                        Log.e(TAG, "Error opening settings", e);
                        Toast.makeText(this, "Unable to open settings", Toast.LENGTH_SHORT).show();
                    }
                });

        networkErrorDialog = builder.create();
        networkErrorDialog.show();
    }

    private void checkForUpdates() {
        if (isCheckingUpdate) {
            return;
        }

        isCheckingUpdate = true;

        HashMap<String, String> params = new HashMap<>();
        params.put("versionName", versionName);
        params.put("versionCode", String.valueOf(versionCode));
        Log.e("versionName",""+versionName);
        // Use the existing PostDataParserObjectResponse implementation
        new PostDataParserObjectResponse(this, ApiLinks.IS_UPDATE_AVAILABLE, params, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                isCheckingUpdate = false;
                handleUpdateCheckResponse(result);
            }

            @Override
            public void onErrorResponse(String error) {
                isCheckingUpdate = false;
                Log.e(TAG, "API Error: " + error);

                runOnUiThread(() -> {
                    // Show error dialog using built-in AlertDialog
                    new AlertDialog.Builder(SplashScreen.this)
                            .setTitle("Connection Error")
                            .setMessage("Unable to connect to the server. Would you like to retry?")
                            .setPositiveButton("Retry", (dialog, which) -> {
                                if (isNetworkAvailable()) {
                                    checkForUpdates();
                                } else {
                                    showNetworkErrorDialog();
                                }
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> {
                                // Just show a toast and proceed to app with limited functionality
                                Toast.makeText(SplashScreen.this, "Some features may not be available", Toast.LENGTH_LONG).show();
                                proceedToApp();
                            })
                            .setCancelable(false)
                            .show();
                });
            }
        });
    }

    private void handleUpdateCheckResponse(JSONObject result) {
        try {
            if (!result.has("versionDetails")) {
                Log.e(TAG, "Invalid API response format - missing versionDetails");
                Toast.makeText(this, "Error checking for updates", Toast.LENGTH_SHORT).show();
                proceedToApp();
                return;
            }

            JSONArray versionDetails = result.getJSONArray("versionDetails");
            if (versionDetails.length() == 0) {
                proceedToApp();
                return;
            }

            JSONObject updateInfo = versionDetails.getJSONObject(0);
            String isAvailable = updateInfo.optString("isAvailable", "0");

            if ("1".equals(isAvailable)) {
                String newVersionName = updateInfo.optString("VersionName", "");
                String newVersionCode = updateInfo.optString("VersionCode", "");
                String updateMessage = updateInfo.optString("LatestUpdate", "");

                boolean isSameVersion = newVersionName.equals(versionName);
                showUpdateDialog(newVersionName, newVersionCode, updateMessage, isSameVersion);
            } else {
                proceedToApp();
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing update response: " + e.getMessage(), e);
            Toast.makeText(this, "Error processing server response", Toast.LENGTH_SHORT).show();
            proceedToApp();
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error: " + e.getMessage(), e);
            Toast.makeText(this, "An unexpected error occurred", Toast.LENGTH_SHORT).show();
            proceedToApp();
        }
    }

    private void showUpdateDialog(String versionName, String versionCode, String updateMessage, boolean isOptionalUpdate) {
        Dialog updateDialog = new Dialog(this);
        updateDialog.setContentView(R.layout.update_available_layout);
        updateDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        updateDialog.setCancelable(false);

        TextView versionNameTextView = updateDialog.findViewById(R.id.VersionName);
        TextView versionCodeTextView = updateDialog.findViewById(R.id.VersionCode);
        Button updateButton = updateDialog.findViewById(R.id.btn_update);
        Button updateLaterButton = updateDialog.findViewById(R.id.btn_update_later);
        TextView updateMessageView = updateDialog.findViewById(R.id.tv_latest_features);

        versionNameTextView.setText(versionName);
        versionCodeTextView.setText(versionCode);
        updateMessageView.setText(updateMessage);
        updateMessageView.setVisibility(View.VISIBLE);

        // Show "Update Later" button only for optional updates
        if (isOptionalUpdate) {
            updateLaterButton.setVisibility(View.VISIBLE);
        } else {
            updateLaterButton.setVisibility(View.GONE);
        }

        updateLaterButton.setOnClickListener(v -> {
            updateDialog.dismiss();
            proceedToApp();
        });

        updateButton.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + getPackageName()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (Exception e) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception ex) {
                    Log.e(TAG, "Error opening Play Store", ex);
                    Toast.makeText(this, "Unable to open Play Store", Toast.LENGTH_SHORT).show();
                }
            }
        });

        updateDialog.show();
    }

    private void proceedToApp() {
        // Add any animation here that was in the original code
        // YoYo.with(Techniques.FadeIn).playOn(findViewById(R.id.tv_splash_activity_title));

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }, SPLASH_DELAY);
    }

    @Override
    public void onClick(View view) {
        if (view == mLl_openGTech) {
            if (isNetworkAvailable()) {
                new OpenLinks(this).openGeniusTechnology();
            } else {
                Toast.makeText(this, "Internet connection required", Toast.LENGTH_SHORT).show();
                showNetworkErrorDialog();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check network connection when returning to the app
        if (networkErrorDialog != null && networkErrorDialog.isShowing() && isNetworkAvailable()) {
            networkErrorDialog.dismiss();
            checkForUpdates();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Clean up resources
        if (networkErrorDialog != null && networkErrorDialog.isShowing()) {
            networkErrorDialog.dismiss();
        }
    }
}