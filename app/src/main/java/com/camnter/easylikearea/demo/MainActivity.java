package com.camnter.easylikearea.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.TextView;

import com.camnter.easylikearea.EasyLikeArea;

public class MainActivity extends AppCompatActivity {

    private EasyLikeArea easyLikeArea;
    private DisplayMetrics metrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.easyLikeArea = (EasyLikeArea) this.findViewById(R.id.ela);
        this.metrics = this.getResources().getDisplayMetrics();
        TextView tv = new TextView(this);
        tv.setText("17人赞过");
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setTextColor(0xffBBBBBB);
        tv.setPadding(0, 30, 10, 30);
        tv.setTextSize(12.0f);

        this.easyLikeArea.setOmitView(tv);
    }

}
