package com.domi.sdklibrary;

import android.content.Context;
import android.content.Intent;

import com.today.step.lib.BaseClickBroadcast;

public class MyReceive  extends BaseClickBroadcast {

    private static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        TSApplication tsApplication = (TSApplication) context.getApplicationContext();
        if (!tsApplication.isForeground()) {
            Intent mainIntent = new Intent(context, sdkMainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainIntent);
        } else {

        }
    }
}
