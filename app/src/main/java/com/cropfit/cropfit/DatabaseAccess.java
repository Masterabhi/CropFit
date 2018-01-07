package com.cropfit.cropfit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 03-11-2017.
 */

public class DatabaseAccess {


    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    public DatabaseAccess(Context context)
    {
        this.openHelper=new DatabaseOpenHelper(context);
    }
    public static DatabaseAccess getInstance(Context context)
    {
        if(instance== null)
        {
            instance=new DatabaseAccess(context);
        }
        return  instance;
    }
    public void open()
    {
        this.database = openHelper.getWritableDatabase();
    }
     public void  close()
     {
         if(database!= null)
         {
             this.database.close();
         }
     }
     public List<String> getQuotes()
     {
         List<String> list = new ArrayList<>();
         Cursor cursor =database.rawQuery("select crop_name from Crops where crop_id IN(Select crop_id from CropDistrict where District_id IN (SELECT District_id from District where D_name='AMBALA'));",null);
         cursor.moveToFirst();
         while (!cursor.isAfterLast()) {
             list.add(cursor.getString(0));
             cursor.moveToNext();
         }
         cursor.close();
         return list;
     }

     public void getInfo(String temp,String humid,String moist,String press,String ph){


     }


}
