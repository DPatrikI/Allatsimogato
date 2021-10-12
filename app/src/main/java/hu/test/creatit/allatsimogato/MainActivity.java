package hu.test.creatit.allatsimogato;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    ConnectivityManager connectivityManager;
    private boolean connectedToNetwork = false;
    private final String user = "user";
    private final String title = "title";
    private final String id = "id";
    private final String time_create = "time_create";
    private final String time_update = "time_update";

    //ha a csatlakozott kapcsolat állapotában változás történik (lecsatlakozik vagy felcsatlakozik), akkor a changeAvailabilityOfLoginButton()-t meghívja,
    //aminek az argumentumában megadja, hogy csatlakozva van-e
    ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback(){
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            changeAvailabilityOfLoginButton(true);
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            changeAvailabilityOfLoginButton(false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpConnectivityManager();


        changeAvailabilityOfLoginButton(isConnectedToNetwork());

    }

    //a connectivityManager inicializálása és a networkCallback regisztrálása, hogy tudjam, ha a network kapcsolat megváltozik
    private void setUpConnectivityManager(){
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        connectivityManager.registerDefaultNetworkCallback(networkCallback);
    }

    //ez csak legelőször nézi meg, hogy csatlakozva van-e az internethez az eszköz
    private boolean isConnectedToNetwork(){
        if (connectivityManager.getActiveNetworkInfo() != null){
            connectedToNetwork = true;
            return true;
        }
        connectedToNetwork = false;
        return false;
    }

    //ez csak a loginButton szövegét és kattinthatóságát állítja annak függvényében, hogy csatlakozva van-e az eszköz valamilyen networkre
    private void changeAvailabilityOfLoginButton(boolean connected){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button loginButton = findViewById(R.id.loginButton);
                loginButton.setClickable(connected);
                if (connected){
                    loginButton.setText("Bejelentkezés");
                }
                else {
                    loginButton.setText("Nincs internet");
                }
            }
        });
    }

    public void onLoginButtonPressed(View view){

        //ha ez true, akkor nem megy végig a bejelentkezésen (username, password, 3mp töltés), hanem egyből a ProfileActivity-re ugik (debughoz)
        if (false){
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);

            JSONObject jsonObject = getJSONObject();
            try {
                intent.putExtra(user, "user");
                intent.putExtra(title, jsonObject.getString(title));
                intent.putExtra(id, String.valueOf(jsonObject.getInt(id)));
                intent.putExtra(time_create, jsonObject.getString(time_create));
                intent.putExtra(time_update, jsonObject.getString(time_update));
            } catch (Exception e){
                Log.e(TAG, String.valueOf(e));
            }

            startActivity(intent);
            return;
        }

        onLoginButtonPressed();
    }

    private void onLoginButtonPressed(){
        TextView usernameTextView = findViewById(R.id.usernameTextView), passwordTextView = findViewById(R.id.passwordTextView);

        //megnézi, hogy helyesek-e a megadott adatok
        if (usernameTextView.getText().toString().equals("user") && passwordTextView.getText().toString().equals("pass")) {
            loginToProfile("user");
        }
        else {
            Toast.makeText(this, "Wrong user or password", Toast.LENGTH_SHORT).show();
        }

    }

    //a bejelentkezés elkezdése (ez akkor, ha helyesek voltak a megadott adatok és van netkapcsolat)
    private void loginToProfile(String profilename){

        //először átállítom a bejelentkezés gombot, hogy ne lehessen ráclickelni megint
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setClickable(false);

        //hozzáadom a progressbart, ami a 3mp-es töltést mutatja
        ProgressBar progressBar = addProgressBar();

        //3mp-es timer:
        new CountDownTimer(3000, 10){
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                //ha letelt a 3mp

                //törlöm a progressbart
                ( (ViewManager) progressBar.getParent()).removeView(progressBar);
                //bejeltkezés gomb megint clickelhető
                loginButton.setClickable(true);
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);

                //a profil json-ből kiszedem az adatokat és átadom az intent-nek (amit a ProfileActivity kap meg)
                JSONObject jsonObject = getJSONObject();
                try {
                    intent.putExtra(user, profilename);
                    intent.putExtra(title, jsonObject.getString(title));
                    intent.putExtra(id, String.valueOf(jsonObject.getInt(id)));
                    intent.putExtra(time_create, jsonObject.getString(time_create));
                    intent.putExtra(time_update, jsonObject.getString(time_update));
                } catch (Exception e){
                    Log.e(TAG, String.valueOf(e));
                }

                startActivity(intent);
            }
        }.start();
    }

    private ProgressBar addProgressBar(){
        ProgressBar progressBar = MyViews.getProgressBar(this);
        progressBar.setId(View.generateViewId());

        ConstraintLayout constraintLayout = findViewById(R.id.mainConstraintLayout);
        constraintLayout.addView(progressBar);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(progressBar.getId(), ConstraintSet.BOTTOM, findViewById(R.id.userPassCardView).getId(), ConstraintSet.TOP, getPxFromDp(10));
        constraintSet.connect(progressBar.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START);
        constraintSet.connect(progressBar.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END);
        constraintSet.applyTo(constraintLayout);
        progressBar.getLayoutParams().height = getPxFromDp(50);
        progressBar.getLayoutParams().height = getPxFromDp(50);

        return progressBar;
    }



    private JSONObject getJSONObject(){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("title", "Test user");
            jsonObject.put("id", 121);
            jsonObject.put("time_create", "2012-12-12T12:12:12+00:00");
            jsonObject.put("time_update", "2018-11-11T13:41:40+00:00");
        } catch (Exception e){

        }

        return jsonObject;
    }

    //megkapja, hogy mekkora dp-t szeretnénk és visszaadja pixelben
    public int getPxFromDp(float dp){
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics() );
        return  (int)px;
    }


}