package com.example.geniousmicro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geniousmicro.Models.UtilityModels.LoanStatementDatewiseModel;
import com.example.geniousmicro.Models.UtilityModels.PolicyCollModel;
import com.example.geniousmicro.R;

import java.util.ArrayList;

public class TodayPolicyCollAdapter extends RecyclerView.Adapter<TodayPolicyCollAdapter.ViewHolder> {

    ArrayList<PolicyCollModel> list;
    Context context;

    public TodayPolicyCollAdapter(ArrayList<PolicyCollModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public TodayPolicyCollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.loan_tr_layout, parent, false);
        return new TodayPolicyCollAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TodayPolicyCollAdapter.ViewHolder holder, int position) {
        PolicyCollModel model = list.get(position);
        holder.policyCode.setText(model.getPolicyCode());
        holder.name.setText(model.getName());
        holder.date.setText(model.getDate());
        holder.status.setText(model.getStatus());
        holder.emiamount.setText(model.getAmount());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView policyCode, name, date, status, emiamount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            policyCode = itemView.findViewById(R.id.loancode);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            emiamount = itemView.findViewById(R.id.emiamount);
        }
    }
}
