package com.example.geniousmicro.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geniousmicro.Adapter.GroupMemberAdapter;
import com.example.geniousmicro.Data.PostDataParserObjectResponse;
import com.example.geniousmicro.Data.VolleyCallback;
import com.example.geniousmicro.Models.GlobalUtilityModels.GlobalDataModel;
import com.example.geniousmicro.Models.UtilityModels.GroupEmiDataModel;
import com.example.geniousmicro.Models.UtilityModels.GroupMemberModel;
import com.example.geniousmicro.Others.ApiLinks;
import com.example.geniousmicro.R;
import com.example.geniousmicro.UserData.GlobalUserData;
import com.example.geniousmicro.databinding.ActivityGroupMemberEmiactivityBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class GroupMemberEMIActivity extends AppCompatActivity {

    String GroupCode = "";
    ActivityGroupMemberEmiactivityBinding binding;
    GroupMemberAdapter adapter;
    ArrayList<GroupMemberModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityGroupMemberEmiactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        GroupCode = getIntent().getStringExtra("GroupCode");
        GlobalDataModel.emidatalist = new HashMap<>();
        getMemberDetails(GroupCode);
        binding.total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTotalamt();
            }
        });
        binding.savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertGroupLoanEmi();
            }
        });

    }

    private void InsertGroupLoanEmi() {
        String Collectiondetails = "";
        Set keys = GlobalDataModel.emidatalist.keySet();
        Iterator i = keys.iterator();
        Collection getValues = GlobalDataModel.emidatalist.values();
        i = getValues.iterator();
        while (i.hasNext()) {
            GroupEmiDataModel model = (GroupEmiDataModel) i.next();
            Collectiondetails += model.getLoanCode() + "," + model.getLSCode() + "," + model.getLSAmount() + "," + model.getEMIAmount()+","+model.getGroupMemberCode() + ";";
        }
        insertGroupLoanEmi(Collectiondetails);
    }

    private void insertGroupLoanEmi(String collectiondetails) {
        HashMap<String,String> map=new HashMap<>();
        map.put("UserName", GlobalUserData.employeeDataModel.getEmployeeID());
        map.put("CollectionList",collectiondetails);
        new PostDataParserObjectResponse(GroupMemberEMIActivity.this, ApiLinks.INSERT_GROUPEMICOLLECTION, map, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    Log.d("InsertResponse", result.toString());
                    JSONObject jso=result.getJSONObject("ReturnData");
                    if (jso.getString("ErrorCode").equals("0")){
                        Toast.makeText(GroupMemberEMIActivity.this, "Successfully Colleted EMI", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(GroupMemberEMIActivity.this,GroupCollActivity.class));
//                        finishAffinity();
                        onBackPressed();
                    }else{
                        Toast.makeText(GroupMemberEMIActivity.this, "Unable to Collect EMI", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(GroupMemberEMIActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.d("EX", e.toString());
                }
            }

            @Override
            public void onErrorResponse(String error) {
                Log.e("Callback Error", error);
            }
        });

    }

    private void getTotalamt() {
        double total_emi = 0, total_ls = 0, total = 0;
        Set keys = GlobalDataModel.emidatalist.keySet();
        Iterator i = keys.iterator();
        Collection getValues = GlobalDataModel.emidatalist.values();
        i = getValues.iterator();
        while (i.hasNext()) {
            GroupEmiDataModel model = (GroupEmiDataModel) i.next();
            total_emi += Double.parseDouble(model.getEMIAmount());
            total_ls += Double.parseDouble(model.getLSAmount());
        }
        total = total_emi + total_ls;
        binding.emiamt.setText("" + total_emi);
        binding.ls.setText("" + total_ls);
        binding.totalamt.setText("" + total);
    }

    private void getMemberDetails(String groupCode) {
        list = new ArrayList<>();
        Log.d("Params", groupCode);
        HashMap<String, String> map = new HashMap<>();
        map.put("GroupCode", groupCode);
        new PostDataParserObjectResponse(GroupMemberEMIActivity.this, ApiLinks.GET_GROUPMEMBERDETAILS, map, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    JSONArray jsa = result.getJSONArray("groupMemberDetails");
                    for (int i = 0; i < jsa.length(); i++) {
                        Gson gson = new Gson();
                        GroupMemberModel model = gson.fromJson(String.valueOf(jsa.getJSONObject(i)), GroupMemberModel.class);

                        list.add(model);
                    }
                    if (list.size() > 0) {
                        adapter = new GroupMemberAdapter(list, GroupMemberEMIActivity.this);
                        LinearLayoutManager llm = new LinearLayoutManager(GroupMemberEMIActivity.this);
                        llm.setOrientation(RecyclerView.VERTICAL);
                        binding.groupMemberRec.setLayoutManager(llm);
                        binding.groupMemberRec.setAdapter(adapter);
                        binding.groupMemberRec.setVisibility(View.VISIBLE);
                    } else {
                        binding.groupMemberRec.setVisibility(View.GONE);
                        Toast.makeText(GroupMemberEMIActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(GroupMemberEMIActivity.this, "Error", Toast.LENGTH_SHORT).show();
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