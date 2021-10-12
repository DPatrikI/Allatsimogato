package hu.test.creatit.allatsimogato;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.util.Freezable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class TovabbiAllatokActivity extends AppCompatActivity {

    private final String TAG = "TovabbiAllatok";

    private final String[] allatok = {"macska", "barna medve", "hörcsög", "kukac", "nyúl", "hangya", "szöcske", "rinocérosz", "bengáli tigris", "oroszlán",
            "párduc", "emu", "sas", "berber majom", "fecske", "leopárd", "pingvin", "egér", "sün", "disznó", "szarvas", "őz", "csótány", "fekete rigó",
            "szentjánosbogár", "hattyú", "hangyász", "vakond", "füsti fecske", "párduc", "bolha", "denevér", "dongó", "elefánt", "fóka", "borz", "galamb", "juh", "ló"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovabbi_allatok);

        setUpAllatok();
    }

    private void setUpAllatok(){

        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutTovabbiAllatok);

        int[] allatIds = new int[allatok.length];
        FrameLayout allat;

        for (int i = 0; i<allatok.length; i++){
            allat = MyViews.getFrameLayoutWithText(this, allatok[i]);
            constraintLayout.addView(allat);
            allatIds[i] = allat.getId();
        }

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        constraintSet.connect(allatIds[0], ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP, getPxFromDp(50));
        constraintSet.connect(allatIds[0], ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START, getPxFromDp(50));

        for (int i = 1; i<allatIds.length; i++){
            constraintSet.connect(allatIds[i], ConstraintSet.TOP, allatIds[i-1], ConstraintSet.BOTTOM, getPxFromDp(15));
            constraintSet.connect(allatIds[i], ConstraintSet.START, allatIds[i-1], ConstraintSet.START);
        }

        constraintSet.applyTo(constraintLayout);




        addPicToAllat(findViewById(allatIds[getIdOfAllat("sün")]), "https://www.fressnapf.hu/wp-content/uploads/2017/08/FNaug28.jpg");
        addPicToAllat(findViewById(allatIds[getIdOfAllat("fóka")]), "https://pecszoo.hu/wp-content/uploads/2017/10/IMG_8417-1024x682.jpg");
        addPicToAllat(findViewById(allatIds[getIdOfAllat("hörcsög")]), "https://m.blog.hu/ha/hazmestermedve/image/.external/.thumbs/4d9a6cd526da166899020c6e8aab4212_d49afe6e3b4eb7cadfe308837303ec67.jpg");

    }

    private int getIdOfAllat(String allatNeve){
        for (int i = 0; i < allatok.length; i++){
            if (allatok[i].equals(allatNeve)){
                return i;
            }
        }
        return 0;
    }

    private void addPicToAllat(FrameLayout allat, String url){

        ImageView imageView = new ImageView(this);
        imageView.setId(View.generateViewId());

        new DownloadImageTask((ImageView) imageView).execute(url);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.END;
        imageView.setLayoutParams(layoutParams);
        resizeImageView(imageView);

        allat.addView(imageView);
    }


    private void resizeImageView(ImageView imageView){
        //int originalHeight = imageView.getLayoutParams().height, originalWidth = imageView.getLayoutParams().width;
        int newHeight = getPxFromDp(100);
        //float divideBy = (float) (originalHeight/newHeight);

        imageView.getLayoutParams().height = newHeight;

        //imageView.getLayoutParams().width = ((int) (originalWidth/divideBy));
    }


    private int getPxFromDp(float dp){
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics() );
        return  (int)px;
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}