package com.haripriya.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity {
    private static final String TAG = "WorkoutActivity" ;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private Workouts mWorkouts;
    private List<Workouts> mWorkoutList;
    private RecyclerView recyclerView;
    private WorkoutAdapter adapter;
    private Context context;
    private String workoutName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            Log.e(TAG, "action bar is null");
        }
        context = this;
        Intent intent = getIntent();
        if(intent == null){
            Toast.makeText(this, "Does not recieved the data", Toast.LENGTH_SHORT).show();
        }
         workoutName  = intent.getStringExtra("workoutName");
        Log.i("got Intent" , workoutName);
        setTitle(workoutName);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("workouts").child(workoutName);

        mWorkoutList = new ArrayList<>();

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Log.i("entere into workouts 1","into the workout activity");

                    mWorkouts = dataSnapshot.getValue(Workouts.class);
                    mWorkoutList.add(mWorkouts);
                    Log.i("entere into workouts",mWorkoutList.get(0).getDescription());

                }
                generateWorkouts(mWorkoutList,workoutName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    /* Method to generate List of workouts using RecyclerView with custom adapter */
    private void generateWorkouts(List<Workouts> workoutList,String workoutName) {
        // set up the RecyclerView
        recyclerView = findViewById(R.id.recycle_workout);
        adapter = new WorkoutAdapter(this, workoutList,workoutName );
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
    //to add items to widget
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_widget_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_widget:
                addWorkoutsToWidget();
                item.setIcon(R.drawable.ic_baseline_star_24);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void addWorkoutsToWidget() {


        // String to store list of workouts


        // StringBuilder to store workouts for  widget
        StringBuilder widgetWorkouts = new StringBuilder();

        if (mWorkoutList != null) {


            for (int i = 0; i < mWorkoutList.size(); i++) {
                widgetWorkouts.append(mWorkoutList.get(i).getName() + "\n");
            }

            // update the workout widget
            WorkoutListDispalyService.handleActionUpdateWorkoutWidgets(context, workoutName
                    , widgetWorkouts.toString());


        }
    }

}