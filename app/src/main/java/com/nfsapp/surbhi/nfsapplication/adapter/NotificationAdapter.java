package com.nfsapp.surbhi.nfsapplication.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.activities.BookingListActivity;
import com.nfsapp.surbhi.nfsapplication.activities.ItemDetails;
import com.nfsapp.surbhi.nfsapplication.activities.sender.TravellerDetails;
import com.nfsapp.surbhi.nfsapplication.beans.Notification;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.saveData;

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
    public View getView(int position,  View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Notification notification = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.notification_list_item, parent, false);
            viewHolder.mainLay =convertView.findViewById(R.id.mainLay);
            viewHolder.sender_nameTV =convertView.findViewById(R.id.nameTv);
            viewHolder.msgTV =convertView.findViewById(R.id.msgTV);
            viewHolder.dateTV =convertView.findViewById(R.id.dateTV);
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
        
        final int not_type = notification.getType();

        viewHolder.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("----------> clicked notification-------->" + not_type);
                switch (not_type)
                {

                    case 1:
                        callIntent( ItemDetails.class,"", String.valueOf(notification.getType()),notification.getId(),
                                notification.getProduct_id());
                        break;
                    case 2:
                        saveData(mContext,"product_id",notification.getProduct_id());

                       Intent resultIntent = new Intent(mContext, TravellerDetails.class);
                        resultIntent.putExtra("notification_message", "");
                        resultIntent.putExtra("act_name", "notification");
                        resultIntent.putExtra("trevaller_id", notification.getId());
                        resultIntent.putExtra("product_id", notification.getProduct_id());
                        mContext.startActivity(resultIntent);
                        break;

                    case 3:
                        callIntent( ItemDetails.class,"", String.valueOf(notification.getType()),notification.getId(),
                                notification.getProduct_id());
                        break;

                    case 4:
                        saveData(mContext,"product_id",notification.getProduct_id());
                        callIntent( ItemDetails.class,"", String.valueOf(notification.getType())
                                ,notification.getId(),notification.getProduct_id());
                        break;

                    case 5:
                        saveData(mContext,"product_id",notification.getProduct_id());
                        callIntent( BookingListActivity.class,"", String.valueOf(notification.getType()),
                                notification.getId(),notification.getProduct_id());
                        break;

                }


            }
        });


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

                case 5:
                    viewHolder.type_img.setImageResource(R.drawable.pickup_truck);
                    break;

                default:
                    viewHolder.type_img.setImageResource(R.drawable.welcome_notify);
                    break;


        }
        

        return convertView;
    }

    public void callIntent(Class nextAct,String act_name,String type,String id,String product_id)
    {
       Intent resultIntent = new Intent(mContext, nextAct);
        resultIntent.putExtra("notification_message", act_name);
        resultIntent.putExtra("type", type);
        resultIntent.putExtra("post_id", id);
        resultIntent.putExtra("product_id", product_id);
        mContext.startActivity(resultIntent);
    }
    // View lookup cache
    private static class ViewHolder {
        TextView sender_nameTV;
        TextView msgTV;
        TextView dateTV;
        ImageView image,type_img;
        RelativeLayout mainLay;
    }
}