package com.nfsapp.surbhi.nfsapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.nfsapp.surbhi.nfsapplication.beans.Traveller;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.getPostDetails;

public class ItemListAdapter extends ArrayAdapter<Traveller> {

    Activity mContext;
    private ArrayList<Traveller> dataSet;
    private int lastPosition = -1;

    public ItemListAdapter(ArrayList<Traveller> data, Activity context) {
        super(context, R.layout.item_list_row, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Traveller traveller = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_list_row, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.title);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.depart);
            viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.arrival);
            viewHolder.head_depart = (TextView) convertView.findViewById(R.id.head_depart);
            viewHolder.head_arrival = (TextView) convertView.findViewById(R.id.head_arrival);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.details_btn = convertView.findViewById(R.id.details_btn);
            viewHolder.product_pic = convertView.findViewById(R.id.product_pic);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        lastPosition = position;
        Typeface face = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/estre.ttf");

        viewHolder.txtName.setTypeface(face);
        viewHolder.txtType.setTypeface(face);
        viewHolder.txtVersion.setTypeface(face);
        viewHolder.date.setTypeface(face);
        viewHolder.head_depart.setTypeface(face);
        viewHolder.head_arrival.setTypeface(face);

        viewHolder.txtName.setText(traveller.getName());
        viewHolder.txtType.setText(traveller.getDeparture_airport());
        viewHolder.txtVersion.setText(traveller.getArrival_airport());
        Picasso.with(mContext).load(traveller.getProduct_pic()).placeholder(R.drawable.item).into(viewHolder.product_pic);
        viewHolder.details_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                  String  user_id = getData(mContext.getApplicationContext(), "user_id", "");

                    getPostDetails(mContext, user_id, traveller.getId(), ItemDetails.class);
            }
        });
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        TextView head_depart;
        TextView head_arrival;
        TextView txtVersion, date;
        ImageView product_pic;
        Button details_btn;

    }
}