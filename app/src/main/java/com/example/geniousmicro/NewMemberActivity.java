
package com.example.geniousmicro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.example.geniousmicro.Adapter.CustomAdapterJson;
import com.example.geniousmicro.Data.GetDataParserObject;
import com.example.geniousmicro.Data.PostDataParserObjectResponse;
import com.example.geniousmicro.Data.VolleyCallback;
import com.example.geniousmicro.MainActivity;
import com.example.geniousmicro.Others.ApiLinks;
import com.example.geniousmicro.Others.CustomPopUpdialog;
import com.example.geniousmicro.R;
import com.example.geniousmicro.UserData.GlobalUserData;
import com.example.geniousmicro.databinding.ActivityNewMemberBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewMemberActivity extends AppCompatActivity implements View.OnClickListener {


    int status = 0;
    EditText adhno;
    TextView verifybtn;
    LottieAnimationView anim;
    LinearLayout baccard;
    Dialog dialog;
    String adharno = "";
    String otp = "";
    byte[] sImage;
    DatePickerDialog datePickerDialog;
    private EditText mEt_panNo;
    private EditText mEt_bankName;
    private EditText mEt_bankAccNo;
    private EditText mEt_ifscCode;
    private EditText mEt_shareAmount;
    private EditText mEt_formNo;
    EditText txtMemberName, txtFatherName, txtAddress, txtPinCode, txtNomineeName;
    EditText txtPhoneNo, txtRegAmt, mEt_emailId, mEt_state, mEt_nomineeCode, mEt_addressProofNo, mEt_idProofNo;
    private TextView mTv_toolbarTitle;
    private TextView mTv_takePicture;
    TextView txtMemberDOB, txtNomineeDOB, txtDateOfJoin;
    private TextView mTv_applicantAge, mTv_nomineeAge;
    private TextView mTv_bankName, mTv_branchName;
    private ImageView mIv_memberPicture;
    private ImageView mIv_memberSignature;
    private ImageView mIv_addrPhoto;
    private ImageView mIv_idPhoto;
    private ImageView mIv_bankAccPhoto;
    private LinearLayout mLl_memberImage;
    private LinearLayout mLl_memberSignature;
    private LinearLayout mLl_addrProof;
    private LinearLayout mLl_idProof;
    private LinearLayout mLl_bankAccProof;
    Spinner mSp_bloodGroup, mSp_GurdianType, mSp_MemberMaritalStatus, spnNomineeRelation, spnIDProofNo, mSp_addressProofName, mSp_officeName, MemberType;
    AppCompatRadioButton rboMale, rboFemale;
    Button btnSave;
    private Button mBtn_submitIfscCode;
    String memberDOJ = "", nomineeDOB = "";
    private String selectedImageType = "";
    private String selectmember_type = "Mr.";
    private String selectedIdProofName = "";
    private String selectedAddressProofName = "";
    private String selectedBloodGroup = "Don't Know";
    private String selectedSignProofName = "Aadhar Card";
    private String SelectedIDProofName = "";
    private String SelectedAddressProofName = "";
    private String selectedGender = "";
    private String St_member_cat = "";
    private Spinner mSp_state;
    private Spinner mSp_dist;
    private String selectedState = "";
    private String St_nomineeRelation = "";
    private String selectedDist = "";
    String Member_marride_status = "";
    private String St_nominetype = "";
    private String Spt_gurdiantype = "S/O";
    ArrayList<String> arrayList_dist;
    private ArrayList<String> spinnerArrayState;
    private float selectedShareAmount = 0.0f;
    int mYear, mMonth, mDay;
    private int currentDay, currentMonth, currentYear;
    int memberDOB = 0;
    int dob_day, dob_month, dob_year;
    private Toolbar mToolbar;
    private CircleImageView mCiv_selectedImage;
    private Spinner mSp_signProofName;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private Calendar mCalendar; // Datepicker
    private ActivityNewMemberBinding binding;

    ArrayList<String> stateList;
    ArrayList<String> distList;

    private int backPressCounter = 0;
    private static final int REQUIRED_BACK_PRESS_COUNT = 2;
    private static final int BACK_PRESS_INTERVAL = 2000;
    String ref_id = "";
    String image_type = "";
    byte[] member_img = null;
    byte[] member_signature = null;
    byte[] member_idproof = null;
    byte[] member_addrProof = null;
    ArrayList<String> AddressProof = new ArrayList<String>();

    final ArrayList<String> IDProof = new ArrayList<String>();
    ProgressDialog pdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewMemberBinding.inflate(getLayoutInflater());

        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.new_member), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setViewReferences();
        bindEventHandlers();
        AccessOfficeName();
        Animation();
        setStateList();

        pdialog = new ProgressDialog(NewMemberActivity.this);
        pdialog.setCancelable(false);
        pdialog.setMessage("Loading.Please wait...");


        getAddrProofs();
        getIDProofList();

        distList = new ArrayList<>();

        // Toast.makeText(this, "" + GlobalUserData.employeeDataModel.getEmployeeID(), Toast.LENGTH_SHORT).show();

        // createDialog();

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Calendar
        mCalendar = Calendar.getInstance();
        currentDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = mCalendar.get(Calendar.MONTH);
        currentYear = mCalendar.get(Calendar.YEAR);

        txtRegAmt.setText("40");
        mEt_shareAmount.setText("100");

        //Nominee Relation
        final ArrayList<String> NomineeRelation = new ArrayList<String>();
        NomineeRelation.add("--Select--");
        NomineeRelation.add("Brother");
        NomineeRelation.add("Brother-In-Law");
        NomineeRelation.add("Daughter");
        NomineeRelation.add("Daughter-In-Law");
        NomineeRelation.add("Father");
        NomineeRelation.add("Father-In-Law");
        NomineeRelation.add("Husband");
        NomineeRelation.add("Mother");
        NomineeRelation.add("Mother-In-Law");
        NomineeRelation.add("Other");
        NomineeRelation.add("Sister-In-Law");
        NomineeRelation.add("Son");
        NomineeRelation.add("Son-In-Law");
        NomineeRelation.add("Wife");

        ArrayAdapter<String> NomineeRelationAdpater = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, NomineeRelation);
        NomineeRelationAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnNewMemberNomineeRelation.setAdapter(NomineeRelationAdpater);
        binding.spnNewMemberNomineeRelation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                St_nomineeRelation = NomineeRelation.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Nominee Address Proof Name
        final ArrayList<String> NomineeAddressProofName = new ArrayList<String>();
        NomineeAddressProofName.add("Aadhar Card");
        NomineeAddressProofName.add("Voter Card");
        NomineeAddressProofName.add("PAN Card");
        NomineeAddressProofName.add("Ration Card");
        ArrayAdapter<String> NomineeAddressProofNameAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, NomineeAddressProofName);

        NomineeAddressProofNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.NomineeIDProofName.setAdapter(NomineeAddressProofNameAdapter);

        binding.NomineeIDProofName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedIDProofName = NomineeAddressProofName.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Load ID Proof Name
//        IDProof.add("Aadhar Card");
//        IDProof.add("Voter Card");
//        IDProof.add("PAN Card");
//        IDProof.add("Ration Card");
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
//                this, android.R.layout.simple_spinner_item, IDProof);
//        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spnIDProofNo.setAdapter(adapter1);
        spnIDProofNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedIdProofName = IDProof.get(position);
                if (position == 0) {
                    int maxLength = 12;
                    InputFilter[] FilterArray = new InputFilter[1];
                    FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                    mEt_idProofNo.setFilters(FilterArray);
                }
                if (position == 1) {
                    int maxLength = 10;
                    InputFilter[] FilterArray = new InputFilter[1];
                    FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                    mEt_idProofNo.setFilters(FilterArray);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //state
        binding.spActivityNewMemberEntrySpinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position != 0) {
                    selectedState = stateList.get(position).toUpperCase();

                    getDistrict(stateList.get(position));
                    Log.e("selectedState", selectedState);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ///nomine type
        final ArrayList<String> nomine_type = new ArrayList<>();
        nomine_type.add("--Select--");
        nomine_type.add("Mr.");
        nomine_type.add("Mrs.");
        nomine_type.add("Miss.");

        ArrayAdapter<String> mnonie_type_adpater = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nomine_type);
        mnonie_type_adpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.nominetype.setAdapter(mnonie_type_adpater);
        binding.nominetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                St_nominetype = nomine_type.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //MEMBER TYPE
        final ArrayList<String> member_type = new ArrayList<>();
        member_type.add("--select--");
        member_type.add("Mr.");
        member_type.add("Mrs.");
        member_type.add("Miss.");
        selectmember_type = "Mr.";

        ArrayAdapter<String> membertype_spinneradapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, member_type);
        membertype_spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.MemberType.setAdapter(membertype_spinneradapter);

        binding.MemberType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectmember_type = member_type.get(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Guardian Type
        final ArrayList<String> GuardianType = new ArrayList<>();
        GuardianType.add("--Select--");
        GuardianType.add("S/O");
        GuardianType.add("D/O");
        GuardianType.add("W/O");
        Spt_gurdiantype = "S/O";

        ArrayAdapter<String> GuardianTypeAdpater = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, GuardianType);
        GuardianTypeAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.GurdianType.setAdapter(GuardianTypeAdpater);
        binding.GurdianType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Spt_gurdiantype = GuardianType.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //member merital status

        final ArrayList<String> MemberMarrideStatus = new ArrayList<>();
        MemberMarrideStatus.add("--Select--");
        MemberMarrideStatus.add("Married");
        MemberMarrideStatus.add("Single");
        MemberMarrideStatus.add("Widow");
        Member_marride_status = "Single";
        CustomAdapterJson MemberMarrideStatusAdapter = new CustomAdapterJson(this, MemberMarrideStatus);
        binding.MaritalStatus.setAdapter(MemberMarrideStatusAdapter);
        binding.MaritalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    Member_marride_status = MemberMarrideStatus.get(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //member category
        final ArrayList<String> MemberCategoryType = new ArrayList<>();

        MemberCategoryType.add("--Select--");
        MemberCategoryType.add("General");
        MemberCategoryType.add("Sr.Citizen");
        MemberCategoryType.add("Minor");
        MemberCategoryType.add("SC");
        MemberCategoryType.add("ST");
        St_member_cat = "General";
        CustomAdapterJson adapter_cagt = new CustomAdapterJson(this, MemberCategoryType);
        binding.MemberCategory.setAdapter(adapter_cagt);

        binding.MemberCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    St_member_cat = MemberCategoryType.get(position);
                    Log.e("mecat", MemberCategoryType.get(position));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //Load ID Proof Name
//        AddressProof.add("Aadhar Card");
//        AddressProof.add("Voter Card");
//        AddressProof.add("PAN Card");
//        AddressProof.add("Ration Card");
//        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
//                this, android.R.layout.simple_spinner_item, AddressProof);
//
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        binding.spActivityNewMemberAddressProof.setAdapter(adapter2);

        binding.spActivityNewMemberAddressProof.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAddressProofName = AddressProof.get(position);
                if (position == 0) {
                    int maxLength = 12;
                    InputFilter[] FilterArray = new InputFilter[1];
                    FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                    mEt_addressProofNo.setFilters(FilterArray);
                }
                if (position == 1) {
                    int maxLength = 10;
                    InputFilter[] FilterArray = new InputFilter[1];
                    FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                    mEt_addressProofNo.setFilters(FilterArray);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Load sign Proof Name
        final ArrayList<String> signProof = new ArrayList<String>();
        signProof.add("Aadhar Card");
        signProof.add("Voter Card");
        signProof.add("PAN Card");
        signProof.add("Ration Card");
        ArrayAdapter<String> adapterSignProof = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, signProof);

        adapterSignProof.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spActivityNewMemberSignProof.setAdapter(adapterSignProof);

        binding.spActivityNewMemberSignProof.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSignProofName = signProof.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Blood group
        final ArrayList<String> spinnerBloodGroup = new ArrayList<>();
        spinnerBloodGroup.add("Don't Know");
        spinnerBloodGroup.add("A+");
        spinnerBloodGroup.add("A-");
        spinnerBloodGroup.add("B+");
        spinnerBloodGroup.add("B-");
        spinnerBloodGroup.add("O+");
        spinnerBloodGroup.add("O-");
        spinnerBloodGroup.add("AB+");
        spinnerBloodGroup.add("AB-");
        ArrayAdapter<String> adapterBloodGroup = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerBloodGroup);

        adapterBloodGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //mSp_bloodGroup.setPrompt("Select");
        binding.spActivityNewMemberBloodGroup.setAdapter(adapterBloodGroup);

        binding.spActivityNewMemberBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBloodGroup = spinnerBloodGroup.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerArrayState = new ArrayList<>();
        // arrayList_dist = new ArrayList<String>();
        binding.spActivityNewMemberEntrySpinnerDist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (arrayList_dist != null) {
                    selectedDist = arrayList_dist.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.txtNewMemberDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* datePickerDialog = new DatePickerDialog(getApplicationContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                txtMemberDOB.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                memberDOB = Integer.toString(year) + String.format("%02d", (monthOfYear + 1)) + String.format("%02d", dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();*/


                DatePickerDialog datePickerDialog = new DatePickerDialog(NewMemberActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month += 1;
                        dob_day = dayOfMonth;
                        dob_month = month;
                        dob_year = year;
                        String age = getAge(dob_year, dob_month, dob_day);
                        txtMemberDOB.setText(dayOfMonth + "-" + month + "-" + year);
                        mTv_applicantAge.setText("Age - " + age + " Years");
                        memberDOB = Integer.parseInt(Integer.toString(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth));
                    }
                }, currentYear, currentMonth, currentDay);
                mCalendar.set(currentYear, currentMonth, currentDay);

                datePickerDialog.getDatePicker().setMaxDate(mCalendar.getTimeInMillis());
                datePickerDialog.show();

                //getAge(dob_year,dob_month,dob_day);
            }
        });

        binding.txtNewMemberDateOfJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "onClick: " + memberDOJ);
              /*  datePickerDialog = new DatePickerDialog(getApplicationContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                txtDateOfJoin.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                memberDOJ = Integer.toString(year) + String.format("%02d", (monthOfYear + 1)) + String.format("%02d", dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();*/
            }
        });

        binding.txtNewMemberNomineeDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(NewMemberActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month += 1;
                        dob_year = year;
                        dob_month = month;
                        dob_day = dayOfMonth;
                        String nom_age = getAge(dob_year, dob_month, dob_day);
                        mTv_nomineeAge.setText(nom_age + "Years");
                        txtNomineeDOB.setText(dayOfMonth + "-" + month + "-" + year);
                        nomineeDOB = String.valueOf(Integer.parseInt(Integer.toString(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth)));
                    }
                }, currentYear, currentMonth, currentDay);
                mCalendar.set(currentYear, currentMonth, currentDay);

                datePickerDialog.getDatePicker().setMaxDate(mCalendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        binding.rboMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rboMale.setChecked(true);
                rboFemale.setChecked(false);
                selectedGender = "Male";
            }
        });

        binding.rboFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rboMale.setChecked(false);
                rboFemale.setChecked(true);
                selectedGender = "Female";
            }
        });

        binding.ivActivityNewMemberPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image_type = "idphoto";
                openBottomSheetDialog();

               /* image_type = "idphoto";
                selectImage();*/

            }
        });

        binding.ivActivityNewMemberSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_type = "signature";
                //selectImage();
                openBottomSheetDialog();
            }
        });

        binding.ivActivityNewMemberAddressProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_type = "addrproof";
                //selectImage();
                openBottomSheetDialog();
            }
        });

        binding.ivActivityNewMemberIdProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_type = "idProof";
                //selectImage();
                openBottomSheetDialog();
            }
        });

        binding.llActivityNewMemberBankAccProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImageType = "bankaccproof";
                openBottomSheetDialog();
            }
        });
        binding.btnNewMemberPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == 1) {
                    binding.leftBtn.setBackgroundDrawable(ContextCompat.getDrawable(NewMemberActivity.this, R.drawable.card_bg_top_right));
                    binding.rightBtn.setBackgroundDrawable(ContextCompat.getDrawable(NewMemberActivity.this, R.drawable.card_invisiable_background));
                    binding.BtnNomineeDetails.setBackgroundDrawable(ContextCompat.getDrawable(NewMemberActivity.this, R.drawable.card_invisiable_background));

                    showPersonaldetails();
                    status = 0;
                } else if (status == 2) {
                    binding.BtnNomineeDetails.setBackgroundDrawable(ContextCompat.getDrawable(NewMemberActivity.this, R.drawable.card_bg_top_right));
                    binding.rightBtn.setBackgroundDrawable(ContextCompat.getDrawable(NewMemberActivity.this, R.drawable.card_invisiable_background));
                    binding.leftBtn.setBackgroundDrawable(ContextCompat.getDrawable(NewMemberActivity.this, R.drawable.card_invisiable_background));

                    showNomineedetails();
                    status = 1;
                }
            }
        });


        binding.btnNewMemberSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status == 0) {
                    if (!binding.txtNewMemberName.getText().toString().trim().isEmpty()) {
                        if (binding.txtNewMemberPhone.getText().toString().trim().length() == 10) {

                           /* if (!binding.txtNewMemberIDProofNo.getText().toString().trim().isEmpty()) {
                                if (!binding.etActivityNewMemberAddressProofNo.getText().toString().trim().isEmpty()) {

                                    if (!binding.etActivityNewMemberPanNo.getText().toString().trim().isEmpty()) {*/
                            showNomineedetails();
                            status = 1;
                            binding.BtnNomineeDetails.setBackgroundDrawable(ContextCompat.getDrawable(NewMemberActivity.this, R.drawable.card_bg_top_right));
                            binding.rightBtn.setBackgroundDrawable(ContextCompat.getDrawable(NewMemberActivity.this, R.drawable.card_invisiable_background));
                            binding.leftBtn.setBackgroundDrawable(ContextCompat.getDrawable(NewMemberActivity.this, R.drawable.card_invisiable_background));

                                   /* } else {
                                        binding.etActivityNewMemberPanNo.setError("Enter Pan No");
                                        binding.etActivityNewMemberPanNo.requestFocus();
                                    }


                                } else {
                                    binding.etActivityNewMemberAddressProofNo.setError("Enter Address Proof No");
                                    binding.etActivityNewMemberAddressProofNo.requestFocus();
                                }

                            } else {
                                binding.txtNewMemberIDProofNo.setError("Enter ID Proof No");
                                binding.txtNewMemberIDProofNo.requestFocus();
                            }*/


                        } else {
                            binding.txtNewMemberPhone.setError("Enter Mobile Number");
                            binding.txtNewMemberPhone.requestFocus();
                        }
                    } else {
                        binding.txtNewMemberName.setError("Enter Name Member Name");
                        binding.txtNewMemberName.requestFocus();
                    }
                } else if (status == 1) {
                    binding.rightBtn.setBackgroundDrawable(ContextCompat.getDrawable(NewMemberActivity.this, R.drawable.card_bg_top_right));
                    binding.leftBtn.setBackgroundDrawable(ContextCompat.getDrawable(NewMemberActivity.this, R.drawable.card_invisiable_background));
                    binding.BtnNomineeDetails.setBackgroundDrawable(ContextCompat.getDrawable(NewMemberActivity.this, R.drawable.card_invisiable_background));

                    showbankdetails();
                    status = 2;
                } else if (status == 2) {

                    //  insertMember();

//                    if (!binding.txtNewMemberName.getText().toString().trim().isEmpty()) {
//                        if (binding.txtNewMemberPhone.getText().toString().trim().length() == 10) {
//
//                            if (!binding.txtNewMemberIDProofNo.getText().toString().trim().isEmpty()) {
//                                if (!binding.etActivityNewMemberAddressProofNo.getText().toString().trim().isEmpty()) {
//
//                                    if (!binding.etActivityNewMemberPanNo.getText().toString().trim().isEmpty()) {

//                                    binding.MemberImage.setVisibility(View.GONE);
//                                    binding.MemberDetails.setVisibility(View.GONE);
//                                    binding.MemberDetailsSecond.setVisibility(View.GONE);
//                                    binding.NomineeDetails.setVisibility(View.VISIBLE);
//                                    binding.BankInformatiomCardView.setVisibility(View.VISIBLE);
//
                    insertMember();


//                                    } else {
//                                        binding.etActivityNewMemberPanNo.setError("Enter Pan No");
//                                        binding.etActivityNewMemberPanNo.requestFocus();
//                                    }
//
//
//                                } else {
//                                    binding.etActivityNewMemberAddressProofNo.setError("Enter Address Proof No");
//                                    binding.etActivityNewMemberAddressProofNo.requestFocus();
//                                }
//
//                            } else {
//                                binding.txtNewMemberIDProofNo.setError("Enter ID Proof No");
//                                binding.txtNewMemberIDProofNo.requestFocus();
//                            }
//
//
//                        } else {
//                            binding.txtNewMemberPhone.setError("Enter Mobile Number");
//                            binding.txtNewMemberPhone.requestFocus();
//                        }
//                    } else {
//                        binding.txtNewMemberName.setError("Enter Name Member Name");
//                        binding.txtNewMemberName.requestFocus();
//                    }
                }

            }
        });
        binding.btnActivityNewMemberSubmitIfscCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://ifsc.razorpay.com/" + binding.etActivityNewMemberIfscCode.getText().toString();
                new GetDataParserObject(NewMemberActivity.this, url, new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(JSONObject response) {
                        Log.d("IFSCResponse", response.toString());
                        try {
                            binding.tvActivityNewMemberBankName.setText(response.getString("BANK"));
                            binding.tvActivityNewMemberBranchName.setText(response.getString("BRANCH"));
                        } catch (Exception e) {

                        }

//                        Toast.makeText(NewMemberActivity.this, ""+response.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onErrorResponse(String error) {

                    }
                });
            }
        });
        binding.leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.MemberImage.setVisibility(View.VISIBLE);
                binding.MemberDetails.setVisibility(View.VISIBLE);
                binding.MemberDetailsSecond.setVisibility(View.VISIBLE);
                binding.NomineeDetails.setVisibility(View.GONE);
                binding.BankInformatiomCardView.setVisibility(View.GONE);
                binding.RegistrationAmountCardView.setVisibility(View.GONE);
                binding.btnNewMemberPrevious.setVisibility(View.GONE);
                binding.leftBtn.setBackgroundDrawable(ContextCompat.getDrawable(NewMemberActivity.this, R.drawable.card_bg_top_right));
                binding.rightBtn.setBackgroundDrawable(ContextCompat.getDrawable(NewMemberActivity.this, R.drawable.card_invisiable_background));
                binding.BtnNomineeDetails.setBackgroundDrawable(ContextCompat.getDrawable(NewMemberActivity.this, R.drawable.card_invisiable_background));


            }
        });

        binding.rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.MemberImage.setVisibility(View.GONE);
             /*   binding.MemberDetails.setVisibility(View.GONE);
                binding.MemberDetailsSecond.setVisibility(View.GONE);
                binding.NomineeDetails.setVisibility(View.GONE);
                binding.BankInformatiomCardView.setVisibility(View.VISIBLE);
                binding.RegistrationAmountCardView.setVisibility(View.VISIBLE);
                binding.btnNewMemberPrevious.setVisibility(View.VISIBLE);
                binding.rightBtn.setBackgroundDrawable(ContextCompat.getDrawable(NewMemberActivity.this, R.drawable.card_bg_top_right));
                binding.leftBtn.setBackgroundDrawable(ContextCompat.getDrawable(NewMemberActivity.this, R.drawable.card_invisiable_background));
                binding.BtnNomineeDetails.setBackgroundDrawable(ContextCompat.getDrawable(NewMemberActivity.this, R.drawable.card_invisiable_background));
*/
//                if(status!=2){
//                    binding.btnNewMemberSave.setVisibility(View.GONE);
//                }
            }
        });

        binding.BtnNomineeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //binding.MemberImage.setVisibility(View.GONE);
             /*   binding.MemberDetails.setVisibility(View.GONE);
                binding.MemberDetailsSecond.setVisibility(View.GONE);
                binding.NomineeDetails.setVisibility(View.VISIBLE);
                binding.BankInformatiomCardView.setVisibility(View.GONE);
                binding.RegistrationAmountCardView.setVisibility(View.GONE);
                binding.btnNewMemberPrevious.setVisibility(View.VISIBLE);
                binding.BtnNomineeDetails.setBackgroundDrawable(ContextCompat.getDrawable(NewMemberActivity.this, R.drawable.card_bg_top_right));
                binding.rightBtn.setBackgroundDrawable(ContextCompat.getDrawable(NewMemberActivity.this, R.drawable.card_invisiable_background));
                binding.leftBtn.setBackgroundDrawable(ContextCompat.getDrawable(NewMemberActivity.this, R.drawable.card_invisiable_background));
*/

            }
        });

//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                if (txtPhoneNo.getText().toString().trim().length() == 10) {
//                    if (mEt_idProofNo.getText().toString().trim().length() > 0) {
//
//                        if (!mEt_shareAmount.getText().toString().equalsIgnoreCase("0")) {
//                            final ProgressDialog progressDialog = new ProgressDialog(NewMemberActivity.this,
//                                    ProgressDialog.THEME_HOLO_DARK);
//                            progressDialog.setMessage("Please Wait...");
//                            progressDialog.show();
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        boolean status_pass_id = false, status_pass_pan = false;
//                                        if (selectedIdProofName.equals("Aadhar Card") && mEt_idProofNo.getText().length() == 12) {
//                                            status_pass_id = true;
//                                        } else if (selectedIdProofName.equals("Voter Card") && mEt_idProofNo.getText().length() == 10) {
//                                            status_pass_id = true;
//                                        } else {
//                                            status_pass_id = false;
//                                            mEt_idProofNo.setError("Enter proper digit");
//                                            mEt_idProofNo.requestFocus();
//                                        }
//
//                                        if (mEt_panNo.getText().toString().length() == 10) {
//                                            status_pass_pan = true;
//                                        } else {
//                                            status_pass_pan = false;
//                                            mEt_panNo.setError("Enter 10 digit");
//                                            mEt_panNo.requestFocus();
//                                        }
//
//                                        if (txtMemberName.getText().toString().length() > 0 && txtAddress.getText().toString().length() > 0
//                                                && txtPhoneNo.getText().toString().length() > 0 && txtPinCode.getText().toString().length() > 0
//                                                && status_pass_id && status_pass_pan) {
//
//                                            selectedShareAmount = Float.parseFloat(mEt_shareAmount.getText().toString());
//                                           /* MemberData m = new MemberData();
//                                            m.setMemberName(txtMemberName.getEditableText().toString());
//                                            m.setFather(txtFatherName.getEditableText().toString());
//                                            m.setAddress(txtAddress.getEditableText().toString());
//                                            m.setPinCode(txtPinCode.getEditableText().toString());
//                                            m.setPhoneNo(txtPhoneNo.getEditableText().toString());
//                                            m.setMemberDOB(String.valueOf(memberDOB));
//                                            m.setDateOfJoin(memberDOJ);
//                                            m.setGender(selectedGender);
//                                            m.setNominee(txtNomineeName.getEditableText().toString());
//                                            m.setNomineeDOB(nomineeDOB);
//                                            m.setNomineeRelation("");
//                                            m.setEmailId(mEt_emailId.getText().toString());
//                                            m.setDistrict(selectedDist);
//                                            m.setState(selectedState);
//                                            m.setNomineeCode(mEt_nomineeCode.getText().toString());             // TODO Nominee code not set when sending data
//                                            m.setBloodGroup(selectedBloodGroup);
//                                            m.setSelectedIdProofNo(mEt_idProofNo.getText().toString());
//                                            m.setSelectedIdProofName(selectedIdProofName);
//                                            m.setSelectedAddressProofName(selectedAddressProofName);
//                                            m.setSelectedAddressProofNo(mEt_addressProofNo.getText().toString());
//
//                                            m.setBankName(mTv_bankName.getText().toString());
//                                            m.setBranchName(mTv_branchName.getText().toString());
//                                            m.setBankAccNo(mEt_bankAccNo.getText().toString());
//                                            m.setBankIfscCode(mEt_ifscCode.getText().toString());
//                                            m.setShareAmount(selectedShareAmount);
//                                            m.setSignProofName(selectedSignProofName);
//                                            m.setPanNo(mEt_panNo.getText().toString());
//                                            m.setFormNo(mEt_formNo.getText().toString());
//                                            *//*if (encodedImageString.equals("")) {*//*
//                                            //byte[] data = Base64.decode(defaultEncodedImageString, Base64.DEFAULT);
//
//
//                                            m.setImageEncodedString(dataMemberPicture);
//                                            m.setSignatureEncodingString(dataMemberSignature);
//                                            m.setIdEncodingString(dataIdProof);
//                                            m.setAddressEncodingString(dataAddressProof);
//*/
//                                            // share
//                                            //   m.setAlltMemberCode(directorMemberCode);
//                                            //  m.setDnoTo(dnoTo);
//                                            //m.setDnoFrom(dnoFrom);
//
//                                            //m.setBankAccEncodingString(databankAccProof);
//                                            /*} else {
//                    byte[] data = Base64.decode(encodedImageString, Base64.DEFAULT);
//                    m.setImageEncodedString(String.valueOf(data));
//                }*/
//
//                                            //m.setSignatureEncodingString();                                                     // TODO Get signature byte array
//
//                                            if (txtRegAmt.getText().toString().trim().length() > 0) {
//                                                // m.setRegAmt(Double.parseDouble(txtRegAmt.getText().toString()));
//                                            } else {
//                                                // m.setRegAmt(0);
//                                            }
//
//                                          /*  if (new MemberManagement().insertMember(m)) {
//                                                //Toast.makeText(getApplicationContext(), "Save Successfully", Toast.LENGTH_LONG);
//                                                AlertDialog.Builder builder = new AlertDialog.Builder(NewMemberActivity.this);
//                                                builder.setCancelable(false);
//                                                builder.setTitle("Successful");
//                                                builder.setMessage("Member Saved Successfully.\n\nThe temporary Member Code is " + TempDataBean.tempMemberCode);
//                                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        //builder.setCancelable(true);
//                                                        Intent i = new Intent(NewMemberActivity.this, NewMemberActivity.class);
//                                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                                                        startActivity(i);
//                                                        finish();
//                                                        progressDialog.dismiss();
//                                                    }
//                                                }).show();
//
//                                            } else {
//                                                if (TempDataBean.NewMemberErrorCode == 50078) {
//                                                    Toast.makeText(NewMemberActivity.this, "Member Already exist", Toast.LENGTH_LONG).show();
//                                                } else {
//                                                    Toast.makeText(getApplicationContext(), "Save Failed", Toast.LENGTH_LONG).show();
//                                                }
//                                                progressDialog.dismiss();
//
//                                            }*/
//                                        } else {
//                                            progressDialog.dismiss();
//                                            Toast.makeText(NewMemberActivity.this, "Please Enter Sufficient Data", Toast.LENGTH_SHORT).show();
//
//                                        }
//
//
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }, 3000);
//                        } else {
//
//                            mEt_shareAmount.setError("Enter Share Amount");
//                            mEt_shareAmount.requestFocus();
//                        }
//                    } else {
//                        mEt_idProofNo.setError("Enter Id Proof no");
//                        mEt_idProofNo.requestFocus();
//                    }
//                } else {
//                    txtPhoneNo.setError("Enter phone no");
//                    txtPhoneNo.requestFocus();
//                }
//
//            }
//        });
    }

    public void openBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(NewMemberActivity.this);
        bottomSheetDialog.setContentView(R.layout.custom_bottom_sheet_dialog);

        ImageView mIv_camera = bottomSheetDialog.findViewById(R.id.iv_custom_bottom_sheet_dialog_select_image);
        mIv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions()) {
                    //TODO: Permission granted
                    takeImage();
                }
            }
        });
        bottomSheetDialog.show();
    }

    public void takeImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }
    @SuppressLint({"NewApi", "SetTextI18n"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUriContent();
                mTv_takePicture.setText("Image selected");
                InputStream imageStream = null;
                try {
                    imageStream = this.getContentResolver().openInputStream(resultUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                String encodedIdString = encodeToBase64(compressImage(yourSelectedImage));
                sImage = Base64.decode(encodedIdString, Base64.DEFAULT);
                Log.e("img", sImage.toString());
                try {
                    if (image_type.equals("idphoto")) {
                        binding.ivActivityNewMemberPicture.setImageBitmap(yourSelectedImage);
                        member_img = sImage;
                        Log.d("img", "" + member_img);
                    } else if (image_type.equals("signature")) {
                        binding.ivActivityNewMemberSignature.setImageBitmap(yourSelectedImage);
                        member_signature = sImage;
                        Log.d("img", member_signature.toString());
                    } else if (image_type.equals("idProof")) {
                        binding.ivActivityNewMemberIdProof.setImageBitmap(yourSelectedImage);
                        member_idproof = sImage;
                        Log.d("img", member_idproof.toString());
                    } else if (image_type.equals("addrproof")) {
                        binding.ivActivityNewMemberAddressProof.setImageBitmap(yourSelectedImage);
                        member_addrProof = sImage;
                        Log.d("img", member_addrProof.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //mCiv_selectedImage.setImageBitmap(yourSelectedImage);
                //encodedImageString = encodeToBase64(yourSelectedImage);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //Exception error = result.getError();
                Toast.makeText(this, "Failed to take image.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void getIDProofList() {
        new PostDataParserObjectResponse(NewMemberActivity.this, ApiLinks.ID_PROOF_LIST, null, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    JSONArray jsa = result.getJSONArray("IDProofs");
                    for (int i = 0; i < jsa.length(); i++) {
                        JSONObject jso = jsa.getJSONObject(i);
                        Log.d("EXad2", jso.getString("IDProofName"));
                        IDProof.add(jso.getString("IDProofName"));

                    }
                    selectedIdProofName = jsa.getJSONObject(0).getString("IDProofName");
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                            NewMemberActivity.this, android.R.layout.simple_spinner_item, IDProof);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnIDProofNo.setAdapter(adapter1);
                } catch (Exception e) {
                    Toast.makeText(NewMemberActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.d("EX", e.toString());
                }
            }

            @Override
            public void onErrorResponse(String error) {
                Log.e("Callback Error", error);
            }
        });
    }

    private void getAddrProofs() {
        new PostDataParserObjectResponse(NewMemberActivity.this, ApiLinks.ADDRESS_PROOF_LIST, null, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    JSONArray jsa = result.getJSONArray("addrProofDetails");
                    for (int i = 0; i < jsa.length(); i++) {
                        JSONObject jso = jsa.getJSONObject(i);
                        Log.d("EXad", jso.getString("AddressProof"));
                        AddressProof.add(jso.getString("AddressProof"));

                    }
                    selectedAddressProofName = jsa.getJSONObject(0).getString("AddressProof");
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                            NewMemberActivity.this, android.R.layout.simple_spinner_item, AddressProof);

                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spActivityNewMemberAddressProof.setAdapter(adapter2);
                } catch (Exception e) {
                    Toast.makeText(NewMemberActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.d("EX", e.toString());
                }
            }

            @Override
            public void onErrorResponse(String error) {
                Log.e("Callback Error", error);
            }
        });
    }

    public void showPersonaldetails() {
        binding.MemberImage.setVisibility(View.VISIBLE);
        binding.MemberDetails.setVisibility(View.VISIBLE);
        binding.MemberDetailsSecond.setVisibility(View.VISIBLE);
        binding.NomineeDetails.setVisibility(View.GONE);
        binding.BankInformatiomCardView.setVisibility(View.GONE);
        binding.btnNewMemberPrevious.setVisibility(View.GONE);

    }

    public void showNomineedetails() {
        binding.MemberImage.setVisibility(View.GONE);
        binding.MemberDetails.setVisibility(View.GONE);
        binding.MemberDetailsSecond.setVisibility(View.GONE);
        binding.NomineeDetails.setVisibility(View.VISIBLE);
        binding.BankInformatiomCardView.setVisibility(View.GONE);
        binding.btnNewMemberPrevious.setVisibility(View.VISIBLE);

    }

    public void showbankdetails() {
        binding.MemberImage.setVisibility(View.GONE);
        binding.MemberDetails.setVisibility(View.GONE);
        binding.MemberDetailsSecond.setVisibility(View.GONE);
        binding.NomineeDetails.setVisibility(View.GONE);
        binding.BankInformatiomCardView.setVisibility(View.VISIBLE);
        binding.btnNewMemberPrevious.setVisibility(View.VISIBLE);
        binding.btnNewMemberSave.setText("SAVE");
    }

    private void getDistrict(String state) {
        distList.clear();
        distList.add("~Select District~");
        try {

            JSONObject obj = new JSONObject(loadJSONFromAsset());
            // fetch JSONArray named users
            JSONArray statearray = obj.getJSONArray("states");
            for (int i = 0; i < statearray.length(); i++) {
                JSONObject jso = statearray.getJSONObject(i);
//                distList.add(jso.getString("state"));
                if (jso.getString("state") == state || jso.getString("state").equals(state)) {
//                    Toast.makeText(this, ""+jso.getString("districts"), Toast.LENGTH_SHORT).show();
                    String[] dsa = jso.getString("districts").replace("[", "").replace("]", "").replace("\"", "").split(",");
                    Collections.addAll(distList, dsa);
                    break;
                }
            }

        } catch (Exception e) {
            Log.d("DistExeption", e.toString());
        }
        CustomAdapterJson customAdapter = new CustomAdapterJson(getApplicationContext(), distList);
        mSp_dist.setAdapter(customAdapter);
    }


    private void setStateList() {
        stateList = new ArrayList<>();
        stateList.add("~Select State~");
        try {

            JSONObject obj = new JSONObject(loadJSONFromAsset());
            // fetch JSONArray named users
            JSONArray statearray = obj.getJSONArray("states");
            for (int i = 0; i < statearray.length(); i++) {
                JSONObject jso = statearray.getJSONObject(i);
                stateList.add(jso.getString("state"));
                //Toast.makeText(this, i+"-"+jso.getString("state"), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();

        }
        CustomAdapterJson customAdapter = new CustomAdapterJson(this, stateList);
        binding.spActivityNewMemberEntrySpinnerState.setAdapter(customAdapter);

    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("stateList.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void Animation() {

        binding.dataLay.setTranslationY(1000);
        binding.dataLay.setAlpha(1);
        binding.dataLay.animate().translationY(0).alpha(1).setDuration(700).setStartDelay(600).start();

        binding.topcard.setTranslationY(-1000);
        binding.topcard.setAlpha(1);
        binding.topcard.animate().translationY(0).alpha(1).setDuration(700).setStartDelay(600).start();


        binding.rightBtn.setTranslationX(1000);
        binding.rightBtn.setAlpha(1);
        binding.rightBtn.animate().translationX(0).alpha(1).setDuration(700).setStartDelay(600).start();


        binding.leftBtn.setTranslationX(-1000);
        binding.leftBtn.setAlpha(1);
        binding.leftBtn.animate().translationX(0).alpha(1).setDuration(700).setStartDelay(600).start();

    }

    private void AccessOfficeName() {
        if (GlobalUserData.employeeDataModel != null) {
            // Get the Officeid
            String officeId = GlobalUserData.employeeDataModel.getOfficeid();
            binding.officeName.setText(officeId);

        }
    }

    private void selectImage() {
        if (ContextCompat.checkSelfPermission(NewMemberActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NewMemberActivity.this
                    , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);

        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Image"), 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    /* *//* *//**//*  @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check condition
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                byte[] bytes = stream.toByteArray();
//                String encodedIdString = encodeToBase64(compressImage(bitmap));
//                byte[] bytes = Base64.decode(encodedIdString, Base64.DEFAULT);
//                sImage = Base64.encodeToString(bytes, Base64.DEFAULT);

                String encodedIdString = encodeToBase64(compressImage(bitmap));
                sImage = Base64.decode(encodedIdString, Base64.DEFAULT);

                Log.d("fv", sImage.toString());
                if (image_type.equals("idphoto")) {
                    binding.ivActivityNewMemberPicture.setImageBitmap(bitmap);
                    member_img = sImage;
                    Log.d("img", "" + member_img);
                } else if (image_type.equals("signature")) {
                    binding.ivActivityNewMemberSignature.setImageBitmap(bitmap);
                    member_signature = sImage;
                    Log.d("img", member_signature.toString());
                } else if (image_type.equals("idProof")) {
                    binding.ivActivityNewMemberIdProof.setImageBitmap(bitmap);
                    member_idproof = sImage;
                    Log.d("img", member_idproof.toString());
                } else if (image_type.equals("addrproof")) {
                    binding.ivActivityNewMemberAddressProof.setImageBitmap(bitmap);
                    member_addrProof = sImage;
                    Log.d("img", member_addrProof.toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

*/
    private void createDialog() {
//        dialog = new Dialog(this);
//        dialog.setContentView(R.layout.pop_data_layout);
//        dialog.getWindow().setLayout(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//        );
        CustomPopUpdialog customPopUpdialog = new CustomPopUpdialog();
        dialog = customPopUpdialog.createDialog(NewMemberActivity.this, R.layout.pop_data_layout);

//        dialog.setCancelable(false);
        adhno = dialog.findViewById(R.id.adhno);
        verifybtn = dialog.findViewById(R.id.verifybtn);
        anim = dialog.findViewById(R.id.animation_view);
        baccard = dialog.findViewById(R.id.baccard);

        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adhno.getText().toString().length() > 0) {
                    if (adharno.length() == 0) {
                        adharno = adhno.getText().toString();
                        getOtp(adharno);
                    } else {
                        otp = adhno.getText().toString();
                        verifyotp();
                    }
                } else {
                    adhno.setError("Enter Data to Continue");
                }
            }
        });
        dialog.show();
    }

    private void getOtp(String adharno1) {
        String url = "http://creditscore.geniustechnoindia.com/Aadhaar_Generate_OTP";

        HashMap<String, String> params = new HashMap<>();
        params.put("API_Key", "");
        params.put("API_Pass", " ");
        params.put("Name", "");
        params.put("DateofBirth", "");
        params.put("MobileNo", "");
        params.put("MobileType", "");
        params.put("Identity_Number", adharno1);
        params.put("Identity_Type", "");
        params.put("otp", "");
        params.put("ref_id", "");


        new PostDataParserObjectResponse(NewMemberActivity.this, url, params, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                if (response != null) {
                    try {
                        Log.d("Otpresponse", response.toString());
                        if (response.toString().contains("SUCCESS")) {
                            adhno.setHint("Enter Otp");
                            adhno.setText("");
//                        Toast.makeText(NewMemberActivity.this, "" + response, Toast.LENGTH_SHORT).show();
                            ref_id = response.getString("ref_id");
                        } else {
                            adharno = "";
                            Toast.makeText(NewMemberActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        adharno = "";
                        Toast.makeText(NewMemberActivity.this, "1.Unable to get OTP", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    adharno = "";
                    Toast.makeText(NewMemberActivity.this, "2.Unable to get OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(String error) {

            }
        });

        verifybtn.setVisibility(View.VISIBLE);
        anim.setVisibility(View.GONE);
        baccard.setBackgroundColor(Color.GREEN);
    }

    private void verifyotp() {

        String url = "http://creditscore.geniustechnoindia.com/Aadhaar_Submit_OTP";

        HashMap<String, String> params = new HashMap<>();
        params.put("API_Key", "");
        params.put("API_Pass", "");
        params.put("Name", "");
        params.put("DateofBirth", "");
        params.put("MobileNo", "");
        params.put("MobileType", "");
        params.put("Identity_Number", adharno);
        params.put("Identity_Type", "");
        params.put("otp", otp);
        params.put("ref_id", ref_id);

        Log.d("mapOtpresponse", params.toString());


        new PostDataParserObjectResponse(NewMemberActivity.this, url, params, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                if (response != null) {
                    try {
                        Log.d("Otpresponse", response.toString());
//                        Toast.makeText(NewMemberActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                        if (response.toString().contains("VALID")) {
                            dialog.dismiss();
                            binding.txtNewMemberName.setText("" + response.getString("name"));
                            binding.txtNewMemberFather.setText("" + response.getString("care_of"));
                            binding.txtNewMemberAddress.setText("" + response.getString("address"));

                            JSONObject split_address = new JSONObject(response.getString("split_address"));
                            binding.txtNewMemberPinCode.setText("" + split_address.getString("pincode"));

                            String age = getAge(Integer.parseInt(response.getString("dob").split("-")[2]),
                                    Integer.parseInt(response.getString("dob").split("-")[1]),
                                    Integer.parseInt(response.getString("dob").split("-")[0]));
                            binding.txtNewMemberDOB.setText(response.getString("dob"));
                            binding.tvActivityNewMemberAge.setText("Age - " + age + " Years");
                            memberDOB = Integer.parseInt(response.getString("dob").split("-")[2] + response.getString("dob").split("-")[1] + response.getString("dob").split("-")[0]);


                            binding.txtNewMemberIDProofNo.setText("" + adharno);
                            binding.etActivityNewMemberAddressProofNo.setText("" + adharno);

                            //dataIdProof = Base64.decode(response.getString("photo_link"), Base64.DEFAULT);
                            member_img = response.getString("photo_link").getBytes();
                            byte[] img = Base64.decode(response.getString("photo_link"), Base64.DEFAULT);
                            Bitmap decodedImage = BitmapFactory.decodeByteArray(img, 0, img.length);
                            binding.ivActivityNewMemberPicture.setImageBitmap(decodedImage);
//                            mIv_memberPicture.setImageBitmap(decodedImage);

                        }
                    } catch (Exception e) {
                        //adharno = "";
                        Log.d("VarError", e.toString());
                        Toast.makeText(NewMemberActivity.this, "2.Unable to Verify OTP", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    //adharno = "";
                    Toast.makeText(NewMemberActivity.this, "1.Unable to Verify OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(String error) {

            }
        });
    }


    private void bindEventHandlers() {
        mTv_takePicture.setOnClickListener(this);
        mBtn_submitIfscCode.setOnClickListener(this);
        //OnClickListeners
        binding.leftBtn.setOnClickListener(this);


    }


    private void setViewReferences() {

        // EditTextView Id Find

        mEt_formNo = findViewById(R.id.et_activity_new_member_form_no);
        txtMemberName = findViewById(R.id.txtNewMemberName);


        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);


        txtFatherName = findViewById(R.id.txtNewMemberFather);
        txtAddress = findViewById(R.id.txtNewMemberAddress);
        txtPinCode = findViewById(R.id.txtNewMemberPinCode);
        txtPhoneNo = findViewById(R.id.txtNewMemberPhone);
        txtMemberDOB = findViewById(R.id.txtNewMemberDOB);
        txtDateOfJoin = findViewById(R.id.txtNewMemberDateOfJoin);

        txtNomineeName = findViewById(R.id.txtNewMemberNomineeName);
        txtNomineeDOB = findViewById(R.id.txtNewMemberNomineeDOB);
        //txtIDProofNo = findViewById(R.id.txtNewMemberIDProofNo);
        txtRegAmt = findViewById(R.id.txtNewMemberRegAmt);
        mEt_emailId = findViewById(R.id.et_activity_new_member_email);
        //mTv_devByGen = findViewById(R.id.tv_activity_new_member_developed_by);
        mEt_panNo = findViewById(R.id.et_activity_new_member_pan_no);

        spnNomineeRelation = findViewById(R.id.spnNewMemberNomineeRelation);
        spnIDProofNo = findViewById(R.id.spnNewMemberIDProofNo);

        rboMale = findViewById(R.id.rboMale);
        rboFemale = findViewById(R.id.rboFemale);

        mTv_takePicture = findViewById(R.id.tv_activity_new_member_take_image);

        mCiv_selectedImage = findViewById(R.id.civ_activity_new_member_selected_image);

        mIv_memberPicture = findViewById(R.id.iv_activity_new_member_picture);
        mIv_memberSignature = findViewById(R.id.iv_activity_new_member_signature);
        mIv_addrPhoto = findViewById(R.id.iv_activity_new_member_address_proof);
        mIv_idPhoto = findViewById(R.id.iv_activity_new_member_id_proof);
        mIv_bankAccPhoto = findViewById(R.id.iv_activity_new_member_bank_acc_proof);

        mLl_memberImage = findViewById(R.id.ll_activity_new_member_picture_root);
        mLl_memberSignature = findViewById(R.id.ll_activity_new_member_signature_root);

        mLl_addrProof = findViewById(R.id.ll_activity_new_member_addr_proof);
        mLl_idProof = findViewById(R.id.ll_activity_new_member_id_proof);
        mLl_bankAccProof = findViewById(R.id.ll_activity_new_member_bank_acc_proof);

        mEt_shareAmount = findViewById(R.id.et_activity_new_member_share_amount);

        btnSave = findViewById(R.id.btnNewMemberSave);
        mEt_state = findViewById(R.id.et_activity_new_member_state);
        mEt_nomineeCode = findViewById(R.id.et_activity_new_member_nominee_code);
        mSp_bloodGroup = findViewById(R.id.sp_activity_new_member_blood_group);
        mSp_GurdianType = findViewById(R.id.GurdianType);
        mSp_MemberMaritalStatus = findViewById(R.id.sp_activity_new_member_entry_spinner_marital_status);
        mSp_addressProofName = findViewById(R.id.sp_activity_new_member_address_proof);

        mEt_addressProofNo = findViewById(R.id.et_activity_new_member_address_proof_no);
        mEt_idProofNo = findViewById(R.id.txtNewMemberIDProofNo);

        mEt_bankName = findViewById(R.id.et_activity_new_member_bank_name);
        mEt_bankAccNo = findViewById(R.id.et_activity_new_member_acc_number);
        mEt_ifscCode = findViewById(R.id.et_activity_new_member_ifsc_code);

        mSp_signProofName = findViewById(R.id.sp_activity_new_member_sign_proof);

        mSp_state = findViewById(R.id.sp_activity_new_member_entry_spinner_state);
        mSp_dist = findViewById(R.id.sp_activity_new_member_entry_spinner_dist);

        mBtn_submitIfscCode = findViewById(R.id.btn_activity_new_member_submit_ifsc_code);
        mTv_bankName = findViewById(R.id.tv_activity_new_member_bank_name);
        mTv_branchName = findViewById(R.id.tv_activity_new_member_branch_name);
        mTv_applicantAge = findViewById(R.id.tv_activity_new_member_age);
        mTv_nomineeAge = findViewById(R.id.tv_activity_new_member_nominee_age);


    }

    private boolean isValidate(String Message) {
        boolean rValue = false;

        if (txtMemberName.getEditableText().toString().isEmpty()) {
            rValue = false;
            Message = "Member Name Required";
        } else if (txtDateOfJoin.getEditableText().toString().isEmpty()) {
            rValue = false;
            Message = "Date Of Join Required";
        } else if (txtMemberDOB.getEditableText().toString().isEmpty()) {
            rValue = false;
            Message = "Member Date Of Birth requred";
        } else {
            rValue = true;
        }
        return rValue;
    }

    private void insertMember() {
//        HashMap<String,String> memberData = new HashMap<>();
        pdialog.show();
        binding.btnNewMemberSave.setVisibility(View.GONE);
        JSONObject memberData = new JSONObject();
        try {
            memberData.put("FormNo", "" + (binding.etActivityNewMemberFormNo.getText().toString().length() > 0 ? binding.etActivityNewMemberFormNo.getText().toString() : "-"));
            memberData.put("MrMrs", "" + selectmember_type);
            memberData.put("MemberName", "" + (binding.txtNewMemberName.getText().toString().length() > 0 ? binding.txtNewMemberName.getText().toString() : "-"));
            memberData.put("Guardiantype", Spt_gurdiantype);
            memberData.put("Father", "" + (binding.txtNewMemberFather.getText().toString().length() > 0 ? binding.txtNewMemberFather.getText().toString() : "-"));
            memberData.put("MemberDOB", "" + (memberDOB != 0 ? memberDOB : "19900101"));
            memberData.put("Age", "" + (binding.tvActivityNewMemberAge.getText().toString().length() > 0 ? binding.tvActivityNewMemberAge.getText().toString() : "0"));
            memberData.put("Phone", "" + (binding.txtNewMemberPhone.getText().toString().length() > 0 ? binding.txtNewMemberPhone.getText().toString() : "9999999999"));
            memberData.put("EMAIL", "" + (binding.etActivityNewMemberEmail.getText().toString().length() > 0 ? binding.etActivityNewMemberEmail.getText().toString() : ""));
            memberData.put("Address", "" + (binding.txtNewMemberAddress.getText().toString().length() > 0 ? binding.txtNewMemberAddress.getText().toString() : "-"));
            memberData.put("PIN", "" + (binding.txtNewMemberPinCode.getText().toString().length() > 0 ? binding.txtNewMemberPinCode.getText().toString() : "000000"));
            memberData.put("State", "" + (selectedState.length() > 0 ? selectedState : "West Bengal"));
            memberData.put("District", "" + (selectedDist.length() > 0 ? selectedDist : "Kolkata"));
            memberData.put("Occupation", "-");
            memberData.put("Education", "-");
            memberData.put("NomineeCode", "" + (binding.etActivityNewMemberNomineeCode.getText().toString().length() > 0 ? binding.etActivityNewMemberNomineeCode.getText().toString() : "-"));
            memberData.put("Nominee", "" + (binding.txtNewMemberNomineeName.getText().toString().length() > 0 ? binding.txtNewMemberNomineeName.getText().toString() : "-"));
            memberData.put("NomineeDOB", "" + (nomineeDOB.length() > 0 ? nomineeDOB : "19900101"));
            memberData.put("NRelation", "" + (St_nomineeRelation.length() > 0 ? St_nomineeRelation : "-"));
            memberData.put("BloodGr", "" + (selectedBloodGroup.length() > 0 ? selectedBloodGroup : "-"));
            memberData.put("Sex", "" + (selectedGender.length() > 0 ? selectedGender : "-"));
            memberData.put("Idproof", "" + (selectedIdProofName.length() > 0 ? selectedIdProofName : "-"));
            memberData.put("IdproofNo", "" + (binding.txtNewMemberIDProofNo.getText().toString().length() > 0 ? binding.txtNewMemberIDProofNo.getText().toString() : "0000"));
            memberData.put("AddrProof", "" + (selectedAddressProofName.length() > 0 ? selectedAddressProofName : "-"));
            memberData.put("AddrProofNo", "" + (binding.etActivityNewMemberAddressProofNo.getText().toString().length() > 0 ? binding.etActivityNewMemberAddressProofNo.getText().toString() : "0000"));
            memberData.put("SignProof", "" + (selectedSignProofName.length() > 0 ? selectedSignProofName : "-"));
            memberData.put("PAN", "" + (binding.etActivityNewMemberPanNo.getText().toString().length() > 0 ? binding.etActivityNewMemberPanNo.getText().toString() : "-"));
            memberData.put("BankName", "" + (binding.tvActivityNewMemberBankName.getText().toString().length() > 0 ? binding.tvActivityNewMemberBankName.getText().toString() : "-"));
            memberData.put("BranchName", "" + (binding.tvActivityNewMemberBranchName.getText().toString().length() > 0 ? binding.tvActivityNewMemberBranchName.getText().toString() : "-"));
            memberData.put("AccountNo", "" + (binding.etActivityNewMemberAccNumber.getText().toString().length() > 0 ? binding.etActivityNewMemberAccNumber.getText().toString() : "-"));
            memberData.put("IFSCCode", "" + (binding.etActivityNewMemberIfscCode.getText().toString().length() > 0 ? binding.etActivityNewMemberIfscCode.getText().toString() : "-"));
            memberData.put("Pics", "" + (member_img != null ? member_img : null));
            memberData.put("Signature", "" + (member_signature != null ? member_signature : null));
            memberData.put("IDPhoto", "" + (member_idproof != null ? member_idproof : null));
            memberData.put("AddrPhoto", "" + (member_addrProof != null ? member_addrProof : null));
            memberData.put("RegAmt", "" + (binding.txtNewMemberRegAmt.getText().toString().length() > 0 ? binding.txtNewMemberRegAmt.getText().toString() : "0"));
            memberData.put("ShareAmount", "" + (binding.etActivityNewMemberShareAmount.getText().toString().length() > 0 ? binding.etActivityNewMemberShareAmount.getText().toString() : "0"));
            memberData.put("PayType", "-");
            memberData.put("ChequeNo", "-");
            memberData.put("SponsorCode", "" + GlobalUserData.employeeDataModel.getEmployeeID());
            memberData.put("UserName", "" + GlobalUserData.employeeDataModel.getEmployeeID());
            memberData.put("ErrorCode", "0");
            memberData.put("img1", (member_img != null ? Base64.encodeToString(member_img, Base64.DEFAULT) : ""));
            memberData.put("img2", (member_signature != null ? Base64.encodeToString(member_signature, Base64.DEFAULT) : ""));
            memberData.put("img3", (member_idproof != null ? Base64.encodeToString(member_idproof, Base64.DEFAULT) : ""));
            memberData.put("img4", (member_addrProof != null ? Base64.encodeToString(member_addrProof, Base64.DEFAULT) : ""));
        } catch (Exception e) {
            Log.d("DataErrors", e.toString());
//            e.printStackTrace();
        }
//        new PostDataParserObjectResponse(NewMemberActivity.this, ApiLinks.INSERT_MEMBER, memberData, new VolleyCallback() {
//            @Override
//            public void onSuccessResponse(JSONObject response) {
//                Log.d("InsertResponse", response.toString());
//                try {
//                    JSONObject jso = new JSONObject(response.getString("MemberData"));
//                    JSONObject r_status = new JSONObject(response.getString("ReturnData"));
//                    if (r_status.getString("ErrorCode").equals("0") && jso.getString("MemberCode").length() > 0) {
//                        Toast.makeText(NewMemberActivity.this, "Member Inserted Successfully", Toast.LENGTH_SHORT).show();
//                        createSuccessDialog(jso.getString("MemberCode"));
//
////                        Toast.makeText(NewMemberActivity.this, "MemberCode-" + jso.getString("MemberCode"), Toast.LENGTH_SHORT).show();
////                        startActivity(new Intent(getApplicationContext(), NewMemberActivity.class));
////                        finish();
//                    } else {
//                        Toast.makeText(NewMemberActivity.this, "Unable to InsertMember", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                    Log.d("MemberError", e.toString());
//                    Toast.makeText(NewMemberActivity.this, "Unable to InsertMember", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onErrorResponse(String error) {
//                // Handle the error response
//                Toast.makeText(NewMemberActivity.this, "Some Error Occurred At Server End", Toast.LENGTH_SHORT).show();
//                Log.e("Callback Error", error);
//            }
//        });

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                ApiLinks.INSERT_MEMBER,
                memberData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("InsertResponse", response.toString());
                        try {
                            JSONObject jso = new JSONObject(response.getString("MemberData"));
                            JSONObject r_status = new JSONObject(response.getString("ReturnData"));
                            if (r_status.getString("ErrorCode").equals("0") && jso.getString("MemberCode").length() > 0) {
                                Toast.makeText(NewMemberActivity.this, "Member Inserted Successfully", Toast.LENGTH_SHORT).show();
                                createSuccessDialog(jso.getString("MemberCode"));

//                        Toast.makeText(NewMemberActivity.this, "MemberCode-" + jso.getString("MemberCode"), Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(getApplicationContext(), NewMemberActivity.class));
//                        finish();
                            } else {
                                Toast.makeText(NewMemberActivity.this, "Unable to InsertMember", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.d("MemberError", e.toString());
                            Toast.makeText(NewMemberActivity.this, "Unable to InsertMember", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NewMemberActivity.this, "Some Error Occurred At Server End", Toast.LENGTH_SHORT).show();
                        Log.e("Callback Error", error.toString());
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(NewMemberActivity.this).add(jsonObjectRequest);

        pdialog.dismiss();
        binding.btnNewMemberSave.setVisibility(View.VISIBLE);
    }

    private void createSuccessDialog(String memberCode) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.member_success);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
//        dialog.setCancelable(false);
        TextView membercode = dialog.findViewById(R.id.membercode);
        TextView okbtn = dialog.findViewById(R.id.Okbtn);
        membercode.setText("Member Code  : " + memberCode);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewMemberActivity.class));
                finishAffinity();
            }
        });
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finishAffinity();
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
              /*  Intent intent = new Intent(NewMemberActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);*/
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mTv_takePicture) {
            //openBottomSheetDialog();
        }
       /* if (v == mBtn_submitIfscCode) {
            String url = "https://ifsc.razorpay.com/" + mEt_ifscCode.getText().toString();
            new GetDataParserObject(this, url, true, new GetDataParserObject.OnGetResponseListner() {
                @Override
                public void onGetResponse(JSONObject response) {
                    try {
                        if (response != null) {
                            mTv_bankName.setText(response.getString("BANK"));
                            mTv_branchName.setText(response.getString("BRANCH"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }*/
    }

   /* public void openBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(NewMemberActivity.this);
        bottomSheetDialog.setContentView(R.layout.custom_bottom_sheet_dialog);

        ImageView mIv_camera = bottomSheetDialog.findViewById(R.id.iv_custom_bottom_sheet_dialog_select_image);
        mIv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions()) {
                    //TODO: Permission granted
                    takeImage();
                }
            }
        });
        bottomSheetDialog.show();
    }*/

    private boolean checkAndRequestPermissions() {
        int permissionCamera = ContextCompat.checkSelfPermission(NewMemberActivity.this,
                android.Manifest.permission.CAMERA);
        int permissionStorage = 0;
        if (Build.VERSION.SDK_INT <= 28) {
            permissionStorage = ContextCompat.checkSelfPermission(NewMemberActivity.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (Build.VERSION.SDK_INT <= 28) {
            if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(NewMemberActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        ageInt++;
        String ageS = ageInt.toString();
        //Toast.makeText(this, ageS, Toast.LENGTH_SHORT).show();
        return ageS;
    }

    /**
     * Trigger image selection for photo
     **/


    private byte[] bitmapToByteArray(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        //bmp.recycle();
        return byteArray;
    }

    public static String encodeToBase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    public Bitmap compressImage(Bitmap image) {
        Bitmap decoded = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 55, out);
            decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            return decoded;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decoded;
    }

//    public static String encodeToBase64(Bitmap image) {
//        Bitmap immagex = image;
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] b = baos.toByteArray();
//        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
//
//        Log.e("LOOK", imageEncoded);
//        return imageEncoded;
//    }
//
//    public Bitmap compressImage(Bitmap image) {
//        Bitmap decoded = null;
//        try {
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            image.compress(Bitmap.CompressFormat.PNG, 55, out);
//            decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
//            return decoded;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return decoded;
//    }

}

