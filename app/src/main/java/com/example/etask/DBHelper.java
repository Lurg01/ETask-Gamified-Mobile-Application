package com.example.etask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "UserIdentifier.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("Create Table user_type(user TEXT)");
        DB.execSQL("Create Table sk_uid(uid TEXT)");
        DB.execSQL("Create Table total_coins(coins TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("DROP TABLE IF EXISTS user_type");
        DB.execSQL("DROP TABLE IF EXISTS sk_uid");
        DB.execSQL("DROP TABLE IF EXISTS total_coins");

    }

    public Boolean insertUserTypeData(String user) {

        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user", user);

        long result = DB.insert("user_type", null, contentValues);
        if (result == -1)
        {
            return false;

        }
        else{
            return true;
        }

    }

    public Boolean insertUidData(String uid) {

        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uid",uid);

        long result = DB.insert("sk_uid", null, contentValues);
        if (result == -1)
        {
            return false;

        }
        else{
            return true;
        }

    }

    public Boolean insertTotalCoinsData(String coins) {

        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("coins",coins);

        long result = DB.insert("total_coins", null, contentValues);
        if (result == -1)
        {
            return false;

        }
        else{
            return true;
        }

    }



    public Boolean deleteUserTypeData(String user)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from user_type where user = ?", new String[] {user});
        if (cursor.getCount() > 0)
        {
            long result = DB.delete("user_type", "user=?", new String[] {user});
            if (result == -1)
            { return false; }
            else
            { return true;}

        }
        else
        { return false;}

    }


    public Boolean deleteUidData(String uid)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from sk_uid where uid = ?", new String[] {uid});
        if (cursor.getCount() > 0)
        {
            long result = DB.delete("sk_uid", "uid=?", new String[] {uid});
            if (result == -1)
            { return false; }
            else
            { return true;}

        }
        else
        { return false;}

    }


    public Boolean deleteTotalCoinsData(String coins)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from total_coins where coins = ?", new String[] {coins});
        if (cursor.getCount() > 0)
        {
            long result = DB.delete("total_coins", "coins=?", new String[] {coins});
            if (result == -1)
            { return false; }
            else
            { return true;}

        }
        else
        { return false;}

    }

    public Cursor getUserTypeData() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from user_type", null);
        return cursor;
    }
    public Cursor getUidData() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from sk_uid", null);
        return cursor;
    }

    public Cursor getTotalCoinsData() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from total_coins", null);
        return cursor;
    }



}
