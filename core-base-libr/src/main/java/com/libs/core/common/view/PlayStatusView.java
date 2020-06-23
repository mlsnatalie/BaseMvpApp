package com.libs.core.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 悬浮窗播放按钮
 */
public class PlayStatusView extends View {

    /**
     * 播放状态
     */
    public enum Status {
        PLAYING, PAUSE;
    }

    private Status status;

    private Paint mPaint;

    //暂停中三角形的边长
    private float triangleWidth;

    //播放竖线的高度
    private float playingWidth;
    //圆的线宽
    private float circleWidth;

    //进度
    private float progress;

    //暂停，进度条，播放颜色
    private int contentColor;

    public PlayStatusView(Context context) {
        super(context);
        init();
    }

    public PlayStatusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayStatusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();

        playingWidth = 10;
        circleWidth = 8f;
        status = Status.PAUSE;
        contentColor = Color.parseColor("#D93A32");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBackground(canvas);

        switch (status) {
            case PAUSE:
                drawPause(canvas);
                break;
            case PLAYING:
                drawPlaying(canvas);
                break;
        }

        drawProgress(canvas);
    }

    /**
     * 圆形背景
     *
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {

        float radius = Math.min(getMeasuredWidth(), getMeasuredHeight()) * 1.0f;

        triangleWidth = radius / 3 + 4f;

        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(radius / 2, radius / 2, radius / 2, mPaint);
    }

    /**
     * 播放中
     *
     * @param canvas
     */
    private void drawPlaying(Canvas canvas) {
        mPaint.reset();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(contentColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(playingWidth);

        canvas.drawLine(getMeasuredWidth() * 1.0f / 2 - playingWidth,
                getMeasuredHeight() * 1.0f / 2 - getMeasuredHeight() * 1.0f / 6,
                getMeasuredWidth() * 1.0f / 2 - playingWidth,
                getMeasuredHeight() * 1.0f / 2 + getMeasuredHeight() * 1.0f / 6, mPaint);

        canvas.drawLine(getMeasuredWidth() * 1.0f / 2 + playingWidth,
                getMeasuredHeight() * 1.0f / 2 - getMeasuredHeight() * 1.0f / 6,
                getMeasuredWidth() * 1.0f / 2 + playingWidth,
                getMeasuredHeight() * 1.0f / 2 + getMeasuredHeight() * 1.0f / 6, mPaint);
    }

    /**
     * 暂停状态
     *
     * @param canvas
     */
    private void drawPause(Canvas canvas) {

        Path path = new Path();
        path.moveTo(getMeasuredWidth() * 1.0f / 2 - triangleWidth / 4,
                getMeasuredHeight() * 1.0f / 2 - triangleWidth / 2);
        path.lineTo(getMeasuredWidth() * 1.0f / 2 - triangleWidth / 4,
                getMeasuredHeight() * 1.0f / 2 + triangleWidth / 2);
        path.lineTo(getMeasuredWidth() * 1.0f / 2 + triangleWidth / 2,
                getMeasuredHeight() * 1.0f / 2);
        path.close();

        mPaint.reset();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(contentColor);
        canvas.drawPath(path, mPaint);

    }

    /**
     * 进度条
     *
     * @param canvas
     */
    private void drawProgress(Canvas canvas) {
        mPaint.reset();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8f);
        mPaint.setColor(contentColor);

        RectF rectF = new RectF(circleWidth, circleWidth, getMeasuredWidth() - circleWidth,
                getMeasuredHeight() - circleWidth);
        Path path = new Path();
        path.addArc(rectF, -90, progress * 360);
        canvas.drawPath(path, mPaint);

    }

    public void setStatus(Status status) {
        this.status = status;
        this.invalidate();
    }

    public void updateProgress(float progress) {
        invalidate();
        this.progress = progress;
    }

    /**
     * 是否在播放状态
     *
     * @return
     */
    public boolean isPlaying() {
        return status == Status.PLAYING;
    }

    /**
     * 获取当前状态的相反状态
     */
    public Status getOppositeStatus() {
        return status == Status.PLAYING ? Status.PAUSE : Status.PLAYING;
    }

    /**
     * 设置相反状态
     */
    public void setOppositeStatus() {
        status = getOppositeStatus();
        this.invalidate();
    }

    public void updateComplete() {
        this.status = Status.PAUSE;
        this.progress = 1.0f;
        this.invalidate();
    }

    public void setContentColor(int contentColor) {
        this.contentColor = contentColor;
        this.invalidate();
    }
}
