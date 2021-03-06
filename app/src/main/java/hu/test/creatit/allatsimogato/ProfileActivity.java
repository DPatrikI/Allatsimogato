package hu.test.creatit.allatsimogato;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.Calendar;


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

        //lekérem a MainActivity-ből kapott JSON adatokat
        String username = intent.getStringExtra(user);
        String userTitle = intent.getStringExtra(title);
        int userId = Integer.parseInt(intent.getStringExtra(id));
        String userTime_create = intent.getStringExtra(time_create);
        String userTime_update = intent.getStringExtra(time_create);

        TextView userTextView = findViewById(R.id.userTextView), time_createTextView = findViewById(R.id.time_createTextView);
        userTextView.setText(username);
        StringBuilder time_createStringBuilder = new StringBuilder();
        time_createStringBuilder.append("Regisztráció dáutma: ");
        Calendar time_createCalendar = getCalendarFromIntentString(userTime_create);

        //év hónap nap összerakása stringbe:
        time_createStringBuilder.append(time_createCalendar.get(Calendar.YEAR)).append(". ")
                .append(time_createCalendar.get(Calendar.MONTH)+1).append(". ").append(time_createCalendar.get(Calendar.DAY_OF_MONTH)).append(".");
        time_createTextView.setText(time_createStringBuilder);

        setUpButtons();


    }

    private void setUpButtons(){
        Button logOutButton = findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogOutButtonPressed();
            }
        });

        Button allatsimogatoButton = findViewById(R.id.allatsimogatoButton);
        allatsimogatoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAllatsimogatoButtonPressed();
            }
        });
    }

    //string->Calendar hackelés (biztos elegánsabban is meg lehetne oldani :'D )
    private Calendar getCalendarFromIntentString(String calendarString){
        Calendar calendar = Calendar.getInstance();

        int year = Integer.parseInt( (((calendarString.split("T"))[0]).split("-"))[0] );
        int month = Integer.parseInt( (((calendarString.split("T"))[0]).split("-"))[1] );
        month --;
        int day = Integer.parseInt( (((calendarString.split("T"))[0]).split("-"))[2] );

        int hour = Integer.parseInt( ((((calendarString.split("T"))[1]).split("\\+"))[0]).split(":")[0] );
        int minute = Integer.parseInt( ((((calendarString.split("T"))[1]).split("\\+"))[0]).split(":")[1] );
        int sec = Integer.parseInt( ((((calendarString.split("T"))[1]).split("\\+"))[0]).split(":")[2] );

        calendar.set(year, month, day, hour, minute, sec);

        return calendar;
    }


    private void onAllatsimogatoButtonPressed(){

        Intent intent = new Intent(this, AllatsimogatoActivity.class);
        startActivity(intent);

    }

    private void onLogOutButtonPressed(){
        finish();
    }
}