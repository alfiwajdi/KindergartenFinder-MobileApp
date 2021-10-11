package com.example.pencaritadika;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Stack;

public class descfragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    String name, address, phone;

    DatabaseReference databaseRating;

    public descfragment() {

    }

    public descfragment(String name, String address, String phone) {
        this.name=name;
        this.address=address;
        this.phone=phone;
        //this.latlng=latlng;
    }

    public static com.example.pencaritadika.descfragment newInstance(String param1, String param2) {
        com.example.pencaritadika.descfragment fragment = new com.example.pencaritadika.descfragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Create database reference
        databaseRating = FirebaseDatabase.getInstance().getReference("kindergarten/"+ name + "/rating");

        View view=inflater.inflate(R.layout.fragment_descfragment, container, false);

        TextView nameholder=view.findViewById(R.id.nameholder);
        TextView addressholder=view.findViewById(R.id.addressholder);
        TextView phoneholder=view.findViewById(R.id.phoneholder);
        //TextView latlngholder=view.findViewById(R.id.latlngholder);
        TextView avgRatingholder=view.findViewById(R.id.avgRating);


        nameholder.setText(name);
        addressholder.setText(address);
        phoneholder.setText(phone);
        //latlngholder.setText(latlng);

        databaseRating.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DecimalFormat df2 = new DecimalFormat("#.##");
                double totRating = 0, avgRating = 0;
                int count = (int)dataSnapshot.getChildrenCount();
                //iterating through all the nodes
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()) {
                    //getting total rating
                    //long temp = (long) postSnapshot.getValue();
                    totRating = totRating += (long) postSnapshot.getValue();
                }
                //get average rating
                if(totRating>0)
                    avgRating = totRating/count;
                String avgRatingText = df2.format(avgRating);
                avgRatingholder.setText(avgRatingText);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        //phone button
        Button call = view.findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callKindergarten(v, phone.toString());
            }
        });

        //navigation button
        Button nav = view.findViewById(R.id.nav);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navKindergarten(v, name);
            }
        });

        //Rating
        RatingBar ratingBars = view.findViewById(R.id.ratingBar);
        ratingBars.setRating(0);   //put your value here like ratingBar.setRating(averageRating);

        // Set ChangeListener to Rating Bar
        ratingBars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                addRating(rating);
                Toast.makeText(getActivity(),"Ratings  : " +   String.valueOf(rating), Toast.LENGTH_LONG).show();

            }
        });

        return  view;
    }

    //navigate kindergarten
    public void navKindergarten(View view, String name){
        // Maps to look for Kindergarten by using name:
        String url = "https://www.google.com/maps/search/?api=1&query="+name;
        Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
        startActivity( intent );

    }

    //call kindergarten
    public void callKindergarten(View view, String phone){
        try
        {
            if(Build.VERSION.SDK_INT > 22)
            {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone));
                startActivity(callIntent);
            }
            else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone));
                startActivity(callIntent);
            }
        }
        catch (Exception ex)
        {ex.printStackTrace(); }
    }

    public void onBackPressed()
    {
        AppCompatActivity activity=(AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new com.example.pencaritadika.recfragment()).addToBackStack(null).commit();
    }

    private void addRating(float rating) {
        //get artistname and convert to string from editextname
        float floatRating = rating;
        //check if the name is not empty

        if (floatRating > 0) {
            //if exist push data to firebase database
            //store inside id in database
            //every time data stored the id will be unique
            String id = databaseRating.push().getKey();
            //store artist inside unique id
            databaseRating.child(id).setValue(floatRating);
            Toast.makeText(getActivity(), "Rating added", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getActivity(), "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }


}