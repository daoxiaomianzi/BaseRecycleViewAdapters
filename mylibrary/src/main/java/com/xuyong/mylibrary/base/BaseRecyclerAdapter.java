package com.xuyong.mylibrary.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.CompoundButton;

import com.xuyong.mylibrary.listener.OnNoDoubleClickListener;
import com.xuyong.mylibrary.listener.recylistener.OnItemChildCheckedChangeListener;
import com.xuyong.mylibrary.listener.recylistener.OnItemChildClickListener;
import com.xuyong.mylibrary.listener.recylistener.OnItemLongChildClickListener;
import com.xuyong.mylibrary.listener.recylistener.OnRVItemChildTouchListener;
import com.xuyong.mylibrary.listener.recylistener.OnRVItemClickListener;
import com.xuyong.mylibrary.listener.recylistener.OnRVItemLongClickListener;
import com.xuyong.mylibrary.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView适配器基类
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements CommonHolder.OnNotifyChangeListener {
    private List<T> dataList = new ArrayList<>();
    private boolean enableHead = false;
    CommonHolder headHolder;
    ViewGroup rootView;
    public final static int TYPE_HEAD = 0;
    public static final int TYPE_CONTENT = 1;
    private OnRVItemClickListener onRVItemClickListener;
    private OnRVItemLongClickListener onRVItemLongClickListener;
    private OnItemChildClickListener onItemChildClickListener;
    private OnItemLongChildClickListener onItemLongChildClickListener;
    private OnRVItemChildTouchListener onRVItemChildTouchListener;
    private OnItemChildCheckedChangeListener onItemChildCheckedChangeListener;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        rootView = parent;
        //设置ViewHolder
        int type = getItemViewType(position);
        if (type == TYPE_HEAD) {
            return headHolder;
        } else {
            return setViewHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        runEnterAnimation(holder.itemView, position);
        //数据绑定
        if (enableHead) {
            if (position == 0) {
                ((CommonHolder) holder).bindHeadData();
            } else {
                ((CommonHolder) holder).bindData(dataList.get(position - 1));
            }
        } else {
            ((CommonHolder) holder).bindData(dataList.get(position));
        }
        ((CommonHolder) holder).setOnNotifyChangeListener(this);
        setOnClick((CommonHolder) holder, position);
        setOnLongClick((CommonHolder) holder, position);
    }


    private void setOnClick(final CommonHolder holder, final int position) {
        holder.itemView.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if (view.getId() == holder.itemView.getId() && null != onRVItemClickListener) {
                    onRVItemClickListener.onRVItemClick(view, position);
                }
            }
        });
    }

    private void setOnLongClick(final CommonHolder holder, final int position) {
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                if (view.getId() == holder.itemView.getId() && null != onRVItemLongClickListener) {
                    onRVItemLongClickListener.onRVItemLongClick(view, position);
                }
                return true;
            }
        });
    }

    public ViewGroup getRootView() {
        return rootView;
    }

    @Override
    public int getItemCount() {
        if (enableHead) {
            return dataList.size() + 1;
        }
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (enableHead) {
            if (position == 0) {
                return TYPE_HEAD;
            } else {
                return TYPE_CONTENT;
            }
        } else {
            return TYPE_CONTENT;
        }
    }

    private int lastAnimatedPosition = -1;
    protected boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;

    private void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;
        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(DensityUtil.dp2px(view.getContext(), 100));//(position+1)*50f
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                        }
                    }).start();
        }
    }


    /**
     * 设置item的点击事件监视器
     *
     * @param onRVItemClickListener
     */
    public void setOnRVItemClickListener(OnRVItemClickListener onRVItemClickListener) {
        this.onRVItemClickListener = onRVItemClickListener;
    }

    /**
     * 设置item的长按事件监视器
     *
     * @param onRVItemLongClickListener
     */
    public void setOnRVItemLongClickListener(OnRVItemLongClickListener onRVItemLongClickListener) {
        this.onRVItemLongClickListener = onRVItemLongClickListener;
    }

    /**
     * 设置item中的子控件点击事件监听器
     *
     * @param onItemChildClickListener
     */
    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    /**
     * 设置item中子控件长按事件监视器
     *
     * @param onItemLongChildClickListener
     */
    public void setOnItemLongChildClickListener(OnItemLongChildClickListener onItemLongChildClickListener) {
        this.onItemLongChildClickListener = onItemLongChildClickListener;
    }

    /**
     * 设置item中子控件触摸事件监视器
     *
     * @param onRVItemChildTouchListener
     */
    public void setOnRVItemChildTouchListener(OnRVItemChildTouchListener onRVItemChildTouchListener) {
        this.onRVItemChildTouchListener = onRVItemChildTouchListener;
    }

    /**
     * 设置item中子控件状态改变事件监视器
     *
     * @param onItemChildCheckedChangeListener
     */
    public void setOnItemChildCheckedChangeListener(OnItemChildCheckedChangeListener onItemChildCheckedChangeListener) {
        this.onItemChildCheckedChangeListener = onItemChildCheckedChangeListener;
    }

    /**
     * 为id为viewId的item子控件设置点击事件监听器
     *
     * @param //viewId
     */
    public void setItemChildClickListener(View view, final int position) {
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != onItemChildClickListener) {
                        onItemChildClickListener.onItemChildClick(view, position);
                    }
                }
            });
        }

    }

    /**
     * 为id为viewId的item子控件设置长按事件监听器
     *
     * @param //viewId
     */
    public void setItemLongChildClickListener(View view, final int position) {
        if (view != null) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (null != onItemLongChildClickListener) {
                        onItemLongChildClickListener.onItemLongChildClick(view, position);
                    }
                    return true;
                }
            });
        }

    }

    /**
     * 为  viewId 的view RecyclerView 的 item 子控件设置触摸事件监听器
     *
     * @param view
     */
    public void setRVItemChildTouchListener(View view) {
        if (view != null) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (null != onRVItemChildTouchListener) {
                        onRVItemChildTouchListener.onRvItemChildTouch(view, motionEvent);
                    }
                    return true;
                }
            });
        }
    }


    /**
     * 为viewId的view的item子控件设置选中状态变化事件监听器
     *
     * @param
     */
    public void setItemChildCheckedChangeListener(View view, final int position) {
        if (view != null && view instanceof CompoundButton) {
            ((CompoundButton) view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    onItemChildCheckedChangeListener.onItemChildCheckedChanged(compoundButton, position, b);
                }
            });
        }
    }


    @Override
    public void onNotify() {
        //提供给CommonHolder方便刷新视图
        notifyDataSetChanged();
    }

    public void setDataList(List<T> datas) {
        dataList.clear();
        if (null != datas) {
            dataList.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public void clearDatas() {
        dataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 添加数据到前面
     */
    public void addItemsAtFront(List<T> datas) {
        if (null == datas) return;
        dataList.addAll(0, datas);
        notifyDataSetChanged();
    }

    /**
     * 添加数据到尾部
     */
    public void addItems(List<T> datas) {
        if (null == datas) return;
        dataList.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 添加单条数据
     */
    public void addItem(T data) {
        if (null == data) return;
        dataList.add(data);
        notifyDataSetChanged();
    }

    /**
     * 删除单条数据
     */
    public void deletItem(T data) {
        dataList.remove(data);
        Log.d("deletItem: ", dataList.remove(data) + "");
        notifyDataSetChanged();
    }

    /**
     * 设置是否显示head
     *
     * @param ifEnable 是否显示头部
     */
    public void setEnableHead(boolean ifEnable) {
        enableHead = ifEnable;
    }

    public void setHeadHolder(CommonHolder headHolder1) {
        enableHead = true;
        headHolder = headHolder1;
    }

    public void setHeadHolder(View itemView) {
        enableHead = true;
        headHolder = new HeadHolder(itemView);
        notifyItemInserted(0);
    }

    public CommonHolder getHeadHolder() {
        return headHolder;
    }

    /**
     * 子类重写实现自定义ViewHolder
     */
    public abstract CommonHolder<T> setViewHolder(ViewGroup parent);

    public class HeadHolder extends CommonHolder<Void> {
        public HeadHolder(View itemView) {
            super(itemView);
        }

        public HeadHolder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        public void bindData(Void aVoid) {//不用实现
        }
    }
}