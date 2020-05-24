package com.example.ticketunion.ui.fragment;

import android.view.View;

import com.example.ticketunion.R;
import com.example.ticketunion.base.BaseFragment;

public class SearchFragment extends BaseFragment {

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }
    @Override
    protected void initView(View rootView) {
        setupState(State.SUCCESS);
    }
}
