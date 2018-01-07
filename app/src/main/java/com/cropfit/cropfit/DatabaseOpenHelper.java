package com.cropfit.cropfit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by hp on 03-11-2017.
 */

public class DatabaseOpenHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME="cropfit.db";
    private static final int DATABASE_VERSION=1;
    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
