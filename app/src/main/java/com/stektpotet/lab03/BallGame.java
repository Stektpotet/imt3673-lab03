package com.stektpotet.lab03;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class BallGame extends AppCompatActivity {

    private ConstraintLayout mBaseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mBaseView = findViewById(R.id.main_baseView);
        EnterImmersive();

    }

    private void EnterImmersive() {
        mBaseView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}
