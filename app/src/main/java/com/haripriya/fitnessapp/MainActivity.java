package com.haripriya.fitnessapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mWorkoutDatabaseReference;
    public  static final int RC_SIGN_IN = 1;
    private ValueEventListener mValueEventListener;


    //Recyclerview
    private RecyclerView recyclerView;
    private WorkoutListAdapter adapter;

    List<String> workotLists ;
    private Toolbar mToolbar;

    private ProgressBar loadingIndicator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        loadingIndicator = findViewById(R.id.pb_loading);




        //intialize the firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //for offline activity
        mFirebaseDatabase.setPersistenceEnabled(true);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mWorkoutDatabaseReference = mFirebaseDatabase.getReference().
                child("workouts").
                child("workoutNames");

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){//user is signed in
                   // Toast.makeText(MainActivity.this, "you are now signed in", Toast.LENGTH_SHORT).show();
                    onSignedInIntialize();

                }else{//user is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setLogo(R.drawable.app_logo)
                                    .setTheme(R.style.AppTheme)
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()
                                          ))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sign_out:
                AuthUI.getInstance().signOut(this);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onSignedInIntialize() {
        attachDatabaseReadListner();

    }
    private void onSignedOutCleanup() {
       if(workotLists!=null)
       workotLists.clear();
        detachDatabaseReadListner();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("main","came to activity for result");
        if(requestCode == RC_SIGN_IN){
            if(resultCode==RESULT_OK){
                Toast.makeText(this, "Sign in", Toast.LENGTH_SHORT).show();
            }else if(resultCode == RESULT_CANCELED){
                finish();
                Toast.makeText(this, "Sign in Cancelled", Toast.LENGTH_SHORT).show();

            }
        }
    }


    @Override
    protected void onResume() {

        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener!=null){
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseReadListner();
        if(workotLists!=null)
        workotLists.clear();
    }

    /* Method to generate List of workouts using RecyclerView with custom adapter */
    private void generateWorkoutList() {

        // set up the RecyclerView
        recyclerView = findViewById(R.id.recycle_workout_list);
        adapter = new WorkoutListAdapter(this, workotLists );
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        loadingIndicator.setVisibility(View.GONE);
    }
    private  void attachDatabaseReadListner(){
        workotLists = new ArrayList<>();
        if(mValueEventListener==null) {
            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        workotLists.add(dataSnapshot.getValue().toString());
                        Log.i("entered", workotLists.get(0));
                    }
                    generateWorkoutList();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            mWorkoutDatabaseReference.addValueEventListener(mValueEventListener);
        }

    }
    private void detachDatabaseReadListner(){
        if(mValueEventListener!=null) {
            mWorkoutDatabaseReference.removeEventListener(mValueEventListener);
            mValueEventListener = null;
        }
    }

}