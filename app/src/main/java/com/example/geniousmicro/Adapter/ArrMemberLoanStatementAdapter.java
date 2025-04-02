package com.example.geniousmicro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geniousmicro.Models.UtilityModels.ArrMemLoanStatementModel;
import com.example.geniousmicro.R;

import java.util.ArrayList;


public class ArrMemberLoanStatementAdapter extends RecyclerView.Adapter<ArrMemberLoanStatementAdapter.LoanStatementViewHolder> {

    ArrayList<ArrMemLoanStatementModel> list;
    Context context;

    public ArrMemberLoanStatementAdapter(ArrayList<ArrMemLoanStatementModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public LoanStatementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.loan_tr_layout, parent, false);
        return new LoanStatementViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ArrMemberLoanStatementAdapter.LoanStatementViewHolder holder, int position) {
        ArrMemLoanStatementModel model = list.get(position);
        holder.loancode.setText(model.getLoanCode());
        holder.name.setText(model.getUserName());
        holder.date.setText(model.getDateofEntry());
        holder.status.setText(model.getStatus());
        holder.emiamount.setText(model.getEMIAmount());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LoanStatementViewHolder extends RecyclerView.ViewHolder {
        TextView loancode, name, date, status, emiamount;

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
