package com.stektpotet.lab03;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

class Ball extends Drawable {

    public static final String TAG = Ball.class.getName();

    public static final int DEFAULT = Color.WHITE;
    public static final int HIT = Color.BLACK;

    private float xPos, yPos;
    private float xVel = 0, yVel = 0;

    private float radius = 40;

    Paint paint = new Paint();

    interface ICollide {
        void onCollide(Ball b);
    }
    ICollide onCollideCallback = null;

    GameManager gameManager = GameManager.getInstance();

    Ball(float x, float y, float radius) {
        this.xPos = x;
        this.yPos = y;
        this.radius = radius;
        this.paint.setColor(DEFAULT);
        this.paint.setStyle(Paint.Style.FILL);
    }

    void applyForces() {
        this.xVel += gameManager.axes[0];
        this.yVel += gameManager.axes[1];
    }

    public float xPosExtrapolated() { return (xPos + xVel) + (xVel + gameManager.axes[0]); }
    public float yPosExtrapolated() { return (yPos + yVel) + (yVel + gameManager.axes[1]); }

    public void setOnCollide(ICollide onCollide) {
        this.onCollideCallback = onCollide;
    }

    private boolean willCollide(float position1, float position2) {
        return (position1 > position2);
    }

    private void update()
    {
        float nextXPos = xPosExtrapolated();
        float nextYPos = yPosExtrapolated();

        this.applyForces();
        boolean collided = false;

        float margin = gameManager.getGameWindowMargin();
        float width  = gameManager.getGameWindowWidth();
        float height = gameManager.getGameWindowHeight();

        if(willCollide(margin, nextXPos-this.radius)) {
            this.xVel *= -GameManager.BOUNCINESS;
            collided = true;
            setColor(Color.YELLOW);
            nextXPos = margin + (this.radius+1);
        }
        if(willCollide(nextXPos+margin, width-margin)) {

            this.xVel *= -GameManager.BOUNCINESS;
            collided = true;
            setColor(Color.RED);
            nextXPos = width - margin - (this.radius+1);
        }

        if(willCollide(margin, nextYPos-this.radius)) {
            this.yVel *= -GameManager.BOUNCINESS;
            collided = true;
            setColor(Color.CYAN);
            nextYPos = margin + (this.radius+1);
        }
        if(willCollide(nextYPos+this.radius, height-margin)) {
            this.yVel *= -GameManager.BOUNCINESS;
            collided = true;
            setColor(Color.GREEN);
            nextYPos = height-margin - (this.radius+1);
        }

        if(collided) {
            if(this.onCollideCallback != null)
                onCollideCallback.onCollide(this);

        }
        this.xPos = nextXPos;
        this.yPos = nextYPos;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        update();
        canvas.drawCircle(xPos, yPos, this.radius, this.paint);
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    @Override
    public void setAlpha(int i) {
        paint.setAlpha(i);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        Log.w(TAG, "THERE's no point in calling this, "+Ball.class.getName()+" has no implementation!!!");

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
