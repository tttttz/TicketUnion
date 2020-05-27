package com.example.ticketunion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.ticketunion.R;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/25 17:46
 * God bless my code!
 */

/**
 * 功能：自动轮播
 */
public class AutoLooperViewPager extends ViewPager {

    private boolean isLoop = false;

    public static final long DEFAULT_DURATION = 3000;

    private long mDuration = DEFAULT_DURATION;

    public AutoLooperViewPager(@NonNull Context context) {
        this(context, null);
    }

    public AutoLooperViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.AutoLooperStyle);
        //获取属性
        mDuration = t.getInteger(R.styleable.AutoLooperStyle_duration, (int) DEFAULT_DURATION);
        //LogUtil.d(this, "mDuration ==> " + mDuration);
        t.recycle();
    }

    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            int currentItem = getCurrentItem();
            currentItem++;
            setCurrentItem(currentItem);
            if (isLoop) {
                postDelayed(this, mDuration);
            }
        }
    };
    public void startLoop() {
        isLoop = true;
        post(mTask);
    }

    public void stopLoop() {
        isLoop = false;
        removeCallbacks(mTask);
    }

    /**
     * 设置切换时长
     * @param duration
     */
    public void setDuration(long duration) {
        mDuration = duration;
    }

}
