package com.nfsapp.surbhi.nfsapplication.activities.sender;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfsapp.surbhi.nfsapplication.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TravellerDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveller_details);
        ImageView image = findViewById(R.id.back_btn);

        TextView header_text = findViewById(R.id.header_text);

        header_text.setText("Traveller Detail");
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }

        });

        ImageView ticketIV =findViewById(R.id.ticketIV);
        TextView nameTV =findViewById(R.id.nameTV);
        TextView pickupET =findViewById(R.id.pickupET);
        TextView pickypdateEt =findViewById(R.id.pickypdateEt);
        TextView destTV =findViewById(R.id.destTV);
        TextView destDateTV =findViewById(R.id.destDateTV);
        TextView flightTV =findViewById(R.id.flightTV);


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
                String trevaller_ticket_pic = obj.getString("trevaller_ticket_pic");

                nameTV.setText(trevaller_name);
                pickupET.setText(trevaller_from);
                pickypdateEt.setText(departure_date);
                destTV.setText(trevaller_to);
                destDateTV.setText(arrival_date);
                flightTV.setText(trevaller_flight_no);

                Picasso.with(getApplicationContext()).load(trevaller_ticket_pic).placeholder(R.drawable.no_pic).into(ticketIV);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
