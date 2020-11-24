package com.example.admin.r_mart.DataBase;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.example.admin.r_mart.Model.CartModel;
import com.example.admin.r_mart.Model.FavoriteList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 1/31/2019.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static String Db_Name = "GroceryShopping";
    private static int VERSION = 3;
    private SQLiteDatabase db;

    public DbHelper(Context context) {
        super(context, Db_Name, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE Cart_master(cart_Id INTEGER PRIMARY KEY AUTOINCREMENT,item_id TEXT,User_Id TEXT" +
                    ",Quantity TEXT,Weight TEXT,Price TEXT,SavePrice TEXT)");
            db.execSQL("CREATE TABLE Register_master(RId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "pincode INTEGER, fName TEXT,lName TEXT, number INTEGER," +
                    "email TEXT,pass TEXT)");
            db.execSQL("CREATE TABLE Pincode_master(PId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "city TEXT,area TEXT,pincode INTEGER)");
            Log.e("Table", "Created table");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS Cart_master", null);
            onCreate(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        public void insertCart(String itemId, String user_Id, String quantity, String weightData, String price,String Saveprice) {
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("item_id", itemId);
        cv.put("User_Id", user_Id);
        cv.put("Quantity", quantity);
        cv.put("Weight", weightData);
        cv.put("Price", price);
        cv.put("SavePrice", Saveprice);

        try {
            long result = db.insert("Cart_master", null, cv);
            Log.e("Test", "Record Inserted : "+result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }
    public boolean itemExist(String itmId,int RId){
        db = getReadableDatabase();
        //Cursor cursor = db.rawQuery("Select * from "+"Cart_master"+"where"+"item_id"+"="+itmId,null);
        Cursor cursor = db.query("Cart_master", null, "item_id=? And User_Id=?", new String[]{itmId, String.valueOf(RId)}, null, null, null);

        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
    public void registerData(String pincode, String fName, String lName, String number, String email, String pass){
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("pincode", pincode);
        cv.put("fName", fName);
        cv.put("lName", lName);
        cv.put("number", number);
        cv.put("email", email);
        cv.put("pass", pass);
        try {
            long reg_rslt = db.insert("Register_master",null,cv);
            Log.e("Test", "Register_master : "+reg_rslt);
        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();
    }

/*
    public void picodedata(){
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("city","Surat");
        cv.put("area","Dumas");
        cv.put("pincode","394120");
        try{
            Long res = db.insert("Pincode_master",null,cv);
            Log.e("Test", "Pincode : "+res);
        }catch (Exception e){
            e.printStackTrace();
        }

    }*/

    public int logindata(String email, String password){
        int RId = -1;
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select RId from Register_master where email='"+email+"' and pass='"+password+"'",null);

        if(cursor.getCount()>0){
            cursor.moveToFirst();
             RId = cursor.getInt(0);
            cursor.close();
        }db.close();

        return RId;
    }

    public String[] getRecord(String User_Id) {
        String[] list = {};
        db = getReadableDatabase();
        Cursor cursor = db.query("Cart_master", null, "User_Id=?", new String[]{User_Id}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            list = new String[cursor.getCount()];
            cursor.moveToFirst();
            int index = 0;
            do {
                String itemId = cursor.getString(cursor.getColumnIndex("item_id"));
                String UserId = cursor.getString(cursor.getColumnIndex("User_Id"));
                list[index] = itemId;
                index++;
                // Log.e("Test","ItemId :" +itemId+"| UserId :"+UserId);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return list;
    }

    public void exportDatabse(String databaseName) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/com.example.admin.r_mart/databases/"+databaseName+"";
                String backupDBPath = "Fonts/backupname.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }

    public List<CartModel> ShowRecord(String User_Id) {
        List<CartModel> list = new ArrayList<>();
        db = getReadableDatabase();

        Cursor cursor = db.query("Cart_master", null, "User_Id=?", new String[]{User_Id}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    CartModel cm = new CartModel();

                    cm.mItem_id = cursor.getString(cursor.getColumnIndex("item_id"));
                    cm.mQuantity = cursor.getString(cursor.getColumnIndex("Quantity"));
                    cm.mWeight = cursor.getString(cursor.getColumnIndex("Weight"));
                    cm.mPrice = cursor.getString(cursor.getColumnIndex("Price"));
                    cm.mSavePrice = cursor.getString(cursor.getColumnIndex("SavePrice"));

                    list.add(cm);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return list;
    }



    public List<FavoriteList> getpincode(){
        List<FavoriteList> favList = new ArrayList<>();
        //Cursor cursor = db.query("Pincode_master", null, null, null, null, null, null);

        String selectQuery = "SELECT  * FROM " + "Pincode_master";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

       // db = getReadableDatabase();
        Log.e("Test","CURSUR :"+cursor.getCount());
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    FavoriteList list = new FavoriteList();
                    // list.RId = cursor.getInt(cursor.getColumnIndex("RId"));
                    list.city = cursor.getString(cursor.getColumnIndex("city"));
                    list.area = cursor.getString(cursor.getColumnIndex("area"));
                    list.pincode = cursor.getInt(cursor.getColumnIndex("pincode"));
                    //Log.e("Test","PinCode" +list.pincode);
                   // Log.e("Test","area" +list.area);
                   // Log.e("Test","city" +list.city);
                    favList.add(list);

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return favList;
    }
   /* public boolean deleteRow(String itemId)
    {
        Log.e("Test","Record Deleted"+itemId);
        //return db.delete("Cart_master","item_id" + "=" + itemId,null)>0;

    }*/
    public void deleteRow(String itemId){
        db = getWritableDatabase();
        db.delete("Cart_master","item_id=?",new String[]{itemId});
        Log.e("Test","Row Deleted");
        db.close();
    }

    public void ShowAllData(String table_name) {
        db = getReadableDatabase();
        
        Cursor c = db.query(table_name, null, null, null, null, null, null);
        
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();

            Log.e("DEBUG", "----------- " + table_name + " -------------");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < c.getColumnCount(); i++) {
                sb.append(String.format(" | %15s", c.getColumnName(i)));
            }
            Log.e("DEBUG", sb.toString());

            do {
                StringBuilder sb1 = new StringBuilder();
                for (int i = 0; i < c.getColumnCount(); i++) {
                    sb1.append(String.format(" | %15s ", c.getString(i)));
                }
                Log.e("DEBUG", sb1.toString());

            }while (c.moveToNext());

            c.close();
        }
        else
            Log.e("DEBUG", "No data found");

        db.close();
    }

    public int GetCartItemCount(String User_Id){
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + "Cart_master",null);
        int count = cursor.getCount();
        cursor.close();
        Log.e("Test","Count :"+count);

        return count;
    }
}
