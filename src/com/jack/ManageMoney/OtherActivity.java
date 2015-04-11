package com.jack.ManageMoney;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.jack.Tools.MySQLiteOpenHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2015/4/10.
 */
public class OtherActivity extends Activity {
    @ViewInject(R.id.all_jieYu)
    private TextView all_jieYu;
    @ViewInject(R.id.all_shouRu)
    private TextView all_shouRu;
    @ViewInject(R.id.all_zhiChu)
    private TextView all_zhiChu;

    private MySQLiteOpenHelper dbHelper;
    private ListView listView;
    private List<Map<String, Object>> totalList;
    private MyAdapter adapter;
    private double sumReceiveMoney;
    private double sumZhiChuMoney;
    private double sumJieYuMoney;
    private static final String TAG = "OtherActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otheractivity);
        //通用的方法，必须要的
        ViewUtils.inject(this);
        initView();
        refreshTotalList();
    }
    public void initView() {
        dbHelper = new MySQLiteOpenHelper(this);
        listView = (ListView)findViewById(R.id.all);
        totalList = new ArrayList<Map<String, Object>>();
        adapter = new MyAdapter(totalList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final String id = totalList.get(position).get("id").toString();
                final String kind = totalList.get(position).get("kind").toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(OtherActivity.this);
                builder.setTitle("删除提示：");
                builder.setMessage("是否要删除？");
                builder.setNegativeButton("取消",null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(kind.equals("工资") || kind.equals("外快") ) {
                            boolean b = dbHelper.execData("delete from tb_receive where id = ?",new String[]{id});
                            if(b) {
                                sumReceiveMoney = 0;
                                sumZhiChuMoney = 0;
                                sumJieYuMoney = 0;
                                refreshTotalList();
                                Toast.makeText(OtherActivity.this,"成功删除收入表中的数据",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(OtherActivity.this,"失败删除收入表中的数据",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            boolean bb = dbHelper.execData("delete from tb_zhiChu where id = ?",new String[]{id});
                            if(bb) {
                                sumReceiveMoney = 0;
                                sumZhiChuMoney = 0;
                                sumJieYuMoney = 0;
                                refreshTotalList();
                                Toast.makeText(OtherActivity.this,"成功删除支出表中的数据",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(OtherActivity.this,"失败删除支出表中的数据",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                builder.show();
            }
        });
    }
    //刷新TotalList
    public void refreshTotalList() {
        List<Map<String, Object>> list1 = dbHelper.selectList("select * from tb_receive",null);
        List<Map<String, Object>> list2 = dbHelper.selectList("select * from tb_zhiChu",null);
        for (int i = 0; i < list1.size(); i++) {
            double money1 = Double.parseDouble(list1.get(i).get("money").toString());
            sumReceiveMoney = sumReceiveMoney + money1;
        }
        for (int j = 0; j < list2.size(); j++) {
            double money2 = Double.parseDouble(list2.get(j).get("money").toString());
            sumZhiChuMoney = sumZhiChuMoney + money2;
        }
        //设置结余了多少钱
        all_shouRu.setText(sumReceiveMoney+"");
        all_zhiChu.setText(sumZhiChuMoney+"");
        sumJieYuMoney = sumReceiveMoney - sumZhiChuMoney;
        all_jieYu.setText(sumJieYuMoney+"");
        totalList.clear();
        totalList.addAll(list1);
        totalList.addAll(list2);
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
}