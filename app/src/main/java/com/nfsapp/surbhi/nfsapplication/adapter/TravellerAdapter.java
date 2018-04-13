package com.nfsapp.surbhi.nfsapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.activities.ItemDetails;
import com.nfsapp.surbhi.nfsapplication.activities.sender.TravellerDetails;
import com.nfsapp.surbhi.nfsapplication.beans.Traveller;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.getPostDetails;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.getTraveller;

public class TravellerAdapter  extends ArrayAdapter<Traveller> implements View.OnClickListener {

    private ArrayList<Traveller> dataSet;
    Activity mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        TextView txtVersion;
        TextView datetV;
        Button details_btn;
        ImageView image;
    }
    public TravellerAdapter(ArrayList<Traveller> data, Activity context) {
        super(context, R.layout.taveller_list_row, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {


    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Traveller Traveller = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        TravellerAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new TravellerAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.taveller_list_row, parent, false);
            viewHolder.txtName =convertView.findViewById(R.id.title);
            viewHolder.datetV =convertView.findViewById(R.id.datetV);
            viewHolder.txtType =convertView.findViewById(R.id.depart);
            viewHolder.txtVersion =convertView.findViewById(R.id.arrival);
            viewHolder.details_btn =convertView.findViewById(R.id.details_btn);
            viewHolder.image =convertView.findViewById(R.id.image);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TravellerAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        Typeface face = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/estre.ttf");

        viewHolder.txtName.setTypeface(face);
        viewHolder.txtType.setTypeface(face);
        viewHolder.txtVersion.setTypeface(face);
        viewHolder.datetV.setTypeface(face);
        viewHolder.details_btn.setTypeface(face);


        viewHolder.txtName.setText(Traveller.getName());
        viewHolder.txtType.setText(Traveller.getDeparture_airport());
        viewHolder.txtVersion.setText(Traveller.getArrival_airport());

        if (Traveller.getProduct_pic()!=null && Traveller.getProduct_pic().length()>0)
            Picasso.with(mContext).load(Traveller.getProduct_pic()).placeholder(R.drawable.profile_pic).into(viewHolder.image);
        viewHolder.image.setImageResource(R.drawable.profile_pic);

        viewHolder.details_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("======= traveller id ========");
                System.out.println(Traveller.getId());
                getTraveller(mContext, Traveller.getId(), TravellerDetails.class);
                //getTraveller
//                mContext.startActivity(new Intent(mContext,TravellerDetails.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });
        return convertView;
    }
}
