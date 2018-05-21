package com.nfsapp.surbhi.nfsapplication.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfsapp.surbhi.nfsapplication.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatAdapter extends BaseAdapter {
    private JSONArray jArray;
    private Context context;
    LayoutInflater inflater;


    //    public ChatAdapter(JSONArray jArray, Activity context) {
//        this.context = context;
//        this.jArray = jArray;
//    }
    public ChatAdapter(JSONArray jArray, Context context) {
        this.context = context;
        this.jArray = jArray;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return jArray.length();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
       @SuppressLint("ViewHolder") View convertView = inflater.inflate(R.layout.msg_row, parent, false);

        final ViewHolder holder = new ViewHolder();
        JSONObject responseobj = new JSONObject();

        holder.oppoIv = convertView.findViewById(R.id.oppnIV);
        holder.userIv = convertView.findViewById(R.id.userIV);
        holder.oppoTV = convertView.findViewById(R.id.oppnTV);
        holder.userTV = convertView.findViewById(R.id.mymsgTV);
        holder.oppnLay = convertView.findViewById(R.id.oppnLay);
        holder.userLay = convertView.findViewById(R.id.myLay);

        convertView.setTag(holder);
//

        try {
            responseobj = jArray.getJSONObject(position);

            String chat_type = responseobj.getString("chat_type");

            String chat_msg = responseobj.getString("chat_msg");
//            String my_pic = responseobj.getString("sender_pic");
            String oppon_pic = responseobj.getString("sender_pic");
            if (chat_type.equalsIgnoreCase("1"))
            {
                //me
                holder.userLay.setVisibility(View.VISIBLE);
                holder.oppnLay.setVisibility(View.GONE);
                holder.userTV.setText(chat_msg);
                Picasso.with(context).load(oppon_pic).placeholder(R.drawable.profile_pic).into(holder.userIv);
            }

            else if (chat_type.equalsIgnoreCase("2"))
            {
                holder.userLay.setVisibility(View.GONE);
                holder.oppnLay.setVisibility(View.VISIBLE);
                holder.oppoTV.setText(chat_msg);
                Picasso.with(context).load(oppon_pic).placeholder(R.drawable.profile_pic).into(holder.oppoIv);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }


    class ViewHolder {
        TextView oppoTV,userTV;
        ImageView oppoIv,userIv;
        RelativeLayout oppnLay,userLay;


    }

}

