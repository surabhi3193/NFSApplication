package com.nfsapp.surbhi.nfsapplication.activities.traveller;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfsapp.surbhi.nfsapplication.R;

public class BookItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_item);

        ImageView back_btn = findViewById(R.id.back_btn);
        TextView header_text = findViewById(R.id.header_text);

        header_text.setText("Traveller Detail");
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        Button book_btn = findViewById(R.id.book_btn);
        book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               warningBooking();
            }
        });

    }

    private void warningBooking() {
            AlertDialog.Builder ab = new AlertDialog.Builder
                    (BookItemActivity.this, R.style.MyAlertDialogStyle1);
            ab.setTitle("Book").setIcon(R.drawable.book_right);
            ab.setMessage("Are you sure ?");
            ab.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                  startActivity(new Intent(BookItemActivity.this,TravellerMainActivity.class));
                  finish();
                }
            });

            ab.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            ab.show();


    }
}
