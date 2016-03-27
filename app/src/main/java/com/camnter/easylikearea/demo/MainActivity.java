package com.camnter.easylikearea.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.camnter.easylikearea.EasyLikeArea;
import com.camnter.easylikearea.widget.EasyLikeImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int[] avatars = {
            R.mipmap.ic_harry_chen,
            R.mipmap.ic_undownding,
            R.mipmap.ic_fython,
            R.mipmap.ic_kaede_akatsuki,
            R.mipmap.ic_qixingchen,
            R.mipmap.ic_peter_cai,
            R.mipmap.ic_drakeet,
    };
    private DisplayMetrics mMetrics;

    private EasyLikeArea topicEla;
    private EasyLikeImageView addIv;
    private boolean added = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.initViews();
        this.initListeners();
        this.initLikeArea();
    }

    private void initViews() {
        this.mMetrics = this.getResources().getDisplayMetrics();
        this.topicEla = (EasyLikeArea) this.findViewById(R.id.topic_ela);
        this.topicEla.setOmitView(LayoutInflater.from(this).inflate(R.layout.view_omit_style_one, null));
        this.addIv = this.createEasyLikeImageView();
        this.addIv.setImageResource(R.mipmap.ic_camnter);
    }

    private void initListeners() {
        View likeTv = this.findViewById(R.id.topic_like_tv);
        if (likeTv != null) likeTv.setOnClickListener(this);
        View chatTv = this.findViewById(R.id.topic_chat_tv);
        if (chatTv != null) chatTv.setOnClickListener(this);
        View shareTv = this.findViewById(R.id.topic_share_tv);
        if (shareTv != null) shareTv.setOnClickListener(this);
    }

    private EasyLikeImageView createEasyLikeImageView() {
        EasyLikeImageView iv = new EasyLikeImageView(this);
        iv.setLayoutParams(new ViewGroup.LayoutParams(this.dp2px(36), this.dp2px(36)));
        return iv;
    }

    private void initLikeArea() {
        for (int idRes : avatars) {
            EasyLikeImageView iv = this.createEasyLikeImageView();
            iv.setImageResource(idRes);
            this.topicEla.addView(iv);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topic_like_tv:
                if (!added) {
                    this.topicEla.addView(this.addIv);
                    this.added = true;
                } else {
                    this.topicEla.removeView(this.addIv);
                    this.added = false;
                }
                break;
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
