package com.example.admin.r_mart.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.admin.r_mart.DataBase.DbHelper;
import com.example.admin.r_mart.Model.CategoryModel;
import com.example.admin.r_mart.R;
import com.example.admin.r_mart.addtocart;
import com.example.admin.r_mart.helpers.AppPreference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Admin on 2/1/2019.
 */

public class cartAdapter extends BaseAdapter {
    Context context;
    ArrayList<CategoryModel> cartArrayList;
    LayoutInflater inflater;
    DbHelper db;
    AppPreference preference;

    public cartAdapter(Context appliContext, ArrayList<CategoryModel> cartArrayList) {
    this.context = appliContext;
    this.cartArrayList = cartArrayList;
    inflater = (LayoutInflater.from(appliContext));
    db = new DbHelper(context);
    this.preference = new AppPreference(context);
    }

    @Override
    public int getCount() {
        return cartArrayList.size();
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
    public View getView(final int position, View view, ViewGroup parent) {
        final cartAdapter CA = (cartAdapter) getItem(position);
        View v = view;
        final Holder holder;
        if(v == null){
            v = inflater.inflate(R.layout.cart_row_item,null);
            holder = new Holder();

            holder.txt_catName = v.findViewById(R.id.txt_catName);
            holder.txt_price = v.findViewById(R.id.txt_price);
            holder.txt_save = v.findViewById(R.id.txt_save);
            holder.icon_img = v.findViewById(R.id.icon_img);
            holder.weight_tv = v.findViewById(R.id.weight_tv);
            holder.quantity_tv = v.findViewById(R.id.quantity_tv);
            holder.txt_remove = v.findViewById(R.id.txt_remove);
            holder.cart_lv = v.findViewById(R.id.cart_lv);

            v.setTag(holder);
        }else {
            holder = (Holder) v.getTag();
        }

        Picasso.get()
                .load("file:///android_asset/image/"+cartArrayList.get(position).cIcon).into(holder.icon_img);
        holder.txt_catName.setText(cartArrayList.get(position).cName);
        holder.txt_price.setText(cartArrayList.get(position).cPrice);
        holder.txt_save.setText(cartArrayList.get(position).cSavePrice);
        holder.quantity_tv.setText(cartArrayList.get(position).selectQuantity);
        holder.weight_tv.setText(cartArrayList.get(position).SelectWeight);

        holder.txt_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemId = cartArrayList.get(position).cItem_id;
                //String RId = String.valueOf(preference.GetInteger("key_user_id"));
                Log.e("Test","Deleted Id :"+itemId);
                db.deleteRow(itemId);
                cartArrayList.remove(position);
                int a = db.GetCartItemCount(itemId);
                Log.e("Test","GetCartItemCount"+a);
                cartAdapter.this.notifyDataSetChanged();

            }
        });

        return v;
    }

    private class Holder {
        private TextView txt_catName,txt_save,txt_price,quantity_tv,weight_tv,txt_remove;
        private ImageView icon_img;
        private ListView cart_lv;
    }
}
