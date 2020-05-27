package com.example.ticketunion.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.ticketunion.model.domain.HomePagerContent;
import com.example.ticketunion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/22 17:39
 * God bless my code!
 */
public class LooperPagerAdapter extends PagerAdapter {

    private List<HomePagerContent.DataBean> mData = new ArrayList<>();

    private OnLooperPagerClickListener mLooperPagerClickListener = null;

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //处理一下越界问题
        int realPosition = position % mData.size();
        HomePagerContent.DataBean dataBean = mData.get(realPosition);

        int measuredWidth = container.getMeasuredWidth();
        int measuredHeight = container.getMeasuredHeight();
        int ivSize = Math.max(measuredWidth, measuredHeight) / 2;
        String coverUrl = UrlUtils.getCoverPath(dataBean.getPict_url(), ivSize);
        Context context = container.getContext();
        ImageView iv = new ImageView(context);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLooperPagerClickListener != null) {
                    mLooperPagerClickListener.onLooperItemClick(dataBean);
                }
            }
        });
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(layoutParams);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(coverUrl).into(iv);
        container.addView(iv);
        return iv;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


    public int getDataSize() {
        return mData.size();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void setData(List<HomePagerContent.DataBean> contents) {
        mData.clear();
        mData.addAll(contents);
        notifyDataSetChanged();
    }

    public void setOnLooperPagerClickListener(OnLooperPagerClickListener looperPagerClickListener){
        this.mLooperPagerClickListener = looperPagerClickListener;
    }

    public interface OnLooperPagerClickListener{
        void onLooperItemClick(HomePagerContent.DataBean data);
    }
}
