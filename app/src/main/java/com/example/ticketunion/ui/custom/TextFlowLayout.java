package com.example.ticketunion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ticketunion.R;
import com.example.ticketunion.utils.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/6/4 15:11
 * God bless my code!
 */
public class TextFlowLayout extends ViewGroup {

    public static final float DEFAULT_SPACE = 10F;
    private float mItemHorizontalSpace = DEFAULT_SPACE;
    private float mItemVerticalSpace = DEFAULT_SPACE;
    private int mSelfWidth;
    private int mItemHeight;
    private OnFlowTextItemClickListener mItemClickListener = null;
    private List<String> mTextList = new ArrayList<>();

    public float getItemHorizontalSpace() {
        return mItemHorizontalSpace;
    }

    public void setItemHorizontalSpace(float itemHorizontalSpace) {
        mItemHorizontalSpace = itemHorizontalSpace;
    }

    public float getItemVerticalSpace() {
        return mItemVerticalSpace;
    }

    public void setItemVerticalSpace(float itemVerticalSpace) {
        mItemVerticalSpace = itemVerticalSpace;
    }

    public TextFlowLayout(Context context) {
        this(context, null);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlowTextStyle);
        mItemHorizontalSpace = ta.getDimension(R.styleable.FlowTextStyle_horizontalSpace, DEFAULT_SPACE);
        mItemVerticalSpace = ta.getDimension(R.styleable.FlowTextStyle_verticalSpace, DEFAULT_SPACE);
        ta.recycle();
        LogUtil.d(this, "mItemHorizontalSpace ==> " + mItemHorizontalSpace);
        LogUtil.d(this, "mItemVerticalSpace ==> " + mItemVerticalSpace);
    }

    //描述单行
    private List<View> line = null;
    //描述所有行
    private List<List<View>> lines = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() == 0) {
            return;
        }
        lines.clear();
        mSelfWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        //LogUtil.d(this, "self width ==> " + mSelfWidth);
        //测量
        int childCount = getChildCount();
        //LogUtil.d(this ,"onMeasure ==> " + childCount);
        //测量孩子
        line = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            View itemView = getChildAt(i);
            if (itemView.getVisibility() != VISIBLE) {
                //若不可见则不用测量
                continue;
            }
            //LogUtil.d(this, "before measure height ==> " + itemView.getMeasuredHeight());
            measureChild(itemView, widthMeasureSpec, heightMeasureSpec);
            //LogUtil.d(this, "after measure height ==> " + itemView.getMeasuredHeight());
            if (canBeAdd(itemView, line)) {
                //能添加到当前行
                line.add(itemView);
            } else {
                //不能添加到当前行
                lines.add(line);
                line = new ArrayList<>();
                line.add(itemView);
            }
//            if (line == null) {
//                //若当前行为空
//                createNewLIne(itemView);
//            } else {
//                //判断是否可以继续添加
//                if (canBeAdd(itemView, line)) {
//                    //可以添加
//                    line.add(itemView);
//                } else {
//                    //新创建一行
//                    createNewLIne(itemView);
//                }
//            }
        }
        lines.add(line);
        //测量自己
        //LogUtil.d(this, "lines size == > " + lines.size());
        mItemHeight = getChildAt(0).getMeasuredHeight();
        int selfHeight = (int) (lines.size() * mItemHeight + mItemVerticalSpace * (lines.size() + 1) + 0.5f);
        setMeasuredDimension(mSelfWidth, selfHeight);
    }

    /**
     * 配合上面被注释的代码使用
     */
//    private void createNewLIne(View itemView) {
//        line = new ArrayList<>();
//        line.add(itemView);
//        lines.add(line);
//    }

    public int getContentSize(){
        return mTextList.size();
    }

   //判断当前行能否继续添加
    private boolean canBeAdd(View itemView, List<View> line) {
        //所有的已经添加的子View宽度 + (line.size + 1) * mItemHorizontalSpace + itemView.getMeasureWidth
        //若小于当前控件的宽度，则可以添加，否则不能添加
        int totalWidth = itemView.getMeasuredWidth();
        for (View view : line) {
            //所有已经添加的view宽度
            totalWidth += view.getMeasuredWidth();
        }
        //水平间距
        totalWidth += mItemHorizontalSpace * (line.size() + 1);
        //LogUtil.d(this, "total width ==> " + totalWidth);
        //LogUtil.d(this, "mSelfWidth ==> " + mSelfWidth);
        return totalWidth <= mSelfWidth;
    }


    /**
     * 用来给暴露外部设置数据
     * @param textList
     */
    public void setTextList(List<String> textList) {
        removeAllViews();
        this.mTextList.clear();
        this.mTextList.addAll(textList);
        Collections.reverse(mTextList);
        for (String text : mTextList) {
            //LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, true);
            //上面一句等价于下面一句 + addView(item);
            TextView item = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, false);
            item.setText(text);
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onFlowItemClick(text);
                    }
                }
            });
            addView(item);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //布局摆放
        int childCount = getChildCount();
        LogUtil.d(this ,"onLayout ==> " + childCount);
        int top = (int) mItemVerticalSpace;
        for (List<View> line : lines) {
            int left = (int) mItemHorizontalSpace;
            for (View view : line) {
                view.layout(left, top, left + view.getMeasuredWidth(), top + view.getMeasuredHeight());
                left += view.getMeasuredWidth() + mItemHorizontalSpace;
            }
            top += mItemHeight + mItemVerticalSpace;
        }
    }

    public void setOnFlowTextItemClickListener(OnFlowTextItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnFlowTextItemClickListener{
        void onFlowItemClick(String text);
    }
}
