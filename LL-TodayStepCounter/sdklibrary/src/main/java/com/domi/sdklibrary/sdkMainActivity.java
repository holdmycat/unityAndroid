package com.domi.sdklibrary;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;
import com.domi.sdklibrary.unity.Unity2Android;
public class sdkMainActivity extends UnityPlayerActivity {
    private static String TAG = "sdkMainActivity";
    private int mStepSum = 0;
    Unity2Android inst;
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.i("sdkMainActivity", "onCreate");
        inst = new Unity2Android();

        inst.callUnity("Main Camera","FromAndroid","zxf:hello");
    }

    //unity调用Android
    public void UnityCallAndroid () {

        Toast.makeText(this,"unity调用android成功", Toast.LENGTH_LONG).show();
        //AndroidCallUnity();
        updateStepCount();
    }

    public void updateStepCount() {
        Log.v(TAG, "updateStepCount : " + mStepSum);
        UnityPlayer.UnitySendMessage("receiveObj", "UnityMethod", String.valueOf(mStepSum));


    }
}
