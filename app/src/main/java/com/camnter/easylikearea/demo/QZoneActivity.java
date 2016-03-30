package com.camnter.easylikearea.demo;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Description：QZoneActivity
 * Created by：CaMnter
 * Time：2016-03-27 22:01
 */
public class QZoneActivity extends TopicActivity {
    public void setOmitView(int count) {
        View omitView = LayoutInflater.from(this).inflate(R.layout.view_omit_style_qzone, null);
        this.omitTv = (TextView) omitView.findViewById(R.id.qzone_omit_tv);
        this.omitTv.setText(this.getString(this.getOmitVieStringFormatId(), count));
        this.topicEla.setOmitView(omitView);
    }

    @Override public int getOmitVieStringFormatId() {
        return R.string.view_omit_style_qzone_content;
    }
}
