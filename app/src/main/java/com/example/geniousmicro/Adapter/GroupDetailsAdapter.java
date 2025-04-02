package com.example.geniousmicro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geniousmicro.Models.UtilityModels.GroupDetailsModel;
import com.example.geniousmicro.R;
import com.example.geniousmicro.activities.GroupMemberEMIActivity;

import java.util.ArrayList;

public class GroupDetailsAdapter extends RecyclerView.Adapter<GroupDetailsAdapter.GroupDetailsViewHolder> {

    ArrayList<GroupDetailsModel> grouplist;
    Context context;

    public GroupDetailsAdapter(ArrayList<GroupDetailsModel> grouplist, Context context) {
        this.grouplist = grouplist;
        this.context = context;
    }

    @NonNull
    @Override
    public GroupDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.group_details_lay,parent,false);
        return new GroupDetailsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupDetailsViewHolder holder, int position) {

        GroupDetailsModel model=grouplist.get(position);
        holder.GroupCode.setText(model.getGroupCode());
        holder.GroupName.setText(model.getGroupName());
        holder.Day.setText(model.getGroupDays());

        if(position%2!=0){
            holder.bac_lay.setBackgroundResource(R.color.list_back);
        }

        holder.groupcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, GroupMemberEMIActivity.class);
                intent.putExtra("GroupCode",model.getGroupCode());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return grouplist.size();
    }

    public class GroupDetailsViewHolder extends RecyclerView.ViewHolder {
        TextView GroupCode,GroupName,Day;
        CardView groupcard;
        LinearLayout bac_lay;

        public GroupDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            GroupCode=itemView.findViewById(R.id.groupcode);
            GroupName=itemView.findViewById(R.id.groupname);
            Day=itemView.findViewById(R.id.day);
            groupcard=itemView.findViewById(R.id.group_card);
            bac_lay=itemView.findViewById(R.id.bac_lay);
        }
    }
}
