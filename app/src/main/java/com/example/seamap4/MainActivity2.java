package com.example.seamap4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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

    private void getData() {
        String url = "http://202.31.147.129:25003/weater.php";

        latitudeArr = new double[10];
        lontitudeArr = new double[10];
        lati = new ArrayList<Double>();
        longti = new ArrayList<Double>();
        waveSize = new ArrayList<Double>();

        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("blackice");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        double latitude = Double.parseDouble(item.getString("latitude"));
                        double longtitude = Double.parseDouble(item.getString("longitude"));

                        String waveS = item.getString("wavesize");
                        String resultWave = waveS.substring(0, waveS.length() - 1);
                        double dWaveSize;
                        try {
                            dWaveSize = Double.parseDouble(resultWave);
                        } catch (Exception e) {
                            dWaveSize = 0;
                        }
                        lati.add(latitude);
                        longti.add(longtitude);
                        waveSize.add(dWaveSize);
                        //latitudeArr[i] = latitude;
                        //lontitudeArr[i] = longtitude;
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Log.d("Latitude", i + ":" + lati.get(i));
                        Log.d("Longitude", i + ":" + longti.get(i));

                        Log.d("WaveSize", i + ":" + waveSize.get(i));
                    }
                    Toast.makeText(getApplicationContext(), "데이터 로드 완료", Toast.LENGTH_SHORT).show();
                    onDataLodaed();// 데이터 로드가 끝난후에
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //에러
                Toast.makeText(getApplicationContext(), "오류발생", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private int calculateColorBaseOnDepth(double depth) {

        int brightness = 0;

        if (depth == 0) {
            brightness = 0;
        } else if (depth < 1.5) {
            brightness = 255 / 6;
        } else if (depth < 2) {
            brightness = 255 / 6 * 2;
        } else if (depth < 2.5) {
            brightness = 255 / 6 * 3;
        } else if (depth < 3) {
            brightness = 255 / 6 * 4;
        } else if (depth < 3.5) {
            brightness = 255 / 6 * 5;
        } else if (depth >= 3.5) {
            brightness = 254;
        }

        return Color.rgb(brightness, 0, 0);
    }

    private LatLng adjustMarkerPosition(LatLng originalPosition, int index) {
        double offset = 0.0001 * index;
        return new LatLng(originalPosition.latitude + offset, originalPosition.longitude + offset);
    }

    // 현재 위치 찍어주기-------------------------------------START-----------------------------------------------------------
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private void checkLocationPermission() {
        // 위치 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없을 경우
            // 권한이 필요한 이유를 설명
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 필요한 권한 설명을 위한 다이얼 로그 표시
            } else {
                // 권한 요청
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            // 권한이 이미 있는 경우
            // 위치 정보 가져오기 진행
            onDataLodaed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onDataLodaed();
            } else {
                // 권한 거부됨
                // 뭐 오류 떳는지 확인하고 싶을때 넣기
            }
        }
    }
    // 현재 위치 찍어주기----------------------------------END-----------------------------------------------------------


    private BitmapDescriptor createColoredMarkerIcon(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    // 현재 위치 정보 제공 클라이언트
    private FusedLocationProviderClient fusedLocationProviderClient;
    // 현재 위치 정보를 저장할 변수
    private LatLng currentLocation;

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;

        gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        for (int i = 0; i < lati.size(); i++) {
            LatLng position = new LatLng(lati.get(i), longti.get(i));

            Bitmap bitmap = Bitmap.createBitmap(48, 48, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(calculateColorBaseOnDepth(waveSize.get(i)));

            BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromBitmap(bitmap);


            gMap.addMarker(new MarkerOptions()
                    .position(position)
                    .icon(markerIcon)
                    .alpha(0.5f).flat(true).zIndex(1.0f)
                    .title("Marker" + i));


            gMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        }

        // 현재 위치 가져와서 마커 추가
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                gMap.addMarker(new MarkerOptions().position(currentLocation).title("현재 위치"));
                gMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            }
        });
    }

    private void onDataLodaed() {
        mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }


    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    // 현재 위치 업데이트 요청 설정
    private void setupLoctionUpdate() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000); // 위치 업데이트 간격(5초)

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult != null) {
                    for (android.location.Location location : locationResult.getLocations()) {
                        // 새로운 위치를 받아서 처리
                        updateCurrentLocation(location);
                    }
                }
            }
        };
    }

    private void startLocationUpdates() {
        //위치 업데이트 시작
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void updateCurrentLocation(android.location.Location location) {
        // 새로운 위치 업데이트 시, 지도에 마커 표시
        if (gMap != null) {
            LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            gMap.clear(); // 기존 마커 제거
            gMap.addMarker(new MarkerOptions().position(newLatLng).title("현재 위치"));
            gMap.moveCamera(CameraUpdateFactory.newLatLng(newLatLng));

            // 위치 업데이트 발생 시 Toast 메시지 표시
            Toast.makeText(this,"현재 위치가 업데이트 되었습니다",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // 위치 권한 확인
        checkLocationPermission();

        // 데이터 가져오기
        getData();

        // 현재 위치 업데이트 요청 설정
        setupLoctionUpdate();

        // 현재 위치 갱신 시작
        startLocationUpdates();

    }


}