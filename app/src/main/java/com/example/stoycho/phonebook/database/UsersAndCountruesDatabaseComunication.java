package com.example.stoycho.phonebook.database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.example.stoycho.phonebook.models.Country;
import com.example.stoycho.phonebook.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stoycho.petrov on 07/12/2016.
 */

public class UsersAndCountruesDatabaseComunication extends Database {

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

    public final static int WITHOUT_COUNTRY_ID = -1;

    public UsersAndCountruesDatabaseComunication(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public UsersAndCountruesDatabaseComunication(Context context) {
        super(context);
    }

    public List<User> selectUsersAndTheirCountries(List<Country> countries, int countryId, String gender, String phone)
    {
        String query = "SELECT * "
                +  "FROM " + USERS_TABLE_NAME + " users " + "INNER JOIN " + COUNTRIES_TABLE_NAME + " countries "
                +  "ON " + "users." + COLUMN_COUNTRY_ID_FK + " = countries." + COLUMN_COUNTRY_ID;
        if(countryId >= 0 && gender == null)
            query += " WHERE users." + COLUMN_COUNTRY_ID_FK + " = " + countryId;
        else if(countryId < 0 && gender != null)
            query += " WHERE users." + COLUMN_GENDER + " = '" + gender + "'";
        else if(countryId >=0 && gender != null)
            query += " WHERE users." + COLUMN_COUNTRY_ID_FK + " = " + countryId + " AND users." + COLUMN_GENDER + " = '" + gender + "'";
        else if(phone != null)
            query += " WHERE users." + COLUMN_PHONE_NUMBER + " = " + phone;

        List<User> users        = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor           = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                Country country = new Country();

                user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
                user.setFirstName(cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)));
                user.setLastName(cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                user.setGender(cursor.getString(cursor.getColumnIndex(COLUMN_GENDER)));
                user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER)));
                user.setCountry(cursor.getInt(cursor.getColumnIndex(COLUMN_COUNTRY_ID_FK)));
                users.add(user);

                country.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_COUNTRY_ID)));
                country.setCountryName(cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY_NAME)));
                country.setCallingCode(cursor.getString(cursor.getColumnIndex(COLUMN_CALLING_CODE)));
                countries.add(country);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return users;
    }
}
