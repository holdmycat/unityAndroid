package com.domi.sdklibrary;

import android.util.Log;
import android.widget.Toast;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

public class sdkMainActivity extends UnityPlayerActivity {
    private static String TAG = "sdkMainActivity";
    private int mStepSum = 0;

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
