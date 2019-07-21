package com.chorestory.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.chorestory.Interface.RetrofitInterface;
import com.chorestory.R;
import com.chorestory.app.App;
import com.chorestory.helpers.TokenHandler;
import com.chorestory.templates.SaveRegistrationIdRequest;
import com.chorestory.templates.SingleResponse;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationService extends FirebaseMessagingService {
    @Inject
    RetrofitInterface retrofitInference;
    @Inject
    TokenHandler tokenHandler;

    private final String TAG = getClass().getName();

    // Only called when app in foreground
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            // TODO: set notificationId
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), 1, getString(R.string.notification_channel_id));
        }
    }

    private void showNotification(String title, String body, int notificationId, String channelId) {
        Log.d(TAG, "Notification title:" + title + ", Notification body: " + body);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setSmallIcon(R.drawable.app_icon);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        // TODO
        // Set the intent that will fire when the user taps the notification
        // builder.setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(notificationId, builder.build());
    }

    public void createNotificationChannel(Context context, String channelId) {
        Log.d(TAG, "creating notification channel: " + channelId);
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notification_channel";
            String description = "notification_channel_description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendRegistrationToServer(String registrationId, Context context) {
        Log.d(TAG, "sending registration ID to server: " + registrationId);
        App.getAppComponent().inject(this);
        String token = tokenHandler.getToken(context);

        SaveRegistrationIdRequest saveRegistrationIdRequest = new SaveRegistrationIdRequest(registrationId);
        Call<SingleResponse<Boolean>> saveRegistrationId = retrofitInference.save_registration_id(token, saveRegistrationIdRequest);

        saveRegistrationId.enqueue(new Callback<SingleResponse<Boolean>>() {
            @Override
            public void onResponse(Call<SingleResponse<Boolean>> call, Response<SingleResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().hasResponse()) {
                    Log.d(TAG, "save registration ID succeeded");
                }
            }
            @Override
            public void onFailure(Call<SingleResponse<Boolean>> call, Throwable t) {
                Log.d(TAG, "save registration ID failed");
            }
        });
    }
}
