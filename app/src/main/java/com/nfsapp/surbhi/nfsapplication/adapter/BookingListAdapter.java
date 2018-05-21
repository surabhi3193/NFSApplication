package com.nfsapp.surbhi.nfsapplication.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.activities.TrackingActivity;
import com.nfsapp.surbhi.nfsapplication.activities.traveller.PackageAfterPickup;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BookingListAdapter extends BaseAdapter {
    private JSONArray jArray;
    private Activity context;

    public BookingListAdapter(JSONArray jArray, Activity context) {
        this.context = context;
        this.jArray = jArray;
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
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("ViewHolder") View convertView = inflater.inflate(R.layout.item_list_row, null, true);
        final ViewHolder holder = new ViewHolder();
        JSONObject responseobj = new JSONObject();

        holder.txtName = convertView.findViewById(R.id.title);
        holder.txtType = convertView.findViewById(R.id.depart);
        holder.txtVersion = convertView.findViewById(R.id.arrival);
        holder.head_depart = convertView.findViewById(R.id.head_depart);
        holder.head_arrival = convertView.findViewById(R.id.head_arrival);
        holder.date = convertView.findViewById(R.id.date);
        holder.details_btn = convertView.findViewById(R.id.details_btn);
        holder.product_pic = convertView.findViewById(R.id.product_pic);


        convertView.setTag(holder);
//

        try {
            responseobj = jArray.getJSONObject(position);

       String product_id = responseobj.getString("product_id");
            String product_name = responseobj.getString("product_name");
            String trevaller_to = responseobj.getString("trevaller_to");
            String trevaller_from = responseobj.getString("trevaller_from");
            String product_pic = responseobj.getString("product_pic");
            String type = responseobj.getString("type");

            holder.txtName.setText(product_id+"- "+product_name);
            holder.txtType.setText(trevaller_from);
            holder.txtVersion.setText(trevaller_to);
            Picasso.with(context).load(product_pic).placeholder(R.drawable.no_pic).into(holder.product_pic);

            if (type.equalsIgnoreCase("sender")) {
                holder.details_btn.setText("Track package");
                holder.details_btn.setBackgroundResource(R.drawable.green_rect);
                final JSONObject finalResponseobj = responseobj;
                holder.details_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        context.startActivity(new Intent(context, TrackingActivity.class)
                                .putExtra("responseObj", finalResponseobj.toString())
                        );
                    }
                });
            } else if (type.equalsIgnoreCase("traveller")) {
                holder.details_btn.setText("View package");
                holder.details_btn.setBackgroundResource(R.drawable.blue_rect);
                final JSONObject finalResponseobj = responseobj;

                holder.details_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        context.startActivity(new Intent(context, PackageAfterPickup.class)
                                .putExtra("responseObj", finalResponseobj.toString())
                        );
                    }
                });
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;
    }


    class ViewHolder {
        TextView txtName;
        TextView txtType;
        TextView head_depart;
        TextView head_arrival;
        TextView txtVersion, date;
        ImageView product_pic;
        Button details_btn;


    }

}