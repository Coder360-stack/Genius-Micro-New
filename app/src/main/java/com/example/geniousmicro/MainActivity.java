package com.example.geniousmicro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Handler;
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
    ActivityMainBinding binding;
    Handler topRatedHandler = new Handler();
    private int dotsCount;
    private ImageView[] dots;
    private int backPressCounter = 0;
    private static final int REQUIRED_BACK_PRESS_COUNT = 2;
    private static final int BACK_PRESS_INTERVAL = 2000;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "UserDetails";
    String USERNAME = "UserName";
    String PASSWORD = "Password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        bindEventHandlers();
        ActivityChange();
        Helper.AddMenuList();


        binding.notificationmsg.setSelected(true);

        sp = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        StatusbarColor();

        if (savedInstanceState == null) {
            Home_Fragment exampleFragment = new Home_Fragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, exampleFragment);
            fragmentTransaction.commit();
        }
        topVp();

        Log.d("data", "onCreate: " + GlobalUserData.employeeDataModel.getEmployeeID());
        binding.gridView.setBackgroundResource(R.drawable.rec_card_bac);
        binding.Arrcode.setText("" + GlobalUserData.employeeDataModel.getEmployeeID());
        binding.ArrangerName.setText("" + GlobalUserData.employeeDataModel.getEmployeeName());

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Are You Want To Logout");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    editor.clear();
                    editor.apply();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finishAffinity();
                });
                builder.setNegativeButton("No", (dialog, which) -> {

                });
                builder.create().show();


            }
        });

        getAllMenus();

        //GridView gridView = findViewById(R.id.grid_view);
//        String[] texts = {"New Member", "Policy Renewal", "Loan EMI", "Loan Details", "Policy Details","Approved Member List","UnApproved Member List","LoanDue Report",  "LS Transaction", "Loan Statement", "LS Transaction Report","LoanCollection","PolicyCollection","Today LoanCollection","Group Loan Collection","Group Collection Report","All Collection",};
//        int[] images = {R.drawable.new_member,R.drawable.policy_renewal,R.drawable.loan_emi,R.drawable.loan_details,R.drawable.policy_details,R.drawable.loan_due_report,R.drawable.policy_details,R.drawable.investment_report,R.drawable.plan_details,R.drawable.plan_details,R.drawable.test1,R.drawable.loan_due_report,R.drawable.investment_report,R.drawable.plan_details,R.drawable.test1,R.drawable.loan_emi,R.drawable.investment_report,R.drawable.loan_due_report,};
        //Helper.AddMenuList();

        // Example data
      /*  String[] texts = {"New Member", "Policy Renewal", "Loan EMI", "Loan Details", "Policy Details","Approved Member List","UnApproved Member List","LoanDue Report",  "LS Transaction", "Loan Statement", "LS Transaction Report","LoanCollection","PolicyCollection","Today LoanCollection","Today PolicyRenewal","Today LSTransaction","Group Loan Collection","Group Collection Report",};
        int[] images = {R.drawable.new_member,R.drawable.policy_renewal,R.drawable.loan_emi,R.drawable.loan_details,R.drawable.policy_details,R.drawable.loan_due_report,R.drawable.policy_details,R.drawable.investment_report,R.drawable.plan_details,R.drawable.plan_details,R.drawable.test1,R.drawable.loan_due_report,R.drawable.test2,R.drawable.test3,R.drawable.investment_report,R.drawable.plan_details,R.drawable.test1,R.drawable.loan_emi,R.drawable.investment_report,};
*/

  /*      String[] texts = {"New Member",  "Loan EMI", "Loan Details", "Approved Member List","UnApproved Member List","LoanDue Report",  "Loan Statement", "LoanCollection","Today LoanCollection","Group Loan Collection","Group Collection Report",};
        int[] images = {R.drawable.new_member,R.drawable.policy_renewal,R.drawable.loan_emi,R.drawable.savings_statement,R.drawable.loan_details,R.drawable.policy_details,R.drawable.loan_due_report,R.drawable.policy_details,R.drawable.investment_report,R.drawable.plan_details,R.drawable.plan_details,};
*/
        ArrangerMenuAdapter adapter = new ArrangerMenuAdapter(this, Constants.menuItemList); //texts, images);
        binding.gridView.setAdapter(adapter);


    }





    private void getAllMenus() {
        //api call
        String[] apiresponse = new String[]{"New Member", "Policy Renewal", "Loan EMI"};
        for (String s : apiresponse
        ) {
            Helper.getCheckedMenu(s);
        }

        //api call
    }

    private void ActivityChange() {


    }



    private void bindEventHandlers() {
        binding.NewMember.setOnClickListener(this);
        binding.LoanCollection.setOnClickListener(this);
        binding.PolicyCollection.setOnClickListener(this);
        binding.Profile.setOnClickListener(this);
        binding.Home.setOnClickListener(this);
    }

    private void StatusbarColor() {


        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.orange));

    }

    public void topVp() {
        List<Integer> latestModels = new ArrayList<>();
        latestModels.add(R.drawable.ic_top_viewpager);
        latestModels.add(R.drawable.ic_top_viewpager2);
        latestModels.add(R.drawable.ic_top_viewpager3);

        TopBannerVpAdapter viewPagerAdapter = new TopBannerVpAdapter(getApplicationContext(), latestModels, binding.vpTopBanner);
        binding.vpTopBanner.setAdapter(viewPagerAdapter);
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

        dotsCount = viewPagerAdapter.getItemCount();
        dots = new ImageView[dotsCount];
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            binding.SliderDots.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
        binding.vpTopBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                topRatedHandler.removeCallbacks(topRatedRunnable);
                topRatedHandler.postDelayed(topRatedRunnable, 4000);
                // Update dots
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                // Looping logic
                if (position == dotsCount - 1) {
                    binding.vpTopBanner.postDelayed(() -> binding.vpTopBanner.setCurrentItem(0, true), 4000);
                }
            }
        });
    }

    private Runnable topRatedRunnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = binding.vpTopBanner.getCurrentItem();
            if (currentItem == dotsCount - 1) {
                binding.vpTopBanner.setCurrentItem(0);
            } else {
                binding.vpTopBanner.setCurrentItem(currentItem + 1);
            }
        }
    };

    @Override
    public void onBackPressed() {
        backPressCounter++;

        if (backPressCounter == REQUIRED_BACK_PRESS_COUNT) {
            finishAffinity();
//            super.onBackPressed();
//            exit(0);
//            return;
            // Exit the app
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
    public void onClick(View v) {
        if (v == binding.NewMember) {
            Intent intent = new Intent(MainActivity.this, NewMemberActivity.class);
            startActivity(intent);
        } else if (v == binding.LoanCollection) {
            Intent intent = new Intent(MainActivity.this, LoanCollectionActivity.class);
            startActivity(intent);
        } else if (v == binding.PolicyCollection) {
            Intent intent = new Intent(MainActivity.this, RenewalCollectionActivity.class);
            startActivity(intent);

        } else if (v == binding.Profile) {

            Intent intent = new Intent(MainActivity.this, SavingsCollectionReportActivity.class);
            intent.putExtra("SBCollectionType", "Today SBTransaction");
            startActivity(intent);
          /*  Intent intent=new Intent(context, SavingsCollectionReportActivity.class);
            intent.putExtra("SBCollectionType","Today SBTransaction");
            context.startActivity(intent);*/
        } else if (v==binding.Home) {
                Toast.makeText(this,"Already in Home Page",Toast.LENGTH_SHORT).show();

        }

    }
}


/////
