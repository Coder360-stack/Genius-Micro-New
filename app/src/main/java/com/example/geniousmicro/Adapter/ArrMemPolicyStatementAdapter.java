package com.example.geniousmicro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geniousmicro.Models.UtilityModels.PolicyStatementModel;
import com.example.geniousmicro.R;

import java.util.ArrayList;


public class ArrMemPolicyStatementAdapter extends RecyclerView.Adapter<ArrMemPolicyStatementAdapter.LoanStatementViewHolder> {

    ArrayList<PolicyStatementModel> list;
    Context context;

    public ArrMemPolicyStatementAdapter(ArrayList<PolicyStatementModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public LoanStatementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.policy_statement_layout, parent, false);
        return new LoanStatementViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ArrMemPolicyStatementAdapter.LoanStatementViewHolder holder, int position) {
        PolicyStatementModel model = list.get(position);
        holder.loancode.setText(model.getPolicyCode());
        holder.name.setText(model.getApplicantName());
        holder.date.setText(model.getDateofEntry());
        holder.status.setText(model.getStatus());
        holder.emiamount.setText(model.getPaidAmount());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LoanStatementViewHolder extends RecyclerView.ViewHolder {
        TextView loancode, name, date, status,emiamount;

        public LoanStatementViewHolder(@NonNull View itemView) {
            super(itemView);
            loancode = itemView.findViewById(R.id.loancode);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            emiamount = itemView.findViewById(R.id.emiamount);

        }
    }
}
