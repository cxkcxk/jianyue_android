//底栏布局,三个按键
package test.com.jianyue;

import android.app.DialogFragment;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by 86758 on 2017/12/12 0012.
 */

public class Bottom_Dialog extends DialogFragment {
    /**
     * log tag for Bottom_Dialog
     */
    private static final String TAG = "Bottom_Dialog";
    public static final String DIALOG_TAG_2 = "dialog2";
    View dialogView;//底栏的对象
    ImageButton bt_adjustBackground,bt_more;
    CheckBox bt_like;

    public static Bottom_Dialog newInstance() {
        return new Bottom_Dialog();
    }

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
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE); // 不显示标题栏
        dialogView = inflater.inflate(R.layout.bottom_dialog, container, false);
        //绑定底栏按键
        bt_adjustBackground=dialogView.findViewById(R.id.bt_adjust_background);
        bt_more=dialogView.findViewById(R.id.bt_more);
        bt_like=dialogView.findViewById(R.id.bt_like);
        //点击换背景按钮
        bt_adjustBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //关闭底栏
                startDownAnimation(dialogView);
                //唤醒设置背景字号对话框
                Dialog_adjust.newInstance().show(getFragmentManager(), DIALOG_TAG_2);
            }
        });
        //点击收藏按钮
        bt_like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(Bottom_Dialog.this.getActivity(), "已收藏", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Bottom_Dialog.this.getActivity(), "取消收藏", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //点击更多按钮
        bt_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //关闭底栏
                startDownAnimation(dialogView);
                //唤醒更多对话框
                Dialog_more.newInstance().show(getFragmentManager(), DIALOG_TAG_2);
            }
        });
        //开启动画
        startUpAnimation(dialogView);
        return dialogView;
    }
    //开启动画
    private void startUpAnimation(View view) {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        slide.setDuration(400);//动画时间
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        view.startAnimation(slide);
    }
    //关闭动画
    private void startDownAnimation(View view) {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        view.startAnimation(slide);
    }
}
