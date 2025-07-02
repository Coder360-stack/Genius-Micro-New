package com.example.geniousmicro.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.geniousmicro.LoginActivity;
import com.example.geniousmicro.R;
import com.example.geniousmicro.databinding.ActivityMemberDashboardBinding;
import com.google.android.material.navigation.NavigationView;

public class MemberDashboardActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    ActivityMemberDashboardBinding binding;
    private int backPressCounter = 0;
    private static final int REQUIRED_BACK_PRESS_COUNT = 2;
    private static final int BACK_PRESS_INTERVAL = 2000;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME="UserDetails";
    String USERNAME="UserName";
    String PASSWORD="Password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMemberDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sp=getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor=sp.edit();

        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        binding.navView.setNavigationItemSelectedListener(this);
        binding.SavingsAccountCard.setOnClickListener(this);
        binding.LoanStatementCard.setOnClickListener(this);
        binding.InvestmentAccountCard.setOnClickListener(this);
        binding.logout.setOnClickListener(this);
        StatusbarColor();

    }

    private void StatusbarColor() {

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.orange));

    }

    @Override
    public void onClick(View view) {

        if (view == binding.SavingsAccountCard) {
            navigateToNextActivity(MemberSavingsAccountDetailsActivity.class);
        } else if (view == binding.LoanStatementCard) {
            navigateToNextActivity(LoanStatementActivity.class);
        } else if (view == binding.InvestmentAccountCard) {
            navigateToNextActivity(InvestmentStatementActivity.class);

        } else if (view==binding.logout) {
            AlertDialog.Builder builder=new AlertDialog.Builder(MemberDashboardActivity.this);
            builder.setTitle("Are You Want To Logout");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                editor.clear();
                editor.apply();
                startActivity(new Intent(MemberDashboardActivity.this, LoginActivity.class));
                finishAffinity();
            });
            builder.setNegativeButton("No", (dialog, which) -> {

            });
            builder.create().show();
        }


    }


    private void navigateToNextActivity(Class<?> ActivityClass) {
        if (ActivityClass != null) {
            startActivity(new android.content.Intent(this, ActivityClass));
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        backPressCounter++;

        if (backPressCounter == REQUIRED_BACK_PRESS_COUNT) {
            super.onBackPressed(); // Exit the app
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressCounter = 0;
                }
            }, BACK_PRESS_INTERVAL);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_drawer_member_my_profile) {
            startActivity(new Intent(MemberDashboardActivity.this, MemberProfileActivity.class));
//            finish();
        }
        else if (item.getItemId() == R.id.nav_drawer_member_logout) {
            AlertDialog.Builder builder=new AlertDialog.Builder(MemberDashboardActivity.this);
            builder.setTitle("Are You Want To Logout");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                editor.clear();
                editor.apply();
                startActivity(new Intent(MemberDashboardActivity.this, LoginActivity.class));
                finishAffinity();
            });
            builder.setNegativeButton("No", (dialog, which) -> {

            });
            builder.create().show();

        } else if (item.getItemId()==R.id.nav_drawer_member_contact_us) {
            startActivity(new Intent(MemberDashboardActivity.this, MemberContactUsActivity.class));
        } else if (item.getItemId()==R.id.nav_drawer_change_pin) {
            startActivity(new Intent(MemberDashboardActivity.this, MemberChangePassword.class));
        }
        return true;
    }
}