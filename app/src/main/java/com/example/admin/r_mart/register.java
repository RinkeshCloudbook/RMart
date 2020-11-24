package com.example.admin.r_mart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.r_mart.DataBase.DbHelper;

public class register extends AppCompatActivity {
    ImageView imgBack;
    Button btn_submit;
    EditText edtxt_pincode,edtxt_fName,edtxt_lName,edtxt_number,edtxt_email,edtxt_pass;
    DbHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new DbHelper(this);
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edtxt_pincode = findViewById(R.id.edtxt_pincode);
        edtxt_fName = findViewById(R.id.edtxt_fName);
        edtxt_lName = findViewById(R.id.edtxt_lName);
        edtxt_number = findViewById(R.id.edtxt_number);
        edtxt_email = findViewById(R.id.edtxt_email);
        edtxt_pass = findViewById(R.id.edtxt_pass);

        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pincode = edtxt_pincode.getText().toString();
                String fName = edtxt_fName.getText().toString();
                String lName = edtxt_lName.getText().toString();
                String number = edtxt_number.getText().toString();
                String email = edtxt_email.getText().toString();
                String pass = edtxt_pass.getText().toString();

                db.registerData(pincode,fName,lName,number,email,pass);
            }
        });
    }

    private void showlist() {

    }
}
