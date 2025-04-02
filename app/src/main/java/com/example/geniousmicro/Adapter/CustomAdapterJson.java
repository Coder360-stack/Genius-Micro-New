package com.example.geniousmicro.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.geniousmicro.R;

import java.util.ArrayList;

public class CustomAdapterJson extends BaseAdapter {
    Context context;
    ArrayList<String> list;
    LayoutInflater layoutInflater;

    public CustomAdapterJson(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
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

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_data_layout, parent, false);
        }
        TextView rname=convertView.findViewById(R.id.religion);
        if (position==0){
            rname.setTextColor(context.getResources().getColor(R.color.colorpurple2));
        }else {
            rname.setTextColor(context.getResources().getColor(R.color.black));
        }
        rname.setText(list.get(position));
        return convertView;
    }
}
