package com.nfsapp.surbhi.nfsapplication.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.anton46.stepsview.StepsView;
import com.nfsapp.surbhi.nfsapplication.R;

public class TrackingActivity extends AppCompatActivity {
    private String status = "airport";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        StepsView mStepsView = findViewById(R.id.stepsView);
        TextView header_text =findViewById(R.id.header_text);
        header_text.setText("Tracking Detail");

        String[] labels ={"Picked Up","On Road","On Airport"};
        mStepsView.setLabels(labels).setBarColorIndicator(getResources().getColor(R.color.gray_light));
        mStepsView.setLabelColorIndicator(getResources().getColor(R.color.colorPrimary));
        if (status.equalsIgnoreCase("pickedup"))
        {
            mStepsView.setProgressColorIndicator(getResources().getColor(R.color.Crimson))
                    .setCompletedPosition(0)
                    .drawView();
        }

       else if (status.equalsIgnoreCase("on road"))
        {
            mStepsView.setProgressColorIndicator(getResources().getColor(R.color.MediumPurple))
                    .setCompletedPosition(1)
                    .drawView();
        }

        else if (status.equalsIgnoreCase("airport"))
        {
            mStepsView.setProgressColorIndicator(getResources().getColor(R.color.green))
                    .setCompletedPosition(2)
                    .drawView();
        }
    }


}
