package com.example.geniousmicro.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geniousmicro.Adapter.AdapterPolicyDueReport;
import com.example.geniousmicro.MainActivity;
import com.example.geniousmicro.R;
import com.example.geniousmicro.UserData.GlobalUserData;
import com.example.geniousmicro.mssql.SqlManager;

import com.example.geniousmicro.mssql.model.SetGetPolicyDueReport;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class AgentPolicyDueReportDaywise extends AppCompatActivity implements View.OnClickListener {
    // toolbar
    private Toolbar mToolbar;
    private TextView mToolbarTitle;

    private TextView mTv_fDate;
    private TextView mTv_tDate;
    private EditText mEt_loanCode;
    private Button mBtn_show;

    private ProgressBar mPb_proggress;

    private EditText mEt_enterOfficeId;
    private Button mBtn_showAll;
    ProgressDialog dialog;

    TextInputLayout ip_ed_lay;
    TextInputEditText ed_search;

    // vars
    private int fdate = 0;
    private int tdate = 0;
    int mYear, mMonth, mDay;
    private int currentDay, currentMonth, currentYear;
    private Calendar mCalendar;

    private Connection cn;

    private ArrayList<SetGetPolicyDueReport> mArrayListDueReport;
    private RecyclerView mRv_loanDueReport;
    private AdapterPolicyDueReport adapterDueReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agent_policy_due_report_daywise);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.policydue), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/


        setViewReferences();
        bindEventHandlers();

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mToolbarTitle.setText("Today Policy Collection");

        dialog = new ProgressDialog(AgentPolicyDueReportDaywise.this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);

        mArrayListDueReport = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRv_loanDueReport.setLayoutManager(linearLayoutManager);

        getCurrentdaydueList();

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    setSearchedLoanData(s.toString());
                }
            }
        });
    }

    private void getCurrentdaydueList() {
        dialog.show();
        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }

        // Format as dd-MM-yyyy
        DateTimeFormatter formatter1 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        }
        String formattedDate1 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedDate1 = currentDate.format(formatter1);
        }
//        System.out.println("Current date in dd-MM-yyyy format: " + formattedDate1);
        mTv_tDate.setText(formattedDate1);
        mTv_fDate.setText(formattedDate1);


        DateTimeFormatter formatter2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter2 = DateTimeFormatter.ofPattern("yyyyMMdd");
        }
        String formattedDate2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedDate2 = currentDate.format(formatter2);
        }
        System.out.println("Current date in yyyyMMdd format: " + formattedDate2);

        fdate = Integer.parseInt(formattedDate2);
        tdate = Integer.parseInt(formattedDate2);

        mArrayListDueReport.clear();
        getLoanDueReport(fdate, tdate, mEt_loanCode.getText().toString());
        dialog.dismiss();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setSearchedLoanData(String s) {
        ArrayList<SetGetPolicyDueReport> mTempArrayListDueReport = new ArrayList<>();
        for (SetGetPolicyDueReport setGetLoanDueReport : mArrayListDueReport) {
            if (//setGetLoanDueReport.getLoanCode().toLowerCase().contains(s.toLowerCase()) ||
            setGetLoanDueReport.getApplicantName().toLowerCase().substring(0, s.length()).equals(s.toLowerCase())) {
                mTempArrayListDueReport.add(setGetLoanDueReport);
            }
        }
        if (mTempArrayListDueReport.size() > 0) {
            adapterDueReport = new AdapterPolicyDueReport(AgentPolicyDueReportDaywise.this, mTempArrayListDueReport);
            mRv_loanDueReport.setAdapter(adapterDueReport);
            adapterDueReport.notifyDataSetChanged();
        } else {
            mTempArrayListDueReport.clear();
            adapterDueReport = new AdapterPolicyDueReport(AgentPolicyDueReportDaywise.this, mTempArrayListDueReport);
            mRv_loanDueReport.setAdapter(adapterDueReport);
            adapterDueReport.notifyDataSetChanged();
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_title);

        mTv_fDate = findViewById(R.id.tv_activity_agent_loan_due_report_fdate);
        mTv_tDate = findViewById(R.id.tv_activity_agent_loan_due_report_tdate);
        mEt_loanCode = findViewById(R.id.ev_activity_agent_loan_due_report_enter_loan_code);
        mBtn_show = findViewById(R.id.btn_activity_agent_loan_due_report_show);
        mRv_loanDueReport = findViewById(R.id.rv_activity_loan_due_report);

        mPb_proggress = findViewById(R.id.pb_activity_agent_loan_due_report);
        ed_search = findViewById(R.id.ed_search);
        ip_ed_lay = findViewById(R.id.ip_ed_lay);

        //mEt_enterOfficeId = findViewById(R.id.et_activity_agent_loan_due_report_enter_office_id);
        mBtn_showAll = findViewById(R.id.btn_activity_agent_loan_due_report_show_all);
    }

    private void bindEventHandlers() {
        mTv_fDate.setOnClickListener(this);
        mTv_tDate.setOnClickListener(this);
        mBtn_show.setOnClickListener(this);
        mBtn_showAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mTv_fDate) {
            selectFDate();
        } else if (v == mTv_tDate) {
            selectTDate();
        } else if (v == mBtn_show) {
            if (mTv_fDate.getText().toString().trim().length() > 0) {
                if (mTv_tDate.getText().toString().trim().length() > 0) {
                    dialog.show();
                    mArrayListDueReport.clear();
                    getLoanDueReport(fdate, tdate, mEt_loanCode.getText().toString());
                    dialog.dismiss();
                } else {
                    mTv_tDate.performClick();
                    Toast.makeText(this, "Select tDate", Toast.LENGTH_SHORT).show();
                }
            } else {
                mTv_fDate.performClick();
                Toast.makeText(this, "Select fDate", Toast.LENGTH_SHORT).show();
            }

        } /*else if (v == mBtn_showAll) {
            if (mTv_fDate.getText().toString().trim().length() > 0) {
                if (mTv_tDate.getText().toString().trim().length() > 0) {
                    mArrayListDueReport.clear();
                    showAllData(fdate, tdate, GlobalStore.GlobalValue.getOfficeID());
                } else {
                    mTv_tDate.performClick();
                    Toast.makeText(this, "Select tDate", Toast.LENGTH_SHORT).show();
                }
            } else {
                mTv_fDate.performClick();
                Toast.makeText(this, "Select fDate", Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    private void showAllData(int fDate, int tDate, String officeId) {
        cn = new SqlManager().getSQLConnection();
        SetGetPolicyDueReport setGetPolicyDueReport = null;
        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GetLoanDueReportByOfficeId(?,?,?)}");
                smt.setInt("@Fdate", fDate);
                smt.setInt("@Tdate", tDate);
                smt.setString("@OfficeID", officeId);
                smt.execute();
                ResultSet rs = smt.getResultSet();
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        setGetPolicyDueReport = new SetGetPolicyDueReport();
                        setGetPolicyDueReport.setLoanCode(rs.getString("LoanCode"));
                        setGetPolicyDueReport.setApplicantName(rs.getString("ApplicantName"));
                        setGetPolicyDueReport.setPhNo(rs.getString("Loandate"));
                        setGetPolicyDueReport.setTotalDueEmiNo(rs.getString("TotalDueEMINo"));
                        setGetPolicyDueReport.setTotalDueEmiAmnt(rs.getString("TotalDueEMIAmount"));
                        mArrayListDueReport.add(setGetPolicyDueReport);
                    }
                    adapterDueReport = new AdapterPolicyDueReport(AgentPolicyDueReportDaywise.this, mArrayListDueReport);
                    mRv_loanDueReport.setAdapter(adapterDueReport);
                } else {
                    Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
        }
    }


    public void getLoanDueReport(int fDate, int tDate, String memberCode) {
        mPb_proggress.setVisibility(View.VISIBLE);
        Connection cn = new SqlManager().getSQLConnection();
        mArrayListDueReport.clear();
        SetGetPolicyDueReport setGetPolicyDueReport = null;

        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_PolicyRenewalDueList(?,?,?)}");
                smt.setString("@Acode", GlobalUserData.employeeDataModel.getEmployeeID());
                smt.setInt("@Fdate", fDate);
                smt.setInt("@Tdate", tDate);

                // smt.setString("@arrCode", GlobalStore.GlobalValue.getUserName());
                //smt.setString("@MemberCode", memberCode);
                smt.executeQuery();
                ResultSet rs = smt.getResultSet();
                Log.e("rsss", "" + rs);
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        setGetPolicyDueReport = new SetGetPolicyDueReport();
                        setGetPolicyDueReport.setLoanCode(rs.getString("PolicyCode"));
                        setGetPolicyDueReport.setApplicantName(rs.getString("ApplicantName"));
                        setGetPolicyDueReport.setPhNo(rs.getString("dateofcom"));
                        setGetPolicyDueReport.setTotalDueEmiNo(rs.getString("INST"));
                        setGetPolicyDueReport.setTotalDueEmiAmnt(rs.getString("Amount"));
                        setGetPolicyDueReport.setNoofPendingRenewal(rs.getString("NoOfPendingRenewal"));
                        mArrayListDueReport.add(setGetPolicyDueReport);
                    }
                    adapterDueReport = new AdapterPolicyDueReport(AgentPolicyDueReportDaywise.this, mArrayListDueReport);
                    mRv_loanDueReport.setAdapter(adapterDueReport);
                    mPb_proggress.setVisibility(View.GONE);
                    ip_ed_lay.setVisibility(View.VISIBLE);
                } else {
                    ip_ed_lay.setVisibility(View.GONE);
                    mPb_proggress.setVisibility(View.GONE);
                    Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
                }
            } else {
                mPb_proggress.setVisibility(View.GONE);
                Log.e("cn", "" + cn);
                Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Log.e("Exception", "" + ex);
            mPb_proggress.setVisibility(View.GONE);
            Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
        }
    }


    private void selectFDate() {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Calendar
        mCalendar = Calendar.getInstance();
        currentDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = mCalendar.get(Calendar.MONTH);
        currentYear = mCalendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AgentPolicyDueReportDaywise.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;

                mTv_fDate.setText(dayOfMonth + "-" + month + "-" + year);
                fdate = Integer.parseInt(Integer.toString(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth));
            }
        }, currentYear, currentMonth, currentDay);
        mCalendar.set(currentYear, currentMonth, currentDay);

        datePickerDialog.getDatePicker().setMaxDate(mCalendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void selectTDate() {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Calendar
        mCalendar = Calendar.getInstance();
        currentDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = mCalendar.get(Calendar.MONTH);
        currentYear = mCalendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AgentPolicyDueReportDaywise.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;

                mTv_tDate.setText(dayOfMonth + "-" + month + "-" + year);
                tdate = Integer.parseInt(Integer.toString(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth));
            }
        }, currentYear, currentMonth, currentDay);
        mCalendar.set(currentYear, currentMonth, currentDay);

        datePickerDialog.getDatePicker().setMaxDate(mCalendar.getTimeInMillis());
        datePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                startActivity(new Intent(AgentPolicyDueReportDaywise.this, MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        startActivity(new Intent(AgentPolicyDueReportDaywise.this, MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}

