package hu.test.creatit.allatsimogato;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AllatsimogatoActivity extends AppCompatActivity {

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
                break;
            case "kacsaButton":
                imageView.setBackgroundResource(R.drawable.kacsa);
                break;
            case "kutyaButton":
                imageView.setBackgroundResource(R.drawable.kutya);
                break;
        }
    }

    public void onVisszaButtonPressed(View view){
        onVisszaButtonPressed();
    }
    private void onVisszaButtonPressed(){
        finish();
    }

}