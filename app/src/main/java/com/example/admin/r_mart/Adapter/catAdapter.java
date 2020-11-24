package com.example.admin.r_mart.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.r_mart.DataBase.DbHelper;
import com.example.admin.r_mart.Login;
import com.example.admin.r_mart.Model.CategoryModel;
import com.example.admin.r_mart.R;
import com.example.admin.r_mart.helpers.AppPreference;
import com.example.admin.r_mart.productLV;
import com.example.admin.r_mart.show_items;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Admin on 1/30/2019.
 */

public class catAdapter extends BaseAdapter {
    Context context;
    ArrayList<CategoryModel> catArrayList;
    LayoutInflater inflater;
    private DbHelper db;
    AppPreference preference;

    public catAdapter(Context applicationContext, ArrayList<CategoryModel> catArrayList) {
        this.context = applicationContext;
        this.catArrayList =catArrayList;
        inflater = (LayoutInflater.from(applicationContext));
        db = new DbHelper(context);
        preference = new AppPreference(context);
    }

    @Override
    public int getCount() {
        return catArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return catArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View view, ViewGroup parent) {
        View v = view;
        final Holder holder;
        if(v == null){
            v = inflater.inflate(R.layout.category_row_items,null);
            holder = new Holder();

            holder.txt_catName = v.findViewById(R.id.txt_catName);
            holder.txt_price = v.findViewById(R.id.txt_price);
            holder.txt_save = v.findViewById(R.id.txt_save);
            holder.icon_img = v.findViewById(R.id.icon_img);
            holder.spn_weight = v.findViewById(R.id.spn_weight);
            //holder.Item_id = v.findViewById(R.id.Item_id);

            holder.quantity_tv = v.findViewById(R.id.quantity_tv);

            holder.btn_addToCart = v.findViewById(R.id.btn_addToCart);
            holder.plus_img = v.findViewById(R.id.plus_img);
            holder.minus_img = v.findViewById(R.id.minus_img);
            holder.config = v.findViewById(R.id.config);
            holder.lin_listCat = v.findViewById(R.id.lin_listCat);

            v.setTag(holder);
        }else {
            holder = (Holder) v.getTag();
        }

        holder.txt_catName.setText(catArrayList.get(position).cName);
        holder.txt_price.setText(catArrayList.get(position).cPrice);
        holder.txt_save.setText(catArrayList.get(position).cSavePrice);
        holder.quantity_tv.setText("1");


        final WeightAdapter weightadapter = new WeightAdapter(context,catArrayList.get(position).weightModels);
        holder.spn_weight.setAdapter(weightadapter);

        holder.lin_weight = v.findViewById(R.id.lin_weight);
        holder.tv_setWeight = v.findViewById(R.id.tv_setWeight);

        holder.config.removeAllViews();
        for (int i = 0; i < catArrayList.get(position).weightModels.size(); i++) {
            final String pakage = catArrayList.get(position).weightModels.get(i).pakageWeight;
            int avalable = catArrayList.get(position).weightModels.get(i).available;
            final int wPrice = catArrayList.get(position).weightModels.get(i).mwprice;

            if(i == 0){
                holder.tv_setWeight.setText(pakage);
                holder.selectedWeight = pakage;
                holder.selectAvalable = avalable;
                holder.selectwPrice = wPrice;
            }

            Button btn = new Button(context);
            btn.setText(pakage);
            btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.tv_setWeight.setText(pakage);
                    holder.selectedWeight = pakage;

                    holder.selectwPrice = wPrice;
                    holder.txt_price.setText(String.valueOf(wPrice));

                    holder.config.setVisibility(View.GONE);
                }
            });
            if (holder.config != null) {
                holder.config.addView(btn);
            }
        }

        holder.lin_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.flag){
                  holder.config.setVisibility(View.GONE);
                  holder.flag = false;
                }else {
                    holder.config.setVisibility(View.VISIBLE);
                    holder.flag = true;
                }
            }
        });

        holder.btn_addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int RId = preference.GetInteger("key_user_id");

                int available = holder.selectAvalable;
                String weightData = holder.tv_setWeight.getText().toString();
                String Quantity = holder.quantity_tv.getText().toString();
                int q = Integer.valueOf(Quantity);
                String itemId = catArrayList.get(position).cItem_id;
                String icon = catArrayList.get(position).cIcon;
                String user_Id = String.valueOf(RId);
                String price = holder.txt_price.getText().toString();
                String Saveprice = holder.txt_save.getText().toString();


                boolean itemExist = db.itemExist(itemId, RId);
                if (RId <= 0) {
                    Intent intent = new Intent(context, Login.class);
                    context.startActivity(intent);
                } else if (itemExist == true) {
                    Toast.makeText(context, "Product Allready in Cart", Toast.LENGTH_LONG).show();
                } else if (itemExist == false) {
                    if (available > q) {
                        db.insertCart(itemId, user_Id, Quantity, weightData, price, Saveprice);
                        Toast.makeText(context,"Product Added in Cart",Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("Test", "out of stock");
                    }
                }
            }
        });

        holder.plus_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cnt = Integer.valueOf(holder.quantity_tv.getText().toString());
                cnt++;
                holder.quantity_tv.setText(String.valueOf(cnt));
                holder.txt_price.setText(String.valueOf(Float.valueOf(catArrayList.get(position).cPrice) * cnt));
                holder.txt_save.setText(String.valueOf(Float.valueOf(catArrayList.get(position).cSavePrice) * cnt));

            }
        });
        holder.minus_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sub = holder.quantity_tv.getText().toString();
                int cnt = Integer.valueOf(sub);
                if (cnt > 0) {
                    cnt = cnt - 1;
                    holder.quantity_tv.setText(String.valueOf(cnt));
                    String getPrice = holder.txt_price.getText().toString();
                    String getSave = holder.txt_save.getText().toString();
                    holder.txt_price.setText(String.valueOf(Float.valueOf(getPrice) - Float.valueOf(catArrayList.get(position).cPrice)));
                    holder.txt_save.setText(String.valueOf(Float.valueOf(getSave) - Float.valueOf(catArrayList.get(position).cSavePrice)));
                } else {
                    Log.d("src", "Value can't be less than 0");
                }
            }
        });

        Picasso.get()
                .load("file:///android_asset/image/"+catArrayList.get(position).cIcon)
                .resize(150, 150)
                .into(holder.icon_img);
        holder.lin_listCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemid = catArrayList.get(position).cItem_id;
                Intent intent = new Intent(context,show_items.class);
                intent.putExtra("ItemId",itemid);
                ((productLV)context).startActivity(intent);
            }
        });

        return v;
    }

    private class Holder {
       private TextView txt_catName,txt_save,txt_price,quantity_tv,Item_id,tv_setWeight;
       private ImageView icon_img;
       private Spinner spn_weight;
       public View btn_addToCart,plus_img,minus_img;
       private LinearLayout config,lin_weight;
       private RelativeLayout lin_listCat;
       private boolean flag = false;
       private String selectedWeight;
       private int selectAvalable,selectwPrice;
    }
}
