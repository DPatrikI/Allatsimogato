package hu.test.creatit.allatsimogato;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

public class AllatsimogatoActivity extends AppCompatActivity {

    private final String TAG = "AllatsimogatoAct";
    private String currentAnimal = "";
    private MediaPlayer mediaPlayer;

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
                currentAnimal = "marha";
                imageView.setBackgroundResource(R.drawable.marha);
                resizeImage(getDrawable(R.drawable.marha));
                break;
            case "kacsaButton":
                currentAnimal = "kacsa";
                imageView.setBackgroundResource(R.drawable.kacsa);
                resizeImage(getDrawable(R.drawable.kacsa));
                break;
            case "kutyaButton":
                currentAnimal = "kutya";
                imageView.setBackgroundResource(R.drawable.kutya);
                resizeImage(getDrawable(R.drawable.kutya));
                break;
        }
    }


    public void onImageViewTap(View view){
        if (mediaPlayer != null){
            mediaPlayer.reset();
        }
        switch (currentAnimal){
            case "marha":
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.moo);
                    mediaPlayer.start();
                break;
            case "kacsa":
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.kacsa);
                    mediaPlayer.start();
                break;
            case "kutya":
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.kutya);
                    mediaPlayer.start();
        }
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_animation));

    }

    private void resizeImage(Drawable image){
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null){
            mediaPlayer.release();
        }
    }
}