package com.nfsapp.surbhi.nfsapplication.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.activities.sender.TravellerDetails;
import com.nfsapp.surbhi.nfsapplication.beans.Traveller;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.getTraveller;

public class TravellerAdapter  extends ArrayAdapter<Traveller> implements View.OnClickListener {

    private ArrayList<Traveller> dataSet;
    Activity mContext;
    String act_name;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        TextView txtVersion;
        TextView datetV;
        Button details_btn;
        ImageView image;
    }
    public TravellerAdapter(ArrayList<Traveller> data, Activity context,String act_name) {
        super(context, R.layout.taveller_list_row, data);
        this.dataSet = data;
        this.mContext = context;
        this.act_name = act_name;

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

//        Typeface face = Typeface.createFromAsset(mContext.getAssets(),
//                "fonts/asap.ttf");
//
//        viewHolder.txtName.setTypeface(face);
//        viewHolder.txtType.setTypeface(face);
//        viewHolder.txtVersion.setTypeface(face);
//        viewHolder.datetV.setTypeface(face);
//        viewHolder.details_btn.setTypeface(face);


        viewHolder.datetV.setText(Traveller.getDate());
        viewHolder.txtName.setText(Traveller.getName());
        viewHolder.txtType.setText(Traveller.getDeparture_airport());
        viewHolder.txtVersion.setText(Traveller.getArrival_airport());

        System.out.println("====== traveller image====");
        System.out.println(Traveller.getProduct_pic());

        if (Traveller.getProduct_pic()!=null && Traveller.getProduct_pic().length()>0)
            Picasso.with(mContext).load(Traveller.getProduct_pic()).placeholder(R.drawable.profile_pic).into(viewHolder.image);
        else
        viewHolder.image.setImageResource(R.drawable.profile_pic);

        if (Traveller.getStatus()!=null && Traveller.getStatus().equalsIgnoreCase("2")) {
            act_name="traveller";
            viewHolder.details_btn.setText("Accepted");
            viewHolder.details_btn.setBackgroundResource(R.drawable.green_rect);
        }

        viewHolder.details_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("======= traveller id ========");
                System.out.println(Traveller.getId());
                getTraveller(mContext, Traveller.getId(), TravellerDetails.class,act_name);

            }
        });
        return convertView;
    }
}
