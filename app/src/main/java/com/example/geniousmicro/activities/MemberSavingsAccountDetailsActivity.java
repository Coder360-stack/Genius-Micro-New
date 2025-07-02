package com.example.geniousmicro.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.geniousmicro.Others.SelectedAccount;

import com.example.geniousmicro.R;
import com.example.geniousmicro.UserData.GlobalUserData;
import com.example.geniousmicro.mssql.SqlManager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class MemberSavingsAccountDetailsActivity extends AppCompatActivity implements View.OnClickListener {


    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    private TextView mTv_memberName;
    private TextView mTv_currentbalance;
    private TextView mTv_memberCode;
    private TextView mTv_openingDate;

    private TextView mTv_selectAccount;

    private Button mBtn_savingsAccountViewStatement,mBtn_downSbSt;

    private PopupMenu popupMenusSavingsAccount;

    private LinearLayout mLl_infoRoot;

    private static int savingsAccountFound = 0;

    private TextView mTv_accCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_savings_account_details);

        setViewReferences();
        bindEventHandlers();

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mTv_toolbarTitle.setText("Savings Account");

        popupMenusSavingsAccount = new PopupMenu(this, mTv_selectAccount);

        getMenuOptions(GlobalUserData.memberDataModel.getMemberID());
        popupMenusSavingsAccount.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mTv_selectAccount.setText(item.getTitle());
                getSavingsAccountSummary((String) item.getTitle(), GlobalUserData.memberDataModel.getMemberID());
                SelectedAccount.savingsCode = (String) item.getTitle();
                mLl_infoRoot.setVisibility(View.VISIBLE);

                return true;
            }
        });

    }

    private boolean getMenuOptions(String memberCode) {
        Connection cn = new SqlManager().getSQLConnection();
        boolean rValue = false;
        String ut = "";
        //  AppData ad;
        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GetMemberSavingsAccountBalance(?)}");
                smt.setString("@MemberCode", memberCode);
                smt.execute();
                ResultSet rs = smt.getResultSet();
                /*String accountCode = smt.getString("AccountCode");
                popupMenusSavingsAccount.getMenu().add(accountCode);*/
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        savingsAccountFound = 1;
                        // TODO this section is commented, because it depends on client whether he wants to show virtual account instead of savings account
                        /*if(rs.getString("BANK_ACCOUNT_NUMBER") != null && rs.getString("BANK_ACCOUNT_NUMBER").trim().length() > 0){
                            mTv_accCode.setText(rs.getString("BANK_ACCOUNT_NUMBER"));
                            SelectedAccount.selectedVirtualAccount = rs.getString("BANK_ACCOUNT_NUMBER");
                        } else {*/
                        mTv_accCode.setText(rs.getString("AccountCode"));
                        //}
                        getSavingsAccountSummary(rs.getString("AccountCode"), GlobalUserData.memberDataModel.getMemberID());
                        SelectedAccount.savingsCode = rs.getString("AccountCode");
                        mLl_infoRoot.setVisibility(View.VISIBLE);
                        rValue = true;
                    }
                } else {
                    savingsAccountFound = 0;
                    Toast.makeText(this, "No savings account found", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception ex) {
            rValue = false;
        } finally {
            if (cn != null) {
                try {
                    cn.close();
                } catch (Exception e) {
                    //
                }
            }
        }
        return rValue;
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        // views
        mTv_memberName = findViewById(R.id.tv_activity_member_savings_account_member_name);
        mTv_currentbalance = findViewById(R.id.tv_activity_member_savings_account_member_current_balance);
        mTv_memberCode = findViewById(R.id.tv_activity_member_savings_account_member_code);
        mTv_openingDate = findViewById(R.id.tv_activity_member_savings_account_opening_date);

        mTv_selectAccount = findViewById(R.id.tv_activity_member_savings_account_select_account_dropdown);

        mBtn_savingsAccountViewStatement = findViewById(R.id.btn_activity_customer_savings_account_view_statement);

        mLl_infoRoot = findViewById(R.id.ll_activity__customer_savings_account_account_info_root);
        mBtn_downSbSt = findViewById(R.id.btn_mem_sb_statement_down);

        mTv_accCode = findViewById(R.id.tvAccCode);
    }

    private void bindEventHandlers() {
        mTv_selectAccount.setOnClickListener(this);
        mBtn_savingsAccountViewStatement.setOnClickListener(this);
        mBtn_downSbSt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mTv_selectAccount) {
            if (savingsAccountFound == 1) {
                popupMenusSavingsAccount.show();
            } else {
                Toast.makeText(MemberSavingsAccountDetailsActivity.this, "No savings account found", Toast.LENGTH_SHORT).show();
            }
        } else if (v == mBtn_savingsAccountViewStatement) {
            startActivity(new Intent(MemberSavingsAccountDetailsActivity.this, SavingsStatementActivity.class));
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }else if(v==mBtn_downSbSt){
            /*startActivity(new Intent(MemberSavingsAccountDetailsActivity.this, MemberSBStatementPDFActivity.class));
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);*/
            startActivity(new Intent(MemberSavingsAccountDetailsActivity.this, SavingsStatementActivity.class));
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    private boolean getSavingsAccountSummary(String accountCode, String membercode) {
        Connection cn = new SqlManager().getSQLConnection();
        boolean rValue = false;
        String ut = "";
        //AppData ad;
        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GetSavingsAccountSummaryView(?,?)}");
                smt.setString("@SBAccount", accountCode);
                smt.setString("@UserCode", membercode);
                smt.execute();
                ResultSet rs = smt.getResultSet();
                while (rs.next()) {
                    //ad = new AppData();
                    mTv_memberCode.setText(rs.getString("FirstApplicantMemberCode"));
                    mTv_memberName.setText(rs.getString("FirstApplicantName"));
                    mTv_openingDate.setText(rs.getString("AccountOpeningDate"));
                    rs.getString("AccountAccessType");
                    mTv_currentbalance.setText(String.valueOf(rs.getFloat("CurrantBalance")));
                    //GlobalStore.GlobalValue = ad;
                    rValue = true;
                }
            }
        } catch (Exception ex) {
            rValue = false;
        } finally {
            if (cn != null) {
                try {
                    cn.close();
                } catch (Exception e) {
                    //
                }
            }
        }
        return rValue;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(MemberSavingsAccountDetailsActivity.this, MemberDashboardActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(MemberSavingsAccountDetailsActivity.this, MemberDashboardActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
