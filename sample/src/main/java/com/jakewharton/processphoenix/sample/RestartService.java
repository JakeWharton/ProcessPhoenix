package com.jakewharton.processphoenix.sample;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import com.jakewharton.processphoenix.ProcessPhoenix;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This example service will attempt to restart after 1 second
 * <p>
 * Please note that restarting a Service multiple times can result in an increasingly long delay between restart times.
 * This is a safety mechanism, since Android registers the restart of this service as a crashed service.
 * <p>
 * The observed delay periods are: 1s, 4s, 16s, 64s, 256s, 1024s. (on an Android 11 device)
 * Which seems to follow this pattern: 4^x, with x being the restart attempt minus 1.
 */
public final class RestartService extends IntentService {

  public RestartService() {
    super("RestartService");
  }

  @SuppressLint("ForegroundServiceType")
  @Override
  protected void onHandleIntent(Intent intent) {
    // Log something to console to easily track successful restarts
    Log.d("ProcessPhoenix", "--- RestartService started with PID: " + Process.myPid() + " ---");

    if (Build.VERSION.SDK_INT >= 26) {
      startForeground(1337, NotificationBuilder.createNotification(RestartService.this));
    }

    // Trigger rebirth from a separate thread, such that the onStartCommand can finish properly
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.execute(
        () -> {
          SystemClock.sleep(1000);
          ProcessPhoenix.triggerServiceRebirth(RestartService.this, RestartService.class);
          //            ProcessPhoenix.triggerServiceRebirth(RestartService.this, new
          // Intent(RestartService.this, RestartService.class));
        });
  }
}
