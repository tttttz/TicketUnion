package com.example.ticketunion.ui.adapter;

import android.content.Context;
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
import com.example.ticketunion.model.domain.HomePagerContent;
import com.example.ticketunion.utils.LogUtil;
import com.example.ticketunion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/21 19:15
 * God bless my code!
 */
public class HomePagerContentAdapter extends RecyclerView.Adapter<HomePagerContentAdapter.InnerHolder> {


    private List<HomePagerContent.DataBean> mData = new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_pager_content, parent, false);

        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        HomePagerContent.DataBean dataBean = mData.get(position);
        holder.setData(dataBean);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 设置数据
     * @param contents
     */
    public void setData(List<HomePagerContent.DataBean> contents) {
        mData.clear();
        mData.addAll(contents);
        notifyDataSetChanged();
    }

    public void addData(List<HomePagerContent.DataBean> contents) {
        //获得原始数据的长度
        int oldSize = mData.size();
        //将新数据添加到集合
        mData.addAll(contents);
        //更新UI
        notifyItemRangeChanged(oldSize, contents.size());
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.goods_cover)
        public ImageView cover;

        @BindView(R.id.goods_title)
        public TextView title;

        @BindView(R.id.goods_off_price)
        public TextView offPriceTv;

        @BindView(R.id.goods_original_price)
        public TextView originalPriceTv;

        @BindView(R.id.goods_after_off_price)
        public TextView afterOffPriceTv;

        @BindView(R.id.goods_volume)
        public TextView sellCountTv;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(HomePagerContent.DataBean dataBean) {
            Context context = itemView.getContext();
            title.setText(dataBean.getTitle());
            ViewGroup.LayoutParams layoutParams = cover.getLayoutParams();
            int width = layoutParams.width;
            int height = layoutParams.height;
            int coverSize = Math.max(width, height) / 2;
            String coverPath = UrlUtils.getCoverPath(dataBean.getPict_url(), coverSize);
            LogUtil.d(this, "image url == >" + coverPath);
            Glide.with(context).load(coverPath).into(cover);

            //省的金额
            long coupon_amount = dataBean.getCoupon_amount();
            //原价格
            String finalPrice = dataBean.getZk_final_price();
            //现价格
            double resultPrice = Double.parseDouble(finalPrice) - coupon_amount;
            //30天销量
            long sellCountNum = dataBean.getVolume();

            //省去的金额 格式化
            String offPrice = String.format(context.getString(R.string.text_goods_off_price),
                    coupon_amount);
            //现价格 格式化
            String nowPrice = String.format("%.2f", resultPrice);
            //原价格格式化
            String originalPrice = String.format(context.getString(R.string.text_goods_original_price), finalPrice);
            //销量格式化
            String sellCount = String.format(context.getString(R.string.text_goods_sell_count), sellCountNum);

            offPriceTv.setText(offPrice);
            afterOffPriceTv.setText(nowPrice);
            originalPriceTv.setText(originalPrice);
            sellCountTv.setText(sellCount);
            //给原价格中间添加一条线
            originalPriceTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}
