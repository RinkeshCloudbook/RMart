package com.example.admin.r_mart.Adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.r_mart.DataBase.DbHelper;
import com.example.admin.r_mart.Model.FavoriteList;
import com.example.admin.r_mart.R;
import com.example.admin.r_mart.pincode_lv;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Locale;

/**
 * Created by Admin on 2/4/2019.
 */

public class pincodeAdapter extends BaseAdapter {

    Context context;
    ArrayList<FavoriteList> newList;
    private ArrayList<FavoriteList> searchList;
    LayoutInflater inflater;

    public pincodeAdapter(Context applicationContext, ArrayList<FavoriteList> newList) {
        this.context = applicationContext;
        this.newList = newList;
        this.searchList = new ArrayList<FavoriteList>();
        inflater = (LayoutInflater.from(applicationContext));
        searchList.addAll(newList);
    }

    @Override
    public int getCount() {
        return newList.size();
    }

    @Override
    public Object getItem(int position) {
        return newList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View v = view;
        Holder holder;
        if(v == null){
            v = inflater.inflate(R.layout.pincode_list_row_item,null);
            holder = new Holder();

            holder.txt_pincode = v.findViewById(R.id.txt_pincode);
            holder.txt_area = v.findViewById(R.id.txt_area);
            v.setTag(holder);
        }else {
            holder = (Holder) v.getTag();
        }
       // holder.txt_pincode.setText(newList.get(position).pincode);
        holder.txt_pincode.setText(String.valueOf(newList.get(position).pincode));
        holder.txt_area.setText(newList.get(position).area);
        return v;
    }

    public void filter(String inputtext) {
        inputtext = inputtext.toLowerCase(Locale.getDefault());
        newList.clear();
        if(inputtext.length() == 0){
            newList.addAll(searchList);
        }else {
            for(FavoriteList fv : searchList)
            {
                String pin= String.valueOf(fv.pincode);
                if(pin.toLowerCase(Locale.getDefault()).contains(inputtext) ||
                        fv.area.toLowerCase(Locale.getDefault()).contains(inputtext)){
                    newList.add(fv);
                }
            }
        }
        notifyDataSetChanged();
    }

    private class Holder {
        TextView txt_pincode,txt_area;
    }
}
