package com.example.geniousmicro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geniousmicro.Models.UtilityModels.SBStatementModel;
import com.example.geniousmicro.R;

import java.util.ArrayList;

public class SBStatementAdapter extends RecyclerView.Adapter<SBStatementAdapter.SBSummaryViewHolder> {

    ArrayList<SBStatementModel> list;
    Context context;

    public SBStatementAdapter(ArrayList<SBStatementModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SBSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.sb_tr_layout, parent, false);
        return new SBSummaryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SBSummaryViewHolder holder, int position) {
        SBStatementModel model = list.get(position);
        holder.acccode.setText(model.getTransactionNo());
        holder.name.setText(model.getRemarks());
        holder.date.setText(model.getTDate());
        holder.depositamt.setText("₹ "+model.getDepositAmount());
        holder.withdrawamt.setText("₹ "+model.getWithdrawlAmount());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SBSummaryViewHolder extends RecyclerView.ViewHolder {
        TextView acccode, name, date, depositamt, withdrawamt;

        public SBSummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            acccode = itemView.findViewById(R.id.acccode);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            depositamt = itemView.findViewById(R.id.depositamt);
            withdrawamt = itemView.findViewById(R.id.withdrawamt);
        }
    }
}
