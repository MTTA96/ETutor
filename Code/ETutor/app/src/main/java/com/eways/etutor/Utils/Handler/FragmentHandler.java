package com.eways.etutor.Utils.Handler;

import android.content.Context;
import android.support.v4.app.Fragment;
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

    /** Change fragment */
    public void changeFragment(Fragment toFragment, String tag, int animationIn, int animationOut){
        if (tag != null)
            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().addToBackStack(tag).setCustomAnimations(animationIn, animationOut).replace(resource, toFragment).commit();
        else
            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().setCustomAnimations(animationIn, animationOut).replace(resource, toFragment).commit();
    }

    /** Delete current fragment */
    public void deleteCurrentFragment() {
        ((AppCompatActivity) context).getSupportFragmentManager().popBackStack();
    }

}
