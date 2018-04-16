package com.nfsapp.surbhi.nfsapplication.activities.sender;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfsapp.surbhi.nfsapplication.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TravellerDetails extends AppCompatActivity {

    String traveller_tckt = "";
     RelativeLayout imageLay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveller_details);
        ImageView ticketIV =findViewById(R.id.ticketIV);
        ImageView back_btn = findViewById(R.id.back_btn);
        ImageView image_back = findViewById(R.id.image_back);
        final ImageView image_full = findViewById(R.id.image_full);

          imageLay = findViewById(R.id.imageLay);
        TextView header_text = findViewById(R.id.header_text);
        TextView nameTV =findViewById(R.id.nameTV);
        TextView pickupET =findViewById(R.id.pickupET);
        TextView pickypdateEt =findViewById(R.id.pickypdateEt);
        TextView destTV =findViewById(R.id.destTV);
        TextView destDateTV =findViewById(R.id.destDateTV);
        TextView flightTV =findViewById(R.id.flightTV);

        header_text.setText("Traveller Detail");



        Bundle bundle = getIntent().getExtras();

        if (bundle!=null)
        {
            try {
                String productDetails = bundle.getString("productDetails");

                JSONObject obj = new JSONObject(productDetails);

                String trevaller_name = obj.getString("trevaller_name");
                String trevaller_from = obj.getString("trevaller_from");
                String departure_date = obj.getString("departure_date");
                String trevaller_to = obj.getString("trevaller_to");
                String arrival_date = obj.getString("arrival_date");
                String trevaller_flight_no = obj.getString("trevaller_flight_no");
                traveller_tckt = obj.getString("trevaller_ticket_pic");
                nameTV.setText(trevaller_name);
                pickupET.setText(trevaller_from);
                pickypdateEt.setText(departure_date);
                destTV.setText(trevaller_to);
                destDateTV.setText(arrival_date);
                flightTV.setText(trevaller_flight_no);

                if (traveller_tckt.length()>0)
                    Picasso.with(getApplicationContext()).load(traveller_tckt).placeholder(R.drawable.no_pic).into(ticketIV);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }

        });




        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLay.setVisibility(View.GONE);
            }
        });

        ticketIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLay.setVisibility(View.VISIBLE);
                if (traveller_tckt.length()>0)
                Picasso.with(getApplicationContext()).load(traveller_tckt).placeholder(R.drawable.no_pic).into(image_full);

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (imageLay.getVisibility()==View.VISIBLE)
            imageLay.setVisibility(View.GONE);

        else
            finish();
    }
}
