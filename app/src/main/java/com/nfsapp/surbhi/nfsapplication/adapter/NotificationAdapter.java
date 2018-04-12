package com.nfsapp.surbhi.nfsapplication.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.activities.ItemDetails;
import com.nfsapp.surbhi.nfsapplication.beans.Notification;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.getPostDetails;

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
        final Notification Notification = getItem(position);
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

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        lastPosition = position;
        Typeface face = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/estre.ttf");

        viewHolder.sender_nameTV.setTypeface(face);
        viewHolder.msgTV.setTypeface(face);
        viewHolder.dateTV.setTypeface(face);

        viewHolder.sender_nameTV.setText(Notification.getName());
        viewHolder.msgTV.setText(Notification.getMsg());
        viewHolder.dateTV.setText(Notification.getDate());

        if (Notification.getPic()!=null && Notification.getPic().length()>0)
        Picasso.with(mContext).load(Notification.getPic()).placeholder(R.drawable.profile_pic).into(viewHolder.image);

        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView sender_nameTV;
        TextView msgTV;
        TextView dateTV;
        ImageView image;
    }
}