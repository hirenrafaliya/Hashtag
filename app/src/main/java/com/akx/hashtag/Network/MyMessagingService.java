package com.akx.hashtag.Network;

import com.akx.hashtag.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.annotation.Keep;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

@Keep
public class MyMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());

        super.onMessageReceived(remoteMessage);
    }

    public void showNotification(String title,String message)
    {
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"myNotification")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.icon_round)
                .setAutoCancel(true)
                .setContentText(message);

        NotificationManagerCompat manager=NotificationManagerCompat.from(this);
        manager.notify(1,builder.build());
    }

}