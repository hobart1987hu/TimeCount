//package com.github.timecount;
//
//import android.view.View;
//
//import java.lang.ref.Reference;
//import java.lang.ref.WeakReference;
//
///**
// * Created by huzy on 2017/4/18.
// */
//
//public class ViewAware {
//
//    protected Reference<View> mViewRef;
//
//    public ViewAware(View view) {
//        if (view == null) throw new IllegalArgumentException("view must not be null");
//        mViewRef = new WeakReference<View>(view);
//    }
//
//    public View getWrappedView() {
//
//        return mViewRef.get();
//    }
//
//    public int getId() {
//
//        View view = mViewRef.get();
//
//        return view == null ? super.hashCode() : view.hashCode();
//    }
//
//}
