package com.jakewharton.processphoenix.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.widget.TextView;
import com.jakewharton.processphoenix.ProcessPhoenix;

public final class MainActivity extends Activity {
  private static final String EXTRA_TEXT = "text";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);

    TextView processIdView = findViewById(R.id.process_id);
    TextView extraTextView = findViewById(R.id.extra_text);
    View restartButton = findViewById(R.id.restart);
    View restartWithIntentButton = findViewById(R.id.restart_with_intent);
    View restartActivityButton = findViewById(R.id.restartActivity);
    View restartServiceButton = findViewById(R.id.restart_service);

    processIdView.setText("Process ID: " + Process.myPid());
    extraTextView.setText("Extra Text: " + getIntent().getStringExtra(EXTRA_TEXT));

    restartButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            ProcessPhoenix.triggerRebirth(MainActivity.this);
          }
        });

    restartWithIntentButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent nextIntent = new Intent(MainActivity.this, MainActivity.class);
            nextIntent.putExtra(EXTRA_TEXT, "Hello!");
            ProcessPhoenix.triggerRebirth(MainActivity.this, nextIntent);
          }
        });

    restartActivityButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            ProcessPhoenix.triggerRebirth(MainActivity.this, MainActivity.class);
          }
        });

    restartServiceButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            // Start the RestartService, which initiates the service restart cycle.
            // TODO: Request permissions when the API level is high enough
            //  to require FOREGROUND_SERVICE or POST_NOTIFICATIONS
            Intent restartServiceIntent = new Intent(MainActivity.this, RestartService.class);
            if (Build.VERSION.SDK_INT >= 26) {
              startForegroundService(restartServiceIntent);
            } else {
              startService(restartServiceIntent);
            }
            finish();
          }
        });
  }
}
