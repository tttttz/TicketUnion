package com.example.ticketunion.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ticketunion.R;
import com.example.ticketunion.base.BaseActivity;
import com.example.ticketunion.model.domain.TicketResult;
import com.example.ticketunion.presenter.impl.TicketPresentImpl;
import com.example.ticketunion.utils.LogUtil;
import com.example.ticketunion.utils.PresenterManager;
import com.example.ticketunion.utils.UrlUtils;
import com.example.ticketunion.view.ITicketPagerCallback;

import butterknife.BindView;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/26 15:18
 * God bless my code!
 */
public class TicketActivity extends BaseActivity implements ITicketPagerCallback {


    private TicketPresentImpl mTicketPresent;

    @BindView(R.id.ticket_cover)
    ImageView mCover;

    @BindView(R.id.ticket_code)
    EditText mTicketCode;

    @BindView(R.id.ticket_copy_or_open_btn)
    TextView mCopyOrOpenBtn;

    @BindView(R.id.ticket_back_press)
    ImageView mBackPress;

    @Override
    protected void initPresenter() {
        mTicketPresent = PresenterManager.getInstance().getTicketPresent();
        mTicketPresent.registerViewCallback(this);
    }

    @Override
    protected void release() {
        if (mTicketPresent != null) {
            mTicketPresent.unregisterViewCallback(this);
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {
        mBackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket;
    }

    //==================================================================
    //以下为callback回调方法
    @Override
    public void onTicketLoaded(String cover, TicketResult result) {
        if (mCover != null && !TextUtils.isEmpty(cover)) {
            String targetUrl = UrlUtils.getCoverPath(cover);
            LogUtil.d(this, "target Url ==> " + targetUrl);
            Glide.with(this).load(targetUrl).into(mCover);
        }
        if (result != null && result.getData().getTbk_tpwd_create_response() != null) {
            String code = result.getData().getTbk_tpwd_create_response().getData().getModel();
            mTicketCode.setText(code);
        }
    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }
}
