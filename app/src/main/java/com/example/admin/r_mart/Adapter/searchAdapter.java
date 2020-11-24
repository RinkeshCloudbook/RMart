package com.example.admin.r_mart.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.r_mart.MainActivity;
import com.example.admin.r_mart.Model.CategoryModel;
import com.example.admin.r_mart.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Admin on 2/7/2019.
 */

public class searchAdapter extends BaseAdapter {
    Context context;
    ArrayList<CategoryModel> searchArraylist;
    LayoutInflater inflater;


    public searchAdapter(MainActivity mainActivity, ArrayList<CategoryModel> searchArraylist) {
        this.context = mainActivity;
        this.searchArraylist = searchArraylist;
        inflater = LayoutInflater.from(mainActivity);
    }

    @Override
    public int getCount() {
        return searchArraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return searchArraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View v = view;
        Holder holder;
        if (v == null) {
            v = inflater.inflate(R.layout.home_search_row_items, null);
            holder = new Holder();
            v.setTag(holder);

            holder.searchItem = v.findViewById(R.id.searchItem);

        } else {
            holder = (Holder) v.getTag();
        }
        holder.searchItem.setText(searchArraylist.get(position).cName);
        holder.itemId = searchArraylist.get(position).cItem_id;
        return v;
    }

    private class Holder {
        TextView searchItem;
        String itemId;
    }
}
