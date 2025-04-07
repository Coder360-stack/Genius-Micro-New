package com.example.geniousmicro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.geniousmicro.Adapter.ArrangerMenuAdapter;
import com.example.geniousmicro.Adapter.TopBannerVpAdapter;
import com.example.geniousmicro.FragmentClass.Home_Fragment;
import com.example.geniousmicro.UserData.GlobalUserData;
import com.example.geniousmicro.activities.SavingsCollectionReportActivity;
import com.example.geniousmicro.databinding.ActivityMainBinding;
import com.example.geniousmicro.util.Constants;
import com.example.geniousmicro.util.Helper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private Handler topRatedHandler = new Handler(Looper.getMainLooper());
    private int dotsCount;
    private ImageView[] dots;
    private int backPressCounter = 0;
    private static final int REQUIRED_BACK_PRESS_COUNT = 2;
    private static final int BACK_PRESS_INTERVAL = 2000;
    private boolean isTopBannerRunning = false;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private static final String SHARED_PREF_NAME = "UserDetails";
    private static final String USERNAME = "UserName";
    private static final String PASSWORD = "Password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            EdgeToEdge.enable(this);
            setContentView(binding.getRoot());

            initSharedPreferences();
            setStatusBarColor();
            setupWindowInsets();

            if (!isNetworkAvailable()) {
                showNetworkErrorDialog();
            }

            bindEventHandlers();
            initHomeFragment(savedInstanceState);
            setupTopViewPager();
            setupArrangerInfo();
            setupLogoutButton();
            loadMenuItems();

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: ", e);
            showErrorDialog("Application Error", "Failed to initialize application properly. Please restart the app.");
        }
    }

    private void initSharedPreferences() {
        sp = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.orange));
    }

    private void initHomeFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            try {
                Home_Fragment homeFragment = new Home_Fragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, homeFragment);
                fragmentTransaction.commit();
            } catch (Exception e) {
                Log.e(TAG, "Error loading home fragment: ", e);
                showErrorDialog("Fragment Error", "Failed to load home screen. Please restart the app.");
            }
        }
    }

    private void setupArrangerInfo() {
        try {
            binding.gridView.setBackgroundResource(R.drawable.rec_card_bac);
            binding.notificationmsg.setSelected(true);

            if (GlobalUserData.employeeDataModel != null) {
                binding.Arrcode.setText(GlobalUserData.employeeDataModel.getEmployeeID());
                binding.ArrangerName.setText(GlobalUserData.employeeDataModel.getEmployeeName());
            } else {
                Log.e(TAG, "Employee data model is null");
                binding.Arrcode.setText("N/A");
                binding.ArrangerName.setText("Employee data unavailable");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting up arranger info: ", e);
        }
    }

    private void setupLogoutButton() {
        binding.logout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Are You Want To Logout");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                try {
                    editor.clear();
                    editor.apply();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finishAffinity();
                } catch (Exception e) {
                    Log.e(TAG, "Error during logout: ", e);
                    showToast("Logout failed. Please try again.");
                }
            });
            builder.setNegativeButton("No", (dialog, which) -> { });
            builder.create().show();
        });
    }

    private void loadMenuItems() {
        try {
            // Load menu items
            Helper.AddMenuList();
            getAllMenus();

            // Set adapter for grid view
            ArrangerMenuAdapter adapter = new ArrangerMenuAdapter(this, Constants.menuItemList);
            binding.gridView.setAdapter(adapter);
        } catch (Exception e) {
            Log.e(TAG, "Error loading menu items: ", e);
            showErrorDialog("Menu Error", "Failed to load menu items. Some features may not be available.");
        }
    }

    private void bindEventHandlers() {
        binding.NewMember.setOnClickListener(this);
        binding.LoanCollection.setOnClickListener(this);
        binding.PolicyCollection.setOnClickListener(this);
        binding.Profile.setOnClickListener(this);
        binding.Home.setOnClickListener(this);
    }

    private void getAllMenus() {
        try {
            // Simulating API call
            String[] apiResponse = new String[]{"New Member", "Policy Renewal", "Loan EMI"};

            for (String menuItem : apiResponse) {
                Helper.getCheckedMenu(menuItem);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting menus from API: ", e);
            showToast("Failed to load all menu items. Some features may not be available.");
        }
    }

    private void setupTopViewPager() {
        try {
            List<Integer> bannerImages = new ArrayList<>();
            bannerImages.add(R.drawable.ic_top_viewpager);
            bannerImages.add(R.drawable.ic_top_viewpager2);
            bannerImages.add(R.drawable.ic_top_viewpager3);

            TopBannerVpAdapter viewPagerAdapter = new TopBannerVpAdapter(getApplicationContext(), bannerImages, binding.vpTopBanner);
            binding.vpTopBanner.setAdapter(viewPagerAdapter);
            configureViewPager();
            setupViewPagerDots(viewPagerAdapter.getItemCount());

            binding.vpTopBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    updateDots(position);
                    resetAutoScroll();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error setting up top banner ViewPager: ", e);
            binding.vpTopBanner.setVisibility(View.GONE);
            binding.SliderDots.setVisibility(View.GONE);
        }
    }

    private void configureViewPager() {
        binding.vpTopBanner.setClipChildren(false);
        binding.vpTopBanner.setClipToPadding(false);
        binding.vpTopBanner.setOffscreenPageLimit(3);
        binding.vpTopBanner.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });
        binding.vpTopBanner.setPageTransformer(compositePageTransformer);
    }

    private void setupViewPagerDots(int itemCount) {
        binding.SliderDots.removeAllViews();
        dotsCount = itemCount;
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            binding.SliderDots.addView(dots[i], params);
        }

        if (dotsCount > 0) {
            dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
        }
    }

    private void updateDots(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
        }
        dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
    }

    private void resetAutoScroll() {
        topRatedHandler.removeCallbacks(topRatedRunnable);
        if (isTopBannerRunning) {
            topRatedHandler.postDelayed(topRatedRunnable, 4000);
        }
    }

    private final Runnable topRatedRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (binding != null && binding.vpTopBanner != null) {
                    int currentItem = binding.vpTopBanner.getCurrentItem();
                    if (currentItem == dotsCount - 1) {
                        binding.vpTopBanner.setCurrentItem(0, true);
                    } else {
                        binding.vpTopBanner.setCurrentItem(currentItem + 1, true);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error in topRatedRunnable: ", e);
            }
        }
    };

    @Override
    public void onClick(View v) {
        try {
            if (v == binding.NewMember) {
                startActivity(new Intent(MainActivity.this, NewMemberActivity.class));
            } else if (v == binding.LoanCollection) {
                startActivity(new Intent(MainActivity.this, LoanCollectionActivity.class));
            } else if (v == binding.PolicyCollection) {
                startActivity(new Intent(MainActivity.this, RenewalCollectionActivity.class));
            } else if (v == binding.Profile) {
                Intent intent = new Intent(MainActivity.this, SavingsCollectionReportActivity.class);
                intent.putExtra("SBCollectionType", "Today SBTransaction");
                startActivity(intent);
            } else if (v == binding.Home) {
                showToast("Already in Home Page");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error handling click: ", e);
            showErrorDialog("Navigation Error", "Failed to navigate to the selected screen. Please try again.");
        }
    }

    @Override
    public void onBackPressed() {
        backPressCounter++;

        if (backPressCounter == REQUIRED_BACK_PRESS_COUNT) {
            finishAffinity();
        } else {
            showToast("Press back again to exit");

            new Handler(Looper.getMainLooper()).postDelayed(() -> backPressCounter = 0, BACK_PRESS_INTERVAL);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTopBannerAutoScroll();
        checkForCriticalUpdates();
    }

    @Override
    protected void onPause() {
        stopTopBannerAutoScroll();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        cleanUp();
        super.onDestroy();
    }

    private void cleanUp() {
        stopTopBannerAutoScroll();
        if (binding != null && binding.vpTopBanner != null) {
            binding.vpTopBanner.unregisterOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {});
        }
        binding = null;
    }

    private void startTopBannerAutoScroll() {
        isTopBannerRunning = true;
        topRatedHandler.postDelayed(topRatedRunnable, 4000);
    }

    private void stopTopBannerAutoScroll() {
        isTopBannerRunning = false;
        topRatedHandler.removeCallbacks(topRatedRunnable);
    }

    private void checkForCriticalUpdates() {
        // This method would check for required app updates
        // Implementation would depend on your update strategy
        try {
            // Example: Check version against server
            // If update needed:
            // showUpdateRequiredDialog();
        } catch (Exception e) {
            Log.e(TAG, "Error checking for updates: ", e);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    private void showNetworkErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Network Error")
                .setMessage("No internet connection. Some features may not work properly.")
                .setPositiveButton("Settings", (dialog, which) -> {
                    // Open network settings
                    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                })
                .setNegativeButton("Continue", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private void showDatabaseErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Database Error")
                .setMessage(message)
                .setPositiveButton("Retry", (dialog, which) -> {
                    // Try to reload data
                    loadMenuItems();
                })
                .setNegativeButton("Continue", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    private void showErrorDialog(String title, String message) {
        if (!isFinishing()) {
            new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method to handle database errors
     * @param e The exception that occurred
     * @param operation The database operation that failed
     */
    public void handleDatabaseError(Exception e, String operation) {
        Log.e(TAG, "Database error during " + operation + ": ", e);
        showDatabaseErrorDialog("Error accessing local data. Please try again later.");
    }

    /**
     * Method to handle network errors during API calls
     * @param e The exception that occurred
     * @param apiName The name of the API that failed
     */
    public void handleNetworkError(Exception e, String apiName) {
        Log.e(TAG, "Network error calling " + apiName + ": ", e);
        if (isNetworkAvailable()) {
            showToast("Server connection error. Please try again later.");
        } else {
            showNetworkErrorDialog();
        }
    }
}