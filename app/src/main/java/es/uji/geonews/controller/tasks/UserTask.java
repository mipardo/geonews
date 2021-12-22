package es.uji.geonews.controller.tasks;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public abstract class UserTask extends AppCompatActivity {
    
    public abstract void execute();

    protected void lockUI(Context context, ConstraintLayout layout){
        layout.setVisibility(View.VISIBLE);
        layout.bringToFront();
        ((Activity)context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    protected void unlockUI(Context context, ViewGroup layout){
        layout.setVisibility(View.GONE);
        ((Activity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    protected void showLoadingAnimation(ViewGroup layout) {
        layout.setAlpha(0);
        layout.setTranslationY(-layout.getHeight());
        layout.setVisibility(View.VISIBLE);
        layout.animate()
                .alpha(1)
                .translationY(0)
                .setDuration(200);
    }

    protected void hideLoadingAnimation(ViewGroup layout) {
        layout.animate()
                .alpha(0)
                .setDuration(300)
                .translationY(-layout.getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        layout.setVisibility(View.GONE);
                    }
                });
    }
}
