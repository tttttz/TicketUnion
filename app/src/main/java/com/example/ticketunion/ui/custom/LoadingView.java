package com.example.ticketunion.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.ticketunion.R;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/27 15:05
 * God bless my code!
 */
public class LoadingView extends AppCompatImageView {

    private float mDegree = 0;

    private boolean mNeedRotate = true;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.loading);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mNeedRotate = true;
        startRotate();
    }

    /**
     * 开始旋转
     */
    private void startRotate() {
        post(new Runnable() {
            @Override
            public void run() {
                mDegree += 10;
                if (mDegree >= 360) {
                    mDegree = 0;
                }
                invalidate();
                if (getVisibility() == VISIBLE && mNeedRotate) {
                    postDelayed(this, 10);
                } else {
                    removeCallbacks(this);
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRotate();
    }

    private void stopRotate() {
        mNeedRotate = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(mDegree, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
    }
}
