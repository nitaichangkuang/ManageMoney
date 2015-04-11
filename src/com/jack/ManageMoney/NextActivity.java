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
public class NextActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nextactivity);
        //通用的方法，必须要的
        ViewUtils.inject(this);
    }
    @OnClick(R.id.button_all)
    public void clickMyButton(View view) {
        Intent intent = new Intent();
        intent.setClass(this,OtherActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.button_gai)
    public void clickButton(View view) {
        Intent intent = new Intent();
        intent.setClass(this,ResultActivity.class);
        startActivity(intent);
    }
}