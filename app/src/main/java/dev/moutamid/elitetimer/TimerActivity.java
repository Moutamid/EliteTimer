package dev.moutamid.elitetimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.view.LayoutInflater.from;
import static android.view.View.VISIBLE;

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
    private ArrayList<String> totalTimeStampsList = new ArrayList<>();

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

                            proBtn.setVisibility(VISIBLE);
                            lockBtn.setVisibility(VISIBLE);
                            programBtn.setVisibility(VISIBLE);
                            durationBtn.setVisibility(VISIBLE);
                            pauseBtn.setVisibility(VISIBLE);
                            finishBtn.setVisibility(VISIBLE);
                            resetBtn.setVisibility(VISIBLE);

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

        durationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDurationDialog();
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

                String currentTimeString = stopWatch.toStringShort();

                if (place == 1) {
                    // GO TIME
                    goTimeStampsList.add(currentTimeString);
                } else {
                    // REST TIME
                    restTimeStampsList.add(currentTimeString);
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

    private void showDurationDialog() {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_duration);
        dialog.setCancelable(true);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        initRecyclerView(dialog);

        totalTimeStampsList.clear();
        for (int position = 0; position <= restTimeStampsList.size() - 1; position++) {

            String finalValue = addTime(goTimeStampsList.get(position), restTimeStampsList.get(position));

            totalTimeStampsList.add(finalValue);
        }

        String previous = null;

        for (String timeStamps : totalTimeStampsList) {

            if (previous == null) {
                previous = timeStamps;
            } else {
                previous = addTime(previous, timeStamps);

            }

        }

        TextView size_of_activities = dialog.findViewById(R.id.size_of_activities);
        TextView size_of_duration = dialog.findViewById(R.id.size_of_duration);

        TextView totalSize_of_activities = dialog.findViewById(R.id.total_size_of_activities);
        TextView total_size_of_go = dialog.findViewById(R.id.total_size_of_go);
        TextView total_size_of_rest = dialog.findViewById(R.id.total_size_of_rest);
        TextView totalsizoftotal = dialog.findViewById(R.id.total_size_of_totals);

        LinearLayout summaryLayout = dialog.findViewById(R.id.summary_option_dialogLayout);
        LinearLayout detailsLayout = dialog.findViewById(R.id.details_option_dialogLayout);

        size_of_activities.setText("        " + totalTimeStampsList.size() + "        ");
        totalSize_of_activities.setText(totalTimeStampsList.size() + "");
        size_of_duration.setText(previous);
        totalsizoftotal.setText(previous);

        previous = null;

        for (String timeStamps : goTimeStampsList) {

            if (previous == null) {
                previous = timeStamps;
            } else {
                previous = addTime(previous, timeStamps);

            }

        }

        total_size_of_go.setText(previous);

        previous = null;

        for (String timeStamps : restTimeStampsList) {

            if (previous == null) {
                previous = timeStamps;
            } else {
                previous = addTime(previous, timeStamps);

            }

        }

        total_size_of_rest.setText(previous);

        TextView detailOption = dialog.findViewById(R.id.details_option_dialog);
        TextView summaryOption = dialog.findViewById(R.id.summary_option_dialog);

        detailOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // CODE HERE
                detailsLayout.setVisibility(VISIBLE);
                summaryLayout.setVisibility(View.GONE);

                detailOption.setTextColor(getResources().getColor(R.color.darkBlue));
                summaryOption.setTextColor(getResources().getColor(R.color.skyBlue));

            }
        });

        summaryOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // CODE HERE

                detailsLayout.setVisibility(View.GONE);
                summaryLayout.setVisibility(VISIBLE);

                summaryOption.setTextColor(getResources().getColor(R.color.darkBlue));
                detailOption.setTextColor(getResources().getColor(R.color.skyBlue));
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(layoutParams);

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
        SimpleDateFormat df = new SimpleDateFormat("mm:ss");
//        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date d = null;
        String newTime = "null";

        String[] secondTimeStamps = secondTimeStamp1.split(":");

        try {
            d = df.parse(firstTimeStamp);

            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.SECOND, Integer.parseInt(secondTimeStamps[1]));
            cal.add(Calendar.MINUTE, Integer.parseInt(secondTimeStamps[0]));

//            cal.add(Calendar.SECOND, Integer.parseInt(secondTimeStamps[2]));
//            cal.add(Calendar.MINUTE, Integer.parseInt(secondTimeStamps[1]));
//            cal.add(Calendar.HOUR, Integer.parseInt(secondTimeStamps[0]));

            newTime = df.format(cal.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newTime;
    }

    private void initRecyclerView(Dialog dialog) {

        conversationRecyclerView = dialog.findViewById(R.id.testRecyclerView);
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

        @Override
        public void onBindViewHolder(@NonNull final ViewHolderRightMessage holder, int position) {

            holder.number.setText((position + 1) + "");

            if (!goTimeStampsList.isEmpty())
                holder.go.setText(goTimeStampsList.get(position));

            if (!restTimeStampsList.isEmpty() && !goTimeStampsList.isEmpty()) {
                holder.rest.setText(restTimeStampsList.get(position));

                String finalValue = addTime(goTimeStampsList.get(position), restTimeStampsList.get(position));

                totalTimeStampsList.add(finalValue);
                holder.total.setText(finalValue);
                return;
            }

            if (!goTimeStampsList.isEmpty()) {
                totalTimeStampsList.add(goTimeStampsList.get(position));
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