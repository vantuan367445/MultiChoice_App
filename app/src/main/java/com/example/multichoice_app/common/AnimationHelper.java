package com.example.multichoice_app.common;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

import com.example.multichoice_app.listener.VoidCallback;

public class AnimationHelper {
    private static final long DURATION = 200;
    private static final float SCALE = 0.94f;

    public static void ScaleAnimation(final View view, final VoidCallback animationListener, float scale) {
        ViewCompat.animate(view)
                .setDuration(DURATION)
                .scaleX(scale != 0 ? scale : SCALE)
                .scaleY(scale != 0 ? scale : SCALE)
                .setInterpolator(new CycleInterpolator())
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {

                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        // some thing
                        if (animationListener != null)
                            animationListener.execute();
                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                })
                .withLayer()
                .start();
    }


//    public static void ScaleAnimation(final View view, final StringCallback animationListener, final String word) {
//        ViewCompat.animate(view)
//                .setDuration(DURATION)
//                .scaleX(SCALE)
//                .scaleY(SCALE)
//                .setInterpolator(new CycleInterpolator())
//                .setListener(new ViewPropertyAnimatorListener() {
//                    @Override
//                    public void onAnimationStart(View view) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(View view) {
//                        // some thing
//                        if (animationListener != null)
//                            animationListener.execute(word);
//                    }
//
//                    @Override
//                    public void onAnimationCancel(View view) {
//
//                    }
//                })
//                .withLayer()
//                .start();
//    }
//
//    public static void ShakeAnimation(Context context, final View view, final VoidCallback animationListener) {
//        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake_3);
//        shake.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                if (animationListener != null)
//                    animationListener.execute();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        view.startAnimation(shake);
//    }

    public static class CycleInterpolator implements android.view.animation.Interpolator {

        private final float mCycles = 0.5f;

        @Override
        public float getInterpolation(final float input) {
            return (float) Math.sin(2.0f * mCycles * Math.PI * input);
        }
    }


}
