package com.jakewharton.processphoenix.sample;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

public final class NotificationBuilder {

  /**
   * Create a Notification, required to support Service restarting on Android 8 and newer
   */
  @TargetApi(26)
  public static Notification createNotification(Context context) {
    // Android 13 or higher requires a permission to post Notifications
    if (Build.VERSION.SDK_INT >= 33) {
      if (context.checkCallingOrSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
          != PackageManager.PERMISSION_GRANTED) {
        Log.e(
            "ProcessPhoenix",
            "Required POST_NOTIFICATIONS permission was not granted, cannot restart Service");
        return null;
      }
    }

    // Android 8 or higher requires a Notification Channel
    if (Build.VERSION.SDK_INT >= 26) {
      // Creating an existing notification channel with its original values performs no operation,
      // so it's safe to call this code multiple times
      NotificationChannel channel =
          new NotificationChannel(
              "ProcessPhoenix", "ProcessPhoenix", NotificationManager.IMPORTANCE_NONE);

      // Create Notification Channel
      NotificationManager notificationManager =
          (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      notificationManager.createNotificationChannel(channel);
    }

    // Create a Notification
    return new Notification.Builder(context, "ProcessPhoenix")
        .setSmallIcon(android.R.mipmap.sym_def_app_icon)
        .setContentTitle("ProcessPhoenix")
        .setContentText("PhoenixService")
        .build();
  }
}
