package com.example.geniousmicro.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;


import com.example.geniousmicro.R;

import java.util.ArrayList;

public class MemberSavingsAccountActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    private TextView mTv_memberName;
    private TextView mTv_currentbalance;
    private TextView mTv_memberCode;
    private TextView mTv_openingDate;

    private TextView mTv_selectAccount;
 /*   private SavingsStatementMembereAdapter savingsStatementMembereAdapter;
    private SetGetSavingsAccountStatement setGetSavingsAccountStatement;

    private ArrayList<SetGetSavingsAccountStatement> mArrayList = new ArrayList<>();
*/
    private Button mBtn_savingsAccountViewStatement, mBtn_downSbSt;

    private PopupMenu popupMenusSavingsAccount;

    private LinearLayout mLl_infoRoot;
    private ArrayList<String> arraylist_savcode = new ArrayList<>();
   // private ArrayList<MemberSavingsAccsetget> adapterSav;
    private static int savingsAccountFound = 0;
    private RecyclerView mRv_investmentAccountReport;

    private TextView mTv_accCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_savings_account_activity);


    }
}

//        setViewReferences();
//        bindEventHandlers();
//        adapters();
//        setSupportActionBar(mToolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//        }
//        mTv_toolbarTitle.setText("Savings Account");
//
//        popupMenusSavingsAccount = new PopupMenu(this, mTv_selectAccount);
//        getMenuOptions(APILinks.Get_Member_SavingsAccountcode + GlobalStore.GlobalValue.getMemberCode());
//
//    }
//
//    private void getSavingsAccountSummary(String s) {
//        new GetDataParserArray(this, s, true, new GetDataParserArray.OnGetResponseListener() {
//            @Override
//            public void onGetResponse(JSONArray response) {
//                try {
//                    if (response.length() > 0) {
//                        for (int i = 0; i < response.length(); i++) {
//                            JSONObject jsonObject = response.getJSONObject(i);
//                            //Toast.makeText(MemberSavingsAccountActivity.this, ""+response.toString(), Toast.LENGTH_SHORT).show();
//                            setGetSavingsAccountStatement = new SetGetSavingsAccountStatement();
//                            setGetSavingsAccountStatement.setAccountAccessType(jsonObject.getString("AccountAccessType"));
//                            setGetSavingsAccountStatement.setAccountOpeningDate(jsonObject.getString("AccountOpeningDate"));
//                            setGetSavingsAccountStatement.setCurrantBalance(jsonObject.getString("CurrantBalance"));
//                            setGetSavingsAccountStatement.setFirstApplicantMemberCode(jsonObject.getString("FirstApplicantMemberCode"));
//                            setGetSavingsAccountStatement.setFirstApplicantGuardian(jsonObject.getString("FirstApplicantGuardian"));
//                            setGetSavingsAccountStatement.setFirstApplicantPhone(jsonObject.getString("FirstApplicantPhone"));
//                            setGetSavingsAccountStatement.setFirstApplicantName(jsonObject.getString("FirstApplicantName"));
//
//                            mArrayList.add(setGetSavingsAccountStatement);
//                            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
//                            llm.setOrientation(LinearLayoutManager.VERTICAL);
//                            mRv_investmentAccountReport.setLayoutManager(llm);
//                            savingsStatementMembereAdapter = new SavingsStatementMembereAdapter(MemberSavingsAccountActivity.this, mArrayList);
//                            mRv_investmentAccountReport.setAdapter(savingsStatementMembereAdapter);
//                            savingsStatementMembereAdapter.notifyDataSetChanged();
//
//                        }
//                    } else {
//                        Toast.makeText(MemberSavingsAccountActivity.this, "No data found", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//    }
//
//    private void adapters() {
//        adapterSav = new ArrayList<>();
//    }
//
//    private void getMenuOptions(String s) {
//        new GetDataParserArray(this, s, true, new GetDataParserArray.OnGetResponseListener() {
//            @Override
//            public void onGetResponse(JSONArray response) {
//                try {
////                    Toast.makeText(MemberSavingsAccountActivity.this, "" + response, Toast.LENGTH_SHORT).show();
////                    if (response.length() > 0) {
////                        for (int i = 0; i < response.length(); i++) {
////                            JSONObject jsonObject = response.getJSONObject(i);
////                            arraylist_savcode.add(jsonObject.getString("AccountCode"));
////                            arraylist_savcode.add(jsonObject.getString("AccountCode") + "-" + jsonObject.getString("BANK_ACCOUNT_NUMBER"));
////                            mTv_accCode.setText(jsonObject.toString().replace("\"", "").replace("]", "").replace("[", ""));
////                            getSavingsAccountSummary(APILinks.Get_Member_SavingsStatemrnt + jsonObject.getString(""));
////                        }
////                        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(MemberSavingsAccountActivity.this,R.layout.spinner_hint, arraylist_savcode);
////                        mTv_accCode(adapterSav);
//                    String accode=response.toString().replace("\"","").replace("[","").replace("]","");
//                    mTv_accCode.setText(""+accode);
//                    getSavingsAccountSummary(APILinks.Get_Member_SavingsStatemrnt + accode);
//
////                } else{
////                    Toast.makeText(MemberSavingsAccountActivity.this, "Loan code not associated with this Executive or No data found", Toast.LENGTH_SHORT).show();
////                }
//            } catch(
//            Exception e)
//
//            {
//                e.printStackTrace();
//                Log.d("AccountError", e.toString());
//            }
//        }
//    });
//
//}
//
////    private boolean getMenuOptions(String memberCode) {
////        Connection cn = new SqlManager().getSQLConnection();
////        boolean rValue = false;
////        String ut = "";
////        AppData ad;
////        try {
////            if (cn != null) {
////                CallableStatement smt = cn.prepareCall("{call ADROID_GetMemberSavingsAccountCodeOrVirtualAccount(?)}");
////                smt.setString("@MemberCode", memberCode);
////                smt.execute();
////                ResultSet rs = smt.getResultSet();
////                /*String accountCode = smt.getString("AccountCode");
////                popupMenusSavingsAccount.getMenu().add(accountCode);*/
////                if (rs.isBeforeFirst()) {
////                    while (rs.next()) {
////                        savingsAccountFound = 1;
////                        // TODO this section is commented, because it depends on client whether he wants to show virtual account instead of savings account
////                        /*if(rs.getString("BANK_ACCOUNT_NUMBER") != null && rs.getString("BANK_ACCOUNT_NUMBER").trim().length() > 0){
////                            mTv_accCode.setText(rs.getString("BANK_ACCOUNT_NUMBER"));
////                            SelectedAccount.selectedVirtualAccount = rs.getString("BANK_ACCOUNT_NUMBER");
////                        } else {*/
////                            mTv_accCode.setText(rs.getString("AccountCode"));
////                        //}
//    //   getSavingsAccountSummary(rs.getString("AccountCode"), GlobalStore.GlobalValue.getMemberCode());
////                        SelectedAccount.savingsCode = rs.getString("AccountCode");
////                        mLl_infoRoot.setVisibility(View.VISIBLE);
////                        rValue = true;
////                    }
////                } else {
////                    savingsAccountFound = 0;
////                    Toast.makeText(this, "No savings account found", Toast.LENGTH_SHORT).show();
////                }
////            }
////        } catch (Exception ex) {
////            rValue = false;
////        } finally {
////            if (cn != null) {
////                try {
////                    cn.close();
////                } catch (Exception e) {
////                    //
////                }
////            }
////        }
////        return rValue;
////    }
//
//    private void setViewReferences() {
//        mToolbar = findViewById(R.id.custom_toolbar);
//        mTv_toolbarTitle = findViewById(R.id.toolbar_title);
//
//        // views
//        mTv_memberName = findViewById(R.id.tv_activity_member_savings_account_member_name);
//        mTv_currentbalance = findViewById(R.id.tv_activity_member_savings_account_member_current_balance);
//        mTv_memberCode = findViewById(R.id.tv_activity_member_savings_account_member_code);
//        mTv_openingDate = findViewById(R.id.tv_activity_member_savings_account_opening_date);
//
//        mTv_selectAccount = findViewById(R.id.tv_activity_member_savings_account_select_account_dropdown);
//
//        mBtn_savingsAccountViewStatement = findViewById(R.id.btn_activity_customer_savings_account_view_statement);
//
//        mLl_infoRoot = findViewById(R.id.ll_activity__customer_savings_account_account_info_root);
//        mBtn_downSbSt = findViewById(R.id.btn_mem_sb_statement_down);
//
//        mTv_accCode = findViewById(R.id.tvAccCode);
//        mRv_investmentAccountReport = findViewById(R.id.tv_savings_Stetement);
//    }
//
//    private void bindEventHandlers() {
//        mTv_selectAccount.setOnClickListener(this);
//        mBtn_savingsAccountViewStatement.setOnClickListener(this);
//        mBtn_downSbSt.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v == mTv_selectAccount) {
//            if (savingsAccountFound == 1) {
//                popupMenusSavingsAccount.show();
//            } else {
//                Toast.makeText(MemberSavingsAccountActivity.this, "No savings account found", Toast.LENGTH_SHORT).show();
//            }
//        } else if (v == mBtn_savingsAccountViewStatement) {
//            startActivity(new Intent(MemberSavingsAccountActivity.this, MemberSavingsAccountStatementActivity.class));
//            finish();
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//        } else if (v == mBtn_downSbSt) {
//            startActivity(new Intent(MemberSavingsAccountActivity.this, MemberSBStatementPDFActivity.class));
//            finish();
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//        }
//    }
//
////    private boolean getSavingsAccountSummary(String accountCode, String membercode) {
////        Connection cn = new SqlManager().getSQLConnection();
////        boolean rValue = false;
////        String ut = "";
////        AppData ad;
////        try {
////            if (cn != null) {
////                CallableStatement smt = cn.prepareCall("{call ADROID_GetSavingsAccountSummaryView(?,?)}");
////                smt.setString("@SBAccount", accountCode);
////                smt.setString("@UserCode", membercode);
////                smt.execute();
////                ResultSet rs = smt.getResultSet();
////                while (rs.next()) {
////                    //ad = new AppData();
////                    mTv_memberCode.setText(rs.getString("FirstApplicantMemberCode"));
////                    mTv_memberName.setText(rs.getString("FirstApplicantName"));
////                    mTv_openingDate.setText(rs.getString("AccountOpeningDate"));
////                    rs.getString("AccountAccessType");
////                    mTv_currentbalance.setText(String.valueOf(rs.getFloat("CurrantBalance")));
////                    //GlobalStore.GlobalValue = ad;
////                    rValue = true;
////                }
////            }
////        } catch (Exception ex) {
////            rValue = false;
////        } finally {
////            if (cn != null) {
////                try {
////                    cn.close();
////                } catch (Exception e) {
////                    //
////                }
////            }
////        }
////        return rValue;
////    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                // todo: goto back activity from here
//                Intent intent = new Intent(MemberSavingsAccountActivity.this, MemberDashboardActivity.class);
//                startActivity(intent);
//                finish();
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        //super.onBackPressed();
//        startActivity(new Intent(MemberSavingsAccountActivity.this, MemberDashboardActivity.class));
//        finish();
//        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//    }
//}
