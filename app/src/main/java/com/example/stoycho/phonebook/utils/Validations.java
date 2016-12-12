package com.example.stoycho.phonebook.utils;

import android.telephony.PhoneNumberUtils;
import android.util.Log;
/**
 * Created by stoycho.petrov on 12/12/2016.
 */

public class Validations {

    private final static String ERROR       = "error";
    private final static String NULL_OBJECT = "The object is null !";
    private final static String EMAIL_ERROR = "The email is not correct !";
    private final static String PHONE_ERROR = "The phone is not correct !";

    public static void checkForNull(Object object)
    {
        if(object == null)
            throw new NullPointerException(NULL_OBJECT);
    }

    public static boolean emailValidation(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean phoneValidation(String phoneNumber)
    {
        if(PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber))
        {
            Log.d(ERROR,EMAIL_ERROR);
            return true;
        }
        else
        {
            Log.d(ERROR,PHONE_ERROR);
            return false;
        }
    }
}