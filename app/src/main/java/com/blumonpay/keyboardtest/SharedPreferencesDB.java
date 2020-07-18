package com.blumonpay.keyboardtest;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesDB {
    private Context context;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFERENCES_NAME="layout_view";

    public SharedPreferencesDB(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
    }

    public void insertViewValue(int view, String amount){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("view",view);
        edit.putString("amount",amount);
        edit.apply();
    }

    public void insertViewValue(int view){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("view",view);
        edit.apply();
    }

    public int getViewValue(){
        return sharedPreferences.getInt("view",0);
    }

    public String getAmountValue(){
        return sharedPreferences.getString("amount","0.0");}

    public void deleteView(){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("view",0);
        edit.putString("amount","0.00");
        edit.apply();
    }


}
