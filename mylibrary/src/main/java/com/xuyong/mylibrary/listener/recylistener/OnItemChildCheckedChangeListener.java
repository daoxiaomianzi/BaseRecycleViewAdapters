package com.xuyong.mylibrary.listener.recylistener;

import android.widget.CompoundButton;

/**
 * Created by xuyong on 2016/12/30.
 */

public interface OnItemChildCheckedChangeListener {
    void onItemChildCheckedChanged(CompoundButton childView, int position, boolean isChecked);

}
