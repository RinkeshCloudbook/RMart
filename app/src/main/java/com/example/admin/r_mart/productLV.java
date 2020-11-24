package com.example.admin.r_mart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.r_mart.Adapter.catAdapter;
import com.example.admin.r_mart.DataBase.DbHelper;
import com.example.admin.r_mart.Model.CategoryModel;
import com.example.admin.r_mart.Model.WeightModel;
import com.example.admin.r_mart.helpers.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class productLV extends AppCompatActivity {
    ListView lv_product;
    String Getvalue,Getpos;
    TextView txt_header,txtCartCount;
    ImageView img_back,cartIcon;
    ArrayList<CategoryModel> catArrayList = new ArrayList<CategoryModel>();
    DbHelper db;
    int cartCount;
    AppPreference preference;
    RelativeLayout rl_cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_lv);

        db = new DbHelper(this);
        preference = new AppPreference(this);
        String User_Id = String.valueOf(preference.GetInteger("key_user_id"));
        cartCount = db.GetCartItemCount(User_Id);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Getvalue = bundle.getString("value");
            Getpos = bundle.getString("D");
        }
        txt_header = findViewById(R.id.txt_header);
        txt_header.setText(Getvalue);

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cartIcon = findViewById(R.id.cartIcon);
        txtCartCount = findViewById(R.id.txtCartCount);
        rl_cart = findViewById(R.id.rl_cart);
        rl_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),addtocart.class);
                startActivity(intent);
            }
        });
        txtCartCount.setText(String.valueOf(cartCount));

        lv_product = findViewById(R.id.lv_product);
        try {
            JSONObject obj = new JSONObject(readJSONFromAsset());
            JSONArray array = obj.getJSONArray("items");

            for (int i = 0; i < array.length(); i++) {
                final JSONObject jsonObject = array.getJSONObject(i);
                String getCat = jsonObject.getString("category_id");

                if(getCat.equalsIgnoreCase(Getvalue)){
                    CategoryModel cm = new CategoryModel();
                    cm.cItem_id = jsonObject.getString("item_id");
                    cm.cName = jsonObject.getString("item_name");
                    cm.cPrice = jsonObject.getString("price");
                    cm.cSavePrice = jsonObject.getString("save_price");
                    cm.cIcon = jsonObject.getString("item_icon");
                    cm.getJson = jsonObject.toString();

                    List<WeightModel> weightModels = new ArrayList<>();
                    for (int j = 0; j < jsonObject.getJSONArray("weight_packs").length(); j++) {
                        JSONObject weiObj = jsonObject.getJSONArray("weight_packs").getJSONObject(j);
                        WeightModel wm = new WeightModel();
                        wm.pakageWeight = weiObj.getString("pack_weight");
                        wm.available = weiObj.getInt("available");
                        wm.mwprice = weiObj.getInt("wPrice");
                        weightModels.add(wm);
                    }
                    cm.weightModels = weightModels;
                    catArrayList.add(cm);
                }
            }
            catAdapter adapter = new catAdapter(this,catArrayList);
            lv_product.setAdapter(adapter);

            /*lv_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("Test","Click");

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String readJSONFromAsset() {

        String json;
        try {
            InputStream is = getAssets().open("item_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

}
