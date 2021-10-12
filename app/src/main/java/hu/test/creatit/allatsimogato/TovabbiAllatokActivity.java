package hu.test.creatit.allatsimogato;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.animation.ValueAnimator;
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
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
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

    private final String urlForSun = "https://www.fressnapf.hu/wp-content/uploads/2017/08/FNaug28.jpg";
    private final String urlForFoka = "https://pecszoo.hu/wp-content/uploads/2017/10/IMG_8417-1024x682.jpg";
    private final String urlForHorcsog = "https://m.blog.hu/ha/hazmestermedve/image/.external/.thumbs/4d9a6cd526da166899020c6e8aab4212_d49afe6e3b4eb7cadfe308837303ec67.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovabbi_allatok);

        setUpAllatok();
    }

    //programmatically rakom le az állatokat, hogy könnyen lehessen hozzáadni/elvenni
    private void setUpAllatok(){

        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutTovabbiAllatok);

        //az állatok id-ját külön tárolom az egyszerűség kedvéért, így könnyen egymáshoz tudom illeszteni őket majd
        int[] allatIds = new int[allatok.length];
        FrameLayout allat;

        for (int i = 0; i<allatok.length; i++){
            //egy állat = egy FrameLayout, ami előre fel van set up-olva text-tel
            allat = MyViews.getFrameLayoutWithText(this, allatok[i]);
            constraintLayout.addView(allat);
            allatIds[i] = allat.getId();
        }

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        //a legelső állatot constraintLayoutTovabbiAllatok-hoz igazítom
        constraintSet.connect(allatIds[0], ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP, getPxFromDp(50));
        constraintSet.connect(allatIds[0], ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START, getPxFromDp(50));
        constraintSet.connect(allatIds[0], ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, getPxFromDp(50));

        //az összes többi állatot pedig az előtte hozzáadotthoz
        for (int i = 1; i<allatIds.length; i++){
            constraintSet.connect(allatIds[i], ConstraintSet.TOP, allatIds[i-1], ConstraintSet.BOTTOM, getPxFromDp(15));
            constraintSet.connect(allatIds[i], ConstraintSet.START, allatIds[i-1], ConstraintSet.START);
            constraintSet.connect(allatIds[i], ConstraintSet.END, allatIds[i-1], ConstraintSet.END);
        }

        constraintSet.applyTo(constraintLayout);

        //képek hozzáadása a 3 állathoz
        addPicToAllat(findViewById(allatIds[getIdOfAllat("sün")]), urlForSun);
        addPicToAllat(findViewById(allatIds[getIdOfAllat("fóka")]), urlForFoka);
        addPicToAllat(findViewById(allatIds[getIdOfAllat("hörcsög")]), urlForHorcsog);

    }

    //ezt csak az egyszerűség kedvéért csináltam, hogy meg lehessen keresni az adott állat indexét az állatok tömbben,
    //ami alapján meg tudjuk kapni az adott állat (FrameLayout) id-ját a feltöltött allatIds tömbben
    //magyarul utólag adom hozzá a képeket, szóval akár dinamikusan is lehetne hozzáadni
    private int getIdOfAllat(String allatNeve){
        for (int i = 0; i < allatok.length; i++){
            if (allatok[i].equals(allatNeve)){
                return i;
            }
        }
        return 0;
    }

    //ez hozzáadja az adott állathoz az adott képet url alapján
    private void addPicToAllat(FrameLayout allat, String url){

        ImageView imageView = new ImageView(this);
        imageView.setId(View.generateViewId());

        //a paraméterben megkapott imageView-t átvarázsolja az url-ben megadott képre
        new DownloadImageTask((ImageView) imageView).execute(url);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.END;
        imageView.setLayoutParams(layoutParams);

        //resize-olom, hogy ne legyen 5km széles a kép:
        resizeImageView(imageView);

        //képre koppintáskor meghívom a resize-olós functiont
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resizeOnTap(view);
            }
        });

        allat.addView(imageView);
    }


    private void resizeImageView(ImageView imageView){
        //30 dp-nek megfelelő magasságúra állítom a képet. Eredetileg a szélességet is át akartam állítani, hogy arányos legyen,
        //de magától arányos lesz, úgyhogy kikommenteztem azokat a részeket


        //int originalHeight = imageView.getLayoutParams().height, originalWidth = imageView.getLayoutParams().width;
        int newHeight = getPxFromDp(30);
        //float divideBy = (float) (originalHeight/newHeight);

        imageView.getLayoutParams().height = newHeight;

        //imageView.getLayoutParams().width = ((int) (originalWidth/divideBy));
    }

    //a képet 30dp-ről 120-ra és 120 dp-ről 30-ra animálja
    private void resizeOnTap(View imageView){
        int originalHeight = imageView.getLayoutParams().height;

        ValueAnimator valueAnimator;

        if (originalHeight == getPxFromDp(30)){
            valueAnimator = ValueAnimator.ofInt(originalHeight, getPxFromDp(120)).setDuration(500);
        }
        else {
            valueAnimator = ValueAnimator.ofInt(originalHeight, getPxFromDp(30)).setDuration(500);
        }


        valueAnimator.setInterpolator(new OvershootInterpolator());

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                imageView.getLayoutParams().height = (int) valueAnimator.getAnimatedValue();
                imageView.requestLayout();
            }
        });

        valueAnimator.start();

    }


    //megkapja, hogy mekkora dp-t szeretnénk és visszaadja pixelben
    private int getPxFromDp(float dp){
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics() );
        return  (int)px;
    }


    //kép betöltése url alapján black magic asyncben, hogy ne kelljen rá várni
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