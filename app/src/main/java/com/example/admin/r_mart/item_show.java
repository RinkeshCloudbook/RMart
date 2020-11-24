package com.example.admin.r_mart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.r_mart.DataBase.DbHelper;
import com.example.admin.r_mart.Model.CartModel;
import com.example.admin.r_mart.Model.CategoryModel;
import com.example.admin.r_mart.Model.WeightModel;
import com.example.admin.r_mart.helpers.AppPreference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class item_show extends AppCompatActivity {
    String getIntent,itemId,selectweightData;
    TextView tvName,tvPrice,tvSave,tv_setWeight,quantity_tv,txtCartCount;
    ScrollView scroll;
    ImageView itemImage,minus_img,plus_img,img_back;
    private boolean flag = false;
    LinearLayout lin_weight,config,ll_center;
    RelativeLayout LinCartcount;
    List<WeightModel> weightModels = new ArrayList<>();
    Button btn_addtocart;
    AppPreference preference;
    CategoryModel cm;
    //List<CartModel> showlist;
    String Itemlist;
    int cartCount;
    boolean itemEsixt;
    int selectwPrice;

    DbHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_show);
        db = new DbHelper(this);

        preference = new AppPreference(this);
        getIntent = getIntent().getStringExtra("Id");

        final String User_Id = String.valueOf(preference.GetInteger("key_user_id"));
        db.ShowRecord(User_Id);

        cartCount = db.GetCartItemCount(User_Id);

        String json;

        tv_setWeight = findViewById(R.id.tv_setWeight);
        config = findViewById(R.id.config);
        lin_weight = findViewById(R.id.lin_weight);
        plus_img = findViewById(R.id.plus_img);
        minus_img = findViewById(R.id.minus_img);
        quantity_tv = findViewById(R.id.quantity_tv);
        tvName = findViewById(R.id.tvName);
        tvPrice = findViewById(R.id.tvPrice);
        tvSave = findViewById(R.id.tvSave);
        itemImage = findViewById(R.id.itemImage);
        btn_addtocart = findViewById(R.id.btn_addtocart);
        img_back = findViewById(R.id.img_back);
        scroll = findViewById(R.id.scroll);
        ll_center = findViewById(R.id.ll_center);
        LinCartcount = findViewById(R.id.LinCartcount);
        txtCartCount = findViewById(R.id.txtCartCount);

        txtCartCount.setText(String.valueOf(cartCount));
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LinCartcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),addtocart.class);
                startActivity(intent);
            }
        });

        try {
            InputStream is = getAssets().open("item_data.json");
            int s = is.available();
            byte[] b = new byte[s];
            is.read(b);
            is.close();
            json = new String(b,"UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String getId = obj.getString("item_id");
                //Log.e("Test","CM DATA :"+getId);
                if(getId.equalsIgnoreCase(getIntent)){
                    cm = new CategoryModel();
                    cm.cItem_id = obj.getString("item_id");
                    cm.cName = obj.getString("item_name");
                    cm.cPrice = obj.getString("price");
                    cm.cSavePrice = obj.getString("save_price");
                    cm.cIcon = obj.getString("item_icon");


                    for (int j = 0; j < obj.getJSONArray("weight_packs").length(); j++) {
                        JSONObject wobj = obj.getJSONArray("weight_packs").getJSONObject(j);
                        WeightModel wm = new WeightModel();
                        wm.pakageWeight = wobj.getString("pack_weight");
                        wm.available = wobj.getInt("available");
                        wm.mwprice = wobj.getInt("wPrice");
                        weightModels.add(wm);
                    }

                    tvName.setText(cm.cName);
                    tvPrice.setText(cm.cPrice);
                    tvSave.setText(cm.cSavePrice);
                    itemId = cm.cItem_id;
                    //itemImage.setImageResource(Integer.parseInt(cm.cIcon));

                    Picasso.get()
                            .load("file:///android_asset/image/"+obj.getString("item_icon"))
                            .resize(200,200).into(itemImage);
                }
            }


            config.removeAllViews();
            for (int i = 0; i < weightModels.size(); i++) {
                final String weightData = weightModels.get(i).pakageWeight;
                int quntity = weightModels.get(i).available;
                final int wPrice = weightModels.get(i).mwprice;


                if(i == 0) {
                    tv_setWeight.setText(weightData);
                    selectweightData = weightData;
                    selectwPrice = wPrice;
                }
                Button btn = new Button(item_show.this);
                btn.setText(weightData);
                btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tv_setWeight.setText(weightData);
                        selectweightData = tv_setWeight.getText().toString();

                        selectwPrice = wPrice;
                        tvPrice.setText(String.valueOf(wPrice));
                        quantity_tv.setText("1");
                        tvSave.setText(cm.cSavePrice);

                        config.setVisibility(View.GONE);
                    }
                });
                if (config != null) {
                    config.addView(btn);
                }
            }
            lin_weight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(flag){
                        config.setVisibility(View.GONE);
                        flag = false;
                    }else {
                        config.setVisibility(View.VISIBLE);
                        flag = true;
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                              runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      scroll.smoothScrollTo(0, ll_center.getBottom());
                                      //scroll.scrollTo(0, ll_center.getBottom());
                                  }
                              });
                            }
                        }, 50);
                    }
                }
            });


            plus_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int cnt = Integer.valueOf(quantity_tv.getText().toString());
                    cnt++;
                    quantity_tv.setText(String.valueOf(cnt));
                    //tvPrice.setText(String.valueOf(Float.valueOf(cm.cPrice) * cnt));
                    tvPrice.setText(String.valueOf(Float.valueOf(selectwPrice) * cnt));
                    tvSave.setText(String.valueOf(Float.valueOf(cm.cSavePrice) * cnt));
                }
            });
            minus_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int cnt = Integer.valueOf(quantity_tv.getText().toString());
                    if(cnt>0){
                        cnt = cnt-1;
                        quantity_tv.setText(String.valueOf(cnt));
                        String getPrice = tvPrice.getText().toString();
                        String getSave = tvSave.getText().toString();
                        tvPrice.setText(String.valueOf(Float.valueOf(getPrice) - Float.valueOf(selectwPrice)));
                        tvSave.setText(String.valueOf(Float.valueOf(getSave) - Float.valueOf(cm.cSavePrice)));
                    }else {
                        Log.d("src", "Value can't be less than 0");
                    }
                }
            });

            btn_addtocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int Rid = preference.GetInteger("key_user_id");
                    itemEsixt = db.itemExist(itemId,Rid);

                        if(Rid <= 0){
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);

                        }else if(itemEsixt == true){
                            Toast.makeText(getApplicationContext(),"Product Allready in Cart",Toast.LENGTH_LONG).show();
                        }
                        else if(itemEsixt == false) {
                            String Quantity = quantity_tv.getText().toString();
                            String user_Id = String.valueOf(Rid);
                            String price = tvPrice.getText().toString();
                            String Saveprice = tvSave.getText().toString();

                            db.insertCart(itemId,user_Id,Quantity,selectweightData,price,Saveprice);
                            Toast.makeText(getApplicationContext(),"Product Added in Cart",Toast.LENGTH_LONG).show();
                        }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
