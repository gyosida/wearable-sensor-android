package com.belatrix.wearablegyroscope;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.DelayedConfirmationView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartingActivity extends Activity implements DelayedConfirmationView.DelayedConfirmationListener {

    private static final long LOADING_TIME = 3000;

    private TextView tvDescription;
    private DelayedConfirmationView mDelayedConfirmation;

    private boolean isAnimating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        tvDescription = (TextView) findViewById(R.id.tv_description);
        mDelayedConfirmation = (DelayedConfirmationView) findViewById(R.id.delay_confirmation);

        findViewById(R.id.rl_loading).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAnimating) {
                    mDelayedConfirmation.setImageResource(R.drawable.ic_check);
                    isAnimating = false;
                    return;
                }
                mDelayedConfirmation.setImageResource(R.drawable.ic_close);
                mDelayedConfirmation.setTotalTimeMs(LOADING_TIME);
                mDelayedConfirmation.start();
                tvDescription.setText(getString(R.string.cancel));
                isAnimating = true;
            }
        });
        mDelayedConfirmation.setListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mDelayedConfirmation.reset();
    }

    @Override
    public void onTimerFinished(View view) {
        isAnimating = false;
        startActivity(new Intent(StartingActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onTimerSelected(View view) {
        mDelayedConfirmation.setImageResource(R.drawable.ic_check);
        mDelayedConfirmation.reset();
        tvDescription.setText(getString(R.string.start));
    }

}
