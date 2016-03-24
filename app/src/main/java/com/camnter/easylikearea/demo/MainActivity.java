package com.camnter.easylikearea.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.camnter.easylikearea.EasyLikeArea;
import com.camnter.easylikearea.widget.EasyLikeImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EasyLikeArea easyLikeArea;
    private DisplayMetrics mMetrics;
    private Button addBt;
    private Button queryBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.easyLikeArea = (EasyLikeArea) this.findViewById(R.id.ela);
        this.mMetrics = this.getResources().getDisplayMetrics();
        this.easyLikeArea.setOmitView(LayoutInflater.from(this).inflate(R.layout.view_omit_style_one, null));
        this.addBt = (Button) this.findViewById(R.id.add_bt);
        this.queryBt = (Button) this.findViewById(R.id.query_bt);
        if (this.addBt != null) this.addBt.setOnClickListener(this);
        if (this.queryBt != null) this.queryBt.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_bt: {
                EasyLikeImageView iv = new EasyLikeImageView(this);
                iv.setTag("drakeet");
                iv.setImageResource(R.mipmap.ic_drakeet);
                iv.setLayoutParams(new ViewGroup.LayoutParams(this.dp2px(36), this.dp2px(36)));
                this.easyLikeArea.addView(iv, 0);
                break;
            }
            case R.id.query_bt: {
                EasyLikeImageView iv = new EasyLikeImageView(this);
                iv.setTag("camnter");
                iv.setImageResource(R.mipmap.ic_camnter);
                iv.setLayoutParams(new ViewGroup.LayoutParams(this.dp2px(36), this.dp2px(36)));
                this.easyLikeArea.addView(iv, 0);
                break;
            }
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
