package com.camnter.easylikearea.demo.debug;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.camnter.easylikearea.EasyLikeArea;
import com.camnter.easylikearea.demo.R;
import com.camnter.easylikearea.widget.EasyLikeImageView;

/**
 * Description：DebugActivity
 * Created by：CaMnter
 * Time：2016-03-27 19:20
 */
public class DebugActivity extends AppCompatActivity implements View.OnClickListener {

    private EasyLikeArea easyLikeArea;
    private DisplayMetrics mMetrics;
    private Button dkBt;
    private Button caBt;
    private Button caRemoveBt;
    private Button queryBt;
    private Button likesBt;
    private Button cacheBt;

    private int dkCount = 0;
    private int caCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        this.easyLikeArea = (EasyLikeArea) this.findViewById(R.id.ela);
        this.mMetrics = this.getResources().getDisplayMetrics();
        this.easyLikeArea.setOmitView(LayoutInflater.from(this).inflate(R.layout.view_omit_style_one, null));
        this.dkBt = (Button) this.findViewById(R.id.dk_bt);
        this.caBt = (Button) this.findViewById(R.id.ca_bt);
        this.queryBt = (Button) this.findViewById(R.id.query_bt);
        this.likesBt = (Button) this.findViewById(R.id.likes_bt);
        this.cacheBt = (Button) this.findViewById(R.id.cache_bt);
        this.caRemoveBt = (Button) this.findViewById(R.id.ca_rm_bt);
        if (this.dkBt != null) this.dkBt.setOnClickListener(this);
        if (this.caBt != null) this.caBt.setOnClickListener(this);
        if (this.queryBt != null) this.queryBt.setOnClickListener(this);
        if (this.likesBt != null) this.likesBt.setOnClickListener(this);
        if (this.cacheBt != null) this.cacheBt.setOnClickListener(this);
        if (this.caRemoveBt != null) this.caRemoveBt.setOnClickListener(this);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dk_bt: {
                EasyLikeImageView iv = new EasyLikeImageView(this);
                iv.setTag("drakeet-" + dkCount++);
                iv.setImageResource(R.mipmap.ic_drakeet);
                iv.setLayoutParams(new ViewGroup.LayoutParams(this.dp2px(36), this.dp2px(36)));
                iv.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(DebugActivity.this, v.getTag() + "\nX = " + v.getX(), Toast.LENGTH_SHORT).show();

                    }
                });
                this.easyLikeArea.addView(iv, 0);
                break;
            }

            case R.id.ca_bt: {
                EasyLikeImageView iv = new EasyLikeImageView(this);
                iv.setTag("camnter-" + caCount++);
                iv.setImageResource(R.mipmap.ic_camnter);
                iv.setLayoutParams(new ViewGroup.LayoutParams(this.dp2px(36), this.dp2px(36)));
                iv.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(DebugActivity.this, v.getTag() + "\nX = " + v.getX(), Toast.LENGTH_SHORT).show();
                    }
                });
                this.easyLikeArea.addView(iv, 0);
                break;
            }
            case R.id.query_bt:
                System.out.println("ChildCount : " + this.easyLikeArea.getChildCount());
                for (int i = 0; i < this.easyLikeArea.getChildCount(); i++) {
                    View child = this.easyLikeArea.getChildAt(i);
                    System.out.println(" i: " + i + "\t\t Name: " + child.getClass().getSimpleName() + "\t\t Tag: " + child.getTag());
                }
                break;
            case R.id.likes_bt:
                System.out.println("LikeViews Count : " + this.easyLikeArea.getLikeViews().size());
                for (int i = 0; i < this.easyLikeArea.getLikeViews().size(); i++) {
                    View child = this.easyLikeArea.getLikeViews().get(i);
                    System.out.println(" i: " + i + "\t\t Name: " + child.getClass().getSimpleName() + "\t\t Tag: " + child.getTag());
                }
                break;
            case R.id.cache_bt:
                System.out.println("Cache Count : " + this.easyLikeArea.getViewCache().size());
                for (int i = 0; i < this.easyLikeArea.getViewCache().size(); i++) {
                    View child = this.easyLikeArea.getViewCache().get(i);
                    System.out.println(" i: " + i + "\t\t Name: " + child.getClass().getSimpleName() + "\t\t Tag: " + child.getTag());
                }
                break;
            case R.id.ca_rm_bt:
                this.easyLikeArea.removeViewAt(0);
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
