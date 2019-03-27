package com.example.dbconnection;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MailboxActivity extends AppCompatActivity {

    ListView messages;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mailbox);

        messages = (ListView)findViewById(R.id.messages);

        final ArrayList<MailboxMessage> adapter = new ArrayList<>();
        MailboxMessage userA = new MailboxMessage(R.drawable.im3, "아이린");
        MailboxMessage userB = new MailboxMessage(R.drawable.im1, "손나은");
        MailboxMessage userC = new MailboxMessage(R.drawable.im5, "정연");

        adapter.add(userA);
        adapter.add(userB);
        adapter.add(userC);

        MailboxAdapter mailboxAdapter = new MailboxAdapter(getApplication(),adapter);
        messages.setAdapter(mailboxAdapter);

        intent = new Intent(getApplicationContext(), MailActivity.class);
        messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //int image = (int) parent.get
                //intent.putExtra("image",adapter.get(i).getPortrait());
                //intent.putExtra("name",adapter.get(i).getName());
                //startActivity(intent);
            }
        });
    }
}
