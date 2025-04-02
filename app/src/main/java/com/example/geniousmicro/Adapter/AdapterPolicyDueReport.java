package com.example.geniousmicro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geniousmicro.R;
import com.example.geniousmicro.RenewalCollectionActivity;
import com.example.geniousmicro.mssql.model.SetGetPolicyDueReport;

import java.util.ArrayList;

public class AdapterPolicyDueReport extends RecyclerView.Adapter<AdapterPolicyDueReport.DueReportViewHolder> {

    private Context context;
    private ArrayList<SetGetPolicyDueReport> arrayList;

    public AdapterPolicyDueReport(Context context, ArrayList<SetGetPolicyDueReport> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public DueReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_policy_daywise_due_report, parent, false);
        return new DueReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DueReportViewHolder holder, int position) {
        holder.mTv_loanCode.setText(arrayList.get(position).getLoanCode());
        holder.mTv_applicantName.setText(arrayList.get(position).getApplicantName());
        holder.mTv_phNo.setText(arrayList.get(position).getPhNo());
        holder.mTv_totalDueEmiNo.setText(arrayList.get(position).getTotalDueEmiNo());
        holder.mTv_totalDueEmiAmt.setText(arrayList.get(position).getTotalDueEmiAmnt());
        holder.mTv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(arrayList.get(position).getNoofPendingRenewal())==0) {
                    Intent intent = new Intent(context.getApplicationContext(), RenewalCollectionActivity.class);
                    intent.putExtra("policyCode", arrayList.get(position).getLoanCode());
                    context.startActivity(intent);
                }else {
                    Toast.makeText(context, "Please Approve the pending Renewal", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class DueReportViewHolder extends RecyclerView.ViewHolder {

        TextView mTv_loanCode;
        TextView mTv_applicantName;
        TextView mTv_phNo;
        TextView mTv_totalDueEmiNo;
        TextView mTv_totalDueEmiAmt;
        Button mTv_pay;

        public DueReportViewHolder(View itemView) {
            super(itemView);

            mTv_pay=itemView.findViewById(R.id.tv_row_loan_due_pay);
            mTv_loanCode = itemView.findViewById(R.id.tv_row_loan_due_report_lcode);
            mTv_applicantName = itemView.findViewById(R.id.tv_row_loan_due_report_name);
            mTv_phNo = itemView.findViewById(R.id.tv_row_loan_due_report_ph_no);
            mTv_totalDueEmiNo = itemView.findViewById(R.id.tv_row_loan_due_report_due_emi);
            mTv_totalDueEmiAmt = itemView.findViewById(R.id.tv_row_loan_due_report_due_emi_amnt);

        }
    }
}
