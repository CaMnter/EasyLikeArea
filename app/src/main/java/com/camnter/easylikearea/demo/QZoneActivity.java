package com.camnter.easylikearea.demo;

import android.view.LayoutInflater;

/**
 * Description：QZoneActivity
 * Created by：CaMnter
 * Time：2016-03-27 22:01
 */
public class QZoneActivity extends TopicActivity {
    @Override
    public void setOmitView() {
        this.topicEla.setOmitView(LayoutInflater.from(this).inflate(R.layout.view_omit_style_qzone, null));
    }
}
