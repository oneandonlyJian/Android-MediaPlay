package com.diggoods.api.only_one;

/**
 * Create by  FengJianyi on 2019/6/25
 */
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 雨滴的类, 移动, 移出屏幕会重新设置位置.
 */
public class RainFlake {

    // 雨滴的移动速度
    private static final float INCREMENT_LOWER = 20f;
    private static final float INCREMENT_UPPER = 40f;

    // 雨滴的大小
    private static final float FLAKE_SIZE_LOWER = 2f;
    private static final float FLAKE_SIZE_UPPER = 4f;

    private final float mIncrement; // 雨滴的速度
    private final float mFlakeSize; // 雨滴的大小
    private final Paint mPaint; // 画笔

    private Line mLine; // 雨滴

    private RainRandomGenerator mRandom;

    private RainFlake(RainRandomGenerator random, Line line, float increment, float flakeSize, Paint paint) {
        mRandom = random;
        mLine = line;
        mIncrement = increment;
        mFlakeSize = flakeSize;
        mPaint = paint;
    }

    //生成雨滴
    public static RainFlake create(int width, int height, Paint paint) {
        RainRandomGenerator random = new RainRandomGenerator();
        int [] nline;
        nline = random.getLine(width, height);

        Line line = new Line(nline[0], nline[1], nline[2], nline[3]);
        float increment = random.getRandom(INCREMENT_LOWER, INCREMENT_UPPER);
        float flakeSize = random.getRandom(FLAKE_SIZE_LOWER, FLAKE_SIZE_UPPER);
        return new RainFlake(random,line, increment, flakeSize, paint);
    }

    // 绘制雨滴
    public void draw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        drawLine(canvas, width, height);
    }

    /**
     * 改成线条，类似于雨滴效果
     * @param canvas
     * @param width
     * @param height
     */
    private void drawLine(Canvas canvas, int width, int height) {
        //设置线宽
        mPaint.setStrokeWidth(mFlakeSize);
        //y是竖直方向，就是下落
        double y1 = mLine.y1 + (mIncrement * Math.sin(1.5));
        double y2 = mLine.y2 + (mIncrement * Math.sin(1.5));

        //这个是设置雨滴位置，如果在很短时间内刷新一次，就是连起来的动画效果
        mLine.set(mLine.x1,(int) y1,mLine.x2 ,(int) y2);

        if (!isInsideLine(height)) {
            resetLine(width,height);
        }

        canvas.drawLine(mLine.x1, mLine.y1, mLine.x2, mLine.y2, mPaint);
    }

    // 判断是否在其中
    private boolean isInsideLine(int height) {
        return mLine.y1 < height && mLine.y2 < height;
    }

    // 重置雨滴
    private void resetLine(int width, int height) {
        int [] nline;
        nline = mRandom.getLine(width, height);
        mLine.x1 = nline[0];
        mLine.y1 = nline[1];
        mLine.x2 = nline[2];
        mLine.y2 = nline[3];
    }

}

