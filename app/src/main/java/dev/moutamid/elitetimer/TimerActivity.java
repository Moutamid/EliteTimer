package dev.moutamid.elitetimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.view.LayoutInflater.from;

public class TimerActivity extends AppCompatActivity {
    private static final String TAG = "TimerActivity";
    private Context context = TimerActivity.this;
    private boolean isGoing = true;
    private boolean firstRun = true;

    private Utils utils = new Utils();
    private Stopwatch stopWatch = new Stopwatch();
    private TextView textViewTimer;
    private LinearLayout pauseBtn, finishBtn, resetBtn,
            dropDownBtn, proBtn, lockBtn, programBtn, durationBtn;
    private Button goButton;
    private Animation slide_down;
    private Animation slide_up;

    private ArrayList<String> goTimeStampsList = new ArrayList<>();
    private ArrayList<String> restTimeStampsList = new ArrayList<>();

    private RecyclerView conversationRecyclerView;
    private RecyclerViewAdapterMessages adapter;

    private boolean isDown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_timer);

        initViews();

        ImageView imageView = findViewById(R.id.dropdownImageView);

        dropDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDown) {

                    animateViews(true);
                    imageView.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);

                    isDown = false;
                } else {

                    animateViews(false);
                    imageView.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);

                    isDown = true;
                }

            }

            private void animateViews(boolean value1) {
                Animation currentAnimation;

                if (value1) {
                    currentAnimation = slide_up;
                } else {
                    currentAnimation = slide_down;
                }

                currentAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (currentAnimation == slide_down) {
                            proBtn.setVisibility(View.INVISIBLE);
                            lockBtn.setVisibility(View.INVISIBLE);
                            programBtn.setVisibility(View.INVISIBLE);
                            durationBtn.setVisibility(View.INVISIBLE);
                            pauseBtn.setVisibility(View.INVISIBLE);
                            finishBtn.setVisibility(View.INVISIBLE);
                            resetBtn.setVisibility(View.INVISIBLE);

                            proBtn.setEnabled(false);
                            lockBtn.setEnabled(false);
                            programBtn.setEnabled(false);
                            durationBtn.setEnabled(false);
                            pauseBtn.setEnabled(false);
                            finishBtn.setEnabled(false);
                            resetBtn.setEnabled(false);
                        } else {

                            proBtn.setVisibility(View.VISIBLE);
                            lockBtn.setVisibility(View.VISIBLE);
                            programBtn.setVisibility(View.VISIBLE);
                            durationBtn.setVisibility(View.VISIBLE);
                            pauseBtn.setVisibility(View.VISIBLE);
                            finishBtn.setVisibility(View.VISIBLE);
                            resetBtn.setVisibility(View.VISIBLE);

                            proBtn.setEnabled(true);
                            lockBtn.setEnabled(true);
                            programBtn.setEnabled(true);
                            durationBtn.setEnabled(true);
                            pauseBtn.setEnabled(true);
                            finishBtn.setEnabled(true);
                            resetBtn.setEnabled(true);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                proBtn.startAnimation(currentAnimation);
                lockBtn.startAnimation(currentAnimation);
                programBtn.startAnimation(currentAnimation);
                durationBtn.startAnimation(currentAnimation);
                pauseBtn.startAnimation(currentAnimation);
                finishBtn.startAnimation(currentAnimation);
                resetBtn.startAnimation(currentAnimation);
            }
        });

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
                    textViewTimer.setText("00:00");
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

                int place;

                if (isGoing) {
                    goButton.setBackgroundResource(R.drawable.bg_rest_btn_timer);
                    goButton.setText("Rest");
                    goButton.setTextColor(getResources().getColor(R.color.darkGrey));

                    place = 0;
                    isGoing = false;
                } else {
                    goButton.setBackgroundResource(R.drawable.bg_go_btn_timer);
                    goButton.setText("Go");
                    goButton.setTextColor(getResources().getColor(R.color.white));

                    place = 1;
                    isGoing = true;
                }

                if (firstRun) {
                    stopWatch.start();
                    firstRun = false;
                    return;
                }

                String currentTimeString = stopWatch.toString();

                if (place == 1) {
                    // GO TIME
                    goTimeStampsList.add(currentTimeString);
                } else {
                    // REST TIME
                    restTimeStampsList.add(currentTimeString);

                    initRecyclerView();
                }
                stopWatch.restart();
            }
        });

        stopWatch.setListener(new Stopwatch.StopWatchListener() {
            @Override
            public void onTick(String time) {
                textViewTimer.setText(time);
            }
        });

    }

    private void initViews() {
        if (!utils.getStoredBoolean(context, Constants.IS_LOGGED_IN)) {
            Intent intent = new Intent(TimerActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            startActivity(intent);
        }

        slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        textViewTimer = findViewById(R.id.timer_text_activity);
        pauseBtn = findViewById(R.id.pauseBtn);
        finishBtn = findViewById(R.id.stopBtn);
        resetBtn = findViewById(R.id.restartBtn);
        goButton = findViewById(R.id.goBtnTimer);

        dropDownBtn = findViewById(R.id.dropdownBtn);
        proBtn = findViewById(R.id.proBtn);
        lockBtn = findViewById(R.id.lockBtn);
        programBtn = findViewById(R.id.programBtn);
        durationBtn = findViewById(R.id.durationBtn);
    }

    private String addTime(String firstTimeStamp, String secondTimeStamp1) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date d = null;
        String newTime = "null";

        String[] secondTimeStamps = secondTimeStamp1.split(":");

        try {
            d = df.parse(firstTimeStamp);

            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.SECOND, Integer.parseInt(secondTimeStamps[2]));
            cal.add(Calendar.MINUTE, Integer.parseInt(secondTimeStamps[1]));
            cal.add(Calendar.HOUR, Integer.parseInt(secondTimeStamps[0]));

            newTime = df.format(cal.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newTime;
    }

    private void initRecyclerView() {

        conversationRecyclerView = findViewById(R.id.testRecyclerView);
        adapter = new RecyclerViewAdapterMessages();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setReverseLayout(true);
        conversationRecyclerView.setLayoutManager(linearLayoutManager);
        conversationRecyclerView.setHasFixedSize(true);
        conversationRecyclerView.setNestedScrollingEnabled(false);

        conversationRecyclerView.setAdapter(adapter);

    }

    private class RecyclerViewAdapterMessages extends RecyclerView.Adapter
            <RecyclerViewAdapterMessages.ViewHolderRightMessage> {

        @NonNull
        @Override
        public ViewHolderRightMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = from(parent.getContext()).inflate(R.layout.layout_time_stamps, parent, false);
            return new ViewHolderRightMessage(view);
        }

        ArrayList<String> total = new ArrayList<>();

        @Override
        public void onBindViewHolder(@NonNull final ViewHolderRightMessage holder, int position) {

            holder.number.setText((position + 1) + "");

            if (!goTimeStampsList.isEmpty())
                holder.go.setText(goTimeStampsList.get(position));

            if (!restTimeStampsList.isEmpty() && !goTimeStampsList.isEmpty()) {
                holder.rest.setText(restTimeStampsList.get(position));

                String finalValue = addTime(goTimeStampsList.get(position), restTimeStampsList.get(position));

                total.add(finalValue);
                holder.total.setText(finalValue);
                return;
            }

            if (!goTimeStampsList.isEmpty()) {
                total.add(goTimeStampsList.get(position));
                holder.total.setText(goTimeStampsList.get(position));
            }

        }

        @Override
        public int getItemCount() {
            if (restTimeStampsList == null)
                return 0;
            return restTimeStampsList.size();
        }

        public class ViewHolderRightMessage extends RecyclerView.ViewHolder {

            TextView number, go, rest, total;

            public ViewHolderRightMessage(@NonNull View v) {
                super(v);
                number = v.findViewById(R.id.number_activity_timestamp);
                go = v.findViewById(R.id.go_activity_timestamp);
                rest = v.findViewById(R.id.rest_activity_timestamp);
                total = v.findViewById(R.id.total_activity_timestamp);

            }
        }

    }

    //Stopwatch stopWatch = new Stopwatch();
    //stopwatch.start();
    //long minutes = stopwatch.getElapsedTimeMin();
    //Log.d("Time", stopwatch.toString());
}