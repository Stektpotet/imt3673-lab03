package com.stektpotet.lab03;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by halvor on 06.04.18.
 *
 * Inspired by:https://google-developer-training.gitbooks.io/android-developer-advanced-course-practicals/unit-5-advanced-graphics-and-views/lesson-11-canvas/11-2-p-create-a-surfaceview/11-2-p-create-a-surfaceview.html
 */

public class GameView extends SurfaceView implements Runnable {

    public static final String TAG = GameView.class.getName();

    private Context mContext;

    @Nullable
    private Canvas mCanvas = null;

    private Margin mMargin;

    @Nullable
    private SurfaceHolder mSurfaceHolder = null;

    private Thread mGameThread = null;

    GameManager gameManager = GameManager.getInstance();

    private boolean mRunning = false;

    private int mBackgroundColor;

    public GameView(Context context) {
        super(context);
        init(context);
    }


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mSurfaceHolder = getHolder();
        mBackgroundColor = ContextCompat.getColor(mContext, R.color.colorPrimaryDark);
    }

    public void pause() {
        mRunning = false;
        try {
            // Stop the thread (rejoin the main thread)
            mGameThread.join();
        } catch (InterruptedException e) {
            Log.w(TAG, "Unable to join game thread!");
        }
    }

    public void resume() {
        mRunning = true;
        mGameThread = new Thread(this);
        mGameThread.start();
    }


    @Override
    public void run() {

        mMargin = new Margin();
        while(mRunning) {
            if (mSurfaceHolder.getSurface().isValid()) {
                mCanvas = mSurfaceHolder.lockCanvas();
                mCanvas.save();

                gameManager.setGameWindowWidth(mCanvas.getWidth());
                gameManager.setGameWindowHeight(mCanvas.getHeight());

                mCanvas.drawColor(mBackgroundColor); //background

                gameManager.ball.draw(mCanvas);


                //finally draw borders
                mMargin.draw(mCanvas);

                mCanvas.restore();
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);

                try {
                    Thread.sleep(1000 / GameManager.LOCKED_FRAME_RATE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {
                Log.w(TAG, "mSurfaceHolder has no valid surface");
                continue;
            }
        }
    }

    private class Margin extends Drawable {
        private Paint mPaint;
        private int mColor;
        private RectF xMin, xMax, yMin, yMax;

        Margin() {
            this(Color.BLACK);
        }

        Margin(int color) {
            mPaint = new Paint();
            mPaint.setColor(color);

            float thickness = gameManager.getGameWindowMargin();
            float windowHeight = gameManager.getGameWindowHeight();
            float windowWidth = gameManager.getGameWindowWidth();
            Log.i(TAG+".TEST", "W: " + windowWidth);
            Log.i(TAG+".TEST", "H: " + windowHeight);
            this.xMin = new RectF(0,0,thickness,windowHeight);
            this.xMax = new RectF(windowWidth-thickness,0,windowWidth,windowHeight);
            this.yMin = new RectF(0,0,windowWidth,thickness);
            this.yMax = new RectF(0,windowHeight-thickness,windowWidth,windowWidth);

        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            mPaint.setColor(Color.YELLOW);
            canvas.drawRect(mMargin.xMin, mPaint);
            mPaint.setColor(Color.RED);
            canvas.drawRect(mMargin.xMax, mPaint);
            mPaint.setColor(Color.CYAN);
            canvas.drawRect(mMargin.yMin, mPaint);
            mPaint.setColor(Color.GREEN);
            canvas.drawRect(mMargin.yMax, mPaint);
        }

        @Override
        public void setAlpha(int i) {
            mPaint.setAlpha(i);
        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {
            Log.w(TAG, "THERE's no point in calling this, "+Margin.class.getName()+" has no implementation!!!");
        }

        @Override
        public int getOpacity() {
            return PixelFormat.OPAQUE;
        }
    }
}