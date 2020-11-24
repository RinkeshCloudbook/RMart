package com.example.admin.r_mart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.r_mart.Adapter.cartAdapter;
import com.example.admin.r_mart.DataBase.DbHelper;
import com.example.admin.r_mart.Model.CartModel;
import com.example.admin.r_mart.Model.CategoryModel;
import com.example.admin.r_mart.helpers.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class addtocart extends AppCompatActivity {

    ImageView img_back,cartIcon;
    TextView txtCartCount,cartItems,txtSaving,totalCartAmount;
    ListView cart_lv;
    DbHelper db;
    List<CartModel> showList;
    ArrayList<CategoryModel> cartArrayList = new ArrayList<CategoryModel>();
    ArrayList<CategoryModel> newcartArrayList = new ArrayList<CategoryModel>();
    AppPreference preference;
    String Ccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtocart);
        preference = new AppPreference(addtocart.this);
        db = new DbHelper(this);

        txtSaving = findViewById(R.id.txtSaving);
        totalCartAmount = findViewById(R.id.totalCartAmount);
        txtCartCount = findViewById(R.id.txtCartCount);
        cartItems = findViewById(R.id.cartItems);
        cartIcon = findViewById(R.id.cartIcon);
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cart_lv = findViewById(R.id.cart_lv);

        String User_Id = String.valueOf(preference.GetInteger("key_user_id"));
        //getRecord = db.getRecord(User_Id);
        //db.ShowRecord(User_Id);


        showList = db.ShowRecord(User_Id);
        int cartCount = db.GetCartItemCount(Ccount);
        txtCartCount.setText(String.valueOf(cartCount));
        cartItems.setText(String.valueOf(showList.size()));
        long totalSave = 0;
        long totalPrice = 0;

        for (int i = 0; i < showList.size(); i++) {

            totalSave += Float.valueOf(showList.get(i).mSavePrice);
            totalPrice += Float.valueOf(showList.get(i).mPrice);
        }
        txtSaving.setText(String.valueOf(totalSave));
        totalCartAmount.setText(String.valueOf(totalPrice));

        String json;
        try {

            InputStream is =getAssets().open("item_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer,"UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray("items");
            //Log.e("Test","Data from dataBase :"+getRecord);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jObj = array.getJSONObject(i);
                String getItemId = jObj.getString("item_id");

                CategoryModel cm = new CategoryModel();
                cm.cItem_id = jObj.getString("item_id");
                cm.cName = jObj.getString("item_name");
                cm.cPrice = jObj.getString("price");
                cm.cSavePrice = jObj.getString("save_price");
                cm.cIcon = jObj.getString("item_icon");

                cartArrayList.add(cm);
                String Ccount = cm.cItem_id;
            }
            //Log.e("Test","Row Items :"+showList.size());

            for (int l = 0; l < cartArrayList.size(); l++) {
                for (int k = 0; k < showList.size() ; k++) {

                    if((cartArrayList.get(l).cItem_id.equalsIgnoreCase(showList.get(k).mItem_id))){
                        cartArrayList.get(l).selectQuantity = showList.get(k).mQuantity;
                        cartArrayList.get(l).SelectWeight = showList.get(k).mWeight;
                        cartArrayList.get(l).cPrice = showList.get(k).mPrice;
                        cartArrayList.get(l).cSavePrice = showList.get(k).mSavePrice;
                        newcartArrayList.add(cartArrayList.get(l));
                    }
                }
            }

            cartAdapter cartAdapter = new cartAdapter(this,newcartArrayList);
            cart_lv.setAdapter(cartAdapter);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
