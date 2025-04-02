package com.example.geniousmicro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geniousmicro.Models.UtilityModels.AdminLoanDueReportModel;
import com.example.geniousmicro.R;

import java.util.ArrayList;


public class AdminLoanDueReportAdapter extends RecyclerView.Adapter<AdminLoanDueReportAdapter.LoanStatementViewHolder> {

    ArrayList<AdminLoanDueReportModel> list;
    Context context;

    public AdminLoanDueReportAdapter(ArrayList<AdminLoanDueReportModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public LoanStatementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.loan_due_layout, parent, false);
        return new LoanStatementViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminLoanDueReportAdapter.LoanStatementViewHolder holder, int position) {
        AdminLoanDueReportModel model = list.get(position);
        holder.loancode.setText(model.getLoanCode());
        holder.name.setText(model.getApplicantName());
        holder.ph.setText(model.getHolderPhoneNo());
        holder.DueEmiNo.setText(model.getNoDueEMI());
        holder.DueEmiAmt.setText(model.getTotalDueEMIAmount());
        holder.phonelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = model.getHolderPhoneNo();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                context.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LoanStatementViewHolder extends RecyclerView.ViewHolder {
        TextView loancode, name, ph, DueEmiNo, DueEmiAmt;
        LinearLayout phonelayout;

        public LoanStatementViewHolder(@NonNull View itemView) {
            super(itemView);
            loancode = itemView.findViewById(R.id.loancode);
            name = itemView.findViewById(R.id.name);
            ph = itemView.findViewById(R.id.ph);
            DueEmiNo = itemView.findViewById(R.id.DueEmiNo);
            DueEmiAmt = itemView.findViewById(R.id.DueEmiAmt);
            phonelayout = itemView.findViewById(R.id.phonelayout);

        }
    }
}
