package com.akx.hashtag.Network;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.akx.hashtag.BuildConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

@Keep
public class CloudMessaging extends AsyncTask {

    Context context;

    public CloudMessaging(Context context) {
        this.context = context;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {
            NotificationChannel channel=
                    new NotificationChannel("myNotification","myNotification", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager=context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Bundle bundle=new Bundle();
                            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"general");
                            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "subscribe");
                            FirebaseAnalytics.getInstance(context).logEvent("subscribe_count",bundle);
                        }

                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic("vn"+BuildConfig.VERSION_NAME).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Bundle bundle=new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,BuildConfig.VERSION_NAME);
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "subscribe");
                FirebaseAnalytics.getInstance(context).logEvent("subscribe_count",bundle);
            }
        });

        return null;
    }
}