package com.github.timecount;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;

    private TimeCounterTask mTask;
    long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTask = new TimeCounterTask();
        mListView = (ListView) findViewById(R.id.mListView);
        startTime = SystemClock.elapsedRealtime();
        for (int i = 0; i < 50; i++) {
            TimeInfo info = new TimeInfo();
            info.id = i;
            info.startTime = startTime;
            info.playSeconds = i + 200;
            mListDatas.add(info);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTask.stop();
    }

    public void show(View view) {
        mListView.setAdapter(new MyAdapter());
    }

    private ArrayList<TimeInfo> mListDatas = new ArrayList<>();

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mListDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mListDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview, null);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.tv_info);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            TimeInfo info = mListDatas.get(position);
            mTask.startTimer(holder.textView, info.id, startTime, info.playSeconds, new TimeCounters.OnTimeCounterListener() {
                @Override
                public void onTick(View view, int newTime) {
                    ((TextView) view).setText("当前时间是：" + newTime + "秒");
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView textView;
        }
    }
}
