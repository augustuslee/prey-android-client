/*******************************************************************************
 * Created by Carlos Yaconi
 * Copyright 2015 Prey Inc. All rights reserved.
 * License: GPLv3
 * Full license at "/LICENSE"
 ******************************************************************************/
package com.prey.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.prey.PreyConfig;
import com.prey.PreyLogger;
import com.prey.R;
import com.prey.backwardcompatibility.FroyoSupport;

public class WelcomeActivity extends FragmentActivity {

    @Override
    public void onResume() {
        PreyLogger.i("onResume of WelcomeActivity");
        super.onResume();
        menu();
    }

    @Override
    public void onPause() {
        PreyLogger.i("onPause of WelcomeActivity");
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        menu();

    }


    public void menu() {
        PreyLogger.i("menu ready:"+PreyConfig.getPreyConfig(this).getProtectReady());
        if (PreyConfig.getPreyConfig(this).getProtectReady()) {
            ready();
        } else {
            new RegisterInitTask().execute();
            Intent intent = null;
            if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO) {
                intent=new Intent(getApplicationContext(), WebViewInitActivity.class);
            }else{
                intent=new Intent(getApplicationContext(), MenuActivity.class);
            }
            startActivity(intent);
            finish();
        }
    }

    public void ready() {
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO) {
            intent = new Intent(getApplicationContext(), WebViewReadyActivity.class);
        }else{
            intent = new Intent(getApplicationContext(), DeviceReadyActivity.class);
        }
        startActivity(intent);
        finish();
    }



    private static final int SECURITY_PRIVILEGES = 10;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        PreyLogger.d("requestCode:" + requestCode + " resultCode:" + resultCode);
        if (requestCode == SECURITY_PRIVILEGES) {
            menu();
            PreyConfig.getPreyConfig(getApplicationContext()).setProtectPrivileges(true);
        }
    }

    public void addPrivileges() {
        Intent intent = FroyoSupport.getInstance(getApplicationContext()).getAskForAdminPrivilegesIntent();
        startActivityForResult(intent, SECURITY_PRIVILEGES);
    }




    private class RegisterInitTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... data) {
           /*
            try {
               PreyWebServices.getInstance().registerInit(getApplicationContext());


            } catch (Exception e) {
                PreyLogger.e("Error, causa:"+e.getMessage(),e);
            }
            */
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

        }

    }
}
