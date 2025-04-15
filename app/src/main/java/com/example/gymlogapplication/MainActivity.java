package com.example.gymlogapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import com.example.gymlogapplication.database.GymLogRepository;
import com.example.gymlogapplication.database.entities.GymLog;
import com.example.gymlogapplication.database.entities.User;
import com.example.gymlogapplication.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_ACTIVITY_USER_ID = "com.declink.gymlogapplication.MAIN_ACTIVITY_USER_IO";

    static final String SHARED_PREFERENCE_USERID_KEY = "com.declink.gymlogapplication.SHARED_PREFERENCE_USERID_KEY";

    static final String SHARED_PREFERENCE_USERID_VALUE = "com.declink.gymlogapplication.SHARED_PREFERENCE_USERID_VALUE";

    static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.declink.gymlogapplication.SAVED_INSTANCE_STATE_USERID_KEY";


    private static final int LOGGED_OUT = -1;
    private ActivityMainBinding binding;

    private GymLogRepository repository;

    public static final String TAG = "TAG_GYMLOG";
    String mExercise = "";

    double mWeight = 0.0;

    int mReps = 0;

    private int loggedInUserId = -1;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.declink.gymlogpractice.databinding.ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = GymLogRepository.getRepository(getApplication());
        loginUser(savedInstanceState);


        if(loggedInUserId == -1){
            Intent intent = LoginActivity.loginIntentFactory(getApplicationContext());
            startActivity(intent);
        }

        updateSharePreference();


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

        binding.exerciseInputEditTest.setOnclickListener( new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    updateDisplay();}
        });

    }

    private void loginUser(Bundle saveInstanceState) {
//        user = new User("user1", "user1");
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);

        loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_userId_key),LOGGED_OUT);


        if (loggedInUserId == LOGGED_OUT & saveInstanceState != null && saveInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)){
            loggedInUserId = saveInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY,LOGGED_OUT);
        }

        if(loggedInUserId == LOGGED_OUT){
            loggedInUserId = getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID, LOGGED_OUT);
        }

        if(loggedInUserId == LOGGED_OUT){
            return;
        }

        LiveData<User> userObserver = repository.getUserByUserId(loggedInUserId);
        userObserver.observe(this, user -> {
            this.user = user;
            if(this.user != null){
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_STATE_USERID_KEY, loggedInUserId);

        updateSharePreference();

//        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_USERID_KEY,
//                Context.MODE_PRIVATE);
//        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
//        sharedPrefEditor.putInt(MainActivity.SHARED_PREFERENCE_USERID_KEY, loggedInUserId);
//        sharedPrefEditor.apply();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.logoutMenuItem);
        item.setVisible(true);
        if (user == null){
            return false;
        }

        item.setTitle(user.getUsername());

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                showLogoutDialog();
                return false;
            }
        });
        return true;
    }

    private void showLogoutDialog(){
        AlertDialog.Builder alerBuilder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog alertDialog = alerBuilder.create();

        alerBuilder.setMessage("Logout?");

        alerBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });

        alerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alerBuilder.create().show();

    }

    private void logout() {
//        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCE_USERID_KEY,Context.MODE_PRIVATE);
//        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
//        sharedPrefEditor.putInt(SHARED_PREFERENCE_USERID_KEY,LOGGED_OUT);
//        sharedPrefEditor.apply();

        loggedInUserId = LOGGED_OUT;
        updateSharePreference();

        getIntent().putExtra(MAIN_ACTIVITY_USER_ID,loggedInUserId);

        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }

    private void updateSharePreference(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_userId_key),loggedInUserId);
        sharedPrefEditor.apply();
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
        ArrayList<GymLog> allLogs = repository.getAllLogsByUserId(loggedInUserId);
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



















