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
public class DrawShouRuActivity extends Activity {
    @ViewInject(R.id.text_shouRu)
    private TextView text_shouRu;
    @ViewInject(R.id.text_shou)
    private TextView text_shou;

    private MySQLiteOpenHelper dbHelper;
    //用来记录工资的总money
    private double totalGongMoney;
    //用来记录外快的总money
    private double totalWaiMoney;
    //用来记录总的money
    private double totalMoney;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawshouruactivity);
        ViewUtils.inject(this);
        initView();
}
    public void initView() {
        dbHelper = new MySQLiteOpenHelper(this);
        List<Map<String,Object>> gongZiList = dbHelper.selectList("select money from tb_receive where kind = ?",new String[]{"工资"});
        List<Map<String,Object>> waiKuaiList = dbHelper.selectList("select money from tb_receive where kind = ?",new String[]{"外快"});
        for (int i = 0; i < gongZiList.size(); i++) {
            double money1 = Double.parseDouble(gongZiList.get(i).get("money").toString());
            totalGongMoney = totalGongMoney + money1;
        }
        for (int j = 0; j < waiKuaiList.size(); j++) {
            double money2 = Double.parseDouble(waiKuaiList.get(j).get("money").toString());
            totalWaiMoney = totalWaiMoney + money2;
        }
        totalMoney = totalGongMoney + totalWaiMoney;
        //工资所占的比例
        double sampleGong =  totalGongMoney/totalMoney;
        text_shouRu.setText("工资:"+" "+(float)(sampleGong*100)+"%"+" "+"("+totalGongMoney+")");
        //外快所占的比例
        double sampleWai =  totalWaiMoney/totalMoney;
        text_shou.setText("外块:"+" "+(float)(sampleWai*100)+"%"+" "+"("+totalWaiMoney+")");
    }
}