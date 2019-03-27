package com.example.dbconnection;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class RandomMatchActivity extends AppCompatActivity {
    private String IP = "192.168.0.6";

    private ImageView userPortrait;
    private TextView userName;
    private Button selectButton, passButton,btnMailbox;
    private String cur_ID, cur_SEX;
    public static final int array[]={
            R.drawable.im1, R.drawable.im2, R.drawable.im3,
            R.drawable.im4, R.drawable.im5, R.drawable.im6,
            R.drawable.im7
    };
    private ArrayList<String> partner_ID;

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "ID";
    private static final String TAG_PD = "PASSWORD";
    private static final String TAG_SEX = "SEX";
    private int partner_idx = 0;
    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_match);

        partner_ID = new ArrayList<String>();
        Intent get = getIntent();
        cur_ID = get.getStringExtra("ID");
        cur_SEX = get.getStringExtra("SEX");

        personList = new ArrayList<HashMap<String, String>>();
        getData("http://" + IP + "/khuton/Search.php?SEX=" + cur_SEX);

        userPortrait = (ImageView)findViewById(R.id.user_portrait);
        userName = (TextView)findViewById(R.id.user_name);
        selectButton = (Button)findViewById(R.id.pick_button);
        btnMailbox = (Button)findViewById(R.id.btnMailbox);

        btnMailbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MailboxActivity.class);
                startActivity(intent);
            }
        });

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

        passButton = (Button)findViewById(R.id.pass_button);
        passButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                ImageView test = (ImageView)findViewById(R.id.user_portrait);

                int random = (int)(Math.random()*7);
                test.setImageResource(array[random]);
                int random2 = (int)(Math.random() * partner_ID.size());
                userName.setText(partner_ID.get(random2));
                partner_idx = random2;
            }
        });
    }

    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("데이트 명세 시작");
        builder.setMessage("1. 데이트 날짜를 정하세요!\n2. 데이트 장소를 정하세요!\n3. 보내기~! 및 기다리기");
        builder.setPositiveButton("시작하자",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(),PackageSelectActivity.class);
                        intent.putExtra("myId",cur_ID);
                        intent.putExtra("partnerId",partner_ID.get(partner_idx));
                        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_SINGLE_TOP));
                    }
                });
        builder.setNegativeButton("다시생각해볼게요",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    protected void showList()
    {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < peoples.length(); i++)
            {
                JSONObject c = peoples.getJSONObject(i);
                String dbid = c.getString(TAG_ID);
                //String dbpd = c.getString(TAG_PD);
                //String name = c.getString(TAG_NAME);
                //String cls = c.getString(TAG_CLASS);

                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_ID, dbid);
                //persons.put(TAG_PD, dbpd);
                //persons.put(TAG_NAME, name);
                //persons.put(TAG_CLASS, cls);

                personList.add(persons);
                partner_ID.add(dbid);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        userName.setText(partner_ID.get(0));
        partner_idx = 0;
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