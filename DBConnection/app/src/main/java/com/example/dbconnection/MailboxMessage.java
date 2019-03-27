package com.example.dbconnection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MailboxMessage extends AppCompatActivity {

    private int user_portrait;
    private String user_name;

    public int getPortrait() {return user_portrait;}
    public String getName() {return user_name;}

    public MailboxMessage(int user_portrait, String user_name){
        this.user_portrait = user_portrait;
        this.user_name = user_name;
    }
}
