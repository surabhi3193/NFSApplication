package com.nfsapp.surbhi.nfsapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.activities.ItemDetails;
import com.nfsapp.surbhi.nfsapplication.activities.traveller.BookItemActivity;
import com.nfsapp.surbhi.nfsapplication.activities.traveller.ParcelPackageDetail;
import com.nfsapp.surbhi.nfsapplication.beans.Traveller;

import java.util.ArrayList;

public class TravelerMainAdapter extends ArrayAdapter<Traveller> implements View.OnClickListener{

    private ArrayList<Traveller> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        TextView txtVersion,view_btn;
        Button details_btn;

    }

    public TravelerMainAdapter(ArrayList<Traveller> data, Context context) {
        super(context, R.layout.item_traveller, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {


    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Traveller Traveller = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_traveller, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.title);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.depart);
            viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.arrival);
            viewHolder.view_btn = (TextView) convertView.findViewById(R.id.view_btn);
            viewHolder.details_btn = convertView.findViewById(R.id.details_btn);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(animation);
        lastPosition = position;
       Typeface face = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/estre.ttf");

        viewHolder.txtName.setTypeface(face);
        viewHolder.txtType.setTypeface(face);
        viewHolder.txtVersion.setTypeface(face);
        viewHolder.view_btn.setTypeface(face);

        viewHolder.txtName.setText(Traveller.getName());
        viewHolder.txtType.setText(Traveller.getDeparture_airport());

        viewHolder.txtVersion.setText(Traveller.getArrival_airport());



        viewHolder.details_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext,BookItemActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        viewHolder.view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext,ParcelPackageDetail.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        return convertView;
    }
}