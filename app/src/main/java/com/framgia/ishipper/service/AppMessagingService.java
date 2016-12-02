package com.framgia.ishipper.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.framgia.ishipper.R;
import com.framgia.ishipper.common.Log;
import com.framgia.ishipper.util.Const;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by HungNT on 10/21/16.
 */

public class AppMessagingService extends FirebaseMessagingService {
    private static final String TAG = AppMessagingService.class.getName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        showNotification(remoteMessage);
        sendBroadcastNewNotification(remoteMessage);
    }

    private void showNotification(RemoteMessage remoteMessage) {
        String action = remoteMessage.getData().get(Const.FirebaseData.CLICK_ACTION);
        Intent intent = new Intent(action);
        Bundle bundle = new Bundle();
        bundle.putString(Const.FirebaseData.INVOICE_ID,
                remoteMessage.getData().get(Const.FirebaseData.INVOICE_ID));
        bundle.putString(Const.FirebaseData.NOTIFICATION_ID,
                remoteMessage.getData().get(Const.FirebaseData.NOTIFICATION_ID));
        intent.putExtras(bundle);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setVibrate(new long[0])
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setContentIntent(resultPendingIntent);

        // Gets an instance of the NotificationManager service
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        notificationManager.notify(Const.Notification.ID, mBuilder.build());
    }

    private void sendBroadcastNewNotification(RemoteMessage remoteMessage) {
        Intent intent = new Intent();
        intent.setAction(Const.ACTION_NEW_NOTIFICATION);
        intent.putExtra(Const.KEY_TITLE, remoteMessage.getNotification().getTitle());
        intent.putExtra(Const.KEY_BODY, remoteMessage.getNotification().getBody());
        if (remoteMessage.getData().containsKey(Const.KEY_USER)) {
            intent.putExtra(Const.KEY_USER, remoteMessage.getData().get(Const.KEY_USER));
        }
        if (remoteMessage.getData().containsKey(Const.KEY_INVOICE_ID)) {
            intent.putExtra(Const.KEY_INVOICE_ID, remoteMessage.getData().get(Const.KEY_INVOICE_ID));
        }
        if (remoteMessage.getData().containsKey(Const.KEY_INVOICE)) {
            intent.putExtra(Const.KEY_INVOICE, remoteMessage.getData().get(Const.KEY_INVOICE));
        }
        sendBroadcast(intent);
    }
}
