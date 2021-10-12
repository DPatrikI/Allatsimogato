package hu.test.creatit.allatsimogato;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

public class MyViews {

    static public ProgressBar getProgressBar(Context context){
        ProgressBar progressBar = new ProgressBar(context);

        progressBar.setId(View.generateViewId());
        progressBar.setMax(100);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(400, 50);
        progressBar.setLayoutParams(layoutParams);

        return progressBar;
    }

    static public FrameLayout getFrameLayoutWithText(Context context, String text){
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setId(View.generateViewId());

        FrameLayout.LayoutParams fLayoutParams = new FrameLayout.LayoutParams(getPxFromDp(context, 300), ViewGroup.LayoutParams.WRAP_CONTENT);
        frameLayout.setLayoutParams(fLayoutParams);

        TextView textView = new TextView(context);
        textView.setId(View.generateViewId());

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;

        String[] textSplit = text.split(" ");
        if (textSplit.length > 1){
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i<textSplit.length; i++){
                if (i == 1){
                    stringBuilder.append("<u>").append(textSplit[1]).append("</u>");
                }
                else {
                    stringBuilder.append(textSplit[i]);
                }
                stringBuilder.append(" ");
            }
            text = stringBuilder.toString();
        }

        textView.setLayoutParams(layoutParams);
        textView.setText(Html.fromHtml(text));
        //textView.setText(text);
        textView.setTextSize((float) getPxFromDp(context, 6));

        frameLayout.addView(textView);


        return frameLayout;
    }

    static public int getPxFromDp(Context context, float dp){
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics() );
        return  (int)px;
    }

}
