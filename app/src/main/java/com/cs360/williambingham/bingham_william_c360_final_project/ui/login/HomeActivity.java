package com.cs360.williambingham.bingham_william_c360_final_project.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Handler;
import com.cs360.williambingham.bingham_william_c360_final_project.DatabaseActivity;
import com.cs360.williambingham.bingham_william_c360_final_project.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent HomePage = new Intent(HomeActivity.this, DatabaseActivity.class);
                startActivity(HomePage);
            }

        }, 3000L);
    }
}