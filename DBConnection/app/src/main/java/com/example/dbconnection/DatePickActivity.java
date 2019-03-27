package com.example.dbconnection;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;

public class DatePickActivity extends AppCompatActivity {

    CalendarView datePicker;
    Intent intent;
    private int tmp;
    private String myId, partnerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_pick);

        intent = getIntent();

        tmp = intent.getIntExtra("id",-1);
        datePicker = (CalendarView)findViewById(R.id.date_picker);

        myId = intent.getStringExtra("myId");
        partnerId = intent.getStringExtra("partnerId");

        datePicker.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                intent = new Intent(getApplicationContext(), PackageDetailsActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month+1);
                intent.putExtra("day", day);
                intent.putExtra("id",tmp);
                intent.putExtra("date", String.valueOf(year) + "." + String.valueOf(month + 1) + "." + String.valueOf(day));
                intent.putExtra("myId",myId);
                intent.putExtra("partnerId",partnerId);
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_SINGLE_TOP));
            }
        });
    }
}