package com.venture.android.firebasefcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by parkheejin on 2017. 3. 15..
 */

public class MyService extends FirebaseMessagingService {
    private static final String TAG = "MessagingService";
    public MyService() {
    }

    // FCM으로부터 메시지가 수신될 경우 실행되는 메소드
    // 앱이 실행중일때는 자동적으로 onMessageReceived가 호출되어
    // Notification상태를 확인할 수 없다. 따라서 본 예제에서는
    // 안드로이드의 Notification을 띄우도록 했다.
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // 앱이 활성화되어 있으면 Firebase의 Notification이 보이지 않으므로
        UserNotification(remoteMessage.getFrom(), remoteMessage.getNotification().getBody());
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }

    public void UserNotification(String From, String message) {


        Resources res = getResources();

        Intent notificationIntent = new Intent(this, UserNotification.class);
        notificationIntent.putExtra("notificationId", 9999); //전달할 값
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentTitle(From)
                .setContentText(message)
                .setTicker(getString(R.string.app_name)+"앱에 메시지가 도착했습니다.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_LIGHTS);



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1234, builder.build());
    }
}

