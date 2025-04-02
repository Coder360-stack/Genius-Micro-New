package com.example.geniousmicro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geniousmicro.Models.UtilityModels.InvestStatementModel;
import com.example.geniousmicro.R;

import java.util.ArrayList;

public class InvestStatementAdapter extends RecyclerView.Adapter<InvestStatementAdapter.SBSummaryViewHolder> {

    ArrayList<InvestStatementModel> list;
    Context context;

    public InvestStatementAdapter(ArrayList<InvestStatementModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SBSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.investment_statement_layout, parent, false);
        return new SBSummaryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SBSummaryViewHolder holder, int position) {
        InvestStatementModel model = list.get(position);
        holder.policy_code.setText(model.getPolicyCode());
        holder.InstNo.setText(model.getInstNo());
        holder.DueDate.setText(model.getDueDate());
        holder.DateofRenewal.setText(model.getDateofRenewal());
        holder.LateFine.setText(model.getLateFine());
        holder.Remarks.setText(model.getRemarks());
        holder.Amount.setText("₹ "+model.getAmount());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SBSummaryViewHolder extends RecyclerView.ViewHolder {
        TextView policy_code, InstNo, DueDate, DateofRenewal, LateFine,Remarks,Amount;

        public SBSummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            policy_code = itemView.findViewById(R.id.tv_row_loan_emi_statement_policy_code);
            InstNo = itemView.findViewById(R.id.tv_row_loan_emi_statement_InstNo);
            DueDate = itemView.findViewById(R.id.tv_row_loan_emi_statement_DueDate);
            DateofRenewal = itemView.findViewById(R.id.tv_row_loan_emi_statement_DateofRenewal);
            LateFine = itemView.findViewById(R.id.tv_row_loan_LateFine);
            Remarks=itemView.findViewById(R.id.tv_row_loan_emi_statement_remarks);
            Amount=itemView.findViewById(R.id.tv_row_loan_emi_statement_Amount);

        }
    }
}
