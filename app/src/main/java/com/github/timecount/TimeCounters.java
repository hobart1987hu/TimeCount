package com.github.timecount;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by huzy on 2017/4/18.
 */

public class TimeCounters {


    private static final String TAG = "TimeCounters";


    private static final int DELAY_TIME = 1 * 1000;

    private volatile int mCurrentTime = 0;

    private Handler mHandler;

    private SparseArray<TimeCountInfo> mTimeCountInfoSparseArray = new SparseArray<>();

    private static class TimeCountInfo {
        ViewAware viewAware;
        OnTimeCounterListener listener;

        public TimeCountInfo(ViewAware viewAware, OnTimeCounterListener listener) {
            this.viewAware = viewAware;
            this.listener = listener;
        }
    }


    public TimeCounters() {
        mHandler = new Handler();
    }

    public interface OnTimeCounterListener {
        void onTick(View view, int newTime);
    }

    public void startTimer(View view, long startTime, int playSeconds, OnTimeCounterListener listener) {

        ViewAware viewAware = new ViewAware(view);

        TimeCountInfo countDownInfo = new TimeCountInfo(viewAware, listener);

        int id = viewAware.getId();

        mTimeCountInfoSparseArray.append(id, countDownInfo);

        mCurrentTime = adjustStartTime(startTime, playSeconds);

        mHandler.post(r);
    }

    /**
     * 调整开始时间
     *
     * @return
     */
    private int adjustStartTime(long startTime, int playSeconds) {
        final int adjustStartTime = (int) ((SystemClock.elapsedRealtime() - startTime) / 1000 + playSeconds - 1);
        Log.d(TAG, "adjustStartTime -->" + adjustStartTime);
        return adjustStartTime;
    }

    private int mTimeInternal = 1;

    Runnable r = new Runnable() {
        @Override
        public void run() {
            mCurrentTime += mTimeInternal;
            doOnTick(mCurrentTime);
            if (mHandler != null) {
                mHandler.postDelayed(r, DELAY_TIME);
            }
        }
    };

    private void doOnTick(int newTime) {
        if (null != mTimeCountInfoSparseArray) {
            for (int i = 0; i < mTimeCountInfoSparseArray.size(); i++) {
                TimeCountInfo countDownInfo = mTimeCountInfoSparseArray.valueAt(i);
                OnTimeCounterListener listener = countDownInfo.listener;
                ViewAware viewAware = countDownInfo.viewAware;
                listener.onTick(viewAware.getWrappedView(), newTime);
            }
        }
    }

    public void cancel(View view) {
        ViewAware viewAware = new ViewAware(view);
        final int id = viewAware.getId();
        Log.d(TAG, "cancel view id:" + id);
        if (null != mTimeCountInfoSparseArray) {
            TimeCountInfo info = mTimeCountInfoSparseArray.get(id);
            if (null != info) {
                mHandler.removeCallbacks(r);
                mTimeCountInfoSparseArray.remove(id);
            }
        }
    }

    public void stop() {
        if (null != mTimeCountInfoSparseArray) {
            mTimeCountInfoSparseArray.clear();
            mTimeCountInfoSparseArray = null;
        }
        mHandler.removeCallbacks(r);
    }
}
