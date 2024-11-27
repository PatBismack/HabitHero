package com.example.habithero;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TimerActivity extends AppCompatActivity {

    SeekBar timerSB;
    TextView fTimer;
    Button startButton;

    CountDownTimer timer;

    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        timerSB = findViewById(R.id.timerSB);
        fTimer = findViewById(R.id.fTimer);
        startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter=timerSB.getProgress() * 60;
                timer = new CountDownTimer(Long.MAX_VALUE, 1000) {
                    @Override
                    public void onTick(long ms) {
                        counter -= 1;
                        long hours = counter / 3600;
                        long minutes = (counter % 3600) / 60;
                        long seconds = counter % 60;

                        String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                        fTimer.setText(timeFormatted);

                    }

                    @Override
                    public void onFinish() {
                        fTimer.setText("Done!");
                    }
                }.start ();

            }
        });

        timerSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                counter=timerSB.getProgress() * 60;
                long hours = counter / 3600;
                long minutes = (counter % 3600) / 60;
                long seconds = counter % 60;

                String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                fTimer.setText(timeFormatted);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}