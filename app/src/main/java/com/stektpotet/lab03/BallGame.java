package com.stektpotet.lab03;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class BallGame extends AppCompatActivity {

    public static final String TAG = BallGame.class.getName();


    private Vibrator mVibrator;
    private MediaPlayer mMediaPlayer;


    GameManager mGameManager;
    private GameView mGameView;
    private Ball ball;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGameManager = GameManager.getInstance();
        mGameView = new GameView(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        setContentView(mGameView);

        initGame();

        startSensorTracking();
    }

    private void initGame() {


        this.mVibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        this.mMediaPlayer = MediaPlayer.create(this, R.raw.pling);

        mMediaPlayer.start();
        mMediaPlayer.seekTo(10);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        this.mGameManager.setGameWindowHeight(height);
        this.mGameManager.setGameWindowWidth(width);
        this.mGameManager.setGameWindowMargin(getResources().getDimension(R.dimen.gameview_margin));


        mGameManager.ball = new Ball(width*0.5f, height*0.5f, height/20);
        mGameManager.ball.onCollideCallback = new Ball.ICollide() {
            @Override
            public void onCollide(Ball b) {
                mVibrator.vibrate(25);
                mMediaPlayer.start();
                mMediaPlayer.seekTo(250);
            }
        };

    }
    private void enterImmersive() {
        mGameView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onResume() {
        Log.i(TAG, "RESUME!!");
        super.onResume();
        enterImmersive();
        mGameView.resume();
        this.mMediaPlayer = MediaPlayer.create(this, R.raw.pling);
    }

    @Override
    public void onPause() {
        super.onPause();
        mGameView.pause();

        mMediaPlayer.release();
    }

    //Gyroscope sensor stuff

    private SensorManager mSensorManager;
    private Sensor mSensorGyroscope;

    private void startSensorTracking() {
        this.mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        this.mSensorGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        this.mSensorManager.registerListener(new RotationEventListener(), mSensorGyroscope, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private class RotationEventListener implements SensorEventListener {

        private static final float NS2S = 1.0f / 1000000000.0f;
        private static final float EPSILON = 0.00000000001f;
        private float mTimeStamp;

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (mTimeStamp != 0) {
                final float dT = (event.timestamp - mTimeStamp) * NS2S;

                final float axisX = event.values[0];
                final float axisY = event.values[1];

//                final float magnitude = (float)sqrt(axisX*axisX + axisY*axisY);
//                if(magnitude > EPSILON) { //ignore very minor changes;
                    mGameManager.axes[0] = GameManager.SENSOR_GRAVITY_SCALE * event.values[0];
                    mGameManager.axes[1] = -GameManager.SENSOR_GRAVITY_SCALE * event.values[1];
//                }
            }
            mTimeStamp = event.timestamp;

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }
}
