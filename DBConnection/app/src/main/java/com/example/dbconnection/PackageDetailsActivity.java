package com.example.dbconnection;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PackageDetailsActivity extends AppCompatActivity {
    private String IP = "192.168.0.6";

    Button cafeButton, mealButton, movieButton, pubButton, nextButton,btnBack;
    TextView cafeSelection, mealSelection, movieSelection, pubSelction;
    Intent send;
    int increment;
    TimePickerDialog timePicker;
    private String myId, partnerId;

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_NAME = "NAME";
    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;
    ArrayList<String> store_name;

    boolean chkc = false, chkf = false, chkt = false, chkb = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_details);
        personList = new ArrayList<HashMap<String, String>>();
        store_name = new ArrayList<String>();

        cafeButton = (Button)findViewById(R.id.cafe_button);
        mealButton = (Button)findViewById(R.id.meal_button);
        movieButton = (Button)findViewById(R.id.movie_button);
        pubButton = (Button)findViewById(R.id.pub_button);
        nextButton = (Button)findViewById(R.id.next_button);
        btnBack = (Button)findViewById(R.id.btnBack);

        cafeSelection = (TextView)findViewById(R.id.cafe_selection);
        mealSelection = (TextView)findViewById(R.id.meal_selection);
        movieSelection = (TextView)findViewById(R.id.movie_selection);
        pubSelction = (TextView)findViewById(R.id.pub_selection);

        Intent get = getIntent();
        myId = get.getStringExtra("myId");
        partnerId = get.getStringExtra("partnerId");
        int tmp = get.getIntExtra("id",-1);
        send = new Intent(getApplicationContext(), FinalMessageActivity.class);
        increment = 0;
        String date = get.getStringExtra("date");
        send.putExtra("date", date);

        timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                send.putExtra("time", hour + ":" + minute);
            }
        }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);

        switch (tmp){
            case 1:
                mealButton.setVisibility(View.GONE);
                movieButton.setVisibility(View.GONE);
                pubButton.setVisibility(View.GONE);
                break;
            case 2:
                movieButton.setVisibility(View.GONE);
                pubButton.setVisibility(View.GONE);
                break;
            case 3:
                pubButton.setVisibility(View.GONE);
                break;
            case 4:
                break;
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cafeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkc = true;
                getData("http://" + IP + "/khuton/ALLstore.php?FIELD=CAFE"); //수정 필요
            }
        });
        mealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkf = true;
                getData("http://" + IP + "/khuton/ALLstore.php?FIELD=f_store"); //수정 필요
            }
        });
        movieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkt = true;
                getData("http://" + IP + "/khuton/ALLstore.php?FIELD=theater"); //수정 필요
            }
        });
        pubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkb = true;
                getData("http://" + IP + "/khuton/ALLstore.php?FIELD=bar"); //수정 필요
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send.putExtra("increment", increment);
                //Intent intent = new Intent(getApplicationContext(),FinalMessageActivity.class);
                send.putExtra("myId", myId);
                send.putExtra("partnerId",partnerId);
                startActivity(send);
            }
        });
    }

    void show_cafe()
    {
        final ArrayList<String> ListItems = new ArrayList<>();
        for(int i=0;i<store_name.size();i++)
        {
            ListItems.add(store_name.get(i));
        }

        //ListItems.add("스타벅스");
        //ListItems.add("엔젤리너스");
        //ListItems.add("탐엔탐스");
        CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

        final List SelectedItems = new ArrayList();
        int defaultItem = 0;
        SelectedItems.add(defaultItem);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("카페 선택");
        builder.setSingleChoiceItems(items, defaultItem,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        SelectedItems.clear();
                        SelectedItems.add(which);
                    }
                });

        builder.setPositiveButton("select",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        String msg = "";

                        if (!SelectedItems.isEmpty()) {
                            int index = (int) SelectedItems.get(0);
                            msg = ListItems.get(index);
                            cafeSelection.setText(msg);

                            if (increment == 0){
                                timePicker.show();
                                send.putExtra("first", "cafe");
                            }

                            send.putExtra("cafe", msg);
                            increment++;
                        }
                        Toast.makeText(getApplicationContext(),
                                "Cafe selected\n" + msg, Toast.LENGTH_LONG).show();

                    }
                });

        builder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                    }
                });
        builder.show();
    }

    void show_meal()
    {
        final ArrayList<String> ListItems = new ArrayList<>();
        for(int i=0;i<store_name.size();i++)
        {
            ListItems.add(store_name.get(i));
        }

        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

        final List SelectedItems = new ArrayList();
        int defaultItem = 0;
        SelectedItems.add(defaultItem);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("음식점 선택");
        builder.setSingleChoiceItems(items, defaultItem,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        SelectedItems.clear();
                        SelectedItems.add(which);
                    }
                });

        builder.setPositiveButton("select",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        String msg = "";

                        if (!SelectedItems.isEmpty()) {
                            int index = (int) SelectedItems.get(0);
                            msg = ListItems.get(index);
                            mealSelection.setText(msg);

                            if (increment == 0){
                                timePicker.show();
                                send.putExtra("first", "meal");
                            }

                            send.putExtra("meal", msg);
                            increment++;
                        }
                        Toast.makeText(getApplicationContext(),
                                "Cafe selected\n" + msg, Toast.LENGTH_LONG).show();

                    }
                });

        builder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                    }
                });
        builder.show();
    }

    void show_movie()
    {
        final ArrayList<String> ListItems = new ArrayList<>();
        for(int i=0;i<store_name.size();i++)
        {
            ListItems.add(store_name.get(i));
        }

        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

        final List SelectedItems = new ArrayList();
        int defaultItem = 0;
        SelectedItems.add(defaultItem);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("영화관 선택");
        builder.setSingleChoiceItems(items, defaultItem,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        SelectedItems.clear();
                        SelectedItems.add(which);
                    }
                });

        builder.setPositiveButton("select",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        String msg = "";

                        if (!SelectedItems.isEmpty()) {
                            int index = (int) SelectedItems.get(0);
                            msg = ListItems.get(index);
                            movieSelection.setText(msg);

                            if (increment == 0){
                                timePicker.show();
                                send.putExtra("first", "movie");
                            }

                            send.putExtra("movie", msg);
                            increment++;
                        }
                        Toast.makeText(getApplicationContext(),
                                "Cafe selected\n" + msg, Toast.LENGTH_LONG).show();

                    }
                });

        builder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                    }
                });
        builder.show();
    }

    void show_pub()
    {
        final ArrayList<String> ListItems = new ArrayList<>();
        for(int i=0;i<store_name.size();i++)
        {
            ListItems.add(store_name.get(i));
        }

        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

        final List SelectedItems = new ArrayList();
        int defaultItem = 0;
        SelectedItems.add(defaultItem);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("술집 선택");
        builder.setSingleChoiceItems(items, defaultItem,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        SelectedItems.clear();
                        SelectedItems.add(which);
                    }
                });

        builder.setPositiveButton("select",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        String msg = "";

                        if (!SelectedItems.isEmpty()) {
                            int index = (int) SelectedItems.get(0);
                            msg = ListItems.get(index);
                            pubSelction.setText(msg);

                            if (increment == 0){
                                timePicker.show();
                                send.putExtra("first", "pub");
                            }

                            send.putExtra("pub", msg);
                            increment++;
                        }
                        Toast.makeText(getApplicationContext(),
                                "Cafe selected\n" + msg, Toast.LENGTH_LONG).show();

                    }
                });

        builder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                    }
                });
        builder.show();
    }

    protected void showList()
    {
        store_name.clear();
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < peoples.length(); i++)
            {
                JSONObject c = peoples.getJSONObject(i);
                //String dbid = c.getString(TAG_ID);
                //String dbpd = c.getString(TAG_PD);
                String name = c.getString(TAG_NAME);
                //String cls = c.getString(TAG_CLASS);

                HashMap<String, String> persons = new HashMap<String, String>();

                //persons.put(TAG_ID, dbid);
                //persons.put(TAG_PD, dbpd);
                persons.put(TAG_NAME, name);
                //persons.put(TAG_CLASS, cls);

                store_name.add(name);
                personList.add(persons);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(chkc) {
            show_cafe();
            chkc = false;
        }
        else if(chkf) {
            show_meal();
            chkf = false;
        }
        else if(chkt) {
            show_movie();
            chkt = false;
        }
        else if(chkb) {
            show_pub();
            chkb = false;
        }
    }

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
            }

        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);

    }
}