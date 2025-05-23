package com.example.geniousmicro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.geniousmicro.LoanCollectionActivity;
import com.example.geniousmicro.Models.UtilityModels.MenuItemModel;
import com.example.geniousmicro.RenewalCollectionActivity;
import com.example.geniousmicro.activities.AgentLoanDueReportDaywise;
import com.example.geniousmicro.activities.AgentPolicyDueReportDaywise;
import com.example.geniousmicro.activities.ArrLoanCollReportActivity;
import com.example.geniousmicro.activities.ArrLoanDueReportActivity;
import com.example.geniousmicro.activities.ArrLoanManualCollection;
import com.example.geniousmicro.activities.ArrPolicyCollReportActivity;
import com.example.geniousmicro.activities.ArrPolicyManualCollection;
import com.example.geniousmicro.activities.ArrTodayPolicyCollReportActivity;
import com.example.geniousmicro.activities.ArrangerMemberLoanStatementActivity;
import com.example.geniousmicro.activities.ArrangerSBCollectionActivity;
import com.example.geniousmicro.activities.CollectionReportActivity;
import com.example.geniousmicro.activities.GroupCollActivity;
import com.example.geniousmicro.activities.GroupCollectionSummaryActivity;
import com.example.geniousmicro.activities.LoanCollectionManualActivity;
import com.example.geniousmicro.activities.LoanDetailsActivity;
import com.example.geniousmicro.NewMemberActivity;
import com.example.geniousmicro.R;
import com.example.geniousmicro.activities.MemberListActivity;
import com.example.geniousmicro.activities.RenewalCollectionManualActivity;
import com.example.geniousmicro.activities.RenewalDetailsActivity;
import com.example.geniousmicro.activities.SavingsCollectionReportActivity;

import java.util.ArrayList;

public class ArrangerMenuAdapter extends BaseAdapter {

    private Context context;
    ArrayList<MenuItemModel> list;

    public ArrangerMenuAdapter(Context context, ArrayList<MenuItemModel> list) {
        this.context = context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_item, parent, false);
        }
       // LinearLayout grid_item=convertView.findViewById(R.id.grid_item);
        ImageView imageView = convertView.findViewById(R.id.image_view);
        TextView textView = convertView.findViewById(R.id.text_view);
        LinearLayout gridlay=convertView.findViewById(R.id.grid_itemlay);

        String menuname=list.get(position).getTitle();
        int img=list.get(position).getIcon();
        imageView.setImageResource(img);
        textView.setText(menuname);
        textView.setSelected(true);

        gridlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuname.equals("Approved Member List")){
                    Intent intent=new Intent(context, MemberListActivity.class);
                    intent.putExtra("isApproved","1");
                    context.startActivity(intent);
                }
                else if (menuname.equals("UnApproved Member List")){
                    Intent intent=new Intent(context, MemberListActivity.class);
                    intent.putExtra("isApproved","0");
                    context.startActivity(intent);
                }
                else if (menuname.equals("New Member")){
                    Intent intent=new Intent(context, NewMemberActivity.class);
                    context.startActivity(intent);
                }


                else if (menuname.equals("LS Transaction")){
                    Intent intent=new Intent(context, ArrangerSBCollectionActivity.class);
                    context.startActivity(intent);
                }
                else if (menuname.equals("Loan EMI")){
                    Intent intent=new Intent(context, LoanCollectionActivity.class);
                    context.startActivity(intent);
                }
                else if (menuname.equals("LS Transaction Report")){
                    Intent intent=new Intent(context, SavingsCollectionReportActivity.class);
                    intent.putExtra("user","Arranger");
                    context.startActivity(intent);
                }
                else if (menuname.equals("LoanCollection")){
                    Intent intent=new Intent(context, ArrLoanCollReportActivity.class);
                    context.startActivity(intent);
                } else if (menuname.equals("Today LoanCollection")){
                    Intent intent=new Intent(context, ArrLoanCollReportActivity.class);
                    intent.putExtra("LoanCollectionType","Today LoanCollection");
                    context.startActivity(intent);
                }
                else if (menuname.equals("Today PolicyCollection")){
                    Intent intent=new Intent(context, ArrTodayPolicyCollReportActivity.class);
                    context.startActivity(intent);
                }
                else if (menuname.equals("Today LSTransaction")){
                    Intent intent=new Intent(context, SavingsCollectionReportActivity.class);
                    intent.putExtra("SBCollectionType","Today SBTransaction");
                    context.startActivity(intent);
                }
               else if (menuname.equals("Loan Collection Report")){
                    Intent intent=new Intent(context, ArrangerMemberLoanStatementActivity.class);
                    context.startActivity(intent);
                } else if (menuname.equals("Policy Collection Report")){
                    Intent intent=new Intent(context, ArrPolicyCollReportActivity.class);
                    context.startActivity(intent);
                }

                else if (menuname.equals("Policy Manual Collection Report")){
                    Intent intent=new Intent(context, ArrPolicyManualCollection.class);
                    context.startActivity(intent);
                }
                else if (menuname.equals("Loan Manual Collection Report")){
                    Intent intent=new Intent(context, ArrLoanManualCollection.class);
                    context.startActivity(intent);
                }

                else if (menuname.equals("Policy Renewal")){
                    Intent intent=new Intent(context,   RenewalCollectionActivity.class);
                    context.startActivity(intent);
                }
                else if (menuname.equals("Group Loan Collection")){
                    Intent intent=new Intent(context, GroupCollActivity.class);
                    context.startActivity(intent);
                }
                else if (menuname.equals("Group Collection Report")){
                    Intent intent=new Intent(context, GroupCollectionSummaryActivity.class);
                    intent.putExtra("user","Arranger");
                    context.startActivity(intent);
                }  else if (menuname.equals("Loan Details")){

                    Intent intent=new Intent(context,LoanDetailsActivity.class);
                    context.startActivity(intent);

                } else if (menuname.equals("Policy Details")){

                    Intent intent=new Intent(context, RenewalDetailsActivity.class);
                    context.startActivity(intent);

                }else if (menuname.equals("All Collection")){

                    Intent intent=new Intent(context, CollectionReportActivity.class);
                    context.startActivity(intent);

                }else if (menuname.equals("LoanDue Report")){

                    Intent intent=new Intent(context, ArrLoanDueReportActivity.class);
                    context.startActivity(intent);

                }else if (menuname.equals("Today LoanDue Coll")){

                    Intent intent=new Intent(context, AgentLoanDueReportDaywise.class);
                    context.startActivity(intent);

                }else if (menuname.equals("Today PolicyDue Coll")){

                    Intent intent=new Intent(context, AgentPolicyDueReportDaywise.class);
                    context.startActivity(intent);


                } else if (menuname.equals("LoanCollection Manual")) {
                    Intent intent = new Intent(context, LoanCollectionManualActivity.class);
                    context.startActivity(intent);

                } else if (menuname.equals("PolicyCollection Manual")) {
                    Intent intent = new Intent(context, RenewalCollectionManualActivity.class);
                    context.startActivity(intent);

                }






               /* else if (menuname.equals("Today LoanCollection")){
                    Intent intent=new Intent(context, .class);
                    context.startActivity(intent);
                }
                else if (menuname.equals("Today LoanCollection")){
                    Intent intent=new Intent(context, .class);
                    context.startActivity(intent);
                }
                else if (menuname.equals("Today LoanCollection")){
                    Intent intent=new Intent(context, .class);
                    context.startActivity(intent);
                }*/





            }
        });





        return convertView;
    }
}