package com.jack.ManageMoney;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.jack.Tools.MySQLiteOpenHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2015/4/10.
 */
public class DrawZhiChuActivity extends Activity {
    @ViewInject(R.id.text_zhiChu)
    private TextView text_zhiChu;
    @ViewInject(R.id.text_zhi)
    private TextView text_zhi;
    @ViewInject(R.id.text_zhiChu3)
    private TextView text_zhiChu3;
    @ViewInject(R.id.text_zhiChu4)
    private TextView text_zhiChu4;
    @ViewInject(R.id.text_zhiChu5)
    private TextView text_zhiChu5;

    private MySQLiteOpenHelper dbHelper;

    private double totalEatMoney;
    private double totalchuangMoney;
    private double totalHouseMoney;
    private double totalXingMoney;
    private double totalYongMoney;
    //用来记录总的money
    private double totalMoney;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawzhichuactivity);
        ViewUtils.inject(this);
        initView();
    }
    public void initView() {
        dbHelper = new MySQLiteOpenHelper(this);
        List<Map<String,Object>> eatList = dbHelper.selectList("select money from tb_zhiChu where kind in('日常','请客','喝酒')",null);
        List<Map<String,Object>> chuangList = dbHelper.selectList("select money from tb_zhiChu where kind in('自用','礼物')",null);
        List<Map<String,Object>> houseList = dbHelper.selectList("select money from tb_zhiChu where kind in('房租','水电费')",null);
        List<Map<String,Object>> xingList = dbHelper.selectList("select money from tb_zhiChu where kind in('公交','出租')",null);
        List<Map<String,Object>> yongList = dbHelper.selectList("select money from tb_zhiChu where kind in('学习用品','生活用品')",null);
        for (int i = 0; i < eatList.size(); i++) {
            double money1 = Double.parseDouble(eatList.get(i).get("money").toString());
            totalEatMoney = totalEatMoney + money1;
        }
        for (int j = 0; j < chuangList.size(); j++) {
            double money2 = Double.parseDouble(chuangList.get(j).get("money").toString());
            totalchuangMoney = totalchuangMoney + money2;
        }
        for (int k = 0; k < houseList.size(); k++) {
            double money3 = Double.parseDouble(houseList.get(k).get("money").toString());
            totalHouseMoney = totalHouseMoney + money3;
        }
        for (int a = 0; a < xingList.size(); a++) {
            double money4 = Double.parseDouble(xingList.get(a).get("money").toString());
            totalXingMoney = totalXingMoney + money4;
        }
        for (int b = 0; b < yongList.size(); b++) {
            double money5 = Double.parseDouble(yongList.get(b).get("money").toString());
            totalYongMoney = totalYongMoney + money5;
        }
        totalMoney = totalEatMoney+totalchuangMoney+ totalHouseMoney+ totalXingMoney+ totalYongMoney;
        //吃所占的比例
        double sampleEat =  totalEatMoney/totalMoney;
        text_zhiChu.setText("吃:"+" "+(float)(sampleEat*100)+"%"+" "+"("+totalEatMoney+")");
        //穿所占的比例
        double sampleChuan =  totalchuangMoney/totalMoney;
        text_zhi.setText("穿:"+" "+(float)(sampleChuan*100)+"%"+" "+"("+totalchuangMoney+")");
        //住所占的比例
        double sampleHouse =  totalHouseMoney/totalMoney;
        text_zhiChu3.setText("住:"+" "+(float)(sampleHouse*100)+"%"+" "+"("+totalHouseMoney+")");
        //行所占的比例
        double sampleXing =  totalXingMoney/totalMoney;
        text_zhiChu4.setText("行:"+" "+(float)(sampleXing*100)+"%"+" "+"("+totalXingMoney+")");
        //用所占的比例
        double sampleYong =  totalYongMoney/totalMoney;
        text_zhiChu5.setText("用:"+" "+(float)(sampleYong*100)+"%"+" "+"("+totalYongMoney+")");
    }
}