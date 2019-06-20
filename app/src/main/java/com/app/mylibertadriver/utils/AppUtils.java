package com.app.mylibertadriver.utils;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class AppUtils {
    public static boolean eMailValidation(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
