package com.example.geniousmicro.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.geniousmicro.R;
import com.example.geniousmicro.mssql.SqlManager;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class MemberContactUsActivity extends AppCompatActivity implements View.OnClickListener {

    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;


    private TextView phone_number,loacation;
    private String Mail="";
    private String Phone="";
    private String address="";

    // views
    private TextInputEditText mTie_name;
    private TextInputEditText mTie_mobileNumber;
    private TextInputEditText mTie_location;
    private TextInputEditText mTie_typeYourMessage;

    private Button mBtn_contactUs;

    private LinearLayout mLl_number58;
    private LinearLayout mLl_number65;
    private LinearLayout mLl_number14;

    private ImageView imageViewWhatsApp;
    private ImageView imageViewPhoneNumber;
    private ImageView imageViewLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us_member);

        setViewReferences();
        bindEventHandlers();

        // toolbar
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Contact Us");
        getCompanyDetails();
    }

    private void getCompanyDetails() {
        Connection cn = new SqlManager().getSQLConnection();
        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GET_COMPANY()}");
                smt.execute();
                ResultSet rs = smt.getResultSet();
                if (rs.isBeforeFirst()){
                    while (rs.next()) {

                        phone_number.setText(rs.getString("PhoneNo"));
                        loacation.setText(rs.getString("RegisteredAddress"));
                        Mail=rs.getString("Email");
                        Phone=rs.getString("PhoneNo");
                        address=rs.getString("RegisteredAddress");


                    }
                }else{
                    Toast.makeText(MemberContactUsActivity.this,"No Data Found",Toast.LENGTH_LONG).show();
                }

            }else{
                Log.d("bbc", "Error ");
            }
        } catch (Exception e) {

            Log.d("bbc", ""+e);
        }


    }

    private void setViewReferences() {
        // toolbar
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        phone_number = findViewById(R.id.phone_number);
        loacation = findViewById(R.id.loacation);

        mTie_name = findViewById(R.id.tie_activity_contact_us_your_name);
        mTie_mobileNumber = findViewById(R.id.tie_activity_contact_us_mobile_number);
        mTie_location = findViewById(R.id.tie_activity_contact_us_location);
        mTie_typeYourMessage = findViewById(R.id.tie_activity_contact_us_typeYourMessage);

        mBtn_contactUs = findViewById(R.id.btn_activity_contact_us_contact);

        mLl_number58 = findViewById(R.id.ll_activity_contact_us_number_58);
        mLl_number65 = findViewById(R.id.ll_activity_contact_us_number_65);
        mLl_number14 = findViewById(R.id.ll_activity_contact_us_number_14);

        imageViewWhatsApp = findViewById(R.id.iv_activity_contact_us_whatsapp);
        imageViewPhoneNumber=findViewById(R.id.iv_activity_contact_us_phone);
        imageViewLocation=findViewById(R.id.iv_activity_contact_us_location);
    }

    private void bindEventHandlers() {
        mBtn_contactUs.setOnClickListener(this);
        mLl_number58.setOnClickListener(this);
        mLl_number65.setOnClickListener(this);
        mLl_number14.setOnClickListener(this);
        imageViewWhatsApp.setOnClickListener(this);
        imageViewPhoneNumber.setOnClickListener(this);
        imageViewLocation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_contactUs) {
            if (mTie_name.getText().toString().trim().length() > 0) {
                if (mTie_mobileNumber.getText().toString().trim().length() > 0) {
                    if (mTie_location.getText().toString().trim().length() > 0) {
                        if (mTie_typeYourMessage.getText().toString().trim().length() > 0) {

                            Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            emailIntent.setType("plain/text");
                            emailIntent.putExtra(Intent.EXTRA_EMAIL,
                                    new String[]{"" + Mail});

                            emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                                    mTie_name.getText().toString() + "-" + mTie_typeYourMessage.getText().toString());

                            emailIntent.putExtra(Intent.EXTRA_TEXT,
                                    "Name : " + mTie_name.getText().toString() + "\n" +
                                            "Mobile number : " + mTie_mobileNumber.getText().toString() + "\n" +
                                            "Location : " + mTie_location.getText().toString() + "\n" +
                                            "Type Your Message : " + mTie_typeYourMessage.getText().toString());

                            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                                startActivity(Intent.createChooser(
                                        emailIntent, "Send mail..."));
                            }

                        } else {
                            mTie_typeYourMessage.setError("Enter type your message");
                            mTie_typeYourMessage.requestFocus();
                        }
                    } else {
                        mTie_location.setError("Enter location");
                        mTie_location.requestFocus();
                    }
                } else {
                    mTie_mobileNumber.setError("Enter mobile number");
                    mTie_mobileNumber.requestFocus();
                }
            } else {
                mTie_name.setError("Enter name");
                mTie_name.requestFocus();
            }
        } else if (v == mLl_number58) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"));
            startActivity(intent);
        } else if (v == mLl_number65) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"));
            startActivity(intent);
        } else if (v == mLl_number14) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"));
            startActivity(intent);
        } else if (v == imageViewWhatsApp) {
            try {
               /* whatsapp(MemberContactUsActivity.this, "");
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                Uri uri = Uri.parse("smsto:" + "91");
                sendIntent.setData(Uri.parse("smsto:"+"+91"));
                sendIntent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"+" +"&text="+"This is my text to send."));
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.putExtra(Intent.ACTION_SENDTO,uri);

                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
                */
                imageViewWhatsApp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String wpurl = "https://wa.me/message/A224PTIFYFEIG1";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(wpurl));
                        startActivity(intent);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if (v==imageViewPhoneNumber){
            try {
                imageViewPhoneNumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+Phone));
                        startActivity(intent);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (v==imageViewLocation){
            try {
                imageViewLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));

                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");

                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        } else {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?q=" + Uri.encode(address)));
                            startActivity(browserIntent);
                        }
//                     Intent intent = new Intent(Intent.ACTION_VIEW);
//                     intent.setData(Uri.parse("https://maps.app.goo.gl/NPP55DRZXqPUjbK7A"));
//                     Intent chooser = Intent.createChooser(intent,"Lauch Maps");
//                     startActivity(chooser);


                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /////////////////////////////////////////////

    @SuppressLint("NewApi")
    public static void whatsapp(Activity activity, String phone) {
        String formattedNumber = phone;//Util.formatPhone(phone);
        try {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "");
            sendIntent.putExtra("jid", formattedNumber + "@s.whatsapp.net");
            sendIntent.setPackage("com.whatsapp");
            activity.startActivity(sendIntent);
        } catch (Exception e) {
            //Toast.makeText(activity, "Error/n" + e.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(activity, "Please Install Whatsapp" , Toast.LENGTH_SHORT).show();
        }
    }

    //////////////////////////////////////////


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(MemberContactUsActivity.this, MemberDashboardActivity.class);
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
        Intent intent = new Intent(MemberContactUsActivity.this, MemberDashboardActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
