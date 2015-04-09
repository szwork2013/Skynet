package com.okar.android;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.works.skynet.base.BaseActivity;

import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 14/11/20.
 */
public class AnimActivity extends BaseActivity{

    @InjectView(R.id.amin_click_btn)
    Button clickBtn;

    @InjectView(R.id.amin_click_btn1)
    Button clickBtn1;

    @InjectView(R.id.amin_label_tv)
    TextView labelTv;

    @Override
    protected void init() {
        setContentView(R.layout.activity_anim);
        clickBtn.setOnClickListener(this);
        clickBtn1.setOnClickListener(this);

        labelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("hahaha");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.amin_click_btn: {
                ViewPropertyAnimator.animate(labelTv).translationX(100).
                        translationY(200).setDuration(500).
                        setListener(new AnimatorListenerAdapter() {

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                ViewPropertyAnimator.animate(labelTv).translationX(-80).
                                        translationY(-80).scaleX(-20).scaleY(-20).
                                        alpha(50).rotation(-90).setDuration(500).
                                        setListener(new AnimatorListenerAdapter() {

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                                labelTv.setText("完事了");
                                            }
                                        });
                            }
                        });
            }break;

            case R.id.amin_click_btn1: {
                ViewPropertyAnimator.animate(labelTv).translationX(-80).
                        translationY(-80).scaleX(-20).scaleY(-20).
                        alpha(50).rotation(-90).setDuration(500).
                        setListener(new AnimatorListenerAdapter() {

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                labelTv.setText("完事了");
                            }
                        });
            }break;

        }
    }
}
