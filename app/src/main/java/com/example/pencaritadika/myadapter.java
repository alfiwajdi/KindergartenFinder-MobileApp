package com.example.pencaritadika;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.json.JSONObject;

import java.text.DecimalFormat;

public class myadapter extends FirebaseRecyclerAdapter<com.example.pencaritadika.model, com.example.pencaritadika.myadapter.myviewholder>
{
    String currentLatLng;
    public myadapter(@NonNull FirebaseRecyclerOptions<model> options, String currentLatLng) {
        super(options);
        this.currentLatLng = currentLatLng;
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull final com.example.pencaritadika.model model) {
            String distance = getDistance(currentLatLng, model.getLatlng());

            holder.nametext.setText(model.getName());
            holder.addresstext.setText(model.getAddress());
            holder.phonetext.setText(model.getPhone());
           // holder.latlngtext.setText(model.getLatlng());
            holder.distancetext.setText(distance);

        holder.nametext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity=(AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new com.example.pencaritadika.descfragment(model.getName(),model.getAddress(),model.getPhone())).addToBackStack(null).commit();
            }
        });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowdesign,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {
        TextView nametext,addresstext,phonetext, distancetext;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            nametext=itemView.findViewById(R.id.nametext);
            addresstext=itemView.findViewById(R.id.addresstext);
            phonetext=itemView.findViewById(R.id.phonetext);
        //    latlngtext=itemView.findViewById(R.id.latlngtext);
            distancetext=itemView.findViewById(R.id.distancetext);
        }
    }

    public String getDistance(String current, String destination) {
//        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=37.7680296,-122.4375126" +
//                "&destinations=side_of_road:37.7663444,-122.4412006" +
//                "&key=AIzaSyA8X9Q8arG40y5YGXQupXIKMLASpM1mTis";
        DecimalFormat df2 = new DecimalFormat("#.##");
        float[] result = new float[1];
        String[] newCurrent = current.split(",");
        String[] newDest = destination.split(",");

        for (String a : newCurrent) {
            a.trim();
        }

        for (String a : newDest) {
            a.trim();
        }
        Location.distanceBetween(Double.parseDouble(newCurrent[0]), Double.parseDouble(newCurrent[1]), Double.parseDouble(newDest[0]), Double.parseDouble(newDest[1]), result);
        String distance = df2.format((result[0]/1000)) + " km";

        return distance;
    }
}
