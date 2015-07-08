package com.jakewharton.processphoenix.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.jakewharton.processphoenix.ProcessPhoenix;

public final class MainActivity extends Activity {
  private static final String EXTRA_TEXT = "text";

  @Bind(R.id.process_id) TextView processIdView;
  @Bind(R.id.extra_text) TextView extraTextView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    processIdView.setText("Process ID: " + Process.myPid());
    extraTextView.setText("Extra Text: " + getIntent().getStringExtra(EXTRA_TEXT));
  }

  @OnClick(R.id.restart) void restart() {
    ProcessPhoenix.triggerRebirth(this);
  }

  @OnClick(R.id.restart_with_intent) void restartWithIntent() {
    Intent nextIntent = new Intent(this, MainActivity.class);
    nextIntent.putExtra(EXTRA_TEXT, "Hello!");
    ProcessPhoenix.triggerRebirth(this, nextIntent);
  }
}
