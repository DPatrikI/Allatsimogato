package hu.test.creatit.allatsimogato;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ProfileActivity extends AppCompatActivity {

    private final String TAG = "ProfileActivity";
    private final String user = "user";
    private final String title = "title";
    private final String id = "id";
    private final String time_create = "time_create";
    private final String time_update = "time_update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        String username = intent.getStringExtra(user);
        String userTitle = intent.getStringExtra(title);
        int userId = Integer.parseInt(intent.getStringExtra(id));
        String userTime_create = intent.getStringExtra(time_create);
        String userTime_update = intent.getStringExtra(time_create);

        Log.e(TAG, String.valueOf(userTitle + " " + userTime_create));

    }
}