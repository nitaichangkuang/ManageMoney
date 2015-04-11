package com.jack.ManageMoney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by asus on 2015/4/10.
 */
public class DrawArcActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawarcactivity);
        ViewUtils.inject(this);
    }
    @OnClick(R.id.drawButton)
    public void drawButton(View view) {
        Intent intent = new Intent();
        intent.setClass(this,DrawShouRuActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.drawButton2)
    public void drawButton2(View view) {
        Intent intent = new Intent();
        intent.setClass(this,DrawZhiChuActivity.class);
        startActivity(intent);
    }
}