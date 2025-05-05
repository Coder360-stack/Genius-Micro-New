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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.geniousmicro.Data.PostDataParserObjectResponse;
import com.example.geniousmicro.Data.VolleyCallback;
import com.example.geniousmicro.Others.ApiLinks;
import com.example.geniousmicro.util.OpenLinks;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class SplashScreen extends AppCompatActivity implements View.OnClickListener {
    // Constants
    private static final String TAG = "SplashScreen";
    private static final int SPLASH_TIMEOUT = 2000; // milliseconds
    private static final int NETWORK_TIMEOUT = 3000; // milliseconds

    // UI Components
    private TextView mTv_splashVersion;
    private LinearLayout mLl_openGTech;
    private ImageView mIv_appLogo;

    // State variables
    public static String updated_app_ver;
    private final AtomicBoolean isNetworkCallComplete = new AtomicBoolean(false);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    // For preventing multiple navigations
    private boolean hasNavigatedAway = false;

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

        setViewReferences();
        bindEventHandlers();
        displayVersionInfo();
        animateLogo();

        // Start loading process
        startSplashSequence();
    }

    private void animateLogo() {
        try {
            Animation logoAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            if (mIv_appLogo != null) {
                mIv_appLogo.startAnimation(logoAnimation);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error animating logo", e);
        }
    }

    private void startSplashSequence() {
        // Check for network connectivity first
        if (isNetworkAvailable()) {
            // Ensure we navigate away after SPLASH_TIMEOUT regardless of network status
            mainHandler.postDelayed(this::proceedToNextScreen, SPLASH_TIMEOUT);
            checkForUpdates();
        } else {
            // No network, show error dialog instead of proceeding
            Log.d(TAG, "No network connection available");
            showNetworkErrorDialog();
        }
    }

    private void setViewReferences() {
        mTv_splashVersion = findViewById(R.id.splash_version);
        mLl_openGTech = findViewById(R.id.ll_open_genius_technology_links);
        mIv_appLogo = findViewById(R.id.iv_app_logo);
    }

    private void bindEventHandlers() {
        mTv_splashVersion.setOnClickListener(this);
        mLl_openGTech.setOnClickListener(this);
    }

    private void displayVersionInfo() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);

            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;

            String versionInfo = "Version Name: " + versionName + "\nVersion Code: " + versionCode;
            mTv_splashVersion.setText(versionInfo);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Error getting package info", e);
            mTv_splashVersion.setText("Version information unavailable");
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mLl_openGTech) {
            new OpenLinks(this).openGeniusTechnology();
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

    private void checkForUpdates() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);

            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;

            HashMap<String, String> params = new HashMap<>();
            params.put("versionName", versionName);
            params.put("versionCode", String.valueOf(versionCode));

            // Start a timeout for the network call
            mainHandler.postDelayed(() -> {
                if (!isNetworkCallComplete.get()) {
                    Log.w(TAG, "Update check timed out");
                    isNetworkCallComplete.set(true);
                }
            }, NETWORK_TIMEOUT);

            // Make the network call on the main thread
            // This is key to fix the error - PostDataParserObjectResponse must be created on main thread
            new PostDataParserObjectResponse(this, ApiLinks.IS_UPDATE_AVAILABLE, params, new VolleyCallback() {
                @Override
                public void onSuccessResponse(JSONObject result) {
                    // Mark network call as complete
                    isNetworkCallComplete.set(true);

                    try {
                        if (result.has("versionDetails")) {
                            JSONArray userData = result.getJSONArray("versionDetails");
                            processUpdateResponse(userData, versionName);
                        } else {
                            Log.e(TAG, "Invalid update response format");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing update response", e);
                        runOnUiThread(() -> Toast.makeText(SplashScreen.this,
                                "Error checking for updates", Toast.LENGTH_SHORT).show());
                    }
                }

                @Override
                public void onErrorResponse(String error) {
                    isNetworkCallComplete.set(true);
                    Log.e(TAG, "Update check failed: " + error);
                    // We'll proceed via the standard splash timeout mechanism
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error during update check", e);
            // Navigate away through the standard splash timeout
        }
    }

    private void processUpdateResponse(JSONArray updateData, String currentVersionName) {
        try {
            for (int i = 0; i < updateData.length(); i++) {
                JSONObject updateInfo = updateData.getJSONObject(i);

                if ("1".equals(updateInfo.optString("isAvailable"))) {
                    String newVersionName = updateInfo.optString("VersionName");
                    String newVersionCode = updateInfo.optString("VersionCode");
                    String updateMessage = updateInfo.optString("LatestUpdate");

                    // Check if this is a critical update or optional one
                    boolean isOptionalUpdate = newVersionName.equals(currentVersionName);

                    // Show update dialog on UI thread
                    runOnUiThread(() -> showUpdateDialog(newVersionName, newVersionCode, updateMessage, isOptionalUpdate));
                    return;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error processing update data", e);
        }
    }

    private void showUpdateDialog(String versionName, String versionCode, String updateMessage, boolean isOptionalUpdate) {
        // Cancel the auto-navigation timer since we're showing a dialog
        mainHandler.removeCallbacksAndMessages(null);

        Dialog updateDialog = new Dialog(SplashScreen.this);
        updateDialog.setContentView(R.layout.update_available_layout);
        updateDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        updateDialog.setCancelable(false);

        TextView versionNameTextView = updateDialog.findViewById(R.id.VersionName);
        TextView versionCodeTextView = updateDialog.findViewById(R.id.VersionCode);
        Button updateButton = updateDialog.findViewById(R.id.btn_update);
        Button updateLaterButton = updateDialog.findViewById(R.id.btn_update_later);
        TextView updateTitleTextView = updateDialog.findViewById(R.id.tv_latest_features);

        // Set visibility of "Update Later" button based on update type
        updateLaterButton.setVisibility(isOptionalUpdate ? View.VISIBLE : View.GONE);

        // Set dialog content
        updateTitleTextView.setText(updateMessage);
        versionNameTextView.setText(versionName);
        versionCodeTextView.setText(versionCode);
        updateTitleTextView.setVisibility(View.VISIBLE);

        // Button handlers
        updateLaterButton.setOnClickListener(v -> {
            updateDialog.dismiss();
            navigateToLoginScreen();
        });

        updateButton.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + getPackageName())));
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        });

        updateDialog.show();
    }

    private void proceedToNextScreen() {
        // Only proceed if network is available
        if (isNetworkCallComplete.get() && isNetworkAvailable()) {
            navigateToLoginScreen();
        }
        // Network call complete but no network available
        else if (isNetworkCallComplete.get() && !isNetworkAvailable()) {
            showNetworkErrorDialog();
        }
        // Network call still in progress, wait for it
    }
    private void showNetworkErrorDialog() {
        Dialog networkErrorDialog = new Dialog(SplashScreen.this);
        networkErrorDialog.setContentView(R.layout.network_error_layout);
        networkErrorDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        networkErrorDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        networkErrorDialog.setCancelable(false);

        Button retryButton = networkErrorDialog.findViewById(R.id.btn_retry);
        Button exitButton = networkErrorDialog.findViewById(R.id.btn_exit);

        retryButton.setOnClickListener(v -> {
            networkErrorDialog.dismiss();
            // Retry the connection check
            startSplashSequence();
        });

        exitButton.setOnClickListener(v -> {
            networkErrorDialog.dismiss();
            finish();
        });

        networkErrorDialog.show();
    }

    private void navigateToLoginScreen() {
        // Prevent multiple navigations
        if (hasNavigatedAway) {
            return;
        }

        hasNavigatedAway = true;
        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        // Cleanup to prevent memory leaks
        mainHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}