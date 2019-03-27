package com.example.dbconnection;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.dbconnection.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {
        private String IP = "192.168.0.6";
        String myJSON;

        private static final String TAG_RESULTS = "result";
        private static final String TAG_ID = "ID";
        private static final String TAG_PD = "PASSWORD";
        private static final String TAG_NAME = "NAME";
        private static final String TAG_SEX = "SEX";

        private Button signIn_;
        private Button logIn_;
        private EditText userID, userPD;
        private String id, pd;

        JSONArray peoples = null;

        ArrayList<HashMap<String, String>> personList;

        ListView list;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            //list = (ListView) findViewById(R.id.listView);
            personList = new ArrayList<HashMap<String, String>>();

            signIn_ = (Button) findViewById(R.id.btnSignIn);
            logIn_ = (Button) findViewById(R.id.btnLogIn);
            userID = (EditText) findViewById(R.id.user_ID);
            userPD = (EditText) findViewById(R.id.user_PD);

            signIn_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                    startActivity(intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_SINGLE_TOP));
                }
            });

            logIn_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id = userID.getText().toString();
                    pd = userPD.getText().toString();
                    getData("http://" + IP + "/khuton/Login.php?ID=" + id + "&PW=" + pd); //수정 필요
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
                    String dbpd = c.getString(TAG_PD);
                    //String name = c.getString(TAG_NAME);
                    String dbsex = c.getString(TAG_SEX);

                    HashMap<String, String> persons = new HashMap<String, String>();

                    persons.put(TAG_ID, dbid);
                    persons.put(TAG_PD, dbpd);
                    //persons.put(TAG_NAME, name);
                    persons.put(TAG_SEX, dbsex);

                    personList.add(persons);
                    if(id.equals(dbid) && pd.equals(dbpd))
                    {
                        Intent intent = new Intent(getApplicationContext(),RandomMatchActivity.class);
                        intent.putExtra("ID", id);
                        intent.putExtra("SEX", dbsex);
                        startActivity(intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_SINGLE_TOP));
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