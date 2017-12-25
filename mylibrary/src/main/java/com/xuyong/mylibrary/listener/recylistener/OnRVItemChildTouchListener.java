package com.xuyong.mylibrary.listener.recylistener;

import android.view.MotionEvent;
import android.view.View;

/**
 ** Created by xuyong on 2016/12/30.
 */

public interface OnRVItemChildTouchListener {
    boolean onRvItemChildTouch(View childView, MotionEvent event);

}
