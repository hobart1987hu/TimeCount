package com.github.timecount;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.mListView);
        long startTime = SystemClock.elapsedRealtime();
        for (int i = 0; i < 15; i++) {
            TimeInfo info = new TimeInfo();
            info.id = i;
            info.startTime = startTime;
            info.playSeconds = i + 200;
            mListDatas.add(info);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter = new MyAdapter();
                mListView.setAdapter(adapter);
            }
        }, 1000);
    }

    private MyAdapter adapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    void stopTimer() {
        final int size = mListDatas.size();
        for (int i = 0; i < size; i++) {
            View view = adapter.getView(i, null, mListView);
            if (null != view) {
                ViewHolder holder = (ViewHolder) view.getTag();
                Log.d("huzeyin", "1111");
                holder.textView.stop();
            }
        }
    }


    int mPointer = 0;

    public void addData(View view) {
        mPointer++;
        TimeInfo info = new TimeInfo();
        info.id = 100 + mPointer;
        info.startTime = SystemClock.elapsedRealtime();
        info.playSeconds = 0;
        mListDatas.add(0, info);
        adapter.notifyDataSetChanged();
    }

    public void deleteData(View view) {
        TimeInfo info = mListDatas.remove(0);
        adapter.notifyDataSetChanged();
    }

    public void updateData(View view) {
        TimeInfo info = mListDatas.get(0);
        info.playSeconds = 10000;
        mListDatas.set(0, info);
        adapter.notifyDataSetChanged();
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
                holder.textView = (CountDownerTextView) convertView.findViewById(R.id.tv_info);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            TimeInfo info = mListDatas.get(position);
            holder.textView.startTimer(info.startTime, info.playSeconds, "当前时间是：", "秒");
            return convertView;
        }
    }

    class ViewHolder {
        CountDownerTextView textView;
    }
}
