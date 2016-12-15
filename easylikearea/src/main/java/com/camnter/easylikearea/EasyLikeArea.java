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
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
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
    private boolean addOmitView = true;

    private DisplayMetrics mMetrics;

    private static final int LAYOUT_DIRECTION_LEFT = 2601;
    private static final int LAYOUT_DIRECTION_RIGHT = 2602;


    @IntDef({ LAYOUT_DIRECTION_LEFT, LAYOUT_DIRECTION_RIGHT })
    @Retention(RetentionPolicy.SOURCE)
    public @interface LayoutDirection {

    }


    @LayoutDirection
    private int layoutDirection;


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
        this.likeSpacing = a.getDimensionPixelOffset(
            R.styleable.EasyLikeArea_easyLikeAreaLikeSpacing, this.dp2px(DEFAULT_LIKE_SPACING));
        this.omitSpacing = a.getDimensionPixelOffset(
            R.styleable.EasyLikeArea_easyLikeAreaOmitSpacing, this.dp2px(DEFAULT_OMIT_SPACING));
        this.omitCenter = a.getBoolean(R.styleable.EasyLikeArea_easyLikeAreaOmitCenter,
            DEFAULT_OMIT_CENTER);
        this.layoutDirection = a.getInt(R.styleable.EasyLikeArea_easyLayoutDirection,
            LAYOUT_DIRECTION_LEFT) == LAYOUT_DIRECTION_RIGHT ? LAYOUT_DIRECTION_RIGHT
                                                             : LAYOUT_DIRECTION_LEFT;
        a.recycle();
    }


    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        this.maxViewWidth = Math.max(this.maxViewWidth, viewWidth);

        final int paddingTop = this.getPaddingTop();
        final int paddingBottom = this.getPaddingBottom();
        final int paddingLeft = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();

        int likesWidth = 0;

        int resultWidth = 0;
        int resultHeight = 0;

        if (this.omitView != null && this.omitView.getVisibility() == VISIBLE) {
            this.measureChild(this.omitView, widthMeasureSpec, heightMeasureSpec);
            this.omitViewWidth = this.omitView.getMeasuredWidth();
            this.omitViewHeight = this.omitView.getMeasuredHeight();
            this.omitViewWidth += this.omitSpacing;
        }

        final int childCount = this.getChildCount();
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

                if (likesWidth + this.likeSpacing + likeWidth >
                    this.maxViewWidth - this.omitViewWidth - (paddingLeft + paddingRight)) {
                    resultWidth = likesWidth;
                    resultWidth += this.omitViewWidth;
                    this.isFull = true;
                    this.fullLikeCount = i + 1;
                    resultHeight = Math.max(resultHeight,
                        Math.max(likeHeight, this.omitViewHeight));
                    break;
                } else if (
                    likesWidth + this.likeSpacing + likeWidth + this.likeSpacing + likeWidth >
                        this.maxViewWidth - this.omitViewWidth -
                            (paddingLeft + paddingRight)) {
                    likesWidth += likeWidth + this.likeSpacing;
                    resultWidth = likesWidth;
                    resultWidth += this.omitViewWidth;
                    this.isFull = true;
                    this.fullLikeCount = i + 1;
                    resultHeight = Math.max(resultHeight,
                        Math.max(likeHeight, this.omitViewHeight));
                    break;
                } else {
                    this.isFull = false;
                    likesWidth += likeWidth + this.likeSpacing;
                    resultWidth = Math.max(resultWidth, likesWidth);
                }

                resultHeight = Math.max(resultHeight, likeHeight);
            }
            // Deal with the addView(...) when onCreate
            if (this.fullLikeCount != NO_FULL && this.fullLikeCount < childCount - 1) {
                super.removeViewAt(this.fullLikeCount);
            }
        }

        resultWidth += paddingLeft + paddingRight;
        resultHeight += paddingTop + paddingBottom;
        this.setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? viewWidth : resultWidth,
            (heightMode == MeasureSpec.EXACTLY) ? viewHeight : resultHeight);
    }


    /**
     * {@inheritDoc}
     *
     * @param changed changed
     * @param l l
     * @param t t
     * @param r r
     * @param b b
     */
    @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
        System.out.println(this.getWidth() + "");
        this.likeViews.clear();

        final int paddingTop = this.getPaddingTop();
        final int paddingLeft = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();

        final int viewWidth = this.getWidth();
        final int viewHeight = this.getHeight();

        int childCount = this.getChildCount();
        if (this.isHasLikes()) {
            for (int i = 0; i < childCount; i++) {
                if (childCount > 1 && i == childCount - 1) break;
                View like = this.getChildAt(i);
                this.likeViews.add(like);
            }
        }

        int start = this.layoutDirection == LAYOUT_DIRECTION_LEFT
                    ? paddingLeft
                    : viewWidth - paddingRight;

        if (this.isFull) {
            for (int i = 0; i < this.likeViews.size(); i++) {
                final View like = this.likeViews.get(i);
                final int likeWidth = like.getMeasuredWidth();
                final int likeHeight = like.getMeasuredHeight();
                int likeLeft;
                int likeRight;
                int likeBottom;
                if (this.layoutDirection == LAYOUT_DIRECTION_LEFT) {
                    likeLeft = start;
                    likeRight = likeLeft + likeWidth;
                    likeBottom = paddingTop + likeHeight;
                    like.layout(likeLeft, paddingTop, likeRight, likeBottom);
                    start += likeWidth;
                    start += this.likeSpacing;
                } else {
                    likeRight = start;
                    likeLeft = likeRight - likeWidth;
                    likeBottom = paddingTop + likeHeight;
                    like.layout(likeLeft, paddingTop, likeRight, likeBottom);
                    start -= likeWidth;
                    start -= this.likeSpacing;
                }
            }

            if (this.existOmitView()) {
                final int omitViewWidth = this.omitView.getMeasuredWidth();
                final int omitViewHeight = this.omitView.getMeasuredHeight();
                if (this.layoutDirection == LAYOUT_DIRECTION_LEFT) {
                    start += this.omitSpacing;
                    final int omitLeft = start;
                    int omitTop = paddingTop;
                    if (this.omitCenter) {
                        omitTop = viewHeight / 2 - omitViewHeight / 2;
                    }
                    final int omitRight = omitLeft + omitViewWidth;
                    final int omitBottom = omitTop + omitViewHeight;
                    this.omitView.layout(omitLeft, omitTop, omitRight, omitBottom);
                } else {
                    start -= this.omitSpacing;
                    final int omitRight = start;
                    int omitTop = paddingTop;
                    if (this.omitCenter) {
                        omitTop = viewHeight / 2 - omitViewHeight / 2;
                    }
                    final int omitLeft = omitRight - omitViewWidth;
                    final int omitBottom = omitTop + omitViewHeight;
                    this.omitView.layout(omitLeft, omitTop, omitRight, omitBottom);
                }

            }
        } else {
            if (this.isHasLikes()) {
                for (int i = 0; i < this.likeViews.size(); i++) {
                    final View like = this.likeViews.get(i);
                    final int likeWidth = like.getMeasuredWidth();
                    final int likeHeight = like.getMeasuredHeight();
                    int likeLeft;
                    int likeRight;
                    int likeBottom;
                    if (this.layoutDirection == LAYOUT_DIRECTION_LEFT) {
                        likeLeft = start;
                        likeRight = likeLeft + likeWidth;
                        likeBottom = paddingTop + likeHeight;
                        like.layout(likeLeft, paddingTop, likeRight, likeBottom);
                        start += likeWidth;
                        start += this.likeSpacing;
                    } else {
                        likeRight = start;
                        likeLeft = likeRight - likeWidth;
                        likeBottom = paddingTop + likeHeight;
                        like.layout(likeLeft, paddingTop, likeRight, likeBottom);
                        start -= likeWidth;
                        start -= this.likeSpacing;
                    }
                }
            }
        }
    }


    @Override public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(this.getContext(), attrs);
    }


    @Override protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }


    @Override protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }


    public boolean isHasLikes() {
        return this.getChildCount() > 1;
    }


    public boolean existOmitView() {
        return this.omitView != null && this.omitView.getVisibility() == VISIBLE;
    }


    public void setOmitView(View v) {
        if (this.omitView != null) this.removeView(this.omitView);
        this.addView(v, this.getChildCount());
        this.omitView = v;
        this.addOmitView = true;
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


    @Override public final void addView(View child) {
        if (this.omitView == null) {
            throw new RuntimeException("You must addView(...) after setOmitView(View v)");
        }
        try {
            this.easyProxy.addViewProxy(child, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override public void addView(View child, int index) {
        if (!this.addOmitView && this.omitView == null) {
            throw new RuntimeException("You must addView(...) after setOmitView(View v)");
        }
        try {
            this.easyProxy.addViewProxy(child, index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override public void removeView(View view) {
        try {
            this.easyProxy.removeViewProxy(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override public void removeViewAt(int index) {
        try {
            this.easyProxy.removeViewAtProxy(index);
        } catch (Exception e) {
            e.printStackTrace();
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


        private void addViewCache(View view) {
            this.mCacheViews.add(0, view);
            if (this.mCacheViews.size() > this.maxSize) {
                this.mCacheViews.remove(this.mCacheViews.size() - 2);
            }
        }


        private void removeViewCache(View view) {
            if (this.mCacheViews.contains(view)) this.mCacheViews.remove(view);
        }


        private void removeViewAtCache(int index) {
            if (index > this.mCacheViews.size() - 1) return;
            if (this.mCacheViews.get(index) != null) this.mCacheViews.remove(index);
        }


        public void addViewProxy(View child, int index) {
            if (EasyLikeArea.this.getChildCount() > 0 &&
                index > EasyLikeArea.this.getChildCount() - 1) {
                index = EasyLikeArea.this.fullLikeCount - 1;
            }
            if (EasyLikeArea.this.fullLikeCount != NO_FULL) {
                EasyLikeArea.super.addView(child, index);
                EasyLikeArea.super.removeViewAt(EasyLikeArea.this.fullLikeCount);
                // Add cache
                //this.addViewCache(child, EasyLikeArea.this.fullLikeCount - 1);
                this.addViewCache(child);
            } else {
                EasyLikeArea.super.addView(child, 0);
                // Add cache
                this.addViewCache(child);
            }
        }


        public void removeViewProxy(View view) {
            if (view == null) return;
            if (view.getParent() == null) return;
            if (EasyLikeArea.this.existOmitView() &&
                view.hashCode() == EasyLikeArea.this.omitView.hashCode()) {
                return;
            }

            if (EasyLikeArea.this.fullLikeCount != NO_FULL) {
                EasyLikeArea.super.removeView(view);

                // Remove cache
                this.removeViewCache(view);

                // Refresh cache
                View cache = this.getViewCache(EasyLikeArea.this.fullLikeCount - 1);
                if (cache != null &&
                    EasyLikeArea.this.existOmitView() &&
                    cache.hashCode() != EasyLikeArea.this.omitView.hashCode()) {
                    if (cache.getParent() != null) {
                        ((ViewGroup) cache.getParent()).removeView(cache);
                    }
                    EasyLikeArea.super.addView(cache, EasyLikeArea.this.fullLikeCount - 1);
                }
            } else {
                EasyLikeArea.super.removeView(view);

                // Remove cache
                this.removeViewCache(view);
            }
        }


        public void removeViewAtProxy(int index) {
            View o = EasyLikeArea.this.getChildAt(index);

            // Overstep
            if (index > EasyLikeArea.this.getChildCount() - 1) {
                index = EasyLikeArea.this.fullLikeCount - 1;
            }

            if (EasyLikeArea.this.omitView != null) {
                if (o == null) o = EasyLikeArea.super.getChildAt(index);

                if (EasyLikeArea.this.existOmitView() &&
                    o.hashCode() == EasyLikeArea.this.omitView.hashCode()) {
                    return;
                }

                if (EasyLikeArea.this.fullLikeCount != NO_FULL) {
                    EasyLikeArea.super.removeViewAt(index);
                    // Remove cache
                    this.removeViewAtCache(index);
                    // Refresh cache
                    View cache = this.getViewCache(EasyLikeArea.this.fullLikeCount - 1);
                    if (cache != null &&
                        EasyLikeArea.this.existOmitView() &&
                        cache.hashCode() != EasyLikeArea.this.omitView.hashCode()) {
                        if (cache.getParent() != null) {
                            ((ViewGroup) cache.getParent()).removeView(cache);
                        }
                        EasyLikeArea.super.addView(cache, EasyLikeArea.this.fullLikeCount - 1);
                    }
                } else {
                    EasyLikeArea.super.removeViewAt(index);
                    // Remove cache
                    this.removeViewCache(o);
                }
            } else {
                EasyLikeArea.super.removeViewAt(index);
                // Remove cache
                this.removeViewAtCache(index);
                if (EasyLikeArea.this.fullLikeCount != NO_FULL) {
                    View cache = this.getViewCache(index);
                    // Refresh cache
                    if (cache != null) {
                        EasyLikeArea.super.addView(cache, EasyLikeArea.this.fullLikeCount - 1);
                    }
                }
            }
        }


        @SuppressWarnings("unchecked") public <V extends View> V getViewCache(int position) {
            if (this.mCacheViews.size() - 1 > position && this.mCacheViews.get(position) != null) {
                return (V) this.mCacheViews.get(position);
            }
            return null;
        }


        public LinkedList<View> getCacheViews() {
            return mCacheViews;
        }
    }

}
