package com.example.ticketunion.ui.fragment;

import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketunion.R;
import com.example.ticketunion.base.BaseFragment;
import com.example.ticketunion.base.IBaseInfo;
import com.example.ticketunion.model.domain.Histories;
import com.example.ticketunion.model.domain.SearchRecommend;
import com.example.ticketunion.model.domain.SearchResult;
import com.example.ticketunion.presenter.impl.SearchPresenterImpl;
import com.example.ticketunion.ui.adapter.LinearItemContentAdapter;
import com.example.ticketunion.ui.custom.TextFlowLayout;
import com.example.ticketunion.utils.LogUtil;
import com.example.ticketunion.utils.PresenterManager;
import com.example.ticketunion.utils.SizeUtils;
import com.example.ticketunion.utils.TickUtils;
import com.example.ticketunion.utils.ToastUtil;
import com.example.ticketunion.view.ISearchCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchFragment extends BaseFragment implements ISearchCallback {

    private SearchPresenterImpl mSearchPresenter;

    @BindView(R.id.search_history_view)
    TextFlowLayout mHistoryView;

    @BindView(R.id.search_recommend_view)
    TextFlowLayout mRecommendView;

    @BindView(R.id.search_history_container)
    View mHistoryContainer;

    @BindView(R.id.search_recommend_container)
    View mRecommendContainer;

    @BindView(R.id.search_history_delete)
    View mHistoryDelete;

    @BindView(R.id.search_result_list)
    RecyclerView mSearchList;

    @BindView(R.id.search_result_container)
    TwinklingRefreshLayout mSearchContainer;

    @BindView(R.id.search_btn)
    TextView mSearchBtn;

    @BindView(R.id.search_remove_btn)
    ImageView mSearchRemoveBtn;

    @BindView(R.id.search_input_box)
    EditText mSearchInputBox;



    private LinearItemContentAdapter mSearchResultAdapter;

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_layout, container, false);
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }
    @Override

    protected void initView(View rootView) {
        //设置布局管理器
        mSearchList.setLayoutManager(new LinearLayoutManager(getContext()));
        //创建适配器
        mSearchResultAdapter = new LinearItemContentAdapter();
        //设置适配器
        mSearchList.setAdapter(mSearchResultAdapter);
        //设置间距
        mSearchList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(), 1.5f);
                outRect.bottom = SizeUtils.dip2px(getContext(), 1.5f);
            }
        });
        mSearchContainer.setEnableRefresh(false);
        mSearchContainer.setEnableLoadmore(true);
        mSearchContainer.setEnableOverScroll(true);
    }

    @Override
    protected void initPresenter() {
        mSearchPresenter = PresenterManager.getInstance().getSearchPresenter();
        mSearchPresenter.registerViewCallback(this);
        mSearchPresenter.getRecommendWords();
        //mSearchPresenter.doSearch("键盘");
        mSearchPresenter.getHistory();
    }

    @Override
    protected void initListener() {
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果输入框有内容则搜索
                String keyword = mSearchInputBox.getText().toString().trim();
                if (keyword.length() > 0) {
                    mSearchPresenter.doSearch(keyword);
                } else {
                    //否则提示输入内容为空
                    ToastUtil.showToast("输入内容为空");
                }
            }
        });
        mSearchRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击删除按钮删除输入框内容
                mSearchInputBox.setText("");
            }
        });
        mSearchInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    //若输入框中内容的长度不为0，则显示删除按钮
                    mSearchRemoveBtn.setVisibility(View.VISIBLE);
                } else {
                    //否则不显示删除按钮
                    mSearchRemoveBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSearchInputBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && mSearchPresenter != null) {
                    //发起搜索
                    String keyword = v.getText().toString().trim();
                    if (TextUtils.isEmpty(keyword)) {
                        return false;
                    }
                    mSearchPresenter.doSearch(keyword);
                }
                return false;
            }
        });
        mHistoryDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除历史记录
                mSearchPresenter.delHistory();
            }
        });
        mHistoryView.setOnFlowTextItemClickListener(new TextFlowLayout.OnFlowTextItemClickListener() {
            @Override
            public void onFlowItemClick(String text) {
                LogUtil.d(this, "click text ==> " + text);
            }
        });
        //加载更多控件的监听
        mSearchContainer.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (mSearchPresenter != null) {
                    mSearchPresenter.loaderMore();
                }
            }
        });
        //搜索列表内容点击事件
        mSearchResultAdapter.setOnListItemClickListener(new LinearItemContentAdapter.OnListItemClickListener() {
            @Override
            public void onItemClick(IBaseInfo item) {
                TickUtils.toTickPage(getContext(), item);
            }
        });
    }

    @Override
    protected void onRetryClick() {
        //重新加载
        mSearchPresenter.reSearch();
    }

    @Override
    protected void release() {
        if (mSearchPresenter != null) {
            mSearchPresenter.unregisterViewCallback(this);
        }
    }

    //===========================================================
    //以下为callback回调方法
    @Override
    public void onHistoriesLoaded(Histories histories) {
        setupState(State.SUCCESS);
        LogUtil.d(this, "history size --> " + histories);
        if (histories == null || histories.getHistories().size() == 0) {
            mHistoryContainer.setVisibility(View.GONE);
        } else {
            mHistoryContainer.setVisibility(View.VISIBLE);
            LogUtil.d(this, "mSearchContainer ==> " + mSearchContainer.getVisibility());
            mHistoryView.setTextList(histories.getHistories());
        }

    }

    @Override
    public void onHistoriesDeleted() {
        if (mSearchPresenter != null) {
            mSearchPresenter.getHistory();
        }
    }

    @Override
    public void onSearchSuccess(SearchResult result) {
        setupState(State.SUCCESS);
        //隐藏历史记录和推荐热词
        mHistoryContainer.setVisibility(View.GONE);
        mRecommendContainer.setVisibility(View.GONE);
        // 显示搜索结果
        mSearchContainer.setVisibility(View.VISIBLE);
        try {
            mSearchResultAdapter.setData(result.getData()
                    .getTbk_dg_material_optional_response()
                    .getResult_list().getMap_data());
            mSearchList.scrollToPosition(0);
        }catch (Exception e) {
            e.printStackTrace();
            setupState(State.EMPTY);
        }
    }

    @Override
    public void onMoreLoaded(SearchResult result) {
        mSearchContainer.finishLoadmore();
        List<SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean> data = result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
        mSearchResultAdapter.addData(data);
        ToastUtil.showToast("加载了" + data.size() + "条数据");
    }

    @Override
    public void onMoreLoadedError() {
        mSearchContainer.finishLoadmore();
        ToastUtil.showToast("网络异常，请稍后重试");
    }

    @Override
    public void onMoreLoadedEmpty() {
        mSearchContainer.finishLoadmore();
        ToastUtil.showToast("没有更多数据");
    }

    @Override
    public void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWords) {
        setupState(State.SUCCESS);
        LogUtil.d(this, "size == > " + recommendWords.size());
        List<String> list = new ArrayList<>();
        LogUtil.d(this, "recommendWords size ==> " + recommendWords.size());
        if (recommendWords == null || recommendWords.size() == 0) {
            mRecommendContainer.setVisibility(View.GONE);
        } else {
            mRecommendContainer.setVisibility(View.VISIBLE);
            for (SearchRecommend.DataBean recommendWord : recommendWords) {
                String keyword = recommendWord.getKeyword();
                LogUtil.d(this, "keyword ==> " + keyword);
                list.add(keyword);
            }
            mRecommendView.setTextList(list);
        }
    }

    @Override
    public void onError() {
        setupState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setupState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setupState(State.EMPTY);
    }
}
