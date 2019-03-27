package com.example.dbconnection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MailboxAdapter extends BaseAdapter {

    private Context ctx;
    private ArrayList<MailboxMessage> data;


    public MailboxAdapter(Context ctx, ArrayList<MailboxMessage> data) {
        this.ctx = ctx;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(ctx);
            convertView = inflater.inflate(R.layout.activity_mailbox_message,parent,false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageId);
        imageView.setImageResource(data.get(position).getPortrait());

        TextView name = (TextView)convertView.findViewById(R.id.sender_name);
        name.setText(data.get(position).getName() + " 님이 당신을 마음에 들어합니다.");

        return convertView;
    }
}
