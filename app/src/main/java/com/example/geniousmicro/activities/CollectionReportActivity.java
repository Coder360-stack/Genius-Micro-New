package com.example.geniousmicro.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geniousmicro.R;
/*import com.example.geniousmicro.bl.DialogUtils;
import com.example.geniousmicro.dl.PolicyManagement;
import com.example.geniousmicro.model.CollectionReportSetGet;
import com.example.geniousmicro.others.APILinks;
import com.example.geniousmicro.parsers.GetDataParserArray;
import com.example.geniousmicro.store.GlobalStore;*/

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CollectionReportActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_setDate, tv_setName, tv_rd_account, tv_loan_account, tv_sb_account, tv_setCollectorName,
            tv_rdTotal, tv_loanTotal, tv_sbTotal, tv_collectionSaveButton, tv_rdAcNumber, tv_loanAcNumber,
            tv_sbAcNumber, tv_totalCollectedAmount;

    private ImageView iv_rdClick, iv_rightClick1, iv_rightClick2, iv_rightClick3;
    private EditText et_searchMenu, mEt_memberCode, et_rdAmountEntry, et_loanAmountEntry, et_sbAmountEntry,
    et_rdAmount, et_loanAmount, et_sbAmount, et_policyRemarks, et_loanRemarks;
    private Button btn_searchButton;
    LinearLayout ll_rd_account, ll_loan_account, ll_sb_account;
    private Button btn_rdAC, btn_loanAC, btn_sbAC, btn_saveDoc, btn_print, btn_save;

    private Spinner mSp_policyNameCodeList, mSp_loanNameCodeList, mSp_sbNameCodeList, sp_amountTotal;
   // private CollectionReportSetGet collectionReportSetGet;
    private String str_memberSearch, str_arrangerCode = "", str_arrangerPassword = "";
    SharedPreferences sharedPreferences_arranger;
    String rdCode, loanCode, sbCode;
    String date;
    BluetoothAdapter bluetoothAdapter;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioCode, mRadioName;
    private String mStr_type = "";
    private ArrayList<String> arrayList_policyCodeName =new ArrayList<>();
    private ArrayList<String> arrayList_policyCode = new ArrayList<>();
    private ArrayList<String> arrayList_loanCode = new ArrayList<>();
    private ArrayList<String> arrayList_loanCodeName = new ArrayList<>();
    private ArrayList<String> arrayList_sbCode = new ArrayList<>();
    private ArrayList<String> arrayList_sbCodeName = new ArrayList<>();
    private int insertErrorCode = 1, count = 0;
    private float totalCollectedAmount = 0;
    boolean isSuccess = false;
    private ArrayList<String> collectArrayList = new ArrayList<>();
    private String allCollect_code_name_amountList = "";
    private String codeNameAmount = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_report);

       // setViewReferences();
       // bindEventHandlers();

        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
      //  tv_setDate.setText(date);

        sharedPreferences_arranger = getApplicationContext().getSharedPreferences("ARRANGERLOGIN", Context.MODE_PRIVATE);
        str_arrangerCode = sharedPreferences_arranger.getString("USERNAME", "");
//        str_arrangerPassword = sharedPreferences_arranger.getString("PASSWORD", "");

//        Intent intent = getIntent();
//        String userName = intent.getStringExtra("USERNAME");
       /* tv_setCollectorName.setText(GlobalStore.GlobalValue.getUserOriginalName());
//        getRDAccountDetails(APILinks.GET_RD_AC_ACCOUNT_DETAILS + mEt_memberCode.getText().toString());
//        try {
//            getLoanAccountDetails(APILinks.GET_Loan_AC_ACCOUNT_DETAILS + mEt_memberCode.getText().toString());
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//        getSBAccountDetails(APILinks.GET_SB_AC_ACCOUNT_DETAILS + mEt_memberCode.getText().toString());

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mRadioCode.isChecked()) {
                    mStr_type = "MemberCode";
                }
                if (mRadioName.isChecked()) {
                    mStr_type = "MemberName";
                }
            }
        });

        mSp_policyNameCodeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    setValueinSpinner(arrayList_policyCode.get(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSp_loanNameCodeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    setValueinSpinner(arrayList_loanCode.get(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSp_sbNameCodeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    setValueinSpinner(arrayList_sbCode.get(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        takeCollection();
    }

    private void takeCollection() {

    }

    private void setValueinSpinner(String s) {
        Toast.makeText(this, "Value set", Toast.LENGTH_SHORT).show();
    }

    private void setViewReferences(){
//        iv_rdClick = findViewById(R.id.iv_rdClick);
//        mEt_memberCode = findViewById(R.id.et_searchMenu);
//        tv_rd_account = findViewById(R.id.tv_rd_account);
//        tv_loan_account = findViewById(R.id.tv_loan_account);
//        tv_sb_account = findViewById(R.id.tv_sb_account);
        tv_setDate = findViewById(R.id.tv_setDate);
        tv_setName = findViewById(R.id.tv_setName);
//        tv_rdAcNumber = findViewById(R.id.tv_rdAcNumber);
//        tv_loanAcNumber = findViewById(R.id.tv_loanAcNumber);
//        tv_sbAcNumber = findViewById(R.id.tv_sbAcNumber);
        ll_rd_account = findViewById(R.id.ll_rd_account);
        ll_loan_account = findViewById(R.id.ll_loan_account);
        ll_sb_account = findViewById(R.id.ll_sb_account);
        tv_setCollectorName = findViewById(R.id.tv_setCollectorName);
        tv_rdTotal = findViewById(R.id.tv_rdTotal);
        tv_loanTotal = findViewById(R.id.tv_loanTotal);
        tv_sbTotal = findViewById(R.id.tv_sbTotal);
        et_searchMenu = findViewById(R.id.et_searchMenu);
//        et_rdAmountEntry = findViewById(R.id.et_rdAmountEntry);
//        et_loanAmountEntry = findViewById(R.id.et_loanAmountEntry);
//        et_sbAmountEntry = findViewById(R.id.et_sbAmountEntry);
        btn_searchButton = findViewById(R.id.btn_searchButton);
//        btn_rdAC = findViewById(R.id.btn_rdAC);
//        btn_loanAC = findViewById(R.id.btn_loanAC);
//        btn_sbAC = findViewById(R.id.btn_sbAC);
//        btn_save = findViewById(R.id.btn_save);
        btn_saveDoc = findViewById(R.id.btn_saveDoc);
        btn_print = findViewById(R.id.btn_print);

        mRadioGroup = findViewById(R.id.radio_group_search);
        mRadioCode = findViewById(R.id.radio_activity_search_by_code);
        mRadioName = findViewById(R.id.radio_activity_search_by_name);
        tv_collectionSaveButton = findViewById(R.id.tv_collectionSaveButton);
        mSp_policyNameCodeList = findViewById(R.id.sp_activity_renewal_collection_policy_list);
        mSp_loanNameCodeList = findViewById(R.id.sp_activity_renewal_collection_loan_list);
        mSp_sbNameCodeList = findViewById(R.id.sp_activity_renewal_collection_sb_list);

        et_rdAmount = findViewById(R.id.et_rdAmount);
        et_loanAmount = findViewById(R.id.et_loanAmount);
        et_sbAmount = findViewById(R.id.et_sbAmount);

        iv_rightClick1 = findViewById(R.id.iv_rightClick1);
        iv_rightClick2 = findViewById(R.id.iv_rightClick2);
        iv_rightClick3 = findViewById(R.id.iv_rightClick3);

        sp_amountTotal = findViewById(R.id.sp_amountTotal);
        tv_totalCollectedAmount = findViewById(R.id.tv_totalCollectedAmount);
        et_policyRemarks = findViewById(R.id.et_policyRemarks);
        et_loanRemarks = findViewById(R.id.et_loanRemarks);
    }

    private void bindEventHandlers(){
//        iv_rdClick.setOnClickListener(this);
        btn_searchButton.setOnClickListener(this);
        ll_rd_account.setOnClickListener(this);
        ll_loan_account.setOnClickListener(this);
        ll_sb_account.setOnClickListener(this);
//        btn_save.setOnClickListener(this);
        btn_print.setOnClickListener(this);
        tv_collectionSaveButton.setOnClickListener(this);

        iv_rightClick1.setOnClickListener(this);
        iv_rightClick2.setOnClickListener(this);
        iv_rightClick3.setOnClickListener(this);
//        btn_rdAC.setOnClickListener(this);
//        btn_loanAC.setOnClickListener(this);
//        btn_sbAC.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
//        if(view == iv_searchButton){
//            if(et_searchMenu.getText().toString().length()>0){
//                str_memberCode = et_searchMenu.getText().toString().trim();
////                str_arrangerCode = GlobalStore.GlobalValue.getArrangerMemberCode();
//                getCollectionInformationDetails(str_memberCode);
//                setDateInViews();
//            }
//        }

//        if(view == iv_rdClick){
//            mSp_policyNameCodeList.setVisibility(View.VISIBLE);
//        }

        if(view == btn_searchButton) {

            str_memberSearch = et_searchMenu.getText().toString();

            if (mStr_type.length() > 0) {
                if (str_memberSearch.length() > 0) {
                    try {
                        getPolicyCodeName(APILinks.GEt_POLICY_BY_PCODE_NAME+str_memberSearch+"&searchTag="+mStr_type+"&arrCode="+ GlobalStore.GlobalValue.getUserName());
                        getLoanCodeName(APILinks.GEt_LOAN_BY_LCODE_NAME+str_memberSearch+"&searchTag="+mStr_type+"&arrCode="+ GlobalStore.GlobalValue.getUserName());

//                        getRDAccountDetails(APILinks.GET_RD_ACCOUNT_DETAILS + "&searchValue=" + str_memberSearch + "&searchType=" + mStr_type + "&ArrangerCode=" + GlobalStore.GlobalValue.getUserName());
//                        getLoanAccountDetails(APILinks.GET_LOAN_ACCOUNT_DETAILS + "&searchValue=" + str_memberSearch + "&searchType=" + mStr_type + "&ArrangerCode=" + GlobalStore.GlobalValue.getUserName());
                        getSBAccountDetails(APILinks.GET_SB_ACCOUNT_DETAILS + "&searchValue=" + str_memberSearch + "&searchType=" +mStr_type + "&ArrangerCode=" + GlobalStore.GlobalValue.getUserName());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    Toast.makeText(this, "Enter Name or Member Code", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Select Type", Toast.LENGTH_SHORT).show();
            }




//            try {
//                getRDAccountDetails(APILinks.GET_RD_ACCOUNT_DETAILS + mStr_type + "&value=" + str_memberCode);
//                getLoanAccountDetails(APILinks.GET_LOAN_ACCOUNT_DETAILS + mStr_type + "&value=" + str_memberCode);
//                getSBAccountDetails(APILinks.GET_SB_ACCOUNT_DETAILS + mStr_type + "&value=" + str_memberCode);
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }

//            getCollectionInformationDetails(str_memberCode);
//            setDateInViews();
        } else if (view == ll_rd_account) {
            Intent intent = new Intent(CollectionReportActivity.this, ArrRenewalCollectionManualActivity.class);
            intent.putExtra("RDCode", rdCode);
            startActivity(intent);

        } else if (view == ll_loan_account) {
            Intent intent = new Intent(CollectionReportActivity.this, ArrLoanCollectionManualActivity.class);
            intent.putExtra("LoanCode", loanCode);
            startActivity(intent);

        } else if (view == ll_sb_account) {
            Intent intent = new Intent(CollectionReportActivity.this, ArrangerSBCollectionActivity.class);
            intent.putExtra("SBCode", sbCode);
            startActivity(intent);

        } else if (view == btn_print) {
            Intent intent = new Intent(CollectionReportActivity.this, CollectionPrintActivity.class);
            startActivity(intent);

        } else if (view == iv_rightClick1) {
            count++;

            if(mSp_policyNameCodeList.getSelectedItem() == null){
                Toast.makeText(this, "No Policy Account Found", Toast.LENGTH_SHORT).show();
            }

            else{
                String polCodeName = mSp_policyNameCodeList.getSelectedItem().toString();
                String rdAmt = et_rdAmount.getText().toString();
                float rd_Amt = Float.parseFloat(rdAmt);
                iv_rightClick1.setImageResource(R.drawable.check);
                String policyRemarks = et_policyRemarks.getText().toString();

                insertAllPolicyCollection(polCodeName, rd_Amt, policyRemarks);

                totalCollectedAmount = totalCollectedAmount + rd_Amt;
                String result_policy = polCodeName +"-"+rdAmt;
                collectArrayList.add(result_policy);
//            allCollect_code_name_amountList = result_policy + "";
//            insertAllCollection(result_policy);
                saved();
            }
//            String polCodeName = mSp_policyNameCodeList.getSelectedItem().toString();
//            String rdAmt = et_rdAmount.getText().toString();
//            float rd_Amt = Float.parseFloat(rdAmt);
//            iv_rightClick1.setImageResource(R.drawable.check);
//            String policyRemarks = et_policyRemarks.getText().toString();
//
//            insertAllPolicyCollection(polCodeName, rd_Amt, policyRemarks);
//
//            totalCollectedAmount = totalCollectedAmount + rd_Amt;
//            String result_policy = polCodeName +"-"+rdAmt;
//            collectArrayList.add(result_policy);
////            allCollect_code_name_amountList = result_policy + "";
////            insertAllCollection(result_policy);
//            saved();

        } else if (view == iv_rightClick2) {
            count++;

            if(mSp_loanNameCodeList.getSelectedItem() == null){
                Toast.makeText(this, "No Loan Account Found", Toast.LENGTH_SHORT).show();
            }

            else{
                String loanCodeName = mSp_loanNameCodeList.getSelectedItem().toString();
                String loanAmt = et_loanAmount.getText().toString();
                float loan_Amt = Integer.parseInt(loanAmt);
                iv_rightClick2.setImageResource(R.drawable.check);
                String loanRemarks = et_loanRemarks.getText().toString();

                insertAllLoanCollection(loanCodeName, loan_Amt, loanRemarks);

                totalCollectedAmount = totalCollectedAmount + loan_Amt;
                String result_loan = loanCodeName +"-"+loanAmt;
                collectArrayList.add(result_loan);


//            allCollect_code_name_amountList = result_loan + "";
//            insertAllCollection(result_loan);
//            Log.d("result_loan", result_loan);
                saved();
            }
//            String loanCodeName = mSp_loanNameCodeList.getSelectedItem().toString();
//            String loanAmt = et_loanAmount.getText().toString();
//            float loan_Amt = Integer.parseInt(loanAmt);
//            iv_rightClick2.setImageResource(R.drawable.check);
//            String loanRemarks = et_loanRemarks.getText().toString();
//
//            insertAllLoanCollection(loanCodeName, loan_Amt, loanRemarks);
//
//            totalCollectedAmount = totalCollectedAmount + loan_Amt;
//            String result_loan = loanCodeName +"-"+loanAmt;
//            collectArrayList.add(result_loan);
//
//
////            allCollect_code_name_amountList = result_loan + "";
////            insertAllCollection(result_loan);
////            Log.d("result_loan", result_loan);
//            saved();


        }  else if (view == iv_rightClick3) {
            count++;

            if(mSp_sbNameCodeList.getSelectedItem() == null){
                Toast.makeText(this, "No SB Account Found", Toast.LENGTH_SHORT).show();
            }

            else{
                String sbCodeName = mSp_sbNameCodeList.getSelectedItem().toString();
                String sbAmt = et_sbAmount.getText().toString();
                float sb_Amt = Integer.parseInt(sbAmt);
                iv_rightClick3.setImageResource(R.drawable.check);

                insertAllSBCollection(sbCodeName, sb_Amt);

                totalCollectedAmount = totalCollectedAmount + sb_Amt;
                String result_sb = sbCodeName +"-"+sbAmt;
                collectArrayList.add(result_sb);
//            allCollect_code_name_amountList = result_sb + "";
//            insertAllCollection(result_sb);

                saved();
            }
//            String sbCodeName = mSp_sbNameCodeList.getSelectedItem().toString();
//            String sbAmt = et_sbAmount.getText().toString();
//            float sb_Amt = Integer.parseInt(sbAmt);
//            iv_rightClick3.setImageResource(R.drawable.check);
//
//            insertAllSBCollection(sbCodeName, sb_Amt);
//
//            totalCollectedAmount = totalCollectedAmount + sb_Amt;
//            String result_sb = sbCodeName +"-"+sbAmt;
//            collectArrayList.add(result_sb);
////            allCollect_code_name_amountList = result_sb + "";
////            insertAllCollection(result_sb);
//
//            saved();


        }
//        else if (view == tv_collectionSaveButton) {
//            String rdAmt = et_rdAmount.getText().toString();
//            String loanAmt = et_loanAmount.getText().toString();
//            String sbAmt = et_sbAmount.getText().toString();
//            int rd_Amt = Integer.parseInt(rdAmt);
//            int loan_Amt = Integer.parseInt(loanAmt);
//            int sb_Amt = Integer.parseInt(sbAmt);
//
//            String polCodeName = mSp_policyNameCodeList.getSelectedItem().toString();
//            String loanCodeName = mSp_loanNameCodeList.getSelectedItem().toString();
//            String sbCodeName = mSp_sbNameCodeList.getSelectedItem().toString();


//            Toast.makeText(this, allCollect_code_name_amountList, Toast.LENGTH_SHORT).show();
//            Intent i = new Intent(CollectionReportActivity.this, AllStringActivity.class);
//            i.putExtra("All",codeNameAmount);
//            startActivity(i);
//            Log.d("All", allCollect_code_name_amountList);
//            insertAllCollection(codeNameAmount);
//        }
    }

        private void saved() {
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(CollectionReportActivity.this,R.layout.spinner_hint, collectArrayList);
        sp_amountTotal.setAdapter(arrayAdapter);
        if(count > 0){
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    iv_rightClick1.setImageResource(R.drawable.background_white);
                    iv_rightClick2.setImageResource(R.drawable.background_white);
                    iv_rightClick3.setImageResource(R.drawable.background_white);

                    tv_totalCollectedAmount.setText(String.valueOf(totalCollectedAmount));
                }
            },2000);

        }

        String codeNameAmount = "";
        for(int index=0; index<collectArrayList.size(); index++){
            codeNameAmount = collectArrayList.get(index) + "";

        }

//        codeNameAmount = (String) sp_amountTotal.getItemAtPosition((Integer) sp_amountTotal.getSelectedItem());
        Log.d("All", codeNameAmount);
    }

    private void insertAllPolicyCollection(String polCodeName, float rd_Amt, String policyRemarks){
        final ProgressDialog dialog = DialogUtils.showProgressDialog(CollectionReportActivity.this, "Loading..");
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        PolicyManagement policyManagement = new PolicyManagement();
                        try{
                            insertErrorCode = policyManagement.insertRenewalCollection(polCodeName, rd_Amt, policyRemarks);
                            if(insertErrorCode == 0){
                                isSuccess = false;
                                et_policyRemarks.setText("");
                                Toast.makeText(CollectionReportActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                            }else if (insertErrorCode == 50005) {
                                Toast.makeText(CollectionReportActivity.this, "Save Failed", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(CollectionReportActivity.this, RenewalCollectionActivity.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            } else {
                                Toast.makeText(CollectionReportActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
        }, 3000);

    }

    private void insertAllLoanCollection(String loanCodeName, float loan_Amt, String loanRemarks){
        final ProgressDialog dialog = DialogUtils.showProgressDialog(CollectionReportActivity.this, "Loading..");
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        PolicyManagement policyManagement = new PolicyManagement();
                        try{
                            insertErrorCode = policyManagement.insertLoanCollection(loanCodeName, loan_Amt, loanRemarks);
                            if(insertErrorCode == 0){
                                isSuccess = false;
                                et_loanRemarks.setText("");
                                Toast.makeText(CollectionReportActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                            }else if (insertErrorCode == 50005) {
                                Toast.makeText(CollectionReportActivity.this, "Save Failed", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(CollectionReportActivity.this, RenewalCollectionActivity.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            } else {
                                Toast.makeText(CollectionReportActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                }, 3000);

    }

    private void insertAllSBCollection(String sbCodeName, float sb_Amt){
        final ProgressDialog dialog = DialogUtils.showProgressDialog(CollectionReportActivity.this, "Loading..");
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        PolicyManagement policyManagement = new PolicyManagement();
                        try{
                            insertErrorCode = policyManagement.insertSBCollection(sbCodeName, sb_Amt);
                            if(insertErrorCode == 0){
                                isSuccess = false;

                                Toast.makeText(CollectionReportActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                            }else if (insertErrorCode == 50005) {
                                Toast.makeText(CollectionReportActivity.this, "Save Failed", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(CollectionReportActivity.this, RenewalCollectionActivity.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            } else {
                                Toast.makeText(CollectionReportActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                }, 3000);

    }

    private void getPolicyCodeName(String url){
        arrayList_policyCodeName.clear();
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        for(int i=0;i<response.length();i++) {
                            JSONObject jsonObject=response.getJSONObject(i);
                            tv_setName.setText(jsonObject.getString("MemberName"));
                            arrayList_policyCode.add(jsonObject.getString("PolicyCode"));
                            arrayList_policyCodeName.add(jsonObject.getString("PolicyCode")+"-"+jsonObject.getString("ApplicantName"));
                        }
                        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(CollectionReportActivity.this,R.layout.spinner_hint, arrayList_policyCodeName);
                        mSp_policyNameCodeList.setAdapter(arrayAdapter);

                    }else{
                        Toast.makeText(CollectionReportActivity.this, "Policy code not associated with this Executive or NoData", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();}
            }
        });
    }

    private void getLoanCodeName(String url){
        arrayList_loanCodeName.clear();

        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        for(int i=0;i<response.length();i++) {
                            JSONObject jsonObject=response.getJSONObject(i);
                            tv_setName.setText(jsonObject.getString("MemberName"));
                            arrayList_loanCode.add(jsonObject.getString("LCode"));
                            arrayList_loanCodeName.add(jsonObject.getString("LCode")+"-"+jsonObject.getString("Name"));
                        }
                        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(CollectionReportActivity.this,R.layout.spinner_hint, arrayList_loanCodeName);
                        mSp_loanNameCodeList.setAdapter(arrayAdapter);
                    }else{
                        Toast.makeText(CollectionReportActivity.this, "Loan code not associated with this Executive or No data found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }


//    private void getRDAccountDetails(String s) throws JSONException{
//        new GetDataParserArray(this, s, true, new GetDataParserArray.OnGetResponseListener() {
//            @Override
//            public void onGetResponse(JSONArray response) {
//                try{
//                    if(response.length()>0){
//                        for(int i=0; i<response.length(); i++){
//                            JSONObject jsonObject = response.getJSONObject(i);
//                            Log.d("Data Settings", response.getString(i));
//                            tv_setDate.setText(jsonObject.getString("Date"));
//                            tv_setName.setText(jsonObject.getString("MemberName"));
//                            tv_rdAcNumber.setText(jsonObject.getString("PolicyCode"));
//                            tv_rdTotal.setText(jsonObject.getString("TotalAmount"));
////                            arrayList_policyCode.add(jsonObject.getString("PolicyCode"));
////                            arrayList_policyCodeName.add(jsonObject.getString("PolicyCode")+"-"+jsonObject.getString("MemberName"));
//
//                            rdCode = tv_rdAcNumber.getText().toString();
//                        }
//
////                        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(CollectionReportActivity.this,R.layout.spinner_hint, arrayList_policyCodeName);
////                        mSp_policyNameCodeList.setAdapter(arrayAdapter);
//                    } else{
//                        Toast.makeText(CollectionReportActivity.this, "No Data Found in RD Account", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        });
//    }



//    private void getLoanAccountDetails(String s) throws JSONException {
//        new GetDataParserArray(this, s, true, new GetDataParserArray.OnGetResponseListener() {
//            @Override
//            public void onGetResponse(JSONArray response){
//                try{
//                    if(response.length()>0){
//                        for(int i=0; i<response.length(); i++){
//                            JSONObject jsonObject = response.getJSONObject(i);
//                            Log.d("Data Settings", response.getString(i));
//                            tv_setName.setText(jsonObject.getString("MemberName"));
//                            tv_loanAcNumber.setText(jsonObject.getString("LoanCode"));
//                            tv_loanTotal.setText(jsonObject.getString("TotalAmount"));
//
//                            loanCode = tv_loanAcNumber.getText().toString();
//                        }
//                    } else{
//                        Toast.makeText(CollectionReportActivity.this, "No Data Found in Loan Account", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    private void getSBAccountDetails(String s) throws JSONException{
        arrayList_sbCodeName.clear();

        new GetDataParserArray(this, s, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {

                try{
                    if(response.length()>0){
                        for(int i=0;i<response.length();i++) {
                            JSONObject jsonObject=response.getJSONObject(i);
                            tv_setName.setText(jsonObject.getString("MemberName"));
                            arrayList_sbCode.add(jsonObject.getString("AccountCode"));
                            arrayList_sbCodeName.add(jsonObject.getString("AccountCode")+"-"+jsonObject.getString("MemberName"));
                        }
                        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(CollectionReportActivity.this,R.layout.spinner_hint, arrayList_sbCodeName);
                        mSp_sbNameCodeList.setAdapter(arrayAdapter);
                    }else{
                        Toast.makeText(CollectionReportActivity.this, "Policy code not associated with this Executive or NoData", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();}
//                try{
//                    if(response.length()>0){
//                        for(int i=0; i< response.length(); i++){
//                            JSONObject jsonObject = response.getJSONObject(i);
//                            Log.d("Data Settings", response.getString(i));
//                            tv_setName.setText(jsonObject.getString("MemberName"));
//                            tv_sbAcNumber.setText(jsonObject.getString("AccountCode"));
//                            tv_sbTotal.setText(jsonObject.getString("TodayAmount"));
//
//                            sbCode = tv_sbAcNumber.getText().toString();
//                        }
//                    } else {
//                        Toast.makeText(CollectionReportActivity.this, "No Data Found in Savings Account", Toast.LENGTH_SHORT).show();
//                    }
//                } catch(Exception e){
//                    e.printStackTrace();
//                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CollectionReportActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
*/

    }

    @Override
    public void onClick(View v) {

    }
}