package com.example.ymittal.backendlesslogin;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.backendless.Backendless;

public class YourBackendlessApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        
        Backendless.setUrl("https://api.backendless.com");
        Backendless.initApp(getApplicationContext(),
                getString(R.string.YOUR_APPLICATION_ID),
                getString(R.string.YOUR_ANDROID_SECRET_KEY));
    }
}