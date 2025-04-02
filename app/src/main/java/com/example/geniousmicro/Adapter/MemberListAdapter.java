package com.example.geniousmicro.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geniousmicro.Models.UtilityModels.MemberListModels;
import com.example.geniousmicro.R;
import com.example.geniousmicro.UserData.GlobalUserData;

import java.util.ArrayList;

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MemberViewHolder> {

    Context context;
    ArrayList<MemberListModels> list;

    public MemberListAdapter(Context context, ArrayList<MemberListModels> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.member_list_item_view, parent, false);
        return new MemberViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        MemberListModels models = list.get(position);
        holder.tv_Member_id.setText(models.getMemberCode());
        holder.tv_Member_name.setText(models.getMemberName());
        holder.tv_joining_date.setText(models.getDateOfJoin());
        holder.tv_branch_id.setText(models.getOfcID());
        holder.tv_employee_id.setText(GlobalUserData.employeeDataModel.getEmployeeID());

        holder.addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleMaps(models.getAddr());
            }
        });
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                    dialIntent.setData(Uri.parse("tel:" + models.getPhone()));
                    dialIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // Check if there is an app that can handle the dial action
                    if (dialIntent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(dialIntent);
                    } else {
                        // Handle the case where no dialer is available
                        Toast.makeText(context, "No dialer app found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.d("Call Err", e.toString());
                    Toast.makeText(context, "Unable to open Contacts", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder {
        TextView tv_Member_id, tv_Member_name, tv_joining_date, tv_branch_id, tv_employee_id;
        ImageView call, addr;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Member_id = itemView.findViewById(R.id.tv_Member_id);
            tv_Member_name = itemView.findViewById(R.id.tv_Member_name);
            tv_joining_date = itemView.findViewById(R.id.tv_joining_date);
            tv_branch_id = itemView.findViewById(R.id.tv_branch_id);
            tv_employee_id = itemView.findViewById(R.id.tv_employee_id);
            call = itemView.findViewById(R.id.call);
            addr = itemView.findViewById(R.id.addr);
        }
    }

    private void requestCallPermission() {
        // Check if we need to explain why the permission is needed
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CALL_PHONE)) {
            Toast.makeText(context, "Phone call permission is required to make calls", Toast.LENGTH_SHORT).show();
        }

        // Request the permission
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void openGoogleMaps(String locationAddress) {
        try {
            // Create the Uri with the geo scheme and the address
            Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(locationAddress));

            // Create an Intent to open Google Maps
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);

            // Set the package to ensure it opens in Google Maps
            mapIntent.setPackage("com.google.android.apps.maps");

            // Add the FLAG_ACTIVITY_NEW_TASK flag since this is not being called from an Activity
            mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Check if the Google Maps app is installed
            if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(mapIntent);
            } else {
                // Handle the case where Google Maps is not installed
                // Optionally, you can redirect the user to install Google Maps from the Play Store
                Uri playStoreUri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
                Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, playStoreUri);
                playStoreIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(playStoreIntent);
            }
        } catch (Exception e) {
            Log.d("Loc Err", e.toString());
            Toast.makeText(context, "Unable to open Google Maps", Toast.LENGTH_SHORT).show();
        }
    }

}
