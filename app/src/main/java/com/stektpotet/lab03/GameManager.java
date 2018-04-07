package com.stektpotet.lab03;

import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by halvor on 06.04.18.
 */

public class GameManager {
    public static final String TAG = GameManager.class.getName();

    //SINGLETON PATTERN ==========================
    private static GameManager INSTANCE = null;
    public static GameManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new GameManager();
        }
        return INSTANCE;
    }
    private GameManager(){}
    //SINGLETON PATTERN ==========================

    public static final int LOCKED_FRAME_RATE = 60;
    public static final float SENSOR_GRAVITY_SCALE = 1f;
    public static final float BOUNCINESS = 1f;

    public void setGameWindowMargin(float f) { gameWindowMargin = f; }
    public float getGameWindowMargin() { return gameWindowMargin; }
    private float gameWindowMargin;

    public void setGameWindowHeight(float f) { gameWindowHeight = f; }
    public float getGameWindowHeight() { return gameWindowHeight; }
    private float gameWindowHeight;

    public void setGameWindowWidth(float f) { gameWindowWidth = f; }
    public float getGameWindowWidth() { return gameWindowWidth; }
    private float gameWindowWidth;

    public float[] axes = {0F,0F};

    public Ball ball;

}
