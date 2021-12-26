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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.TimeZone;

public abstract class UserTask extends AppCompatActivity {
    
    public abstract void execute();

    public String getCurrentDate() {
        DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                .appendPattern("dd MMMM")
                .toFormatter(new Locale("es", "ES"));
        return LocalDateTime.now().format(fmt);
    }

    private String getTomorrowDate() {
        DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                .appendPattern("dd MMMM")
                .toFormatter(new Locale("es", "ES"));
        return LocalDateTime.now().plusDays(1).format(fmt);
    }

    public String getFormatedTimestamp(long timestamp){
        DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                .appendPattern("HH:mm")
                .toFormatter(new Locale("es", "ES"));
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp*1000),
                TimeZone.getDefault().toZoneId());
        return localDateTime.format(fmt);
    }

    protected void lockUI(Context context, ViewGroup layout){
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

    protected int getHoursLeftOfToday(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth() + 1, 0, 0);
        long hoursLeft = ChronoUnit.HOURS.between(now, tomorrow);
        return (int) hoursLeft + 1;
    }
}
