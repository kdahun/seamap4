# 안드로이드 코드

### getData()
: 서버에 저장된 위도, 경도, 파고 데이터를 lati, ongti, waveSize에 저장

### calculateColorBaseOnDepth(double depth)
: depth를 받아서 파고 높이별로 색 지정후 Color.rgb(brightness, 0, 0)으로 색 밝기값을 반환

### private void checkLocationPermission()
### public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
: 위치 권한 확인

### private FusedLocationProviderClient fusedLocationProviderClient;
: 현재 위치 정보 제공 클라이언트 변수
(Android 애플리케이션에서 위치 정보를 제공하는 클래스이다. 안드로이드 스튜디오에서 위치 기반 애플리케이션을 개발할 때 사용되는 중요한 요소 중 하나이다. 위치 정보를 사용하는 앱은 사용자의 현재 위치를 파악하거나, 위치에 기반한 서비스를 제공하는 등 다양한 용도로 활용될 수 있다.
FusedLocationProviderClient는 위치 정보를 효율적으로 제공하고, 다양한 위치 정보 소스(GPS, 네트워크 기반 위치, 센서 등)을 통합하여 개발자에게 사용하기 쉬운 API를 제공한다.
일반적으로 'FusedLoactionProviderClient'를 사용하려면 위치 권한을 앱에 부여하고, 이를 통해 사용자의 위치 정보를 요청하고 처리하는 코드를 작성해야 한다. 위치 정보 업데이트를 받아오기 위해 리스너를 등록하고, 필요에 따라 위치 정보 업데이트 주기 등을 설정할 수 있다.)

    import android.location.Location;
    import com.google.android.gms.location.LocationServices;
    import com.google.android.gms.location.FusedLocationProviderClient;
    
    // ...
    
    // FusedLocationProviderClient 객체 생성
    FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    
    // 위치 권한을 체크하고, 권한이 있으면 위치 정보 업데이트 요청
    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        fusedLocationClient.getLastLocation()
            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // 위치 정보를 받아와서 처리
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        Log.d(TAG, "Latitude: " + latitude + ", Longitude: " + longitude);
                    }
                }
            });
    }
    FusedLocationProviderClient를 사용하여 위치 정보를 얻고, addSuccessListener를 사용하여 위치 정보를 처리하고 있었다. 실제 앱에서는 위치 정보 업데이트 주기 등을 설정하고 필요한 위치 정보 처리를 수행하는 로직을 추가해야 한다.

### private LatLng currentLocation;
: 현재 위치 정보를 저장할 변수
