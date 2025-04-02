package com.example.geniousmicro;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.geniousmicro.Data.PostDataParserObjectResponse;
import com.example.geniousmicro.Data.VolleyCallback;
import com.example.geniousmicro.Others.ApiLinks;
import com.example.geniousmicro.databinding.ActivityLoginBinding;
import com.example.geniousmicro.databinding.UpdateAvailableLayoutBinding;
import com.example.geniousmicro.util.OpenLinks;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class SplashScreen extends AppCompatActivity implements View.OnClickListener {
    // from sullair
    private TextView mTv_splashVersion;
    private LinearLayout mLl_openGTech;
    private LinearLayout mLl_devByGen;

    public static String updated_app_ver;

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

        //TextView versionInfoTextView = findViewById(R.id.version_info);

        // Retrieve app version name and version code
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);

            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode; // Deprecated in API level 28; for newer versions use 'long versionCode'

            String versionInfo = "Version Name: " + versionName + "\nVersion Code: " + versionCode;
            mTv_splashVersion.setText(versionInfo);
            // mTv_splashVersion.setText("Version " + BuildConfig.VERSION_NAME);


            isUpdateAvail(versionName, versionCode);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        //  mTv_splashVersion.setText("Version " + BuildConfig.VERSION_NAME);

        //  isUpdateAvail();
        //callWaitMethod(false);

    }


    private void setViewReferences() {
        mTv_splashVersion = findViewById(R.id.splash_version);
        mLl_openGTech = findViewById(R.id.ll_open_genius_technology_links);
    }

    private void bindEventHandlers() {
        mTv_splashVersion.setOnClickListener(this);
        mLl_openGTech.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mLl_openGTech) {
            new OpenLinks(this).openGeniusTechnology();
        }
    }


    public void
    isUpdateAvail(String versionName, int versionCode) {
        HashMap<String, String> hasmap = new HashMap<>();


        hasmap.put("versionName", versionName);
        hasmap.put("versionCode", String.valueOf(versionCode));


        new PostDataParserObjectResponse(this, ApiLinks.IS_UPDATE_AVAILABLE, hasmap, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

                try {
                    JSONArray userData = result.getJSONArray("versionDetails");
                    for (int i = 0; i < userData.length(); i++) {
                        Log.d("data", "onSuccessResponse: " + userData.getJSONObject(i).getString("isAvailable"));
                        if (userData.getJSONObject(i).getString("isAvailable").equals("1")) {
                            String NewVersionName = userData.getJSONObject(i).getString("VersionName");
                            String NewVersionCode = userData.getJSONObject(i).getString("VersionCode");
                            String NewUpdateMessage = userData.getJSONObject(i).getString("LatestUpdate");


                            Log.e("updateMessage2", "" + NewUpdateMessage);

                            if (!NewVersionName.equals(versionName)) {
                                Boolean IsFinalUpdate = false;
                                showServerDialog(NewVersionName, NewVersionCode, NewUpdateMessage, IsFinalUpdate);
                            } else {
                                Boolean IsFinalUpdate = true;
                                showServerDialog(NewVersionName, NewVersionCode, NewUpdateMessage, IsFinalUpdate);

                              /* if (String.valueOf(versionCode).equals(NewVersionCode)){
                                   Boolean IsFinalUpdate = true;
                                   showServerDialog(NewVersionName,NewVersionCode,NewUpdateMessage,IsFinalUpdate);
                               }else {
                                   Boolean IsFinalUpdate = true;
                                   showServerDialog(NewVersionName,NewVersionCode,NewUpdateMessage,IsFinalUpdate);

                               }*/
                            }

/*
                           if (String.valueOf(versionCode).equals(NewVersionCode)){
                               Boolean IsFinalUpdate = false;
                               showServerDialog(NewVersionName,NewVersionCode,NewUpdateMessage,IsFinalUpdate);
                           }else {
                               Boolean IsFinalUpdate = true;
                               showServerDialog(NewVersionName,NewVersionCode,NewUpdateMessage,IsFinalUpdate);
                           }*/


                          /* if (NewVersionName.equals(versionName)){
                               callWaitMethod(true);
                           }else {
                               if (String.valueOf(versionCode).equals(NewVersionCode)){
                                   Boolean IsFinalUpdate = true;
                                   showServerDialog(NewVersionName,NewVersionCode,NewUpdateMessage,IsFinalUpdate);
                               }else {
                                   Boolean IsFinalUpdate = false;
                                   showServerDialog(NewVersionName,NewVersionCode,NewUpdateMessage,IsFinalUpdate);
                               }
                           }

                           if (String.valueOf(versionCode).equals(NewVersionCode)){
                               Boolean IsFinalUpdate = true;
                               showServerDialog(NewVersionName,NewVersionCode,NewUpdateMessage,IsFinalUpdate);
                           }else {
                               Boolean IsFinalUpdate = false;
                               showServerDialog(NewVersionName,NewVersionCode,NewUpdateMessage,IsFinalUpdate);
                           }*/


                            // if(!userData.getJSONObject(i).getString("VersionCode").equals(versionCode)){
                       /*    if(!userData.getJSONObject(i).getString("VersionCode").equals(versionCode)){
                               showServerDialog(versionName,versionCode);
                           }else {
                               callWaitMethod(true);
                           }*/


                        } else {

                            callWaitMethod(false);
                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(SplashScreen.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.d("EX", e.toString());
                }


            }


            @Override
            public void onErrorResponse(String error) {
                Toast.makeText(SplashScreen.this, "Error ", Toast.LENGTH_SHORT).show();
                Log.d("EX", error.toString());
            }



          /*  @Override
            public void onSuccessResponse(JSONArray response) throws JSONException {
                if (response != null) {
                    JSONObject jsonObject = response.getJSONObject(0);
                    updated_app_ver = jsonObject.getString("VersionName");
                    if (jsonObject.getInt("isAvailable") == 1) {
                        callWaitMethod(true);
                    } else {
                        callWaitMethod(false);
                    }
                }
            }

            @Override
            public void onErrorResponse(String error) {
                Log.e("isUpdateAvail", error);
            }*/

         /*   @Override
            public void onGetResponse(JSONArray response) {
                try {
                    if (response != null) {
                        JSONObject jsonObject = response.getJSONObject(0);
                        updated_app_ver = jsonObject.getString("VersionName");
                        if (jsonObject.getInt("isAvailable") == 1) {
                            callWaitMethod(true);
                        } else {
                            callWaitMethod(false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
        });
    }

    private void callWaitMethod(Boolean flag) {
        if (flag) {
//            YoYo.with(Techniques.StandUp)
//                    .duration(3000)
//                    .repeat(0)
//                    .playOn(findViewById(R.id.tv_splash_activity_title));


            int secondsDelayed = 2;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this, UpdateAppActivity.class));
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }, secondsDelayed * 2000);
        } else {
//            YoYo.with(Techniques.StandUp)
//                    .duration(3000)
//                    .repeat(0)
//                    .playOn(findViewById(R.id.tv_splash_activity_title));

            int secondsDelayed = 2;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }, secondsDelayed * 2000);
        }
    }


    private void showServerDialog(String versionName, String versionCode, String updateMessage, Boolean flag) {
        Dialog customDialog = new Dialog(SplashScreen.this);
        customDialog.setContentView(R.layout.update_available_layout);
        customDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        customDialog.setCancelable(false);

        TextView versionNameTextView = customDialog.findViewById(R.id.VersionName); // Replace with actual ID
        TextView versionCodeTextView = customDialog.findViewById(R.id.VersionCode); // Replace with actual ID
        Button updateButton = customDialog.findViewById(R.id.btn_update);
        Button UpdateLater = customDialog.findViewById(R.id.btn_update_later);
        TextView tv_update_title = customDialog.findViewById(R.id.tv_latest_features);
        //TextView testt = customDialog.findViewById(R.id.testt); // Replace with actual ID
        if (flag) {
            UpdateLater.setVisibility(View.VISIBLE);
        } else {
            UpdateLater.setVisibility(View.GONE);
            // UpdateLater.setVisibility(View.VISIBLE);
        }
        Log.e("updateMessage4", "" + updateMessage);
        tv_update_title.setText("" + updateMessage);
        versionNameTextView.setText("" + versionName);
        versionCodeTextView.setText("" + versionCode);
        tv_update_title.setVisibility(View.VISIBLE);
        Log.e("updateMessage34", "" + tv_update_title.getText().toString());
        UpdateLater.setOnClickListener(v -> {
            // Handle the update button click
            customDialog.dismiss();
            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        });


        updateButton.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse((Uri.parse("market://details?id=") + getPackageName()))));
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Uri.parse("https://play.google.com/store/apps/details?id=") + getPackageName())));
            }

        });

        customDialog.show();
    }


}
