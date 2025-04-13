package com.example.gymlogapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gymlogapplication.database.GymLogRepository;
import com.example.gymlogapplication.database.entities.GymLog;
import com.example.gymlogapplication.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_ACTIVITY_USER_ID = "com.declink.gymlogapplication.MAIN_ACTIVITY_USER_IO";
    private ActivityMainBinding binding;

    private GymLogRepository repository;

    public static final String TAG = "TAG_GYMLOG";
    String mExercise = "";

    double mWeight = 0.0;

    int mReps = 0;

    //todo
    int loggedInUserId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginUser();

        if(loggedInUserId == -1){
            Intent intent = LoginActivity.loginIntentFactory(getApplicationContext());
            startActivity(intent);
        }

        repository = GymLogRepository.getRepository(getApplication());

        binding.logDisplayTextView.setMovementMethod(new ScrollingMovementMethod());

        updateDisplay();
        binding.logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInformationFromDisplay();
                insertGymlogRecord();
                updateDisplay();
            }
        });

    }

    private void loginUser() {
        //Todo, create login method
        loggedInUserId = getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID, -1);
    }

    static Intent mainActivityIntentFactory(Context context, int userId){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MAIN_ACTIVITY_USER_ID, userId);
        return intent;
    }

    private void insertGymlogRecord(){
        if(mExercise.isEmpty()){
            return;
        }
        GymLog log = new GymLog(mExercise,mWeight,mReps, loggedInUserId);
        repository.insertGymLog(log);
    }

    private void updateDisplay(){
        ArrayList<GymLog> allLogs = repository.getAllLogs();
        if(allLogs.isEmpty()){
            binding.logDisplayTextView.setText(R.string.noting_here_time_to_hit_the_gym);
        }
        StringBuilder sb = new StringBuilder();
        for (GymLog log : allLogs){
            sb.append(log);
        }


        binding.logDisplayTextView.setText(sb.toString());

    }

    private void getInformationFromDisplay(){
        mExercise = binding.exerciseEditText.getText().toString();

        try {
            mWeight = Double.parseDouble(binding.weightEditText.getText().toString());
        }catch (NumberFormatException e){
            Log.d(TAG,"Error reading value from weight edit text.");
        }

        try {
            mReps = Integer.parseInt(binding.repsEditText.getText().toString());
        }catch (NumberFormatException e){
            Log.d(TAG,"Error reading value from reps edit text.");
        }

    }

}



















