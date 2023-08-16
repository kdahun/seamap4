package com.example.seamap4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback  {

    private GoogleMap googleMap1;


    private Marker des;

    private double lat;
    private double log;
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap1 = googleMap;

        LatLng korea = new LatLng(38,128);
        googleMap1.moveCamera(CameraUpdateFactory.newLatLngZoom(korea,5));

        googleMap1.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                if(des==null) {
                    des = googleMap1.addMarker(new MarkerOptions().position(latLng).title("destination"));
                    lat = latLng.latitude;
                    log = latLng.longitude;
                }else{
                    des.setPosition(latLng);
                    lat = latLng.latitude;
                    log = latLng.longitude;
                }

                Log.d("lat,log : ",lat+" ,"+log);
            }
        });

        //googleMap1.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.button);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frameLayout);

        if(mapFragment == null){
            mapFragment = SupportMapFragment.newInstance();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frameLayout,mapFragment);
            fragmentTransaction.commit();
        }

        mapFragment.getMapAsync(this);





        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                startActivity(intent);
            }
        });
    }
}