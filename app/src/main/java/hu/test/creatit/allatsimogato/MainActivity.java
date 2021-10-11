package hu.test.creatit.allatsimogato;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    ConnectivityManager connectivityManager;
    private boolean connectedToNetwork = false;

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

        Log.e(TAG, String.valueOf("isConnectedToNetwork: " + isConnectedToNetwork()));

        changeAvailabilityOfLoginButton(isConnectedToNetwork());

    }

    //a connectivityManager inicializálása és a networkCallback regisztrálása, hogy tudjam, ha a network kapcsolat megváltozik
    private void setUpConnectivityManager(){
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        connectivityManager.registerDefaultNetworkCallback(networkCallback);
    }

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


}