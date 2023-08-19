package com.example.seamap4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



// 해구 번호, 위도, 경도, 파고를 저장하는 구조체?
class GridData{
    int gridNumber;
    double latitude;
    double lonitude;
    double wave;

    // 생성자
    public GridData(int gridNumber, double latitude, double lonitude, double wave){
        this.gridNumber = gridNumber;
        this.latitude = latitude;
        this.lonitude = lonitude;
        this.wave = wave;
    }
}

// GridData로 ArrayList를 만들어서  gridDataList에 저장
class GridDataManager{
    public List<GridData> gridDataList;
    public GridDataManager(){
        gridDataList = new ArrayList<>();
    }

    void addGrid(int number, double lat, double lng,double wave){
        gridDataList.add(new GridData(number,lat,lng,wave));
    }
}

public class MainActivity2 extends AppCompatActivity implements OnMapReadyCallback {

    RequestQueue queue;
    //double[] latitudeArr;

    ArrayList<Double> lati;
    ArrayList<Double> longti;
    ArrayList<Double> waveSize;
    //double[] lontitudeArr;
    GoogleMap gMap;
    MapFragment mapFrag;

    double Current_lat;
    double Current_log;

    // 메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0,1,0,"실시간 파고");
        SubMenu subMenu = menu.addSubMenu("예측 파고 >>");
        subMenu.add(0,2,0,"10분");
        menu.add(0,3,0,"실시간 경로");

        return true;
    }

    // 메뉴 클릭 이벤트
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case 1:
                Toast.makeText(getApplicationContext(),"실시간",Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    // =======================================데이터를 가져오는 부분===================================================
    private void getData() {
        String url = "http://202.31.147.129:25003/weater.php";


        lati = new ArrayList<Double>();                             // 위도 저장
        longti = new ArrayList<Double>();                           // 경도 저장
        waveSize = new ArrayList<Double>();                         // 파도 저장


        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }

        // 서버(웹페이지 파싱)?에서 위치 해구 정보 가져오기
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("blackice");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);

                        // 위도 저장
                        double latitude = Double.parseDouble(item.getString("latitude"));

                        // 경도 저장
                        double longtitude = Double.parseDouble(item.getString("longitude"));

                        // 파고 저장
                        String waveS = item.getString("wavesize");
                        String resultWave = waveS.substring(0, waveS.length() - 1);
                        double dWaveSize;

                        //파고가 널값이면 0을 넣어준다.
                        try {
                            dWaveSize = Double.parseDouble(resultWave);
                        } catch (Exception e) {
                            dWaveSize = 0;
                        }

                        // 위도, 경도, 파고 arraylist에 저장
                        lati.add(latitude);
                        longti.add(longtitude);
                        waveSize.add(dWaveSize);
                    }

                    // Logcat에 위도, 경도, 파고 출력해주기
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Log.d("Latitude", i + ":" + lati.get(i));
                        Log.d("Longitude", i + ":" + longti.get(i));
                        Log.d("WaveSize", i + ":" + waveSize.get(i));
                    }

                    // 데이터가 잘 가져와졌으면 "데이터 로드 완료"  메시지 toast
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
//========================================================================================================================

    private int calculateColorBaseOnDepth(double depth) {
        // 파고 높이별로 색 지정
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
        // Color.rgb에 색 밝기값을 넣어서 반환
        return brightness;
    }

    // 추가: 최단 경로 데이터를 가져오는 함수
    private void getShortestPathData() {
        String url = "http://202.31.147.129:25003/shortest.php";

        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }
        // 서버에서 최단 경로 데이터 가져오기
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("shortest");

                    // 최단 경로 위도 경도를 ArrayList에 저장
                    ArrayList<LatLng> latLngs = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);

                        // 위도 저장
                        double latitude = Double.parseDouble(item.getString("latitude"));

                        // 경도 저장
                        double longitude = Double.parseDouble(item.getString("longitude"));

                        latLngs.add(new LatLng(latitude, longitude));
                    }

                    //이 부분에서 최단 경로 찍어줌.
                    drawShortestPath(latLngs);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "오류발생", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    // 추가: 최단 경로 선 그리기 함수
    private void drawShortestPath(ArrayList<LatLng> latLngs) {
        if (gMap != null && latLngs != null && !latLngs.isEmpty()) {
            for (int i = 0; i < latLngs.size() - 1; i++) {
                LatLng start = latLngs.get(i);
                LatLng end = latLngs.get(i + 1);

                gMap.addPolyline(new PolylineOptions()

                        .add(start, end)
                        .width(5)
                        .color(Color.BLUE)
                );
            }
        }
    }
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

// 오류 발생시 사용되는 메서드
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                onDataLodaed();
//            } else {
//                // 권한 거부됨
//                // 뭐 오류 떳는지 확인하고 싶을때 넣기
//            }
//        }
//    }

// 데이터 POST 메서드
    private void sendData() {
        String url = "http://202.31.147.129:25003/get.php";

        //사용자 디바이스 고유ID 가져오는 코드
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // 서버에서 전송된 결과를 처리할 코드를 합니다.
                Toast.makeText(getApplicationContext(), "데이터 전송 완료", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 에러 발생 시 처리할 코드를 이곳에 작성하세요.
                Toast.makeText(getApplicationContext(), "오류발생", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Intent intent = getIntent();
                //Toast.makeText(getApplicationContext(),intent.getStringExtra("destination_lat"),Toast.LENGTH_SHORT).show();
                String destination_lat = intent.getStringExtra("destination_lat");
                String destination_log = intent.getStringExtra("destination_log");


                Map<String, String> params = new HashMap<>();
                params.put("dest_latitude", String.valueOf(destination_lat)); // 목적지 경도
                params.put("dest_longitude", String.valueOf(destination_log)); // 목적지 위도
                params.put("cur_latitude", String.valueOf(Current_lat)); // 현재 경도
                params.put("cur_longitude", String.valueOf(Current_log)); // 현재 위도
                params.put("device_id", deviceId);
                return params;
            }
        };

        queue.add(stringRequest);
    }


    // 현재 위치 정보 제공 클라이언트
    private FusedLocationProviderClient fusedLocationProviderClient;
    // 현재 위치 정보를 저장할 변수
    private LatLng currentLocation;

    // 구글맵 셋팅?
    // onMapReady 메서드는 Google Maps가 준비가 되었을 때 호출되며, 이를 통해 개발자가 지도를 커스터마이징하고 지도 작업을 수행할 수 있도록 해준다.
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;

        LatLng korea = new LatLng(37, 128);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(korea, 5));

        GridDataManager manager = new GridDataManager(); //

        // 위도 배열 크기 만큼 반복
        for (int i = 0; i < lati.size(); i++) {

            manager.addGrid(i,lati.get(i), longti.get(i),waveSize.get(i));
        }

        for (GridData data : manager.gridDataList) {

            PolygonOptions polygonOptions = createPolygonForCoordinate(data.latitude, data.lonitude);
            int b = calculateColorBaseOnDepth(data.wave);
            int fillColor = Color.argb(178 /*투명도*/, b, 0, 0);
            polygonOptions.fillColor(fillColor);

            googleMap.addPolygon(polygonOptions);

        }


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

    }

    private PolygonOptions createPolygonForCoordinate(double latCenter, double lngCenter){
        PolygonOptions rectOptions=new PolygonOptions()
                .add(new LatLng(latCenter - 0.25 , lngCenter - 0.25))
                .add(new LatLng(latCenter - 0.25 , lngCenter + 0.25))
                .add(new LatLng(latCenter + 0.25 , lngCenter + 0.25))
                .add(new LatLng(latCenter + 0.25 , lngCenter - 0.25))
                .strokeWidth(2)
                .strokeColor(Color.BLACK);

        return rectOptions;
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
        locationRequest.setInterval(10000); // 위치 업데이트 간격(5초)

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult != null) {
                    for (android.location.Location location : locationResult.getLocations()) {

                        // 현재 위치의 경위도 값을 변수에 저장
                        Current_lat = location.getLatitude();
                        Current_log = location.getLongitude();
                        sendData();

                        Toast.makeText(getApplicationContext(),Current_lat+":"+Current_log,Toast.LENGTH_SHORT).show();
                        // 새로운 위치를 받아서 처리
                        updateCurrentLocation(location);
                    }
                }
            }
        };
    }

    private void startLocationUpdates() {
        //위치 업데이트 시작+
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

    private Marker currentLoactionMarker; // 현재 위치를 나타내는 마커

    private Circle currentCircle;

    private void updateCurrentLocation(android.location.Location location) {
        // 새로운 위치 업데이트 시, 지도에 마커 표시
        if (gMap != null) {
            LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            if(currentLoactionMarker == null){
                // 초기에 마커가 없다면 추가
                currentLoactionMarker = gMap.addMarker(new MarkerOptions().position(newLatLng).zIndex(2.0f));
            }else{
                // 이미 마커가 있다면 위치만 업데이트
                currentLoactionMarker.setPosition(newLatLng);
            }

            // 반경 원을 그리기 위한 설정
            CircleOptions circleOptions = new CircleOptions().
                    center(newLatLng).
                    radius(10000).
                    strokeColor(Color.BLUE).
                    fillColor(Color.TRANSPARENT).zIndex(1.0f);

            // 이전에 추가된 반경원이 있다면 제거
            if(currentCircle != null){
                currentCircle.remove();
            }

            // 새로운 반경원을 지도에 추가
            currentCircle = gMap.addCircle(circleOptions);

            // 위치 업데이트 발생 시 Toast 메시지 표시
            //Toast.makeText(this,"현재 위치가 업데이트 되었습니다",Toast.LENGTH_SHORT).show();
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

        // 추가: 최단 경로 데이터 가져오기
        getShortestPathData();
    }

}
