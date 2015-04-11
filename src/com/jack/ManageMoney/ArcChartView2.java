package com.jack.ManageMoney;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.jack.Tools.MySQLiteOpenHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2015/4/10.
 */
public class ArcChartView2 extends View {
    public ArcChartView2(Context context) {
        super(context);
    }

    public ArcChartView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ArcChartView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private Paint arcPaint;
    //代表弧线的尺寸
    private RectF arcRect;
    //画矩形
    private Paint recPaint;

    private MySQLiteOpenHelper dbHelper;

    private double totalEatMoney;
    private double totalchuangMoney;
    private double totalHouseMoney;
    private double totalXingMoney;
    private double totalYongMoney;
    //用来记录总的money
    private double totalMoney;
    private static final String TAG = "ArcChartView";
    /**
     * 通用的初始化方法
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        dbHelper = new MySQLiteOpenHelper(getContext());
        arcPaint = new Paint();
        arcPaint.setColor(Color.BLACK);
        //Paint.Style.FILL使用paint设置的颜色进行填充绘制
        arcPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //反锯齿
        arcPaint.setAntiAlias(true);
        arcRect = new RectF(50, 50, 170, 170);
        //画矩形
        recPaint = new Paint();
        recPaint.setColor(Color.BLACK);
        //Paint.Style.FILL使用paint设置的颜色进行填充绘制
        recPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //反锯齿
        recPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //清除内容，颜色为白色就行
        canvas.drawColor(Color.WHITE);
        //画一个弧线
        //第一个参数：弧线的尺寸范围
        //第二个参数：起始角度
        //第三个参数：弧线的角度
        //第四个参数：是否连接中心点。true，形成饼图的效果，即是形成扇形
        //第五个参数：Paint
        //首先获取数据库中的money，最终得到比例
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
//        Log.i(TAG,"----totalMoney-->>"+totalMoney);
//        Log.i(TAG,"----totalGongMoney-->>"+totalGongMoney);
//        Log.i(TAG,"----totalWaiMoney-->>"+totalWaiMoney);
        //吃所占的比例
        double sampleEat =  totalEatMoney/totalMoney;
        //穿所占的比例
        double sampleChuan =  totalchuangMoney/totalMoney;
        //住所占的比例
        double sampleHouse =  totalHouseMoney/totalMoney;
        //行所占的比例
        double sampleXing =  totalXingMoney/totalMoney;
        //用所占的比例
        double sampleYong =  totalYongMoney/totalMoney;
//        Log.i(TAG,"----sampleGong-->>"+sampleGong);

        //画绿色吃的圆
        arcPaint.setColor(Color.GREEN);
        canvas.drawArc(arcRect, 0, (float)(sampleEat * 360), true, arcPaint);
        //0E6AB8
        //画蓝色穿的圆
        arcPaint.setColor(Color.parseColor("#FF0E6AB8"));
        canvas.drawArc(arcRect,(float)(sampleEat * 360),(float)(sampleChuan*360), true, arcPaint);

        //画红色住的圆
        arcPaint.setColor(Color.RED);
        canvas.drawArc(arcRect, (float)(sampleEat*360 + sampleChuan*360), (float)(sampleHouse * 360), true, arcPaint);
        //0E6AB8
        //画..色行的圆
        arcPaint.setColor(Color.CYAN);
        canvas.drawArc(arcRect,(float)(sampleEat*360 + sampleChuan*360+sampleHouse * 360),(float)(sampleXing*360), true, arcPaint);
        //画黑色用的圆
        arcPaint.setColor(Color.BLACK);
        canvas.drawArc(arcRect,(float)(sampleEat*360 + sampleChuan*360+sampleHouse * 360+sampleXing*360),(float)(sampleYong*360), true, arcPaint);
//        Log.i(TAG,"----(float)(sampleGong * 360)-->>"+(float)(sampleGong * 360));
//        Log.i(TAG,"----(float)(360 - sampleGong*360)-->>"+(float)(360 - sampleGong*360));

        //画绿色的矩形代表吃
        recPaint.setColor(Color.GREEN);
        canvas.drawRect(70,230,90,245,recPaint);
        //画蓝色的矩形代表穿
        recPaint.setColor(Color.parseColor("#FF0E6AB8"));
        canvas.drawRect(70,250,90,265,recPaint);
        //画红色的矩形代表住
        recPaint.setColor(Color.RED);
        canvas.drawRect(70,270,90,285,recPaint);
        //画蓝白色的矩形代表行
        recPaint.setColor(Color.CYAN);
        canvas.drawRect(70,290,90,305,recPaint);
        //画黑色的矩形代表用
        recPaint.setColor(Color.BLACK);
        canvas.drawRect(70,310,90,325,recPaint);
    }
}

