package com.example.admin.r_mart;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.r_mart.Adapter.CatListAdapter;
import com.example.admin.r_mart.Adapter.searchAdapter;
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
import java.util.LinkedHashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private PopupWindow popupWindow;
    private DrawerLayout drawer;
    DbHelper db;
    ImageView navigationicon,cartIcon,moreIcon;
    EditText edt_txtsearch;
    ArrayList<CategoryModel> searchArraylist = new ArrayList<CategoryModel>();
    ArrayList<String> withoutDuplicat = new ArrayList<String>();
    ArrayList<CategoryModel> dupCat = new ArrayList<CategoryModel>();
    ArrayList<CategoryModel> catList = new ArrayList<CategoryModel>();
    ArrayList<CategoryModel> list = new ArrayList<CategoryModel>();
    List<CartModel> showList;
    ListView searchLV,menuList;
    searchAdapter adapter;
    AppPreference preference;
    TextView txtCartCount;
    RelativeLayout LinCartcount;
    int cartCount;
    String User_Id;
    String getCat;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preference = new AppPreference(this);

        txtCartCount = findViewById(R.id.txtCartCount);
        LinCartcount = findViewById(R.id.LinCartcount);
        LinCartcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),addtocart.class);
                startActivity(intent);
            }
        });

        db = new DbHelper(this);
        User_Id = String.valueOf(preference.GetInteger("key_user_id"));

        showList = db.ShowRecord(User_Id);
        /*Load JSON dat fir SEARCH opration*/
        searchLV = findViewById(R.id.searchLV);

        try {
            JSONObject obj = new JSONObject(readJSONFromAsset());
            JSONArray array = obj.getJSONArray("items");
            for (int i = 0; i < array.length(); i++) {
                final JSONObject jsonObject = array.getJSONObject(i);
                getCat = jsonObject.getString("category_id");

                    CategoryModel cm = new CategoryModel();
                    cm.cItem_id = jsonObject.getString("item_id");
                    cm.cName = jsonObject.getString("item_name");
                    cm.cPrice = jsonObject.getString("price");
                    cm.cSavePrice = jsonObject.getString("save_price");
                    cm.cIcon = jsonObject.getString("item_icon");
                    cm.getJson = jsonObject.toString();

                    searchArraylist.add(cm);

            }
            list.addAll(searchArraylist);
            searchArraylist.clear();
            adapter = new searchAdapter(this,searchArraylist);
            searchLV.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        searchLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemid = searchArraylist.get(position).cItem_id;

                Intent intent = new Intent(getApplicationContext(),item_show.class);
                intent.putExtra("Id",itemid);
                startActivity(intent);
            }
        });
        /*---------------JSON-------------------*/
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        menuList = hView.findViewById(R.id.menuList);

        navigationView.setNavigationItemSelectedListener(this);
        Menu m = navigationView.getMenu();
        String json;
        try {
            InputStream is = getAssets().open("item_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");

            JSONObject jobj = new JSONObject(json);
            JSONArray jArray = jobj.getJSONArray("items");

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jobject = jArray.getJSONObject(i);
                String category_id = jobject.getString("category_id");
                CategoryModel cm = new CategoryModel();
                cm.cName = jobject.getString("category_id");
               /// catList.add(cm);

                //m.add(cm.cName);
                //dupCat.add(cm);
                withoutDuplicat.add(cm.cName);
            }

            LinkedHashSet<String> duplist = new LinkedHashSet<String>(withoutDuplicat);
            ArrayList<String> catwithoutDup = new ArrayList<String>(duplist);

            final CatListAdapter catListAdapter = new CatListAdapter(this,catwithoutDup);
            menuList.setAdapter(catListAdapter);

            menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("Test","Test :"+position);
                    if(position == 0){
                        String value = (String) catListAdapter.getItem(position);
                        String pos = String.valueOf(position);
                        //Toast.makeText(getApplicationContext(),pos,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),productLV.class);
                        intent.putExtra("value",value);
                        intent.putExtra("D",pos);
                        startActivity(intent);
                    }else if(position == 1){
                        String value = (String) catListAdapter.getItem(position);
                        String pos = String.valueOf(position);
                        Intent intent = new Intent(getApplicationContext(),productLV.class);
                        intent.putExtra("value",value);
                        intent.putExtra("D",pos);
                        startActivity(intent);

                    }else if(position == 2){
                        String value = (String) catListAdapter.getItem(position);
                        String pos = String.valueOf(position);
                        Intent intent = new Intent(getApplicationContext(),productLV.class);
                        intent.putExtra("value",value);
                        intent.putExtra("D",pos);
                        startActivity(intent);

                    }else {}
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // add dynamic menus
      /*  String json;
        try {
            InputStream is = getAssets().open("item_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");

            JSONObject jobj = new JSONObject(json);
            JSONArray jArray = jobj.getJSONArray("items");

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jobject = jArray.getJSONObject(i);
                String category_id = jobject.getString("category_id");
                CategoryModel cm = new CategoryModel();
                cm.cName = jobject.getString("category_id");
                catList.add(cm);

                //m.add(cm.cName);
                dupCat.add(cm.cName);

            }
            LinkedHashSet<String> duplist = new LinkedHashSet<String>(dupCat);
            ArrayList<String> catwithoutDup = new ArrayList<String>(duplist);
            for (int i = 0; i < catwithoutDup.size(); i++) {
                m.add(catwithoutDup.get(i));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        ImageView menuIcon = findViewById(R.id.menuIcon);
        menuIcon.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }

        navigationicon = findViewById(R.id.navigationicon);
        navigationicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),pincode_lv.class);
                startActivity(i);
            }
        });

        moreIcon = findViewById(R.id.moreIcon);
        moreIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupWindow popUp = popupWindowsort();
                popUp.showAsDropDown(v, 0, 0);
            }
        });
        edt_txtsearch = findViewById(R.id.edt_txtsearch);
        edt_txtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchArraylist.clear();
                if(s.length() == 0){
                    searchArraylist.clear();
                }else {
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i).cName.toLowerCase().contains(s.toString().toLowerCase())){
                            searchArraylist.add(list.get(i));
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void onResume() {
        super.onResume();
        cartCount = db.GetCartItemCount(User_Id);
        txtCartCount.setText(String.valueOf(cartCount));
    }

    private String readJSONFromAsset()  {

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

    private PopupWindow popupWindowsort() {

        popupWindow = new PopupWindow(this);

        ArrayList<String> sortList = new ArrayList<String>();
        sortList.add("Register");
        sortList.add("Login");
        sortList.add("setting");
        sortList.add("Logout");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                sortList);
        ListView listViewSort = new ListView(this);
        listViewSort.setAdapter(adapter);

        listViewSort.setOnItemClickListener(onItemClickListener());

        popupWindow.setFocusable(true);
        popupWindow.setWidth(250);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.customborder));
        //popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setContentView(listViewSort);

        return popupWindow;
    }

    private AdapterView.OnItemClickListener onItemClickListener() {
        return new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                AppPreference preference = new AppPreference(MainActivity.this);
                if (position == 0) {
                    Intent intent = new Intent(getApplicationContext(),register.class);
                    startActivity(intent);
                } else if (position == 1) {

                    Intent intent = new Intent(getApplicationContext(),Login.class);
                    startActivity(intent);
                } else if(position == 2) {
                }else {
                    openDialog();
                    //preference.SetInteger("key_user_id",-1);

                }
                dismissPopup();
            }

        };
    }

    private void openDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        //dialog.setContentView(R.layout.dialog_logout);
        dialog.setTitle("Alert");
        dialog.setMessage("Are you sure...?");

        dialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        preference.SetInteger("key_user_id",-1);
                    }
                });
        dialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        dialog.show();
    }

    private void dismissPopup() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        navigationView.setCheckedItem(id);

        //   Log.e("Test","Data :"+navigationView.getMenu().getItem(0).setChecked(true));



        Log.e("TEst","Click Item :"+id);

        if(id == R.id.nav_RmartGrocery){
            Intent intent = new Intent(getApplicationContext(),RMartGrocery.class);
            intent.putExtra("RMart","RMart Grocery");
            startActivity(intent);
        }


       /* if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuIcon:
                if (!drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.openDrawer(GravityCompat.START);
                }
                break;
        }
    }

}
