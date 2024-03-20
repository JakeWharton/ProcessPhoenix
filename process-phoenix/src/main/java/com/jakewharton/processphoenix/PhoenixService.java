package com.jakewharton.processphoenix;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.os.Process;
import android.os.StrictMode;

/**
 * Please note that restarting a Service multiple times can result in an increasingly long delay between restart times.
 * This is a safety mechanism, since Android registers the restart of this service as a crashed service.
 * <p>
 * The observed delay periods are: 1s, 4s, 16s, 64s, 256s, 1024s. (on an Android 11 device)
 * Which seems to follow this pattern: 4^x, with x being the restart attempt minus 1.
 */
public final class PhoenixService extends IntentService {

  public PhoenixService() {
    super("PhoenixService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent == null) {
      return;
    }

    Process.killProcess(
        intent.getIntExtra(ProcessPhoenix.KEY_MAIN_PROCESS_PID, -1)); // Kill original main process

    Intent nextIntent;
    if (Build.VERSION.SDK_INT >= 33) {
      nextIntent = intent.getParcelableExtra(ProcessPhoenix.KEY_RESTART_INTENT, Intent.class);
    } else {
      nextIntent = intent.getParcelableExtra(ProcessPhoenix.KEY_RESTART_INTENT);
    }

    if (Build.VERSION.SDK_INT > 31) {
      // Disable strict mode complaining about out-of-process intents. Normally you save and restore
      // the original policy, but this process will die almost immediately after the offending call.
      StrictMode.setVmPolicy(
          new StrictMode.VmPolicy.Builder(StrictMode.getVmPolicy())
              .permitUnsafeIntentLaunch()
              .build());
    }

    if (Build.VERSION.SDK_INT >= 26) {
      startForegroundService(nextIntent);
    } else {
      startService(nextIntent);
    }

    Runtime.getRuntime().exit(0); // Kill kill kill!
  }
}
