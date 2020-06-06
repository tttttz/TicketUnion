package com.example.ticketunion.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/26 15:17
 * God bless my code!
 */


public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mBind;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        //================================================================
        //实现清明节模式
        // ColorMatrix cm = new ColorMatrix();
        // cm.setSaturation(0);
        // Paint paint = new Paint();
        // paint.setColorFilter(new ColorMatrixColorFilter(cm));
        // View contentContainer = getWindow().getDecorView();
        // contentContainer.setLayerType(View.LAYER_TYPE_SOFTWARE, paint);
        //================================================================
        mBind = ButterKnife.bind(this);
        initView();
        initEvent();
        initPresenter();
    }

    protected abstract void initPresenter();

    /**
     * 初始化时间
     */
    protected void initEvent() {

    }

    protected abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
        release();
    }

    protected void release() {
        //释放资源
    }

    protected abstract int getLayoutResId();
}
