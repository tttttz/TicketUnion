package com.example.ticketunion.ui.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ticketunion.R;
import com.example.ticketunion.model.domain.OnSellContent;
import com.example.ticketunion.utils.LogUtil;
import com.example.ticketunion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/6/1 17:48
 * God bless my code!
 */
public class OnSellContentAdapter extends RecyclerView.Adapter<OnSellContentAdapter.InnerHolder> {

    private List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mData = new ArrayList<>();
    private OnSellPageItemClickListener mSellItemListener = null;

    @NonNull
    @Override
    public OnSellContentAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_on_sell_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OnSellContentAdapter.InnerHolder holder, int position) {
        OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean item = mData.get(position);
        holder.setData(item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSellItemListener != null) {
                    mSellItemListener.onSellItemClick(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(OnSellContent result) {
        mData.clear();
        mData.addAll(result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data());
        notifyDataSetChanged();
    }

    /**
     * 加载更多的内容
     * @param result
     */
    public void loadMore(OnSellContent result) {
        List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> moreData = result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        int oldDataSize = mData.size();
        mData.addAll(moreData);
        notifyItemRangeChanged(oldDataSize - 1, moreData.size());
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.on_sell_cover)
        ImageView coverIv;
        @BindView(R.id.on_sell_title)
        TextView titleTv;
        @BindView(R.id.on_sell_origin_price_tv)
        TextView originPriceTv;
        @BindView(R.id.on_sell_off_price_tv)
        TextView offPriceTv;
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean item) {
            String title = item.getTitle();
            String originPrice = item.getZk_final_price();
            int couponAmount = item.getCoupon_amount();
            float originPriceFloat = Float.parseFloat(originPrice);
            float finalPrice = originPriceFloat - couponAmount;
            String coverPath = UrlUtils.getCoverPath(item.getPict_url());
            LogUtil.d(this, "item url " + coverPath);
            titleTv.setText(title);
            Glide.with(itemView.getContext()).load(coverPath).into(coverIv);
            originPriceTv.setText("￥" + originPrice);
            originPriceTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            offPriceTv.setText(String.format("券后价:%.2f", finalPrice));
        }
    }

    public void setOnSellPageItemClickListener(OnSellPageItemClickListener listener) {
        this.mSellItemListener = listener;
    }

    public interface OnSellPageItemClickListener{
        void onSellItemClick(OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean item);
    }
}
