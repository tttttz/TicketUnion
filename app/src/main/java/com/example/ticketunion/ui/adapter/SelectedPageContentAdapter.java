package com.example.ticketunion.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ticketunion.R;
import com.example.ticketunion.base.IBaseInfo;
import com.example.ticketunion.model.domain.SelectedContent;
import com.example.ticketunion.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/31 15:53
 * God bless my code!
 */
public class SelectedPageContentAdapter extends RecyclerView.Adapter<SelectedPageContentAdapter.InnerHolder> {

    private static final int SUCCESS_CODE = 10000;

    private List<SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean> mData
            = new ArrayList<>();
    private OnSelectedPageContentItemClickListener mContentItemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean itemData = mData.get(position);
        holder.setData(itemData);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContentItemClickListener != null) {
                    mContentItemClickListener.onContentItemClick(itemData);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(SelectedContent content) {
        if (content.getCode() == SUCCESS_CODE) {
            List<SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean> item = content.getData().getTbk_uatm_favorites_item_get_response().getResults().getUatm_tbk_item();
            mData.clear();
            mData.addAll(item);
            notifyDataSetChanged();
        }
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.selected_cover)
        ImageView cover;
        @BindView(R.id.selected_off_price)
        TextView offPriceTv;
        @BindView(R.id.selected_title)
        TextView title;
        @BindView(R.id.selected_origin_price)
        TextView originPriceTv;
        @BindView(R.id.selected_buy_btn)
        TextView buyBtn;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean itemData) {
            title.setText(itemData.getTitle());
            String coverUrl = itemData.getPict_url();
            LogUtil.d(this, "cover url in right ==> " + coverUrl);
            if (coverUrl != null) {
                Glide.with(itemView.getContext()).load(coverUrl).into(cover);
            } else {
                cover.setImageResource(R.mipmap.no_image);
            }
            if (TextUtils.isEmpty(itemData.getCoupon_click_url())) {
                originPriceTv.setText("晚了，没有优惠券了");
                buyBtn.setVisibility(View.GONE);
            } else {
                originPriceTv.setText("原价:" + itemData.getZk_final_price());
                buyBtn.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(itemData.getCoupon_info())) {
                offPriceTv.setVisibility(View.GONE);
            } else {
                offPriceTv.setVisibility(View.VISIBLE);
                offPriceTv.setText(itemData.getCoupon_info());
            }
        }
    }

    public void setOnSelectedPageContentItemClickListener(OnSelectedPageContentItemClickListener listener) {
        this.mContentItemClickListener = listener;
    }

    public interface OnSelectedPageContentItemClickListener {
        void onContentItemClick(IBaseInfo item);
    }
}
