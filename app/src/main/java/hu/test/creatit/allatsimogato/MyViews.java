package hu.test.creatit.allatsimogato;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class MyViews {

    static public ProgressBar getProgressBar(Context context){
        ProgressBar progressBar = new ProgressBar(context);

        progressBar.setId(View.generateViewId());
        progressBar.setMax(100);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(400, 50);
        progressBar.setLayoutParams(layoutParams);

        return progressBar;
    }

}
