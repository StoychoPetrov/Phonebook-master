package com.example.stoycho.phonebook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.stoycho.phonebook.models.Country;
import com.example.stoycho.phonebook.models.User;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stoycho on 10/20/2016.
 */

public class Database extends SQLiteOpenHelper {

    /********** Database info ************/
    private static final String DATABASE_NAME           = "phonebook";
    private static final int DATABASE_VERSION           = 1;

    /********** Table names **************/
    private final static String USERS_TABLE_NAME        = "users";
    private final static String COUNTRIES_TABLE_NAME    = "countries";

    /********** Users table columns ************/
    private final static String COLUMN_USER_ID          = "user_id";
    private final static String COLUMN_FIRST_NAME       = "first_name";
    private final static String COLUMN_LAST_NAME        = "last_name";
    private final static String COLUMN_EMAIL            = "email";
    private final static String COLUMN_PHONE_NUMBER     = "phone_number";
    private final static String COLUMN_GENDER           = "gender";
    private final static String COLUMN_COUNTRY_ID_FK    = "coutry_id_fk";

    /*********** Countries table columns*********/
    private final static String COLUMN_COUNTRY_ID       = "country_id";
    private final static String COLUMN_COUNTRY_NAME     = "country_name";
    private final static String COLUMN_CALLING_CODE     = "calling_code";

    /*********** Create tables *******************/
    private final static String CREATE_USERS = " CREATE TABLE " + USERS_TABLE_NAME + " ( " + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_FIRST_NAME + " TEXT, " + COLUMN_LAST_NAME + " TEXT, "
            + COLUMN_EMAIL + " TEXT, " + COLUMN_PHONE_NUMBER + " TEXT, " + COLUMN_GENDER + " TEXT, " + COLUMN_COUNTRY_ID_FK + " INTEGER, FOREIGN KEY (" + COLUMN_COUNTRY_ID_FK + ") REFERENCES " + COUNTRIES_TABLE_NAME
            + "(" + COLUMN_COUNTRY_ID + "))";
    private final static String CREATE_COUNTRIES = " CREATE TABLE " + COUNTRIES_TABLE_NAME + " ( " + COLUMN_COUNTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_COUNTRY_NAME + " TEXT, "
            + COLUMN_CALLING_CODE + " TEXT)";

    public final static int SELECT_ALL_COUNTRIES =      0;
    public final static int SELECT_SEARCH_PLACES =      1;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COUNTRIES);
        db.execSQL(CREATE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + COUNTRIES_TABLE_NAME);
            onCreate(db);
        }
    }
}
