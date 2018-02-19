package com.example.arturmusayelyan.myweatherforecast.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.example.arturmusayelyan.myweatherforecast.R;

/**
 * Created by artur.musayelyan on 19/02/2018.
 */

public class Loader extends RelativeLayout {
    private final int ANIM_DURATION=200;
    private Context context;
    private View circleLoaderIcon;
    private Animation animRotLeft;
    private boolean isStarting;
    private ObjectAnimator animator;

    public Loader(Context context) {
        super(context);
        this.context = context;
    }

    public Loader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        View view = inflate(context, R.layout.loader_view, this);
        circleLoaderIcon = view.findViewById(R.id.circle_loading_view);
    }

    public void start() {
        if(isStarting){
            return;
        }
        isStarting=true;

        setAlpha(0f);
        setVisibility(VISIBLE);

        animate().alpha(1f).setDuration(ANIM_DURATION).setListener(null);
        animRotLeft= AnimationUtils.loadAnimation(context,R.anim.rotate_animation_left);
        circleLoaderIcon.startAnimation(animRotLeft);
    }

    public void end() {
        if(!isStarting){
            return;
        }
        isStarting=false;
        animRotLeft.cancel();
        animate().alpha(0f).setDuration(ANIM_DURATION).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(GONE);
            }
        });
       // animator.end();
       // setVisibility(GONE);
    }
}
