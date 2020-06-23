package com.libs.core.common.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

public class AnimUtils {

    private final static long DURATION=350;

    private static int alphaColor(int alpha, int color) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    public static void translucentBlink(final View view, final int bg, double change) {
        if (view == null) return;
        final GradientDrawable gradientDrawable;
        if (Math.abs(change) == 0) {
            view.setBackgroundResource(bg);
            return;
        } else if (change > 0) {
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{alphaColor(25, QuoteUtils.getIncreaseColor()), alphaColor(51, QuoteUtils.getIncreaseColor()), alphaColor(102, QuoteUtils.getIncreaseColor()), alphaColor(51, QuoteUtils.getIncreaseColor())});
        } else {
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{alphaColor(25, QuoteUtils.getDecreaseColor()), alphaColor(51, QuoteUtils.getDecreaseColor()), alphaColor(102, QuoteUtils.getDecreaseColor()), alphaColor(51, QuoteUtils.getDecreaseColor())});
        }
        if (view.getBackground() instanceof GradientDrawable) {
            ((GradientDrawable) view.getBackground()).setCallback(null);
        }
        if (view.getAnimation() != null) {
            view.getAnimation().cancel();
        }
        view.setBackgroundDrawable(gradientDrawable);
        final ObjectAnimator valueAnimator = ObjectAnimator.ofInt(gradientDrawable, "alpha", 255, 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int alpha = (int) animation.getAnimatedValue();
                gradientDrawable.setAlpha(alpha);
            }

        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (gradientDrawable != null) {
                    gradientDrawable.setCallback(null);
                }
                animation.removeAllListeners();
                view.setBackgroundResource(bg);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (gradientDrawable != null) {
                    gradientDrawable.setCallback(null);
                }
                animation.removeAllListeners();
                view.setBackgroundResource(bg);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setDuration(DURATION);
        valueAnimator.start();
    }

}
