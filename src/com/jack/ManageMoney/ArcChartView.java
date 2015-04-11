package com.jack.ManageMoney;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.jack.Tools.MySQLiteOpenHelper;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2015/4/10.
 */
public class ArcChartView extends View {
    public ArcChartView(Context context) {
        super(context);
    }

    public ArcChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ArcChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private Paint arcPaint;
    //代表弧线的尺寸
    private RectF arcRect;
    //画矩形
    private Paint recPaint;

    private MySQLiteOpenHelper dbHelper;
    //用来记录工资的总money
    private double totalGongMoney;
    //用来记录外快的总money
    private double totalWaiMoney;
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
        //里面只有两组数据，要不工资，要不就是外快
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
//        Log.i(TAG,"----totalMoney-->>"+totalMoney);
//        Log.i(TAG,"----totalGongMoney-->>"+totalGongMoney);
//        Log.i(TAG,"----totalWaiMoney-->>"+totalWaiMoney);
        //工资所占的比例
        double sampleGong =  totalGongMoney/totalMoney;
//        Log.i(TAG,"----sampleGong-->>"+sampleGong);
        //外快所占的比例
//        double sampleWai =  totalWaiMoney/totalMoney;

        //画绿色工资圆
        arcPaint.setColor(Color.GREEN);
        canvas.drawArc(arcRect, 0, (float)(sampleGong * 360), true, arcPaint);
        //0E6AB8
        //画蓝色外快圆
        arcPaint.setColor(Color.parseColor("#FF0E6AB8"));
        canvas.drawArc(arcRect,(float)(sampleGong * 360),(float)(360 - sampleGong*360), true, arcPaint);
//        Log.i(TAG,"----(float)(sampleGong * 360)-->>"+(float)(sampleGong * 360));
//        Log.i(TAG,"----(float)(360 - sampleGong*360)-->>"+(float)(360 - sampleGong*360));

        //画绿色的矩形代表工资
        recPaint.setColor(Color.GREEN);
        canvas.drawRect(70,230,90,245,recPaint);
        //画蓝色的矩形代表外快
        recPaint.setColor(Color.parseColor("#FF0E6AB8"));
        canvas.drawRect(70,250,90,265,recPaint);
    }
}

