package com.eways.etutor.Utils.Handler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by zzzzz on 10/8/2017.
 *
 * Description: Quản lý các fragment
 */

public class FragmentHandler {
    private Context context;
    private int resource;

    public FragmentHandler(Context context, int resource) {
        this.context = context;
        this.resource = resource;
    }

    public void  ChangeFragment(Fragment toFragment){
        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(resource, toFragment).commit();
    }
}