package com.example.geniousmicro.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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
import com.example.geniousmicro.Others.ApiLinks;
import com.example.geniousmicro.R;
import com.example.geniousmicro.UserData.GlobalUserData;
import com.example.geniousmicro.databinding.ActivityMemberProfileBinding;

import org.json.JSONObject;

import java.util.HashMap;

public class MemberProfileActivity extends AppCompatActivity {

    ActivityMemberProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityMemberProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getMemberProfileDetails();
    }


    private void getMemberProfileDetails() {
        HashMap<String,String> map=new HashMap<>();
        Log.d("mdata", GlobalUserData.memberDataModel.getMemberID());
        map.put("Membercode", GlobalUserData.memberDataModel.getMemberID());
        new PostDataParserObjectResponse(MemberProfileActivity.this, ApiLinks.GET_MEMBERDETAILSBYCODE, map, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    JSONObject jso = result.getJSONObject("memberDetails");
                    Log.d("print", String.valueOf(jso));
                    binding.name.setText(jso.getString("MemberName"));
                    binding.mcode.setText(jso.getString("MemberCode"));
                    binding.dob.setText(jso.getString("MemberDOB"));
                    binding.doj.setText(jso.getString("DateOfJoin"));
                    binding.addr.setText(jso.getString("Address"));
                    binding.bloodgr.setText(jso.getString("BloodGr"));
                    binding.gender.setText(jso.getString("Gender"));
                    binding.phoneno.setText(jso.getString("Phone"));
                    binding.pincode.setText(jso.getString("Pincode"));
                    binding.gname.setText(jso.getString("Father"));
                    binding.idproofname.setText(jso.getString("IdentityProof"));

                    if(jso.getString("ImageData")!=null){
                        String pic = jso.getString("ImageData");
                        byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        binding.proimg.setImageBitmap(decodedByte);
                    }

                } catch (Exception e) {
                    Toast.makeText(MemberProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.d("EX", e.toString());
                }
            }

            @Override
            public void onErrorResponse(String error) {
                Log.e("Callback Error", error);
            }
        });
    }
}