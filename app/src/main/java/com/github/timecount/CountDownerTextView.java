package com.github.timecount;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by huzy on 2017/5/5.
 */

public class CountDownerTextView extends TextView {
    private static final int DEFAULT_DELAY_TIME = 1000;

    private static final int DEFAULT_TIME_INTERVAL = 1;//1秒钟

    private MyHandler mHandler;

    public CountDownerTextView(Context context) {
        super(context);
        init();
    }

    public CountDownerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
        mHandler = new MyHandler(CountDownerTextView.this);
    }

    public void startTimer(int delayTime, int timeInterval, long startTime, int playSeconds, String part1, String part2) {
        int currentTime = adjustStartTime(startTime, playSeconds);
        setText(part1 + currentTime + part2);
        mHandler.setData(currentTime, timeInterval, delayTime, part1, part2);
        mHandler.removeMessages(MEG_UPDATE_VIEW);
        mHandler.sendEmptyMessage(MEG_UPDATE_VIEW);
    }

    public void startTimer(long startTime, int playSeconds, String part1, String part2) {
        startTimer(DEFAULT_DELAY_TIME, DEFAULT_TIME_INTERVAL, startTime, playSeconds, part1, part2);
    }

    private int adjustStartTime(long startTime, int playSeconds) {
        final int adjustStartTime = (int) ((SystemClock.elapsedRealtime() - startTime) / 1000 + playSeconds - 1);
        return adjustStartTime;
    }

    private static final int MEG_UPDATE_VIEW = 100;

    /**
     * 这边必须要用WeakReference来处理这个view，否则这个view无法被回收
     */
    private static class MyHandler extends Handler {

        private final WeakReference<CountDownerTextView> mView;

        public MyHandler(CountDownerTextView view) {
            mView = new WeakReference<CountDownerTextView>(view);
        }

        private int mCurrentTime, mTimeInterval;
        private long mDelayTime;
        private String part1, part2;

        public void setData(int currentTime, int timeInterval, long delayTime, String part1, String part2) {
            this.mCurrentTime = currentTime;
            this.mTimeInterval = timeInterval;
            this.mDelayTime = delayTime;
            this.part1 = part1;
            this.part2 = part2;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MEG_UPDATE_VIEW) {
                CountDownerTextView view = mView.get();
                if (null != view) {
                    mCurrentTime += mTimeInterval;
                    view.setText(part1 + mCurrentTime + part2);
                    sendEmptyMessageDelayed(MEG_UPDATE_VIEW, mDelayTime);
                }
            }
        }
    }

    public void stop() {
        mHandler.removeMessages(MEG_UPDATE_VIEW);
        mHandler = null;
    }
}
