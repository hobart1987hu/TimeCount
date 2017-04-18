package com.github.timecount;

import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by huzy on 2017/4/18.
 */

public class TimeCounterTask {

    private Map<Integer, TimeCounters> mMap;
    private volatile SparseArray<TimeCounters> mViewIds;

    public TimeCounterTask() {
        mMap = Collections.synchronizedMap(new LinkedHashMap<Integer, TimeCounters>());
        mViewIds = new SparseArray<>();
    }

    public TimeCounters get(int id) {
        synchronized (this) {
            TimeCounters timer = mMap.get(id);
            if (timer == null) {
                timer = new TimeCounters();
                mMap.put(id, timer);
            }
            return timer;
        }
    }

    public void startTimer(View view, int id, long startTime, int playSeconds, TimeCounters.OnTimeCounterListener listener) {
        synchronized (this) {
            TimeCounters timeCounters = get(id);
            addViewIds(view, timeCounters);
            timeCounters.startTimer(view, startTime, playSeconds, listener);
        }
    }

    private TimeCounters addViewIds(View view, TimeCounters timers) {
        int id = new ViewAware(view).getId();
        Log.d("TimeCounters", "addViewIds id:" + id);
        synchronized (this) {
            TimeCounters oldTimers = mViewIds.get(id);
            if (oldTimers != timers) {
                if (oldTimers != null) {
                    Log.d("TimeCounters", "cancel view ----");
                    oldTimers.cancel(view);
                }
                mViewIds.append(id, timers);
            }
            return oldTimers;
        }
    }

    public void stop() {
        if (mMap != null) {
            synchronized (this) {
                for (TimeCounters task : mMap.values()) {
                    if (task != null) {
                        task.stop();
                    }
                }
                mMap.clear();
            }
        }

        if (mViewIds != null) {
            synchronized (this) {
                mViewIds.clear();
            }
        }
    }
}
