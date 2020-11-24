package com.example.admin.r_mart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.r_mart.DataBase.DbHelper;
import com.example.admin.r_mart.helpers.AppPreference;

public class RMartGrocery extends AppCompatActivity {

    ListView RmartLV;
    TextView txt_groceryview,txt_header,txtCartCount;
    String[] listItems;
    String Getvalue;
    ImageView img_back,cartIcon;
    int cartCount;
    AppPreference preference;
    DbHelper db;
    RelativeLayout rl_cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rmart_grocery);

        db = new DbHelper(this);
        preference = new AppPreference(this);
        String User_Id = String.valueOf(preference.GetInteger("key_user_id"));
        cartCount = db.GetCartItemCount(User_Id);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Getvalue = bundle.getString("RMart");
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
        rl_cart = findViewById(R.id.rl_cart);
        txtCartCount = findViewById(R.id.txtCartCount);
        txtCartCount.setText(String.valueOf(cartCount));
        rl_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),addtocart.class);
                startActivity(intent);
            }
        });


        RmartLV = findViewById(R.id.RmartLV);
       // txt_groceryview = findViewById(R.id.txt_groceryview);
        listItems = getResources().getStringArray(R.array.array_grocery);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, listItems);
        RmartLV.setAdapter(adapter);

        RmartLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = adapter.getItem(position);
                String pos = String.valueOf(position);
                //Toast.makeText(getApplicationContext(),pos,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),productLV.class);
                intent.putExtra("value",value);
                intent.putExtra("D",pos);
                startActivity(intent);
            }
        });
    }
}
