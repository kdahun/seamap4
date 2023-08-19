package com.example.seamap4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.telephony.TelephonyManager;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap1;
    private Marker des;
    private double lat;
    private double log;
    private RequestQueue queue;

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // googleMap 객체만들기
        googleMap1 = googleMap;


        // 위치를 한국으로 위치 변경
        LatLng korea = new LatLng(38, 128);
        googleMap1.moveCamera(CameraUpdateFactory.newLatLngZoom(korea, 5));

        googleMap1.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                if (des == null) {
                    // des는 마커가 현재 찍혀 있는지 아닌지 확인하는 변수
                    // 마커 찍기
                    des = googleMap1.addMarker(new MarkerOptions().position(latLng).title("destination"));

                    //lat에 위도 저장
                    lat = latLng.latitude;

                    //log에 경도 저장
                    log = latLng.longitude;
                } else {
                    // 마커 위치 변경
                    des.setPosition(latLng);

                    // 위도 저장
                    lat = latLng.latitude;

                    // 경도 저장
                    log = latLng.longitude;
                }
                Log.d("lat,log : ", lat + " ," + log);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.button);


        // fragement에 googlemap 띄우기
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frameLayout);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frameLayout, mapFragment);
            fragmentTransaction.commit();
        }

        mapFragment.getMapAsync(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendData(lat, log);

                // 버튼을 누르면 intent로 넘어가기 , 목적지 위도 경도는 넘겨줌
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                intent.putExtra("destination_lat",String.valueOf(lat));
                intent.putExtra("destination_log",String.valueOf(log));
                startActivity(intent);
            }
        });
    }
}
