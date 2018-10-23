package com.cortado.geolocator;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Activity2 extends AppCompatActivity{
private FrameLayout fragmentContainer;
    ArrayList<String> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_2);
        fragmentContainer =(FrameLayout) findViewById (R.id.fragmnet_conatiner);

        Bundle extras = getIntent ().getExtras ();
        if (extras != null) {
            data = getIntent ().getStringArrayListExtra ("data");
            TextView textView = (TextView) findViewById (R.id.history);
            int arraySize = data.size ();
            int ent = 1;
            String temp1 = "Longitude: ";
            String temp2 = "Latitude: ";
            for (int i = 0; i < arraySize; i++) {
                textView.append ("Entry: " + String.valueOf (ent) + " at Time= " + data.get (i));
                textView.append ("\n");
                textView.append (temp1 + data.get (i + 1));
                textView.append ("\n");
                textView.append (temp2 + data.get (i + 2));
                textView.append ("\n");
                ent++;
                i = i + 2;
            }
        }
        Button onMaps = (Button) findViewById (R.id.button);
        onMaps.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
            openFragment(data);
            }
        });
    }
    private void openFragment(ArrayList<String> data) {
        BlankFragment fragment = BlankFragment.newInstance(data);
        FragmentManager fragmentManager= getSupportFragmentManager ();
        FragmentTransaction transaction = fragmentManager.beginTransaction ();
        transaction.setCustomAnimations (R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.enter_from_right);
        transaction.addToBackStack (null);
        transaction.add(R.id.fragmnet_conatiner, fragment, "BLANK_FRAGMENT").commit ();
    }
}