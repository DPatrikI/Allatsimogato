package hu.test.creatit.allatsimogato;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AllatsimogatoActivity extends AppCompatActivity {

    private final String TAG = "AllatsimogatoAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allatsimogato);
    }


    public void onAllatGombPressed(View view){
        ImageView imageView = findViewById(R.id.imageViewAllatsimogato);
        String resourceId = getResources().getResourceEntryName(view.getId());
        switch (resourceId){
            case "marhaButton":
                imageView.setBackgroundResource(R.drawable.marha);
                resizeImage(getDrawable(R.drawable.marha));
                break;
            case "kacsaButton":
                imageView.setBackgroundResource(R.drawable.kacsa);
                resizeImage(getDrawable(R.drawable.kacsa));
                break;
            case "kutyaButton":
                imageView.setBackgroundResource(R.drawable.kutya);
                resizeImage(getDrawable(R.drawable.kutya));
                break;
        }
    }


    public void resizeImage(Drawable image){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        Bitmap bitmap = ((BitmapDrawable)image).getBitmap();
        float aspectRatio = (float) bitmap.getHeight() / (float) bitmap.getWidth();

        ImageView imageView = findViewById(R.id.imageViewAllatsimogato);

        imageView.getLayoutParams().width = (int) (width * 0.2f);
        imageView.getLayoutParams().height = (int) ((width * 0.2f) * aspectRatio);
    }

    public void onVisszaButtonPressed(View view){
        onVisszaButtonPressed();
    }
    private void onVisszaButtonPressed(){
        finish();
    }

}