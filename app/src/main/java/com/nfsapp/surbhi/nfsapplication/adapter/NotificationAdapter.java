package com.nfsapp.surbhi.nfsapplication.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.beans.Notification;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends ArrayAdapter<Notification> {

    Activity mContext;
    private ArrayList<Notification> dataSet;
    private int lastPosition = -1;

    public NotificationAdapter(ArrayList<Notification> data, Activity context) {
        super(context, R.layout.notification_list_item, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Notification notification = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.notification_list_item, parent, false);
            viewHolder.sender_nameTV = (TextView) convertView.findViewById(R.id.nameTv);
            viewHolder.msgTV = (TextView) convertView.findViewById(R.id.msgTV);
            viewHolder.dateTV = (TextView) convertView.findViewById(R.id.dateTV);
            viewHolder.image =convertView.findViewById(R.id.image);
            viewHolder.type_img =convertView.findViewById(R.id.type_img);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        lastPosition = position;
        viewHolder.sender_nameTV.setText(notification.getName());
        viewHolder.msgTV.setText(notification.getMsg());
        viewHolder.dateTV.setText(notification.getDate());

        if (notification.getPic()!=null && notification.getPic().length()>0)
        Picasso.with(mContext).load(notification.getPic()).placeholder(R.drawable.profile_pic).into(viewHolder.image);
        
        int not_type = notification.getType();

        switch (not_type)
        {
            case 1:
                viewHolder.type_img.setImageResource(R.drawable.add_notify);
                break;
            case 2:
                viewHolder.type_img.setImageResource(R.drawable.request_notify);
                break;

            case 3:
                viewHolder.type_img.setImageResource(R.drawable.cancel_notify);
                break;

            case 4:
                viewHolder.type_img.setImageResource(R.drawable.accept_notify);
                break;

                default:
                    viewHolder.type_img.setImageResource(R.drawable.welcome_notify);
                    break;


        }
        

        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView sender_nameTV;
        TextView msgTV;
        TextView dateTV;
        ImageView image,type_img;
    }
}