package com.example.dbconnection;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class FinalMessageActivity extends AppCompatActivity {

    private String IP = "192.168.0.6";
    String myJSON;
    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;

    private Button submit;
    private Button back;
    private Button update;
    private TextView final_msg;
    Intent intent;
    private String myId, partnerId;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "ID";
    private static final String TAG_PD = "PASSWORD";
    private static final String TAG_NAME = "NAME";
    private static final String TAG_SEX = "SEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_message);
        submit = (Button)findViewById(R.id.btnSubmit);
        back = (Button)findViewById(R.id.btnBack);
        update = (Button)findViewById(R.id.btnUpdate);
        final_msg = (TextView) findViewById(R.id.textFinal);
        personList = new ArrayList<HashMap<String, String>>();

        String message = "";
        intent = getIntent();

        int temp = intent.getIntExtra("increment", 0);
        myId = intent.getStringExtra("myId");
        partnerId = intent.getStringExtra("partnerId");

        message += "\n우리 이때 봬요!!\n\n";

        message += "날짜 : " + intent.getStringExtra("date") + "\n";
        message += "시간 : " + intent.getStringExtra("time") + "\n";
        message += "장소 : " + intent.getStringExtra(intent.getStringExtra("first")) + "\n\n";

        if (temp > 0){
            message += "카페 : " + intent.getStringExtra("cafe") +"\n";
        }
        if (temp > 1){
            message += "식사 : " + intent.getStringExtra("meal") + "\n";
        }
        if (temp > 2){
            message += "영화 : " + intent.getStringExtra("movie") + "\n";
        }
        if (temp > 3){
            message += "주점 : " + intent.getStringExtra("pub") + "\n\n";
        }

        final_msg.setText(message);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData("http://" + IP + "/khuton/Match.php?ASK_ID=" + myId + "&ACK_ID=" + partnerId + "&MESSAGE=" + final_msg.getText().toString()); //수정 필요
                Toast.makeText(getApplicationContext(),"제출 완료되었습니다.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),RandomMatchActivity.class);
                startActivity(intent);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"제출 완료되었습니다.",Toast.LENGTH_SHORT).show();

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                //String dbsex = c.getString(TAG_SEX);

                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_ID, dbid);
                //persons.put(TAG_PD, dbpd);
                //persons.put(TAG_NAME, name);
                //persons.put(TAG_SEX, dbsex);

                personList.add(persons);
                Toast.makeText(getApplicationContext(), myId + " " + partnerId + " " + final_msg.getText().toString() + " " + dbid , Toast.LENGTH_SHORT).show();
                if(dbid.equals("true"))
                {
                    Toast.makeText(getApplicationContext(), "요청 성공", Toast.LENGTH_SHORT).show();
                    break;
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "요청 실패", Toast.LENGTH_SHORT).show();
                    break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
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
