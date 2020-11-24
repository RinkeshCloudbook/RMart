package com.example.admin.r_mart.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.admin.r_mart.Model.CategoryModel;
import com.example.admin.r_mart.Model.WeightModel;
import com.example.admin.r_mart.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 1/30/2019.
 */

public class WeightAdapter extends BaseAdapter{
    Context context;
    List<WeightModel> weightList;
    LayoutInflater inflater;
    public WeightAdapter(Context applicationContext, List<WeightModel> weightList) {

        this.context = applicationContext;
        this.weightList =weightList;
        inflater = (LayoutInflater.from(applicationContext));

    }

    @Override
    public int getCount() {
        return weightList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

             view = inflater.inflate(R.layout.spinner_layout_row,null);

             TextView txt_spinner = view.findViewById(R.id.txt_spinner);
             txt_spinner.setText(weightList.get(position).pakageWeight);

        return view;
    }

}
