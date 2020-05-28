package com.domi.sdklibrary;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;
import android.support.v4.content.ContextCompat;
import com.today.step.lib.ISportStepInterface;
import com.today.step.lib.TodayStepManager;
import com.today.step.lib.TodayStepService;
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

        //开启计步Service，同时绑定Activity进行aidl通信
        Intent intent = new Intent(this, TodayStepService.class);
        startService(intent);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //Activity和Service通过aidl进行通信
                iSportStepInterface = ISportStepInterface.Stub.asInterface(service);
                try {
                    mStepSum = iSportStepInterface.getCurrentTimeSportStep();
                    updateStepCount();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT, TIME_INTERVAL_REFRESH);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);

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
                            updateStepCount();
                        }
                    }
                    mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT, TIME_INTERVAL_REFRESH);

                    break;
                }
            }
            return false;
        }
    }

    private void updateStepCount() {
        Log.i(TAG, "updateStepCount : " + mStepSum);
        String step = String.valueOf(mStepSum);
        inst.callUnity("Main Camera","FromAndroid",step);
//        TextView stepTextView = (TextView) findViewById(R.id.stepTextView);
//        stepTextView.setText(mStepSum + "步");

    }

    /*****************计时器*******************/
    private Runnable timeRunable = new Runnable() {
        @Override
        public void run() {

            currentSecond = currentSecond + 1000;

            if (!isPause) {
                //递归调用本runable对象，实现每隔一秒一次执行任务
                mhandmhandlele.postDelayed(this, 1000);
            }
        }
    };
    //计时器
    private Handler mhandmhandlele = new Handler();
    private boolean isPause = false;//是否暂停
    private long currentSecond = 0;//当前毫秒数
    /*****************计时器*******************/

    /**
     * 根据毫秒返回时分秒
     *
     * @param time
     * @return
     */
    public static String getFormatHMS(long time) {
        time = time / 1000;//总秒数
        int s = (int) (time % 60);//秒
        int m = (int) (time / 60);//分
        int h = (int) (time / 3600);//秒
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}
