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

import com.example.geniousmicro.Adapter.GroupDetailsAdapter;
import com.example.geniousmicro.Data.PostDataParserObjectResponse;
import com.example.geniousmicro.Data.VolleyCallback;
import com.example.geniousmicro.Models.UtilityModels.GroupDetailsModel;
import com.example.geniousmicro.Others.ApiLinks;
import com.example.geniousmicro.R;
import com.example.geniousmicro.UserData.GlobalUserData;
import com.example.geniousmicro.databinding.ActivityGroupCollBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class GroupCollActivity extends AppCompatActivity {
    ActivityGroupCollBinding binding;
    ArrayList<String> daylist;
    ArrayList<GroupDetailsModel> grouplist;
    int dayindex = 0;
    GroupDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityGroupCollBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        daylist=new ArrayList<>();
        daylist.add("Sunday");
        daylist.add("Monday");
        daylist.add("Tuesday");
        daylist.add("Wednesday");
        daylist.add("Thrusday");
        daylist.add("Friday");
        daylist.add("Saturday");
        dayindex = getDay();


        getGroupDetails("all", GlobalUserData.employeeDataModel.getEmployeeID(), daylist.get((dayindex - 1) % daylist.size()));

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.edSearchtxt.getText().toString().length() > 0) {
                    getGroupDetails(binding.edSearchtxt.getText().toString(), GlobalUserData.employeeDataModel.getEmployeeID(), daylist.get((dayindex - 1) % daylist.size()));
                } else {
                    binding.edSearchtxt.setError("Enter Group Code");
                    Toast.makeText(GroupCollActivity.this, "Please Enter Group Code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getGroupDetails(String groupCode, String employeeID, String day) {
        grouplist=new ArrayList<>();
        Log.d("Params",groupCode+"-"+employeeID+"-"+day);
        HashMap<String, String> map = new HashMap<>();
        map.put("Day", day);
        map.put("SearchValue", groupCode);
        map.put("ArrangerCode", employeeID);
        new PostDataParserObjectResponse(GroupCollActivity.this, ApiLinks.GET_GROUPDETAILSDAYWISE, map, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    JSONArray jsa = result.getJSONArray("GroupDetails");
                    for (int i = 0; i < jsa.length(); i++) {
                        Gson gson = new Gson();
                        GroupDetailsModel model = gson.fromJson(String.valueOf(jsa.getJSONObject(i)), GroupDetailsModel.class);

                        grouplist.add(model);
                    }
                    if (grouplist.size() > 0) {
                        adapter = new GroupDetailsAdapter(grouplist, GroupCollActivity.this);
                        LinearLayoutManager llm = new LinearLayoutManager(GroupCollActivity.this);
                        llm.setOrientation(RecyclerView.VERTICAL);
                        binding.groupRec.setLayoutManager(llm);
                        binding.groupRec.setAdapter(adapter);
                        binding.groupRec.setVisibility(View.VISIBLE);
                    } else {
                        binding.groupRec.setVisibility(View.GONE);
                        Toast.makeText(GroupCollActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(GroupCollActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.d("EX", e.toString());
                }
            }

            @Override
            public void onErrorResponse(String error) {
                Log.e("Callback Error", error);
            }
        });
    }

    private int getDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_WEEK);
    }
}