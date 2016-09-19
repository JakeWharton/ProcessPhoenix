/*
 * Copyright (C) 2014 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jakewharton.processphoenix;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Process;

import static android.content.Intent.ACTION_MAIN;
import static android.content.Intent.CATEGORY_DEFAULT;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Process Phoenix facilitates restarting your application process. This should only be used for
 * things like fundamental state changes in your debug builds (e.g., changing from staging to
 * production).
 * <p>
 * Trigger process recreation by calling {@link #triggerRebirth} with a {@link Context} instance.
 */
public final class ProcessPhoenix extends Activity {
  private static final String KEY_RESTART_INTENT = "phoenix_restart_intent";

  /**
   * Call to restart the application process using the {@linkplain Intent#CATEGORY_DEFAULT default}
   * activity as an intent.
   * <p>
   * Behavior of the current process after invoking this method is undefined.
   */
  public static void triggerRebirth(Context context) {
    triggerRebirth(context, getRestartIntent(context));
  }

  /**
   * Call to restart the application process using the specified intent.
   * <p>
   * Behavior of the current process after invoking this method is undefined.
   */
  public static void triggerRebirth(Context context, Intent nextIntent) {
    Intent intent = new Intent(context, ProcessPhoenix.class);
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK); // In case we are called with non-Activity context.
    intent.putExtra(KEY_RESTART_INTENT, nextIntent);
    context.startActivity(intent);
    if (context instanceof Activity) {
      ((Activity) context).finish();
    }
    Runtime.getRuntime().exit(0); // Kill kill kill!
  }

  private static Intent getRestartIntent(Context context) {
    Intent defaultIntent = new Intent(ACTION_MAIN, null);
    defaultIntent.addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
    defaultIntent.addCategory(CATEGORY_DEFAULT);

    String packageName = context.getPackageName();
    PackageManager packageManager = context.getPackageManager();
    for (ResolveInfo resolveInfo : packageManager.queryIntentActivities(defaultIntent, 0)) {
      ActivityInfo activityInfo = resolveInfo.activityInfo;
      if (activityInfo.packageName.equals(packageName)) {
        defaultIntent.setComponent(new ComponentName(packageName, activityInfo.name));
        return defaultIntent;
      }
    }

    throw new IllegalStateException("Unable to determine default activity for "
        + packageName
        + ". Does an activity specify the DEFAULT category in its intent filter?");
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = getIntent().getParcelableExtra(KEY_RESTART_INTENT);
    startActivity(intent);
    finish();
    Runtime.getRuntime().exit(0); // Kill kill kill!
  }

  /**
   * Checks if the current process is a temporary Phoenix Process.
   * This can be used to avoid initialisation of unused resources or to prevent running code that
   * is not multi-process ready.
   *
   * @return true if the current process is a temporary Phoenix Process
   */
  public static boolean isPhoenixProcess(Context context) {
    int currentPid = Process.myPid();
    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
      if (processInfo.pid == currentPid && processInfo.processName.endsWith(":phoenix")) {
        return true;
      }
    }
    return false;
  }
}
