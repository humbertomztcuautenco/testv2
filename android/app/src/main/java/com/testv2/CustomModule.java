package com.testv2; // replace your-apps-package-name with your app’s package name

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import java.util.Map;
import java.util.HashMap;
import com.facebook.react.bridge.Promise;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.Calendar;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.testv2.AlarmReceiver;

public class CustomModule extends ReactContextBaseJavaModule {
    private static final int JOB_ID = 2011;
    private static ReactApplicationContext reactContext;

    private static final String TAG = "AlarmModule";
    private static final int ALARM_REQUEST_CODE = 123;

    CustomModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }
    // add to Model
    @Override
    public String getName() {
        return "CustomModule";
    }

    @ReactMethod
    public void customEvent(String name, Promise promise) {
        try {
            promise.resolve(name);
        } catch(Exception e) {
            promise.reject("Create Event Error", e);
        }
    }

     @ReactMethod
    public void scheduleAlarm(int hora1, int min1, int hora2, int min2) {
        Log.d(TAG, "Alarmas programadas: " + hora1 + ":" + min1 + " --- " + hora2 + ":" + min2);
        Context context = getReactApplicationContext();
        AlarmManager alarmManager = (AlarmManager) reactContext.getSystemService(Context.ALARM_SERVICE);

        // Configura la primera alarma a las 3 p.m.
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.HOUR_OF_DAY, hora1);
        calendar1.set(Calendar.MINUTE, min1);
        calendar1.set(Calendar.SECOND, 0);

        // Configura la segunda alarma a las 12 p.m.
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.HOUR_OF_DAY, hora2);
        calendar2.set(Calendar.MINUTE, min2);
        calendar2.set(Calendar.SECOND, 0);

         // Crea un intent para el receptor
        Intent intent1 = new Intent(reactContext, AlarmReceiver.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(reactContext, ALARM_REQUEST_CODE + 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        // Crea un intent para el receptor
        Intent intent2 = new Intent(reactContext, AlarmReceiver.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(reactContext, ALARM_REQUEST_CODE + 2, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        // Configura las alarmas
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent1);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent2);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String currentDate = dateFormat.format(new Date());

        Log.d(TAG, "Alarmas programadas: " + currentDate);
    }

    @ReactMethod
    public void cancelAlarm() {
        Context context = getReactApplicationContext();
        AlarmManager alarmManager = (AlarmManager) reactContext.getSystemService(Context.ALARM_SERVICE);

        // Crea un intent para el receptor de la primera alarma
        Intent intent1 = new Intent(reactContext, AlarmReceiver.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(reactContext, ALARM_REQUEST_CODE + 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        // Crea un intent para el receptor de la segunda alarma
        Intent intent2 = new Intent(reactContext, AlarmReceiver.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(reactContext, ALARM_REQUEST_CODE + 2, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        // Cancela las alarmas
        alarmManager.cancel(pendingIntent1);
        alarmManager.cancel(pendingIntent2);

        Log.d(TAG, "Alarmas canceladas");
    }

}