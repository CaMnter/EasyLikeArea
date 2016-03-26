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
import java.util.LinkedList;
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

    private EasyViewProxy easyProxy;

    /**************
     * Default px *
     **************/
    private int likeSpacing;
    private int omitSpacing;

    private View omitView;
    private int omitViewWidth;
    private int omitViewHeight;
    private boolean omitCenter;

    private int maxViewWidth = 0;

    private static final int NO_FULL = -106;
    private int fullLikeCount = NO_FULL;

    private boolean isFull = false;

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
        this.easyProxy = new EasyViewProxy();

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

        this.maxViewWidth = Math.max(this.maxViewWidth, viewWidth);

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
        if (this.isHasLikes()) {
            for (int i = 0; i < childCount; i++) {
                if (i == childCount - 1) break;
                View like = this.getChildAt(i);
                if (like.getVisibility() == View.VISIBLE) {
                    this.measureChild(like, widthMeasureSpec, heightMeasureSpec);
                } else {
                    continue;
                }
                int likeWidth = like.getMeasuredWidth();
                int likeHeight = like.getMeasuredHeight();

                if (likesWidth + this.likeSpacing + likeWidth > this.maxViewWidth - this.omitViewWidth) {
                    resultWidth = likesWidth;
                    resultWidth += this.omitViewWidth;
                    this.isFull = true;
                    resultHeight = Math.max(resultHeight, Math.max(likeHeight, this.omitViewHeight));
                    break;
                } else {
                    this.isFull = false;
                    likesWidth += likeWidth + this.likeSpacing;
                    resultWidth = Math.max(resultWidth, likesWidth);
                }
                resultHeight = Math.max(resultHeight, likeHeight);
            }
        }

        resultWidth += paddingLeft + paddingRight;
        resultHeight += paddingTop + paddingBottom;
        this.setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? viewWidth : resultWidth, (heightMode == MeasureSpec.EXACTLY) ? viewHeight : resultHeight);
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

        int childCount = this.getChildCount();
        if (this.isHasLikes()) {
            for (int i = 0; i < childCount; i++) {
                if (childCount > 1 && i == childCount - 1) break;
                View like = this.getChildAt(i);
                int likeWidth = like.getMeasuredWidth();

                // Add likeSpacing
                likesWidth += this.likeSpacing;
                if (this.isFull) {
                    if (likesWidth + likeWidth > viewWidth - this.omitViewWidth) {
                        break;
                    } else {
                        likesWidth += likeWidth;
                        this.likeViews.add(like);
                    }
                } else {
                    likesWidth += likeWidth;
                    this.likeViews.add(like);
                }
            }
        }
        int left = paddingLeft;
        if (this.isFull) {
            this.fullLikeCount = this.likeViews.size();
//            /*
//             * Fix addView bug:
//             * Because onLayout() after addView(),and the addView() is not the first time know the
//             * fullLikeCount.
//             * So, will also have more than one child to the View in the childViews of the EasyLikeArea
//             */
            for (int i = 0; i < this.likeViews.size(); i++) {
                View like = this.likeViews.get(i);
                int likeLeft = left;
                int likeRight = likeLeft + like.getMeasuredWidth();
                int likeBottom = paddingTop + like.getMeasuredHeight();
                like.layout(likeLeft, paddingTop, likeRight, likeBottom);
                left += like.getMeasuredWidth();
                left += this.likeSpacing;
            }

            if (this.existOmitView()) {
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
            if (this.isHasLikes()) {
                for (int i = 0; i < this.likeViews.size(); i++) {
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

    public boolean isHasLikes() {
        return this.getChildCount() > 1;
    }

    public boolean existOmitView() {
        return this.omitView != null && this.omitView.getVisibility() == VISIBLE;
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

    public List<View> getLikeViews() {
        return likeViews;
    }

    public LinkedList<View> getViewCache() {
        return this.easyProxy.getCacheViews();
    }

    @Override
    public final void addView(View child) {
//        if (this.omitView != null) {
//            if (this.fullLikeCount != -1) {
//                super.addView(child, this.fullLikeCount - 1);
//                super.removeViewAt(this.fullLikeCount);
//                this.viewProxy.addView(child, this.fullLikeCount - 1);
//            } else {
//                super.addView(child, this.getChildCount() - 1);
//                this.viewProxy.addView(child);
//            }
//        } else {
//            super.addView(child);
//            this.viewProxy.addView(child);
//        }
        this.easyProxy.addViewProxy(child);
    }

    @Override
    public void addView(View child, int index) {
        this.easyProxy.addViewProxy(child, index);
//        if (this.omitView != null && index > this.getChildCount() - 1) {
//            if (this.fullLikeCount != -1) {
//                super.addView(child, this.fullLikeCount - 1);
//                super.removeViewAt(this.fullLikeCount);
//                this.viewProxy.addView(child, this.fullLikeCount - 1);
//            } else {
//                super.addView(child, this.getChildCount() - 1);
//                this.viewProxy.addView(child);
//            }
//        } else {
//            super.addView(child, index);
//            this.viewProxy.addView(child);
//        }
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

    private class EasyViewProxy {

        private static final int DEFAULT_MAX_CACHE_VIEW_COUNT = 17;

        private LinkedList<View> mCacheViews;
        private int maxSize;

        public EasyViewProxy() {
            this(DEFAULT_MAX_CACHE_VIEW_COUNT);
        }

        public EasyViewProxy(int maxSize) {
            this.mCacheViews = new LinkedList<>();
            this.maxSize = maxSize;
        }

        public void addView(View view) {
            this.mCacheViews.add(0, view);
            if (this.mCacheViews.size() > this.maxSize)
                this.mCacheViews.remove(this.mCacheViews.size() - 2);
        }

        public void addView(View view, int index) {
            if (index < this.mCacheViews.size() - 2) {
                this.mCacheViews.add(index, view);
                if (this.mCacheViews.size() > this.maxSize)
                    this.mCacheViews.remove(this.mCacheViews.size() - 2);
            } else {
                if (this.mCacheViews.size() + 1 > this.maxSize)
                    this.mCacheViews.remove(this.mCacheViews.size() - 2);
                this.mCacheViews.add(this.mCacheViews.size() - 2, view);
            }
        }

        public void addViewProxy(View child) {
            if (EasyLikeArea.this.omitView != null) {
                if (child.hashCode() == EasyLikeArea.this.omitView.hashCode()) return;
                if (EasyLikeArea.this.fullLikeCount != NO_FULL) {
                    EasyLikeArea.super.addView(child, EasyLikeArea.this.fullLikeCount - 1);
                    EasyLikeArea.super.removeViewAt(EasyLikeArea.this.fullLikeCount);
                    // cache
                    this.addView(child, EasyLikeArea.this.fullLikeCount - 1);
                } else {
                    EasyLikeArea.super.addView(child, EasyLikeArea.this.getChildCount() - 1);
                    // cache
                    this.addView(child);
                }
            } else {
                if (EasyLikeArea.this.fullLikeCount != NO_FULL) {
                    EasyLikeArea.super.addView(child, EasyLikeArea.this.fullLikeCount - 1);
                    EasyLikeArea.super.removeViewAt(EasyLikeArea.this.fullLikeCount);
                    // cache
                    this.addView(child, EasyLikeArea.this.fullLikeCount - 1);
                } else {
                    EasyLikeArea.super.addView(child, EasyLikeArea.this.getChildCount() - 1);
                    // cache
                    this.addView(child);
                }
//                EasyLikeArea.super.addView(child);
//                // cache
//                this.addView(child);
            }
        }

        public void addViewProxy(View child, int index) {
            if (EasyLikeArea.this.omitView != null && index > EasyLikeArea.this.getChildCount() - 1) {
                if (child.hashCode() == EasyLikeArea.this.omitView.hashCode()) return;
                if (EasyLikeArea.this.fullLikeCount != NO_FULL) {
                    EasyLikeArea.super.addView(child, EasyLikeArea.this.fullLikeCount - 1);
                    EasyLikeArea.super.removeViewAt(EasyLikeArea.this.fullLikeCount);
                    // cache
                    this.addView(child, EasyLikeArea.this.fullLikeCount - 1);
                } else {
                    EasyLikeArea.super.addView(child, EasyLikeArea.this.getChildCount() - 1);
                    // cache
                    this.addView(child);
                }
            } else {
                if (EasyLikeArea.this.fullLikeCount != NO_FULL) {
                    EasyLikeArea.super.addView(child, index);
                    EasyLikeArea.super.removeViewAt(EasyLikeArea.this.fullLikeCount - 1);

                    this.addView(child, index);
                } else {
                    EasyLikeArea.super.addView(child, index);
                    // cache
                    this.addView(child);
                }
//                EasyLikeArea.super.addView(child, index);
//                // cache
//                this.addView(child);
            }
        }

        public void addViewProxy(View child, int index, LayoutParams params) {
            if (EasyLikeArea.this.omitView != null && index > EasyLikeArea.this.getChildCount() - 1) {
                if (child.hashCode() == EasyLikeArea.this.omitView.hashCode()) return;
                if (EasyLikeArea.this.fullLikeCount != NO_FULL) {
                    EasyLikeArea.super.addView(child, EasyLikeArea.this.fullLikeCount - 1, params);
                    EasyLikeArea.super.removeViewAt(EasyLikeArea.this.fullLikeCount);
                    // cache
                    this.addView(child, EasyLikeArea.this.fullLikeCount - 1);
                } else {
                    EasyLikeArea.super.addView(child, EasyLikeArea.this.getChildCount() - 1, params);
                    // cache
                    this.addView(child);
                }
            } else {
                EasyLikeArea.super.addView(child, index, params);
                // cache
                this.addView(child);
            }
        }

        @SuppressWarnings("unchecked")
        public <V extends View> V getView(int position) {
            return (V) this.mCacheViews.get(position);
        }

        public List<View> getFullLikeView(int fullLikeCount) {
            List<View> fullLikeView = new ArrayList<>();
            for (int i = 0; i < fullLikeCount; i++) {
                fullLikeView.add(this.mCacheViews.get(i));
            }
            return fullLikeView;
        }

        public LinkedList<View> getCacheViews() {
            return mCacheViews;
        }

    }

}
