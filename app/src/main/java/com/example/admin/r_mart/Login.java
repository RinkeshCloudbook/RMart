package com.example.admin.r_mart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.r_mart.DataBase.DbHelper;
import com.example.admin.r_mart.helpers.AppPreference;

public class Login extends AppCompatActivity {
    Button btn_login;
    EditText input_email,input_password;
    TextView link_signup;
    DbHelper db;
    AppPreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DbHelper(this);
        btn_login = findViewById(R.id.btn_login);
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        link_signup = findViewById(R.id.link_signup);

        preference = new AppPreference(this);

        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),register.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
            Log.e("Test","Login Method");
            if(!validate()){
                onLoginFailed();
            }
            btn_login.setEnabled(false);

        final ProgressDialog pd = new ProgressDialog(Login.this,
                R.style.Theme_AppCompat);
        pd.setIndeterminate(true);
        pd.setMessage("Authenticating...");
        pd.show();

        String email = input_email.getText().toString();
        String password = input_password.getText().toString();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        onLoginSuccess();
                        pd.dismiss();
                    }
                },3000);

        int id = db.logindata(email,password);

        preference.SetInteger("key_user_id", id);

        Log.e("Test","RID Log :"+preference.GetInteger("key_user_id"));

    }

    public void onBackPressed() {

        //moveTaskToBack(false);
        finish();
    }

    private void onLoginSuccess() {
        btn_login.setEnabled(true);
        finish();
    }

    private void onLoginFailed() {
        Toast.makeText(getBaseContext(),"Login failed",Toast.LENGTH_LONG).show();
        btn_login.setEnabled(true);
    }

    private boolean validate() {

        boolean valid = true;

        String email = input_email.getText().toString();
        String password = input_password.getText().toString();

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setText("Enter Valid Email Address");
            valid = false;
        }else {
            input_email.setError(null);
        }

        if(password.isEmpty() || password.length() < 4 || password.length() > 10)
        {
            input_password.setText("Between 4 and 10 alphanumeric characters");
            valid = false;
        }else {
            input_password.setError(null);
        }
        Log.e("Test","Validation :"+valid);
        return valid;
    }
}
