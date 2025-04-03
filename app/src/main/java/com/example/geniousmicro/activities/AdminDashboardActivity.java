package com.example.geniousmicro.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.geniousmicro.LoginActivity;
import com.example.geniousmicro.R;
import com.example.geniousmicro.databinding.ActivityAdminDashboardBinding;

public class AdminDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityAdminDashboardBinding binding;

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
        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sp=getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor=sp.edit();
        layoutManagers();


        binding.logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(AdminDashboardActivity.this);
                builder.setTitle("Are You Want To Logout");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    editor.clear();
                    editor.apply();
                    startActivity(new Intent(AdminDashboardActivity.this, LoginActivity.class));
                    finishAffinity();
                });
                builder.setNegativeButton("No", (dialog, which) -> {

                });
                builder.create().show();
            }
        });


        StatusbarColor();




    }

    private void StatusbarColor() {

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.background_blue));

    }




    @Override
    public void onClick(View view) {

        if (view == binding.tvAdminDashChainReport) {
            navigateToNextActivity(AdminDashChainReportActivity.class,"");
        }
        else if (view== binding.tvAdminDashNewRenewReport) {
            navigateToNextActivity(AdminDashNewRenewReportActivity.class,"");
        }
        else if (view == binding.tvAdminDashExecutiveCollectionReport) {
            navigateToNextActivity(AdminDashExecutiveCollectionReportActivity.class,"");
        }
        else if (view==binding.tvactivityAdminLoanDueReport) {
           // navigateToNextActivity(AdminLoanDueReportActivity.class,"AdminArangerWiseReport");
            navigateToNextActivityWithIntentValue(AdminLoanDueReportActivity.class,"AdminArangerWiseReport");

        }
        else if (view==binding.tvAdminDashAccountSavingsTransReport) {
            Intent intent=new Intent(getApplicationContext(),SavingsCollectionReportActivity.class);
            intent.putExtra("user","Admin");
            startActivity(intent);


        }
      /*  else if (view==binding.tvAdminDashMobileCollectionReport) {
            navigateToNextActivity(AdminDashMobileCollectionReportActivity.class,"");
        }
        else if (view == binding.AdminChainReport) {
            navigateToNextActivity(AdminChainReportActivity.class,"");
        }
        else if (view == binding.tvAdminDashExecutiveCashSheet) {
            navigateToNextActivity(AdminDashExecutiveCashSheetActivity.class,"");
        }
        else if (view==binding.tvAdminDashExecutiveBankTransReport) {
            navigateToNextActivity(AdminDashExecutiveBankTransReportActivity.class,"");
        }*/

        else if (view==binding.tvDailyPolicyCollectionRpt) {



            navigateToNextActivity(AdminPolicyCollReportActivity.class,"");
        }
        else if (view== binding.tvAdminApproveLoan) {
            Intent intent=new Intent(getApplicationContext(),SavingsCollectionReportActivity.class);
            intent.putExtra("user","Admin");
            startActivity(intent);
        } else if (view==binding.tvDailyCollectionRpt) {
            navigateToNextActivityWithIntentValue(ArrLoanCollReportActivity.class,"AdminArangerWiseReport"); //----use this
        }else if (view==binding.tdaysavings) {

            Intent intent=new Intent(getApplicationContext(),AdminTodaySavingsCollectionReportActivity.class);
            intent.putExtra("user","Admin");
            startActivity(intent);

        } else if (view==binding.TodayLoanColl) {
            navigateToNextActivityWithIntentValue(AdminTodayLoanCollReportActivity.class,"AdminArangerWiseReport"); //----use this
        }


    }


    private void navigateToNextActivity(Class<?> ActivityClass ,String user) {
        if (ActivityClass != null) {
            startActivity(new android.content.Intent(this, ActivityClass));
            finish();
        }

    }
    private void navigateToNextActivityWithIntentValue(Class<?> ActivityClass, String user) {
        if (ActivityClass != null) {
            Intent intent = new Intent(this, ActivityClass);
            intent.putExtra("LoanCollectionType", user); // Add the 'user' parameter as an extra
            startActivity(intent);
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
          private void layoutManagers(){
              binding.tvAdminDashChainReport.setOnClickListener(this);
              binding.tvAdminDashNewRenewReport.setOnClickListener(this);
              binding.tvAdminDashExecutiveCollectionReport.setOnClickListener(this);
              binding.tvactivityAdminLoanDueReport.setOnClickListener(this);
              binding.tvAdminDashAccountSavingsTransReport.setOnClickListener(this);
             // binding.tvAdminDashMobileCollectionReport.setOnClickListener(this);
              binding.AdminChainReport.setOnClickListener(this);
             // binding.tvAdminDashExecutiveCashSheet.setOnClickListener(this);
            //  binding.tvAdminDashExecutiveBankTransReport.setOnClickListener(this);
              binding.tvDailyCollectionRpt.setOnClickListener(this);
              binding.tvDailyPolicyCollectionRpt.setOnClickListener(this);
              binding.tvAdminApproveLoan.setOnClickListener(this);
              binding.tdaysavings.setOnClickListener(this);
              binding.TodayLoanColl.setOnClickListener(this);




          }


    }