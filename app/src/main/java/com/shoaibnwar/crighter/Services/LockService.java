package com.shoaibnwar.crighter.Services;

/**
 * Created by gold on 8/11/2018.
 */

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.shoaibnwar.crighter.BroadCast.PowerButtonBroadCast;

public class LockService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        final BroadcastReceiver mReceiver = new PowerButtonBroadCast();
        registerReceiver(mReceiver, filter);
        return START_STICKY;
    }
    public class LocalBinder extends Binder {
        LockService getService() {
            return LockService.this;
        }
    }

    @Override
    public void onDestroy() {
        final BroadcastReceiver mReceiver = new PowerButtonBroadCast();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
