package es.uji.geonews.controller.tasks;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public abstract class UserTask extends AppCompatActivity {
    
    public abstract void execute();

    protected void lockUI(Context context, ProgressBar progressBar){
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();
        ((Activity)context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    protected void unlockUI(Context context, ProgressBar progressBar){
        progressBar.setVisibility(View.INVISIBLE);
        ((Activity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
