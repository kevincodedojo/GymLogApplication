package com.example.gymlogapplication;

import android.app.Activity;
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

import com.example.gymlogapplication.databinding.ActivityMainBinding;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    public static final String TAG = "TAG_GYMLOG";
    String mExercise = "";

    double mWeight = 0.0;

    int mReps = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.logDisplayTextView.setMovementMethod(new ScrollingMovementMethod());

        binding.logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInformationFromDisplay();
                updateDisplay();
            }
        });
    }

    private void updateDisplay(){
        String currentInfo = binding.logDisplayTextView.getText().toString();
        Log.d(TAG,"current info" + currentInfo);
        String newDisplay = String.format(Locale.US,"Exercise:%s%nWeight:%.2f%nReps:%d%n=-=-=-=-=-=%n%s",mExercise,mWeight,mReps,currentInfo);
        binding.logDisplayTextView.setText(newDisplay);

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



















