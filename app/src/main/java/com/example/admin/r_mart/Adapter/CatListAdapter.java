package com.example.admin.r_mart.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.r_mart.Model.CategoryModel;
import com.example.admin.r_mart.R;

import java.util.ArrayList;

/**
 * Created by Admin on 2/14/2019.
 */

public class CatListAdapter extends BaseAdapter{
    Context context;
    ArrayList<String> dupCat;
    LayoutInflater inflater;

    public CatListAdapter(Context applicationContext, ArrayList<String> catwithoutDup) {
        this.context = applicationContext;
        this.dupCat = catwithoutDup;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return dupCat.size();
    }

    @Override
    public Object getItem(int position) {
        return dupCat.get(position);
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
            v = inflater.inflate(R.layout.drawer_row_list_items,null);
            holder = new Holder();
            holder.tv_catName = v.findViewById(R.id.tv_catName);
            v.setTag(holder);
        }else {
            holder = (Holder) v.getTag();
        }
        holder.tv_catName.setText(dupCat.get(position));
        return v;
    }

    private class Holder {
        private TextView tv_catName;
    }
}
