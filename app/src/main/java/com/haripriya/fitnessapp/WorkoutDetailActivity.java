package com.haripriya.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.haripriya.fitnessapp.databinding.ActivityWorkoutDetailBinding;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class WorkoutDetailActivity extends AppCompatActivity {
    private static final String TAG = "WorkoutDetailActivity";
    private Workouts mWorkouts;
    private ActivityWorkoutDetailBinding binding;
    String workoutName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_workout_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            Log.e(TAG, "action bar is null");
        }

        Intent intent = getIntent();
        if(intent ==null){
            Toast.makeText(this, "does not recive the intent", Toast.LENGTH_SHORT).show();
        }

        mWorkouts = intent.getParcelableExtra("Details");
        workoutName = intent.getStringExtra("workoutName");
        setTitle(mWorkouts.getName());
        YouTubePlayerView youTubePlayerView = binding.youtubePlayerView;
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = mWorkouts.getVideoId();
                youTubePlayer.loadVideo(videoId, 0);
            }
        });
        binding.tvDescription.setText(mWorkouts.getDescription());
        binding.tvTitle.setText(mWorkouts.getTitle());
        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkoutDetailActivity.this,WorkoutActivity.class);
                intent.putExtra("workoutName",workoutName);
                startActivity(intent);
                finish();

            }
        });

    }
}