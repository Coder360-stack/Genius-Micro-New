package com.example.geniousmicro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geniousmicro.Models.UtilityModels.LoanStatementModel;
import com.example.geniousmicro.R;

import java.util.ArrayList;

public class LoanStatementAdapter extends RecyclerView.Adapter<LoanStatementAdapter.SBSummaryViewHolder> {

    ArrayList<LoanStatementModel> list;
    Context context;

    public LoanStatementAdapter(ArrayList<LoanStatementModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SBSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.loan_statement_layout, parent, false);
        return new SBSummaryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SBSummaryViewHolder holder, int position) {
        LoanStatementModel model = list.get(position);
        holder.emi_number.setText(model.getEMINO());
        holder.EMIAmoun.setText("₹ "+model.getEMIAmount());
        holder.EMIDueDate.setText(model.getEMIDueDate());
        holder.PayMode.setText(model.getPayMode());
        holder.LateFine.setText(model.getLateFine());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SBSummaryViewHolder extends RecyclerView.ViewHolder {
        TextView emi_number, EMIAmoun, EMIDueDate, PayMode, LateFine;

        public SBSummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            emi_number = itemView.findViewById(R.id.tv_row_loan_emi_statement_emi_no);
            EMIAmoun = itemView.findViewById(R.id.tv_row_loan_emi_statement_emi_amnt);
            EMIDueDate = itemView.findViewById(R.id.tv_row_loan_emi_statement_due_date);
            PayMode = itemView.findViewById(R.id.tv_row_loan_emi_statement_pay_mode);
            LateFine = itemView.findViewById(R.id.tv_row_loan_LateFine);
        }
    }
}
