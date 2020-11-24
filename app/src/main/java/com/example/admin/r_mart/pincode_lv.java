package com.example.admin.r_mart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.admin.r_mart.Adapter.pincodeAdapter;
import com.example.admin.r_mart.DataBase.DbHelper;
import com.example.admin.r_mart.Model.FavoriteList;

import java.util.ArrayList;
import java.util.List;

public class pincode_lv extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ListView pincodeListview;
    DbHelper db;
    List<FavoriteList> listItem;
   // EditText edt_search;
    SearchView edt_search;
    ArrayList<FavoriteList> newList = new ArrayList<>();
    pincodeAdapter adapter;
    ImageView imgBack;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pincode_lv);

         imgBack = findViewById(R.id.imgBack);
         imgBack.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 finish();
             }
         });

        db = new DbHelper(this);

        pincodeListview = findViewById(R.id.pincodeListview);
        listItem = db.getpincode();

        for (int i = 0; i < listItem.size(); i++) {

            FavoriteList fv = new FavoriteList();
            fv.area = listItem.get(i).area;
            fv.city = listItem.get(i).city;
            fv.pincode = listItem.get(i).pincode;
           // Log.e("Test","Data :"+fv.pincode);
            newList.add(fv);
        }
        adapter = new pincodeAdapter(getApplicationContext(),newList);
        pincodeListview.setAdapter(adapter);

         edt_search = findViewById(R.id.edt_search);
         edt_search.setOnQueryTextListener(this);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
         String inputtext = newText;
         adapter.filter(inputtext);
        return false;
    }
}
