package dev.moutamid.elitetimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimerActivity extends AppCompatActivity {
    private static final String TAG = "TimerActivity";
    private Context context = TimerActivity.this;
    private boolean isGoing = true;

    private Utils utils = new Utils();
    private Stopwatch stopWatch = new Stopwatch();
    private TextView textViewTimer;
    private LinearLayout pauseBtn, finishBtn, resetBtn;
    private Button goButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_timer);

        if (!utils.getStoredBoolean(context, Constants.IS_LOGGED_IN)) {
            Intent intent = new Intent(TimerActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            startActivity(intent);
        }

        textViewTimer = findViewById(R.id.timer_text_activity);

        pauseBtn = findViewById(R.id.pauseBtn);
        finishBtn = findViewById(R.id.stopBtn);
        resetBtn = findViewById(R.id.restartBtn);
        goButton = findViewById(R.id.goBtnTimer);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stopWatch.isRunning()) {
                    stopWatch.restart();
                }

            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stopWatch.isRunning()) {
                    stopWatch.stop();
                }
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imageView = findViewById(R.id.pauseImageView);

                if (stopWatch.isRunning()) {
                    imageView.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                    stopWatch.pause();
                } else {
                    imageView.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                    stopWatch.resume();
                }
            }
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isGoing) {

                    goButton.setBackgroundResource(R.drawable.);


                    isGoing = false;
                }


//                if (stopWatch.isRunning()) {
//                    stopWatch.restart();
//                } else {
//                    stopWatch.start();
//                }
            }
        });

        stopWatch.setListener(new Stopwatch.StopWatchListener() {
            @Override
            public void onTick(String time) {
                textViewTimer.setText(time);
            }
        });

    }

    //Stopwatch stopWatch = new Stopwatch();
    //stopwatch.start();
    //long minutes = stopwatch.getElapsedTimeMin();
    //Log.d("Time", stopwatch.toString());
}