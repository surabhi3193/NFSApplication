package com.nfsapp.surbhi.nfsapplication.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nfsapp.surbhi.nfsapplication.R;
import com.nfsapp.surbhi.nfsapplication.activities.FullImageActivity;
import com.nfsapp.surbhi.nfsapplication.activities.sender.SenderMainActivity;
import com.nfsapp.surbhi.nfsapplication.fragment.AddItemFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SliderAdapter  extends PagerAdapter {

    private ArrayList<Uri> images;

    private int size=0;
    private LayoutInflater inflater;
    private Activity context;
    private boolean clickable=false;

    public SliderAdapter(Activity context, ArrayList<Uri> images) {
        this.context = context;
        this.images=images;
        this.size=images.size();
        inflater = LayoutInflater.from(context);
        clickable=true;
    }

    public SliderAdapter(Activity context, ArrayList<Uri> images,int size) {
        this.context = context;
        this.images=images;
        this.size=size;
        inflater = LayoutInflater.from(context);
        clickable=false;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {

        return size;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);

        Picasso.with(context).load(images.get(position)).placeholder(R.drawable.no_pic).into(myImage);

        if (clickable) {
            myImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    context.startActivity(new Intent(context, FullImageActivity.class)
                            .putParcelableArrayListExtra("imageArray", images)
                    );

                }
            });
        }
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}