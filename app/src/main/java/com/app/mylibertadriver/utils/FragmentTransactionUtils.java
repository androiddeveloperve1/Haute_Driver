package com.app.mylibertadriver.utils;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */

public class FragmentTransactionUtils {
    public static void replaceFragmnet(Context context, int layout, Fragment fragment) {

        String Tag = fragment.getClass().getName();
        FragmentManager mngr = ((AppCompatActivity) context).getSupportFragmentManager();
        boolean fragmentPopped = mngr.popBackStackImmediate(Tag, 0);
        if (!fragmentPopped && mngr.findFragmentByTag(Tag) == null) {
            Log.e("@@@@@@", "ReplACED");
            FragmentTransaction ft = mngr.beginTransaction();
            ft.replace(layout, fragment, Tag);
            ft.addToBackStack(Tag);
            ft.commit();
        }
    }


    public static void addFragment(Context context, int layout, Fragment fragment) {
        String Tag = fragment.getClass().getName();
        FragmentManager mngr = ((AppCompatActivity) context).getSupportFragmentManager();
        boolean fragmentPopped = mngr.popBackStackImmediate(Tag, 0);
        if (!fragmentPopped && mngr.findFragmentByTag(Tag) == null) {
            Log.e("@@@@@@", "Created");
            FragmentTransaction ft = mngr.beginTransaction();
            ft.add(layout, fragment, Tag);
            ft.addToBackStack(Tag);
            ft.commit();
        }
    }

    public static void removeAllFragments(FragmentManager fragmentManager) {
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }
    }
}
