package com.chiragagg5k.bu_news_android;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Currently not working properly
 */
public class PushNotification extends FirebaseMessagingService {

    final String CHANNEL_ID = "BU_NEWS";
    final String CHANNEL_NAME = "com.chiragagg5k.bu_news_android";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {

        if (message.getNotification() != null) {
            String title = message.getNotification().getTitle();
            String body = message.getNotification().getBody();
            generateNotification(title, body);
        }
    }

    private RemoteViews getCustomDesign(String title, String message) {
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_layout);
        remoteViews.setTextViewText(R.id.notif_title, title);
        remoteViews.setTextViewText(R.id.notif_description, message);
        remoteViews.setImageViewResource(R.id.notif_icon, R.drawable.bunews_logo);
        return remoteViews;
    }

    void generateNotification(String title, String body) {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Flag One shot = PendingIntent can be used only once
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.bunews_logo)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        notificationBuilder = notificationBuilder.setContent(getCustomDesign(title, body));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);

        notificationManager.createNotificationChannel(channel);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
