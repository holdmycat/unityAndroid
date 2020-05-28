package com.domi.sdklibrary;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.today.step.lib.ISportStepInterface;
import com.today.step.lib.TodayStepManager;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;
import com.domi.sdklibrary.unity.Unity2Android;
public class sdkMainActivity extends UnityPlayerActivity {
    private static String TAG = "sdkMainActivity";
    private int mStepSum = 0;
    Unity2Android inst;

    private static final int REFRESH_STEP_WHAT = 0;

    //循环取当前时刻的步数中间的间隔时间
    private long TIME_INTERVAL_REFRESH = 3000;

    private Handler mDelayHandler = new Handler(new TodayStepCounterCall());

    private ISportStepInterface iSportStepInterface;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.i("sdkMainActivity", "onCreate");
        inst = new Unity2Android();
        //初始化计步模块
        TodayStepManager.startTodayStepService(getApplication());
        inst.callUnity("Main Camera","FromAndroid","zxf:hello");
    }

    class TodayStepCounterCall implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_STEP_WHAT: {
                    //每隔500毫秒获取一次计步数据刷新UI
                    if (null != iSportStepInterface) {
                        int step = 0;
                        try {
                            step = iSportStepInterface.getCurrentTimeSportStep();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        if (mStepSum != step) {
                            mStepSum = step;
                            //updateStepCount();
                        }
                    }
                    mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT, TIME_INTERVAL_REFRESH);

                    break;
                }
            }
            return false;
        }
    }

}
