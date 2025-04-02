package com.example.geniousmicro.Others;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;

import com.example.geniousmicro.R;

public class CustomPopUpdialog {

    Dialog dialog;
    public Dialog createDialog(Context context, int resource){
        dialog = new Dialog(context);
        dialog.setContentView(resource);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        return dialog;
    }
    public void show(){
        dialog.show();
    }
    public void dismiss(){
        dialog.dismiss();
    }
}
