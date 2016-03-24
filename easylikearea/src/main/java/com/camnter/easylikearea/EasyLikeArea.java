/*
 * Copyright (C) 2016 CaMnter yuanyu.camnter@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.camnter.easylikearea;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：EasyLikeArea
 * Created by：CaMnter
 * Time：2016-03-23 15:01
 */
public class EasyLikeArea extends ViewGroup {

    private List<View> likeViews = new ArrayList<>();

    private static final boolean DEFAULT_OMIT_CENTER = true;

    /**************
     * Default dp *
     **************/
    private static final int DEFAULT_LIKE_SPACING = 5;
    private static final int DEFAULT_OMIT_SPACING = 8;

    /**************
     * Default px *
     **************/
    private int likeSpacing;
    private int omitSpacing;

    private View omitView;
    private int omitViewWidth;
    private int omitViewHeight;
    private boolean omitCenter;

    private int fullLikeCount = -1;

    private DisplayMetrics mMetrics;

    public EasyLikeArea(Context context) {
        super(context);
        this.init(context, null);
    }

    public EasyLikeArea(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public EasyLikeArea(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EasyLikeArea(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mMetrics = this.getResources().getDisplayMetrics();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EasyLikeArea);
        this.likeSpacing = a.getDimensionPixelOffset(R.styleable.EasyLikeArea_easyLikeAreaLikeSpacing, this.dp2px(DEFAULT_LIKE_SPACING));
        this.omitSpacing = a.getDimensionPixelOffset(R.styleable.EasyLikeArea_easyLikeAreaOmitSpacing, this.dp2px(DEFAULT_OMIT_SPACING));
        this.omitCenter = a.getBoolean(R.styleable.EasyLikeArea_easyLikeAreaOmitCenter, DEFAULT_OMIT_CENTER);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int paddingTop = this.getPaddingTop();
        int paddingBottom = this.getPaddingBottom();
        int paddingLeft = this.getPaddingLeft();
        int paddingRight = this.getPaddingRight();

        int likesWidth = 0;

        int resultWidth = 0;
        int resultHeight = 0;

        if (this.omitView != null && this.omitView.getVisibility() == VISIBLE) {
            this.measureChild(this.omitView, widthMeasureSpec, heightMeasureSpec);
            this.omitViewWidth = this.omitView.getMeasuredWidth();
            this.omitViewHeight = this.omitView.getMeasuredHeight();
            this.omitViewWidth += this.omitSpacing;
        }

        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View like = this.getChildAt(i);
            if (like.getVisibility() == View.VISIBLE) {
                this.measureChild(like, widthMeasureSpec, heightMeasureSpec);
            } else {
                continue;
            }
            int likeWidth = like.getMeasuredWidth();
            int likeHeight = like.getMeasuredHeight();

            // Add likeSpacing
            likesWidth += this.likeSpacing;
            if (likesWidth + likeWidth > viewWidth - this.omitViewWidth) {
                resultWidth = likesWidth;
                break;
            } else {
                likesWidth += likeWidth;
                resultWidth = Math.max(resultWidth, likesWidth);
            }
            resultHeight = Math.max(resultHeight, Math.max(likeHeight, this.omitViewHeight));
        }
        resultWidth += this.omitViewWidth;

        resultWidth += paddingLeft + paddingRight;
        resultHeight += paddingTop + paddingBottom;
        this.setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? viewWidth : resultWidth,
                (heightMode == MeasureSpec.EXACTLY) ? viewHeight : resultHeight);
    }

    /**
     * {@inheritDoc}
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        this.likeViews.clear();

        int paddingTop = this.getPaddingTop();
        int paddingLeft = this.getPaddingLeft();

        int likesWidth = 0;
        int viewWidth = this.getWidth();
        boolean isFull = false;

        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View like = this.getChildAt(i);
            int likeWidth = like.getMeasuredWidth();

            // Add likeSpacing
            likesWidth += this.likeSpacing;
            if (likesWidth + likeWidth > viewWidth - this.omitViewWidth) {
                isFull = true;
                break;
            } else {
                likesWidth += likeWidth;
                this.likeViews.add(like);
            }
        }
        int left = paddingLeft;
        if (isFull) {
            this.fullLikeCount = this.likeViews.size();
            for (int i = 0; i < this.likeViews.size(); i++) {
                View like = this.likeViews.get(i);
                int likeLeft = left;
                int likeRight = likeLeft + like.getMeasuredWidth();
                int likeBottom = paddingTop + like.getMeasuredHeight();
                like.layout(likeLeft, paddingTop, likeRight, likeBottom);
                left += like.getMeasuredWidth();
                left += this.likeSpacing;
            }
            if (this.omitView != null && this.omitView.getVisibility() == VISIBLE) {
                left += this.omitSpacing;
                int omitLeft = left;
                int omitTop = paddingTop;
                if (this.omitCenter) {
                    omitTop = this.getHeight() / 2 - this.omitView.getMeasuredHeight() / 2;
                }
                int omitRight = omitLeft + this.omitView.getMeasuredWidth();
                int omitBottom = omitTop + this.omitView.getMeasuredHeight();
                this.omitView.layout(omitLeft, omitTop, omitRight, omitBottom);
            }
        } else {
            for (int i = 0; i < this.likeViews.size(); i++) {
                if (i == this.likeViews.size() - 1) break;
                View like = this.likeViews.get(i);
                int likeLeft = left;
                int likeRight = likeLeft + like.getMeasuredWidth();
                int likeBottom = paddingTop + like.getMeasuredHeight();
                like.layout(likeLeft, paddingTop, likeRight, likeBottom);
                left += like.getMeasuredWidth();
                left += this.likeSpacing;
            }
        }

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(this.getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    public void setOmitView(View v) {
        if (this.omitView != null)
            this.removeView(this.omitView);
        this.addView(v, this.getChildCount());
        this.omitView = v;
        this.invalidate();
    }

    public View getOmitView() {
        return this.omitView;
    }

    @Override
    public final void addView(View child) {
        if (this.omitView != null) {
            if (this.fullLikeCount != -1) {
//                super.addView(child, this.fullLikeCount - 1);
                super.addView(child, this.fullLikeCount - 2);
            } else {
                super.addView(child, this.getChildCount() - 1);
            }
        } else {
            super.addView(child);
        }
    }

    @Override
    public void addView(View child, int index) {
        if (this.omitView != null && index > this.getChildCount() - 1) {
            if (this.fullLikeCount != -1) {
                super.addView(child, this.fullLikeCount - 1);
            } else {
                super.addView(child, this.getChildCount() - 1);
            }
        } else {
            super.addView(child, index);
        }
    }

    /**
     * Dp to px
     *
     * @param dp dp
     * @return px
     */
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, this.mMetrics);
    }

}
