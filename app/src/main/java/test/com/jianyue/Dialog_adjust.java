//设置背景字号菜单

package test.com.jianyue;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sevenheaven.iosswitch.ShSwitchView;

import lib.kingja.switchbutton.SwitchMultiButton;

public class Dialog_adjust extends DialogFragment {

    /**
     * log tag for Dialog_adjust
     */
    private static final String TAG = "Dialog_adjust";

    final Context context = getActivity();
    public static Dialog_adjust newInstance() {
        return new Dialog_adjust();
    }

    //绑定按键
    RadioGroup background_color;
    RadioButton bt_white,bt_green,bt_yellow,bt_pink;
    ShSwitchView bt_night;
    SwitchMultiButton bt_size;

    View dialogView;

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM; // 显示在底部
        params.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度填充满屏
        window.setAttributes(params);
        // 这里用透明颜色替换掉系统自带背景
        int color = ContextCompat.getColor(getActivity(), android.R.color.transparent);
        window.setBackgroundDrawable(new ColorDrawable(color));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        // 不显示标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //布局实例化
        dialogView = inflater.inflate(R.layout.adjust_dialog, container, false);

        //字体大小选择框
        bt_size = dialogView.findViewById(R.id.switchmultibutton);
        //初始化字体大小选择框
        SharePreference sp = new SharePreference(Dialog_adjust.this.getActivity());
        int i=sp.getSize();//获取字号
        bt_size.setSelectedTab(i);
        bt_size.setText("小","中","大").setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            int i;
            SharePreference sp = new SharePreference(Dialog_adjust.this.getActivity());
            //点击事件
            @Override
            public void onSwitch(int position, String tabText) {
                if(tabText=="小"){
                  i=0;
                }
                else if(tabText=="中"){
                    i=1;
                }
                else if(tabText=="大"){
                    i=2;
                }
                //把字号写入缓存
                sp.setSize(i);
            }
        });

        //绑定按键
        background_color=dialogView.findViewById(R.id.background_color);//RadioGroup
        bt_white=dialogView.findViewById(R.id.bt_white);//RadioButton
        bt_green=dialogView.findViewById(R.id.bt_green);//RadioButton
        bt_yellow=dialogView.findViewById(R.id.bt_yellow);//RadioButton
        bt_pink=dialogView.findViewById(R.id.bt_pink);//RadioButton
        bt_night=dialogView.findViewById(R.id.switch_Night);//夜间模式

        //初始化按键的状态
        set_button();
        //开启动画
        startUpAnimation(dialogView);
        return dialogView;
    }

    //开启动画
    private void startUpAnimation(View view) {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        view.startAnimation(slide);
    }

    //设置按键状态
    public void set_button(){
        //设置RadioButton状态
        SharePreference sp1 = new SharePreference(Dialog_adjust.this.getActivity());
        if(sp1.getWhite()){
            background_color.check(R.id.bt_white);
        }
        if(sp1.getGreen()){
            background_color.check(R.id.bt_green);
        }
        if(sp1.getYellow()){
            background_color.check(R.id.bt_yellow);
        }
        if(sp1.getPink()){
            background_color.check(R.id.bt_pink);
        }
        if(sp1.getNight()){
            bt_night.setOn(true);
        }
        //背景RadioGroup监听
        background_color.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            SharePreference sp1 = new SharePreference(Dialog_adjust.this.getActivity());
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(R.id.bt_white==i){
                    sp1.setWhiteTrue();
                    sp1.setGreenFalse();
                    sp1.setYellowFalse();
                    sp1.setPinkFalse();
                }
                if(R.id.bt_green==i){
                    sp1.setWhiteFalse();
                    sp1.setGreenTrue();
                    sp1.setYellowFalse();
                    sp1.setPinkFalse();
                    //textView.setBackgroundColor(Color.parseColor("#8bdf72"));
                }
                if(R.id.bt_yellow==i){
                    sp1.setWhiteFalse();
                    sp1.setGreenFalse();
                    sp1.setYellowTrue();
                    sp1.setPinkFalse();

                }
                if(R.id.bt_pink==i){
                    sp1.setWhiteFalse();
                    sp1.setGreenFalse();
                    sp1.setYellowFalse();
                    sp1.setPinkTrue();
                }
            }
        });
        //夜间模式监听
        bt_night.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            SharePreference sp1 = new SharePreference(Dialog_adjust.this.getActivity());
            @Override
            public void onSwitchStateChange(boolean isOn) {
                if(isOn){
                    sp1.setNightTrue();
                }
                else{
                    sp1.setNightFalse();
                }
            }
        });

    }

}
