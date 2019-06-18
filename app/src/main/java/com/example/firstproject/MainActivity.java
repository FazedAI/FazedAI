package com.example.firstproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import com.example.firstproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main );
    }

    public void load(View view) {

        animateButtonWidth();
        fadeoutTextAndSetProgressDialog();
        nextAction();

    }

    private void animateButtonWidth() {
        ValueAnimator anim = ValueAnimator.ofInt(mBinding.signInBtn.getMeasuredWidth(),getFinalWidth());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int value = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mBinding.signInBtn.getLayoutParams();
                layoutParams.width = value;
                mBinding.signInBtn.requestLayout();
            }
        });

        anim.setDuration(250);
        anim.start();

    }

    private void fadeoutTextAndSetProgressDialog() {

        mBinding.signInText.animate().alpha(0f).setDuration(250).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                showProgressDialog();
            }
        }).start();
    }

    private void showProgressDialog() {
        mBinding.progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
        mBinding.progressBar.setVisibility(View.VISIBLE);

    }

    private void nextAction() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                revealButton();
                fadeOutProgressDialog();
                delayedStartNextActivity();

            }
        },   2000);
    }

    private void revealButton() {

        mBinding.signInBtn.setElevation(0f);
        mBinding.revealView.setVisibility(View.VISIBLE);

        int x = mBinding.revealView.getWidth();
        int y = mBinding.revealView.getHeight();

        int startX = (int) (getFinalWidth() / 2 + mBinding.signInBtn.getX());
        int startY = (int) (getFinalWidth() / 2 + mBinding.signInBtn.getY());

        float radius = Math.max(x, y) * 1.2f;

        Animator reveal = ViewAnimationUtils.createCircularReveal(mBinding.revealView, startX, startY, getFinalWidth(), radius);
        reveal.setDuration(350);
        reveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                finish();
            }
        });

        reveal.start();
    }

    private void fadeOutProgressDialog() {

        mBinding.progressBar.animate().alpha(0f).setDuration(200).start();
    }

    private void delayedStartNextActivity() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, SecondView.class));
            }
        }, 100);

    }

    private int getFinalWidth() {
        return (int) getResources().getDimension(R.dimen.get_width);
    }
}
