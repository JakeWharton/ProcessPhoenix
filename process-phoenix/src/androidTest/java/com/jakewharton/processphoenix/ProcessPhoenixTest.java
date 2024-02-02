package com.jakewharton.processphoenix;

import android.app.Instrumentation;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Context.KEYGUARD_SERVICE;
import static android.content.Context.POWER_SERVICE;
import static android.content.Intent.CATEGORY_LAUNCHER;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP;
import static android.os.PowerManager.FULL_WAKE_LOCK;
import static android.os.PowerManager.ON_AFTER_RELEASE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public final class ProcessPhoenixTest {
  private static final String PROCESS_ID = "Process ID: ";
  private static final String EXTRA_TEXT = "Extra Text: ";
  private static final String TARGET_PACKAGE = "com.jakewharton.processphoenix.sample";

  private final Context context = InstrumentationRegistry.getTargetContext();
  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
  private final UiDevice device = UiDevice.getInstance(instrumentation);

  private PowerManager.WakeLock wakeLock;

  @Before public void launchSample() {
    // Wake up the screen.
    PowerManager power = (PowerManager) context.getSystemService(POWER_SERVICE);
    wakeLock = power.newWakeLock(FULL_WAKE_LOCK | ACQUIRE_CAUSES_WAKEUP | ON_AFTER_RELEASE, "test");
    wakeLock.acquire();

    // Unlock the device so that the tests can input keystrokes.
    KeyguardManager keyguard = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
    keyguard.newKeyguardLock("test").disableKeyguard();

    // Launch the sample app.
    PackageManager pm = context.getPackageManager();
    Intent intent = pm.getLaunchIntentForPackage(TARGET_PACKAGE);
    if (intent == null) {
      throw new AssertionError("ProcessPhoenix Sample not installed.");
    }
    intent.addCategory(CATEGORY_LAUNCHER);
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
    waitForWindow();
  }

  private void waitForWindow() {
    assertTrue(device.waitForWindowUpdate(TARGET_PACKAGE, SECONDS.toMillis(15)));
  }

  @After public void tearDown() {
    wakeLock.release();
  }

  @Test public void triggerRebirthCreatesNewProcess() throws UiObjectNotFoundException {
    UiObject originalObject = device.findObject(new UiSelector().textStartsWith(PROCESS_ID));
    int originalId = Integer.parseInt(originalObject.getText().substring(PROCESS_ID.length()));

    device.findObject(new UiSelector().text("Restart Process")).click();
    waitForWindow();

    UiObject newObject = device.findObject(new UiSelector().textStartsWith(PROCESS_ID));
    int newId = Integer.parseInt(newObject.getText().substring(PROCESS_ID.length()));

    assertNotEquals(originalId, newId);
  }

  @Test public void triggerRebirthWithIntentCreatesNewProcessUsingIntent()
      throws UiObjectNotFoundException {
    UiObject originalObject = device.findObject(new UiSelector().textStartsWith(PROCESS_ID));
    int originalId = Integer.parseInt(originalObject.getText().substring(PROCESS_ID.length()));

    device.findObject(new UiSelector().text("Restart Process with Intent")).click();
    waitForWindow();

    UiObject newObject = device.findObject(new UiSelector().textStartsWith(PROCESS_ID));
    int newId = Integer.parseInt(newObject.getText().substring(PROCESS_ID.length()));

    assertNotEquals(originalId, newId);

    UiObject extraObject = device.findObject(new UiSelector().textStartsWith(EXTRA_TEXT));
    String extraText = extraObject.getText().substring(EXTRA_TEXT.length());

    assertEquals("Hello!", extraText);
  }
}
