package com.my.path.ui.dialog;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.my.path.R;
import com.my.path.app.util.CacheUtil;
import com.my.path.data.model.bean.ItemOnClick;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.core.CenterPopupView;


/**
 * @author GuangNian
 * @description:    确定弹窗
 * @date : 2021/5/28 2:53 下午
 */
public  class CustomServiceAgreementPopup extends CenterPopupView {
    private Activity context;
    private String describe;
    private ItemOnClick onItemClick;
    //注意：自定义弹窗本质是一个自定义View，但是只需重写一个参数的构造，其他的不要重写，所有的自定义弹窗都是这样。
    public CustomServiceAgreementPopup(@NonNull Activity context, String describe, ItemOnClick onItemClick) {
        super(context);
        this.context =context;
        this.describe = describe;
        this.onItemClick = onItemClick;
    }
    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_window_service_agreement;
    }
    // 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
    @Override
    protected void onCreate() {
        super.onCreate();
        if(describe!=null&&!describe.equals("")){

        }

        findViewById(R.id.bt_yes).setOnClickListener(v ->{
            CacheUtil.INSTANCE.setFirst(false);
            dismiss(); // 关闭弹窗
            //权限
            onItemClick.OnClick("");
        });

        findViewById(R.id.tv_on).setOnClickListener(v ->{
            context.finish();
            dismiss(); // 关闭弹窗
        });

    }

    // 设置最大宽度，看需要而定，
    @Override
    protected int getMaxWidth() {
        return super.getMaxWidth();
    }
    // 设置最大高度，看需要而定
    @Override
    protected int getMaxHeight() {
        return super.getMaxHeight();
    }
    // 设置自定义动画器，看需要而定
    @Override
    protected PopupAnimator getPopupAnimator() {
        return super.getPopupAnimator();
    }
    /**
     * 弹窗的宽度，用来动态设定当前弹窗的宽度，受getMaxWidth()限制
     *
     * @return
     */
    @Override
    protected int getPopupWidth() {
        return 0;
    }

    /**
     * 弹窗的高度，用来动态设定当前弹窗的高度，受getMaxHeight()限制
     *
     * @return
     */
    @Override
    protected int getPopupHeight() {
        return 0;
    }
}

