package com.example.ticketunion.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import com.example.ticketunion.ui.custom.LoadingView;
import com.example.ticketunion.utils.LogUtil;
import com.example.ticketunion.utils.PresenterManager;
import com.example.ticketunion.utils.ToastUtil;
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

    @BindView(R.id.ticket_over_loading)
    LoadingView mLoadingView;

    @BindView(R.id.ticket_load_retry)
    TextView mRetryLoadText;

    //用来标记是否有淘宝
    private boolean mHasTaoBapApp = false;

    @Override
    protected void initPresenter() {
        mTicketPresent = PresenterManager.getInstance().getTicketPresent();
        if (mTicketPresent != null) {
            mTicketPresent.registerViewCallback(this);
        }
        //检查是否有安装淘宝
        searchForTaobao();
    }

    private void searchForTaobao() {
        //判断是否安装淘宝
        //act=android.intent.action.VIEW
        // flg=0x4000000
        // hwFlg=0x10
        // pkg=com.taobao.taobao
        // cmp=com.taobao.taobao/com.taobao.tao.TBMainActivity (has extras)
        //包名是:com.taobao.taobao
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            mHasTaoBapApp = packageInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mHasTaoBapApp = false;
        }
        LogUtil.d(this, "has taobao ==> " + mHasTaoBapApp);
        mCopyOrOpenBtn.setText(mHasTaoBapApp ? "打开淘宝领券" : "复制淘口令");
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
        mCopyOrOpenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //复制淘口令
                String ticketCode = mTicketCode.getText().toString().trim();
                LogUtil.d(TicketActivity.this, "ticket code == > " + ticketCode);
                //复制到粘贴板
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("sob_taobao_ticket_code", ticketCode);
                cm.setPrimaryClip(clipData);
                //判断有没有淘宝
                if (mHasTaoBapApp) {
                    //有就打开淘宝
                    Intent taobaoIntent = new Intent();
//                    taobaoIntent.setAction("android.intent.action.MAIN");
//                    taobaoIntent.addCategory("android.intent.categoty.LAUNCHER");
                    ComponentName componentName = new ComponentName("com.taobao.taobao", "com.taobao.tao.TBMainActivity");
                    taobaoIntent.setComponent(componentName);
                    startActivity(taobaoIntent);
                } else {
                    //没有就提示复制成功
                    ToastUtil.showToast("已经复制,粘贴分享,或打开淘宝");
                }
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
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
        if (mRetryLoadText != null) {
            mRetryLoadText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoading() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
        if (mRetryLoadText != null) {
            mRetryLoadText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onEmpty() {

    }
}
