package com.example.geniousmicro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    String username="",password="";
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME="UserDetails";
    String USERNAME="UserName";
    String PASSWORD="Password";
    private boolean isPasswordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sp=getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor=sp.edit();

        if(sp.contains(USERNAME)){
            Toast.makeText(this, "Auto Login..", Toast.LENGTH_SHORT).show();
            login(sp.getString(USERNAME,""),sp.getString(PASSWORD,""));
        }
        binding.btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=binding.loginUserName.getText().toString();
                password=binding.loginPassword.getText().toString();
                if(username.length()>0 && password.length()>0){
                    login(username,password);
                }else {
                    Toast.makeText(LoginActivity.this, "Please Fill Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.ivTogglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Hide Password
                    binding.loginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    binding.loginPassword.setSelection(binding.loginPassword.length()); // Move cursor to the end
                    binding.ivTogglePassword.setImageResource(R.drawable.visibility_off);
                } else {
                    // Show Password
                    binding.loginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    binding.loginPassword.setSelection(binding.loginPassword.length()); // Move cursor to the end
                    binding.ivTogglePassword.setImageResource(R.drawable.visibility_on);
                }
                isPasswordVisible = !isPasswordVisible;
            }
        });

    }

    private void login(String username, String password) {
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("username",username);
        hashMap.put("password",password);
        new PostDataParserObjectResponse(LoginActivity.this,ApiLinks.LOGIN, hashMap, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                // Handle the successful response
                try {
                    JSONObject userDetails = response.getJSONObject("Login");
                    Log.e("userDetails",""+userDetails);
                    if(userDetails.getString("status")!=null ||userDetails.getString("status").equals("")) {
                        if (userDetails.getString("status").equals("arranger")) {
                            EmployeeDataModel ed =
                                    new EmployeeDataModel(userDetails.getString("usercode"),
                                            userDetails.getString("username"),
                                            userDetails.getString("PassWord"),
                                            userDetails.getString("Officeid"),
                                            userDetails.getBoolean("isActive"));
                            GlobalUserData.employeeDataModel = ed;
                            if (binding.rememberMe.isChecked()) {
                                editor.putString(USERNAME, userDetails.getString("usercode"));
                                editor.putString(PASSWORD, userDetails.getString("PassWord"));
                                editor.apply();
                            }
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else if(userDetails.getString("status").equals("member")){


                            MemberDataModel ed =
                                    new MemberDataModel(
                                            userDetails.getString("usercode"),
                                            userDetails.getString("PassWord")
                                           );

                            GlobalUserData.memberDataModel = ed;
                            if (binding.rememberMe.isChecked()) {
                                editor.putString(USERNAME, userDetails.getString("usercode"));
                                editor.putString(PASSWORD, userDetails.getString("PassWord"));
                                editor.apply();
                            }
                            startActivity(new Intent(LoginActivity.this, MemberDashboardActivity.class));
                            finish();


                        }else if(userDetails.getString("status").equals("admin")){


                            AdminDataModel ed =
                                    new AdminDataModel(
                                            userDetails.getString("usercode"),
                                            userDetails.getString("PassWord")
                                    );

                            GlobalUserData.adminDataModel = ed;
                            if (binding.rememberMe.isChecked()) {
                                editor.putString(USERNAME, userDetails.getString("usercode"));
                                editor.putString(PASSWORD, userDetails.getString("PassWord"));
                                editor.apply();
                            }
                            startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                            finish();

                        }else {
                            Toast.makeText(LoginActivity.this, "In Valid Password/Username", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Log.d("EX","no data");
                        Toast.makeText(LoginActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("Callback Response", response.toString());
                    Log.d("EX","1");
                    //Toast.makeText(LoginActivity.this, "In Valid Password/Username", Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.d("EX",e.toString());
                }
            }

            @Override
            public void onErrorResponse(String error) {
                // Handle the error response
                Log.e("Callback Error", error);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}