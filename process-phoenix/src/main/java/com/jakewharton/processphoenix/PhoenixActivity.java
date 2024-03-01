package com.jakewharton.processphoenix;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.os.StrictMode;

public class PhoenixActivity extends Activity {
   @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Process.killProcess(getIntent().getIntExtra(ProcessPhoenix.KEY_MAIN_PROCESS_PID, -1)); // Kill original main process

    Intent[] intents = getIntent()
        .<Intent>getParcelableArrayListExtra(ProcessPhoenix.KEY_RESTART_INTENTS)
        .toArray(new Intent[0]);

    if (Build.VERSION.SDK_INT > 31) {
      // Disable strict mode complaining about out-of-process intents. Normally you save and restore
      // the original policy, but this process will die almost immediately after the offending call.
      StrictMode.setVmPolicy(
          new StrictMode.VmPolicy.Builder(StrictMode.getVmPolicy())
              .permitUnsafeIntentLaunch()
              .build());
    }

    startActivities(intents);
    finish();
    Runtime.getRuntime().exit(0); // Kill kill kill!
  }
}
