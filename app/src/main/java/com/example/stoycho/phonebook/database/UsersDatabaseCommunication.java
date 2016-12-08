package com.example.stoycho.phonebook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.example.stoycho.phonebook.models.User;

/**
 * Created by stoycho.petrov on 07/12/2016.
 */

public class UsersDatabaseCommunication extends Database {

    /********** Users table columns ************/
    private final static String COLUMN_USER_ID          = "user_id";
    private final static String COLUMN_FIRST_NAME       = "first_name";
    private final static String COLUMN_LAST_NAME        = "last_name";
    private final static String COLUMN_EMAIL            = "email";
    private final static String COLUMN_PHONE_NUMBER     = "phone_number";
    private final static String COLUMN_GENDER           = "gender";
    private final static String COLUMN_COUNTRY_ID_FK    = "coutry_id_fk";

    private final static String USERS_TABLE_NAME        = "users";
    private static UsersDatabaseCommunication instance = null;

    public UsersDatabaseCommunication(Context context) {
        super(context);
    }

    public UsersDatabaseCommunication(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public static UsersDatabaseCommunication getInstance(Context context)
    {
        if(instance == null)
            instance = new UsersDatabaseCommunication(context);
        return instance;
    }

    public long saveInDatabase(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME,       user.getFirstName());
        values.put(COLUMN_LAST_NAME,        user.getLastName());
        values.put(COLUMN_EMAIL,            user.getEmail());
        values.put(COLUMN_PHONE_NUMBER,     user.getPhoneNumber());
        values.put(COLUMN_GENDER,           user.getGender());
        values.put(COLUMN_COUNTRY_ID_FK,    user.getCountry());
        long id = db.insert(USERS_TABLE_NAME, null, values);
        db.close();
        user.setId((int) id);
        return id;
    }

    public boolean updateUserInDatabase(User user)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME,    user.getFirstName());
        values.put(COLUMN_LAST_NAME,     user.getLastName());
        values.put(COLUMN_EMAIL,         user.getEmail());
        values.put(COLUMN_PHONE_NUMBER,  user.getPhoneNumber());
        values.put(COLUMN_GENDER,        user.getGender());
        values.put(COLUMN_COUNTRY_ID_FK, user.getCountry());

        int result = database.update(USERS_TABLE_NAME,values,COLUMN_USER_ID + "=?",new String[]{String.valueOf(user.getId())});
        database.close();

        return result > 0;
    }

    public boolean deleteUserFromDatabase(User user)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        boolean result          = database.delete(USERS_TABLE_NAME,COLUMN_USER_ID + "=?",new String[]{String.valueOf(user.getId())}) > 0;
        database.close();
        return result;
    }
}
