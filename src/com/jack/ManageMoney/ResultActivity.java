package com.jack.ManageMoney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.jack.Tools.MySQLiteOpenHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import org.w3c.dom.Text;

import java.util.*;

/**
 * Created by asus on 2015/4/10.
 */
public class ResultActivity extends Activity {
    private MySQLiteOpenHelper dbHelper;
    //本周
    private double sumReceiveMoney1;
    private double sumZhiChuMoney1;
    private double sumJieYuMoney1;
    //本月
    private double sumReceiveMoney2;
    private double sumZhiChuMoney2;
    private double sumJieYuMoney2;
    //本年
    private double sumReceiveMoney3;
    private double sumZhiChuMoney3;
    private double sumJieYuMoney3;
    //本周
    @ViewInject(R.id.money)
    private TextView money1;
    @ViewInject(R.id.text_sr)
    private TextView shouRu1;
    @ViewInject(R.id.text_data)
    private TextView zhiChu1;
    //本月
    @ViewInject(R.id.money2)
    private TextView money2;
    @ViewInject(R.id.text_sr2)
    private TextView shouRu2;
    @ViewInject(R.id.text_data2)
    private TextView zhiChu2;
    //本年
    @ViewInject(R.id.money3)
    private TextView money3;
    @ViewInject(R.id.text_sr3)
    private TextView shouRu3;
    @ViewInject(R.id.text_data3)
    private TextView zhiChu3;
    private int year,monthOfYear,dayOfMonth;
    private static final String TAG = "ResultActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultactivity);
        //通用的方法，必须要的
        ViewUtils.inject(this);
        initView();
    }
    public void initView() {
        dbHelper = new MySQLiteOpenHelper(this);
        //用户点击日期选择按钮时，可以获取到默认的日期，即是当天的日期
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        Log.i(TAG,"---year--->>"+year);
        Log.i(TAG,"---monthOfYear--->>"+monthOfYear);
        Log.i(TAG,"---dayOfMonthr--->>"+dayOfMonth);
        refreshZhouData();
        refreshMonthData();
        refreshYearData();
    }

    public void refreshZhouData() {
        //查询本周记录
        List<Map<String, Object>> list1 = dbHelper.selectList("select * from tb_receive where day between ? and ? ",new String[]{dayOfMonth+"",dayOfMonth+7+""});
        List<Map<String, Object>> list2 = dbHelper.selectList("select * from tb_zhiChu where day between ? and ?",new String[]{dayOfMonth+"",dayOfMonth+7+""});
        for (int i = 0; i < list1.size(); i++) {
            double money1 = Double.parseDouble(list1.get(i).get("money").toString());
            //本周的总收入
            sumReceiveMoney1 = sumReceiveMoney1 + money1;
        }
        for (int j = 0; j < list2.size(); j++) {
            double money2 = Double.parseDouble(list2.get(j).get("money").toString());
            //本周的总支出
            sumZhiChuMoney1 = sumZhiChuMoney1 + money2;
        }
        shouRu1.setText(sumReceiveMoney1+"");
        zhiChu1.setText(sumZhiChuMoney1+"");
        //设置结余了多少钱
        sumJieYuMoney1 = sumReceiveMoney1 - sumZhiChuMoney1;
        money1.setText(sumJieYuMoney1+"");
    }

    public void refreshMonthData() {
        //查询本月记录
        List<Map<String, Object>> list21 = dbHelper.selectList("select * from tb_receive where month = ? ",new String[]{monthOfYear+1+""});
        List<Map<String, Object>> list22 = dbHelper.selectList("select * from tb_zhiChu where month = ? ",new String[]{monthOfYear+1+""});
        for (int i = 0; i < list21.size(); i++) {
            double money21 = Double.parseDouble(list21.get(i).get("money").toString());
            //本月的总收入
            sumReceiveMoney2 = sumReceiveMoney2 + money21;
        }
        for (int j = 0; j < list22.size(); j++) {
            double money22 = Double.parseDouble(list22.get(j).get("money").toString());
            //本周的总支出
            sumZhiChuMoney2 = sumZhiChuMoney2 + money22;
        }
        shouRu2.setText(sumReceiveMoney2+"");
        zhiChu2.setText(sumZhiChuMoney2+"");
        //设置结余了多少钱
        sumJieYuMoney2 = sumReceiveMoney2 - sumZhiChuMoney2;
        money2.setText(sumJieYuMoney2+"");
    }

    public void refreshYearData() {
        //查询本年记录
        List<Map<String, Object>> list31 = dbHelper.selectList("select * from tb_receive where year = ? ",new String[]{year+""});
        List<Map<String, Object>> list32 = dbHelper.selectList("select * from tb_zhiChu where year = ? ",new String[]{year+""});
        for (int i = 0; i < list31.size(); i++) {
            double money31 = Double.parseDouble(list31.get(i).get("money").toString());
            //本年的总收入
            sumReceiveMoney3 = sumReceiveMoney3 + money31;
        }
        for (int j = 0; j < list32.size(); j++) {
            double money32 = Double.parseDouble(list32.get(j).get("money").toString());
            //本年的总支出
            sumZhiChuMoney3 = sumZhiChuMoney3 + money32;
        }
        shouRu3.setText(sumReceiveMoney3+"");
        zhiChu3.setText(sumZhiChuMoney3+"");
        //设置结余了多少钱
        sumJieYuMoney3 = sumReceiveMoney3 - sumZhiChuMoney3;
        money3.setText(sumJieYuMoney3+"");
    }
    //clickLinearLayout监听
    public void clickLinearLayout(View view) {
        Intent intent = new Intent();
        intent.setClass(this,DrawArcActivity.class);
        startActivity(intent);
    }
}