package com.jack.ManageMoney;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.jack.Tools.MySQLiteOpenHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.*;

public class MainActivity extends Activity {
    @ViewInject(R.id.kind)
    private Button kind;
    @ViewInject(R.id.date)
    private Button MyDate;
    @ViewInject(R.id.save)
    private Button save;
    @ViewInject(R.id.editText)
    private EditText moneyEditText;
    @ViewInject(R.id.editText2)
    private EditText beiZhuEditText;
    /**
     * Called when the activity is first created.
     */
    private MySQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;
    private RadioGroup group;
    private static final String TAG = "MainActivity";
    private ListView listView;
    //用来为收入或者支出做标记
    private boolean tag = false;
    private List<Map<String,Object>> totalList;
    private MyAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //通用的方法，必须要的
        ViewUtils.inject(this);
        dbHelper = new MySQLiteOpenHelper(this);
        db = dbHelper.getReadableDatabase();
        group = (RadioGroup)findViewById(R.id.radioGroup);
        //给组添加监听
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            //第一个参数接收的是被选中的组对象
            //第二个参数接收的是组中被选中的单选按钮的id
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getId();
                if(id==R.id.radioGroup)
                {
                    switch(checkedId)
                    {
                        case R.id.receive:
                            //点击了收入
                            tag = true;
                            break;
                        case R.id.zhiChu:
                            //点击了支出
                            tag = false;
                            break;
                    }
                }

            }
        });
        initView();
    }
    //初始化View
    public void initView() {
        listView = (ListView)findViewById(R.id.listView);
        totalList = new ArrayList<Map<String, Object>>();
        adapter = new MyAdapter(totalList);
        listView.setAdapter(adapter);
    }
    //刷新TotalList
    public void refreshTotalList(int type,String money) {
        //type用来表示不同的表  0，代表收入 1或者其他，代表支出
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if(type==0) {
             list = dbHelper.selectList("select * from tb_receive where money = ?",new String[]{money});
        }else {
             list = dbHelper.selectList("select * from tb_zhiChu where money = ?",new String[]{money});
        }
        totalList.addAll(list);
        adapter.notifyDataSetChanged();
    }
    //定义一个listView的适配器
    class MyAdapter extends BaseAdapter {
        private List<Map<String, Object>> list = null;

        public MyAdapter(List<Map<String, Object>> list) {
            super();
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(
                        R.layout.listview_item, null);
                viewHolder = new ViewHolder();
                viewHolder.textView_title = (TextView) convertView
                        .findViewById(R.id.text_title);
                viewHolder.textView_money = (TextView) convertView
                        .findViewById(R.id.text_money);
                viewHolder.textView_kind = (TextView) convertView
                        .findViewById(R.id.text_kind);
                viewHolder.textView_beiZhu = (TextView) convertView
                        .findViewById(R.id.text_beiZhu);
                viewHolder.textView_date = (TextView) convertView.findViewById(R.id.text_date);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String sKind = list.get(position).get("kind")
                    .toString();
            viewHolder.textView_money.setText(list.get(position).get("money")
                    .toString());
            viewHolder.textView_kind.setText(sKind);
            if (sKind.equals("工资") || sKind.equals("外快")) {
                viewHolder.textView_title.setText("收入:");
            }else {
                viewHolder.textView_title.setText("支出:");
            }
            viewHolder.textView_beiZhu.setText(list.get(position)
                    .get("beiZhu").toString());
            viewHolder.textView_date.setText(list.get(position)
                    .get("date").toString());
            return convertView;
        }

        class ViewHolder {
            TextView textView_title;
            TextView textView_money;
            TextView textView_kind;
            TextView textView_beiZhu;
            TextView textView_date;
        }
    }
    //设置种类按钮的点击事件
    @OnClick(R.id.kind)
    public void clickButton(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择类别");
        String[] arrSize = new String[] { "吃", "穿", "住","行","用","收入","其他" };
        builder.setItems(arrSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog.Builder kindBuild = new AlertDialog.Builder(MainActivity.this);
                switch (i) {
                    case 0:
                        kindBuild.setTitle("选择具体支出");
                        String[] arrKind = new String[]{"日常","请客","喝酒"};
                        kindBuild.setItems(arrKind, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch(i) {
                                    case 0:
                                        kind.setText("日常");
                                        break;
                                    case 1:
                                        kind.setText("请客");
                                        break;
                                    case 2:
                                        kind.setText("喝酒");
                                        break;
                                }
                            }
                        });
                        kindBuild.show();
                        break;
                    case 1:
                        kindBuild.setTitle("选择具体支出");
                        String[] arrCh = new String[]{"自用","礼物"};
                        kindBuild.setItems(arrCh, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        kind.setText("自用");
                                        break;
                                    case 1:
                                        kind.setText("礼物");
                                        break;
                                }
                            }
                        });
                        kindBuild.show();
                        break;
                    case 2:
                        kindBuild.setTitle("选择具体支出");
                        String[] arrHouse = new String[]{"房租","水电费"};
                        kindBuild.setItems(arrHouse, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        kind.setText("房租");
                                        break;
                                    case 1:
                                        kind.setText("水电费");
                                        break;
                                }
                            }
                        });
                        kindBuild.show();
                        break;
                    case 3:
                        kindBuild.setTitle("选择具体支出");
                        String[] arrZou = new String[]{"公交","出租"};
                        kindBuild.setItems(arrZou, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        kind.setText("公交");
                                        break;
                                    case 1:
                                        kind.setText("出租");
                                        break;
                                }
                            }
                        });
                        kindBuild.show();
                        break;
                    case 4:
                        kindBuild.setTitle("选择具体支出");
                        String[] arrUse = new String[]{"学习用品","生活用品"};
                        kindBuild.setItems(arrUse, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        kind.setText("学习用品");
                                        break;
                                    case 1:
                                        kind.setText("生活用品");
                                        break;
                                }
                            }
                        });
                        kindBuild.show();
                        break;
                    case 5:
                        kindBuild.setTitle("选择具体收入");
                        String[] arrReceive = new String[]{"工资","外快"};
                        kindBuild.setItems(arrReceive, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        kind.setText("工资");
                                        break;
                                    case 1:
                                        kind.setText("外快");
                                        break;
                                }
                            }
                        });
                        kindBuild.show();
                        break;
                    case 6:
                        kind.setText("其他");
                        break;
                }
            }
        });
        builder.show();
    }
    //设置日期按钮的点击事件
    @OnClick(R.id.date)
    public void clickDateButton(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                String dateString = year + "-" + (monthOfYear + 1)
                        + "-" + dayOfMonth;
                MyDate.setText(dateString);
            }
        },year,monthOfYear,dayOfMonth);
        datePickerDialog.show();
    }
    //设置保存按钮的点击事件
    @OnClick(R.id.save)
    public void clickSave(View view) {
        //得到输入的money
        String money = moneyEditText.getText().toString();
        if(money==null || money.equals("")) {
            Toast.makeText(this,"金额不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        //得到添加备注
        String beiZhu = beiZhuEditText.getText().toString();
        //得到选则的种类
        String MyKind = kind.getText().toString();
        //得到选择的日期
        String date = MyDate.getText().toString();
        if(date==null || date.equals("") || date.equals("日期")) {
            Toast.makeText(this,"日期不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        String[] str = date.split("-");
        String year = str[0];
        String month = str[1];
        String day = str[2];
        ContentValues values = new ContentValues();
        values.put("money",money);
        values.put("beiZhu",beiZhu);
        values.put("kind",MyKind);
        values.put("date",date);
        values.put("year",year);
        values.put("month",month);
        values.put("day",day);
        if (tag) {
            //说明点击了收入，将数据插入到收入表中
            Long id = db.insert("tb_receive",null,values);
            if (id > 0) {
                Toast.makeText(this,"成功保存数据到收入表中",Toast.LENGTH_SHORT).show();
                refreshTotalList(0,money);

            }
        }else {
            Long id2 = db.insert("tb_zhiChu",null,values);
            if (id2 > 0) {
                Toast.makeText(this, "成功保存数据到支出表中", Toast.LENGTH_SHORT).show();
                refreshTotalList(1,money);
            }
        }
    }
    //设置查看按钮的点击事件
    @OnClick(R.id.history)
    public void clickHistoryButton(View view) {
        Intent intent = new Intent();
        intent.setClass(this,NextActivity.class);
        startActivity(intent);
    }

}
