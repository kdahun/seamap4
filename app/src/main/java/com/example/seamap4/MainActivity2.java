package com.example.seamap4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements OnMapReadyCallback {

    RequestQueue queue;
    double[] latitudeArr;

    ArrayList<Double> lati;
    ArrayList<Double> longti;
    ArrayList<Double> waveSize;
    double[] lontitudeArr;
    GoogleMap gMap;
    MapFragment mapFrag;

    private void getData(){
        String url = "http://202.31.147.129:25003/weater.php";

        latitudeArr = new double[10];
        lontitudeArr = new double[10];
        lati=new ArrayList<Double>();
        longti = new ArrayList<Double>();
        waveSize = new ArrayList<Double>();

        if(queue == null){
            queue = Volley.newRequestQueue(this);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("blackice");

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject item = jsonArray.getJSONObject(i);
                        double latitude = Double.parseDouble(item.getString("latitude"));
                        double longtitude = Double.parseDouble(item.getString("longitude"));

                        String waveS = item.getString("wavesize");
                        String resultWave = waveS.substring(0,waveS.length()-1);
                        double dWaveSize;
                        try {
                            dWaveSize = Double.parseDouble(resultWave);
                        }catch(Exception e){
                            dWaveSize=0;
                        }
                        lati.add(latitude);
                        longti.add(longtitude);
                        waveSize.add(dWaveSize);
                        //latitudeArr[i] = latitude;
                        //lontitudeArr[i] = longtitude;
                    }
                    for(int i=0;i<jsonArray.length();i++){
                        Log.d("Latitude",i+":"+lati.get(i));
                        Log.d("Longitude",i+":"+longti.get(i));

                        Log.d("WaveSize",i+":"+waveSize.get(i));
                    }
                    Toast.makeText(getApplicationContext(),"데이터 로드 완료",Toast.LENGTH_SHORT).show();
                    onDataLodaed();// 데이터 로드가 끝난후에
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //에러
                Toast.makeText(getApplicationContext(),"오류발생",Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private int calculateColorBaseOnDepth(double depth){

        int brightness = 0;

        if(depth==0){
            brightness = 0;
        }
        else if(depth<1.5){
            brightness = 255/6;
        }
        else if(depth<2){
            brightness = 255/6*2;
        }
        else if(depth<2.5){
            brightness = 255/6*3;
        }
        else if(depth<3){
            brightness = 255/6*4;
        }
        else if(depth<3.5){
            brightness = 255/6*5;
        }
        else if(depth>=3.5){
            brightness = 254;
        }

        return Color.rgb(brightness,0,0);
    }


    private LatLng adjustMarkerPosition(LatLng originalPosition, int index){
        double offset = 0.0001*index;
        return new LatLng(originalPosition.latitude+offset,originalPosition.longitude+offset);
    }

    private BitmapDescriptor createColoredMarkerIcon(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;

        gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        for(int i=0;i<lati.size();i++){
            LatLng position = new LatLng(lati.get(i),longti.get(i));

            Bitmap bitmap = Bitmap.createBitmap(48,48,Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(calculateColorBaseOnDepth(waveSize.get(i)));

            BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromBitmap(bitmap);


            gMap.addMarker(new MarkerOptions()
                    .position(position)
                    .icon(markerIcon)
                    .alpha(0.4f).flat(true).zIndex(1.0f)
                    .title("Marker"+i));


            gMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        }
    }

    private void onDataLodaed(){
        mapFrag =(MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getData();



    }


}