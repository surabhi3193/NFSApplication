package com.nfsapp.surbhi.nfsapplication.activities.traveller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.other.GifImageView;

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
          final Dialog dialog = new Dialog(BookItemActivity.this,R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(R.layout.alertdialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        TextView ok_btn = dialog.findViewById(R.id.ok_btn);
        TextView cancel_btn = dialog.findViewById(R.id.cancel_btn);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(BookItemActivity.this,TravellerMainActivity.class));
                finish();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
