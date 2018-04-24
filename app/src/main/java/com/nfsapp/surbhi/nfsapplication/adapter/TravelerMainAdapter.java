package com.nfsapp.surbhi.nfsapplication.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.activities.traveller.BookItemActivity;
import com.nfsapp.surbhi.nfsapplication.activities.traveller.ParcelPackageDetail;
import com.nfsapp.surbhi.nfsapplication.beans.Traveller;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.nfsapp.surbhi.nfsapplication.other.MySharedPref.getData;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.BASE_IMAGE_URL;
import static com.nfsapp.surbhi.nfsapplication.other.NetworkClass.getPostDetails;

public class TravelerMainAdapter extends ArrayAdapter<Traveller> implements View.OnClickListener{

    private ArrayList<Traveller> dataSet;
    Activity mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtDeparture;
        TextView tctArrival,view_btn;
        Button details_btn;
        TextView head_depart;
        TextView head_arrival;
        TextView head_amount,date,distanceTV,amountTv;
        ImageView product_pic;

    }

    public TravelerMainAdapter(ArrayList<Traveller> data, Activity context) {
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
        final Traveller Traveller = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_traveller, parent, false);
            viewHolder.txtName =convertView.findViewById(R.id.title);
            viewHolder.txtDeparture =convertView.findViewById(R.id.depart);
            viewHolder.tctArrival =convertView.findViewById(R.id.arrival);
            viewHolder.view_btn =convertView.findViewById(R.id.view_btn);
            viewHolder.head_depart =convertView.findViewById(R.id.head_depart);
            viewHolder.head_arrival =convertView.findViewById(R.id.head_arrival);
            viewHolder.head_amount =convertView.findViewById(R.id.head_amount);
            viewHolder.distanceTV =convertView.findViewById(R.id.distanceTV);
            viewHolder.date =convertView.findViewById(R.id.dateTV);
            viewHolder.amountTv =convertView.findViewById(R.id.amountTv);

            viewHolder.product_pic = convertView.findViewById(R.id.productIV);
            viewHolder.details_btn = convertView.findViewById(R.id.details_btn);
            convertView.setTag(viewHolder);
        }

        else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;
//       Typeface face = Typeface.createFromAsset(mContext.getAssets(),
//                "fonts/asap.ttf");
//
//        viewHolder.txtName.setTypeface(face);
//        viewHolder.txtDeparture.setTypeface(face);
//        viewHolder.tctArrival.setTypeface(face);
//        viewHolder.view_btn.setTypeface(face);
//        viewHolder.date.setTypeface(face);
//        viewHolder.head_depart.setTypeface(face);
//        viewHolder.head_arrival.setTypeface(face);
//        viewHolder.head_amount.setTypeface(face);
//        viewHolder.distanceTV.setTypeface(face);
//        viewHolder.amountTv.setTypeface(face);

        viewHolder.txtName.setText(Traveller.getName());
        viewHolder.txtDeparture.setText(Traveller.getDeparture_airport());
        viewHolder.tctArrival.setText(Traveller.getArrival_airport());
        viewHolder.date.setText(Traveller.getDate());
        viewHolder.distanceTV.setText(Traveller.getDistance());
        viewHolder.amountTv.setText(Traveller.getCost());

        System.out.println("=========== image in traveller activity=====");
        System.out.println(Traveller.getProduct_pic());
        if (Traveller.getProduct_pic()!=null && Traveller.getProduct_pic().length()>0&& !Traveller.getProduct_pic().equals(BASE_IMAGE_URL))
        {

            Picasso.with(mContext).load(Traveller.getProduct_pic()).placeholder(R.drawable.no_pic).into(viewHolder.product_pic);
        }
        else
        viewHolder.product_pic.setImageResource(R.drawable.no_pic);


        String trevaller_status = Traveller.getStatus();
        System.out.println("====== traveller status====");
        System.out.println(trevaller_status);
        if (trevaller_status.equalsIgnoreCase("1")) {
            viewHolder.details_btn.setText("Applied");
            viewHolder.details_btn.setBackgroundResource(R.drawable.green_rect);
            viewHolder.view_btn.setVisibility(View.GONE);
        }
        else {
            viewHolder.details_btn.setBackgroundResource(R.drawable.btn);
            viewHolder.details_btn.setText("Book Now");

            viewHolder.details_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mContext.startActivity(new Intent(mContext,BookItemActivity.class)
                            .putExtra("sender_id",Traveller.getSender_id())
                            .putExtra("post_id",Traveller.getId())
                            .putExtra("pickup",Traveller.getDeparture_airport())
                            .putExtra("destination",Traveller.getArrival_airport())
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
            viewHolder.view_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String  user_id = getData(mContext.getApplicationContext(), "user_id", "");
                    getPostDetails(mContext, user_id, Traveller.getId(), ParcelPackageDetail.class,"TravellerMain");
                }
            });
        }

        return convertView;

    }
}