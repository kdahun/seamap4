# 안드로이드 코드
---
### getData()
: 서버에 저장된 위도, 경도, 파고 데이터를 lati, ongti, waveSize에 저장
---
### calculateColorBaseOnDepth(double depth)
: depth를 받아서 파고 높이별로 색 지정후 Color.rgb(brightness, 0, 0)으로 색 밝기값을 반환
---
### private void checkLocationPermission()
### public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
: 위치 권한 확인
---
### private FusedLocationProviderClient fusedLocationProviderClient;
: 현재 위치 정보 제공 클라이언트 변수
(Android 애플리케이션에서 위치 정보를 제공하는 클래스이다. 안드로이드 스튜디오에서 위치 기반 애플리케이션을 개발할 때 사용되는 중요한 요소 중 하나이다. 위치 정보를 사용하는 앱은 사용자의 현재 위치를 파악하거나, 위치에 기반한 서비스를 제공하는 등 다양한 용도로 활용될 수 있다.
FusedLocationProviderClient는 위치 정보를 효율적으로 제공하고, 다양한 위치 정보 소스(GPS, 네트워크 기반 위치, 센서 등)을 통합하여 개발자에게 사용하기 쉬운 API를 제공한다.
일반적으로 'FusedLoactionProviderClient'를 사용하려면 위치 권한을 앱에 부여하고, 이를 통해 사용자의 위치 정보를 요청하고 처리하는 코드를 작성해야 한다. 위치 정보 업데이트를 받아오기 위해 리스너를 등록하고, 필요에 따라 위치 정보 업데이트 주기 등을 설정할 수 있다.)
```
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
```

FusedLocationProviderClient를 사용하여 위치 정보를 얻고, addSuccessListener를 사용하여 위치 정보를 처리하고 있었다. 실제 앱에서는 위치 정보 업데이트 주기 등을 설정하고 필요한 위치 정보 처리를 수행하는 로직을 추가해야 한다.

---
### private LatLng currentLocation;
: 현재 위치 정보를 저장할 변수(안드로이드 애플리케이션에서 위치 정보를 저장하기 위한 변수이다.)
(LatLng은 안드로이드에서 위도와 경도 정보를 표현하기 위한 클래스이다.
위치 정보를 다루는 경우, 이 변수를 사용하여 현재 위치의 위도와 경도를 저장하고 관리할 수 있다. 위치 정보를 가져오거나 설정할 때 이 변수를 사용하여 해당 위치를 나타낼 수 있다.
예를 들어, 사용자의 현재 위치를 추적하는 앱을 개발하거나, 위치 정보를 기반으로 지도에 마커를 표시하고 해당 위치를 저장하려는 경우에 'currentLocation'변수가 유용하게 사용될 수 있다. 위치 정보를 더 쉽게 관리하고 조작하기 위해 LatLng객체를 사용하는 것은 일반적인 관행이다.)

---

### public void onMapReady(@NonNull GoogleMap googleMap)
: Google Maps SDK의 OnMapReadyCallback 인터페이스에서 사용되는 콜백 메서드이다.

        콜백 메서드? : 특정 이벤트나 상황이 발생했을 때 시스템이나 다른 코드에 의해 자동으로 호출되는 메서드이다. 이벤트가 발생하면 시스템이 등록된 콜백 메서드를 호출하여 필요한 작업을 수행할 수 있도록 하는 프로그래밍 패턴이다.
        1. 비동기 작업 : 비동기 작업에서 결과가 준비되었을 때, 이를 처리하는 데 사용한다. 예를 들어, 파일 다운로드나 네트워크 요청의 완료 시점을 알리기 위해 콜백 메서드를 등록될 수 있다.
        2. 이벤트 처리 : GUI 프로그래밍에서 버튼 클릭, 마우스 이벤트, 키보드 입력 등의 이벤트를 처리하기 위해 콜백 메서드가 사용된다.
        3. 프레임워크나 라이브러리 사용 : 프레임 워크나 라이브러리에서 사용자 정의 코드가 호출되어야 할 때 콜백 메서드를 등록할 수 있다. 예를 들어, 안드로이드에서 위에서 설명한 onMapReady 메서드는 Google Maps SDK에서 제공하는 콜백 메서드이다.
        4. 이벤트 기반 프로그래밍 : 이벤트 중심의 프로그래밍에서 발생하는 상황에 따라 콜백 메서드를 호출하여 로직을 실행하는 패턴이다.

        콜백 메서드는 인터페이스, 함수 포인터, 람다 함수 등을 통해 구현될 수 있으며, 이를 통해 코드의 모듈성과 재사용성을 높일 수 있다. 이벤트 기반의 프로그래밍에서 매우 중요한 역할을 하며, 비동기 작업과 이벤트 처리를 효율적으로 다루는 데 사용한다.

이 메서드는 Google Maps가 준비되었을 때 호출되며, 이를 통해 개발자가 지도를 커스터마이징하고 지도 작업을 수행할 수 있도록 해준다.
```
@Override
public void onMapReady(@NonNull GoogleMap googleMap){
    // 이 메서드가 호출되면 Google Maps가 준비된 상태이다.
    // googleMap 객체를 사용하여 지도 작업을 수행하면 된다.

    // 지도 유형 설정
    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    // 예: 마커를 추가
    LatLng location = new LatLng(37.7749, -122.4194); // 위도와 경도 설정
    MarkderOptions markerOptions = new MarkerOptions().postion(location).title("San Francisco");// 마커 타이틀 설정

    googleMap.addMarker(markerOptions);
    // 예 : 지도 줌 레벨 조정
    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,12));
}
```
위 예시 코드에서 onMapReady 메서드에서는 지도가 준비되었을 때 호출되며 googleMap 객체를 사용하여 다양한 지도 작업을 수행하고 있다. 이 메서드는 MapView나 MapFragment에서 Google Maps가 초기화될 때 자동으로 호출되도록 설정되어야 한다.

---
### 마커를 직사각형 모양으로 바꾸고 색깔 지정
1. Bitmap.createBitmap(48,48,Bitmap.Config.ARGB_888); :
이 부분은 너비 48 픽셀, 높이 48 픽셀의 비트맵(이미지) 객체를 생성한다. 비트맵의 픽셀 포맷을 ARGB_888로 설정되어 있다.
ARGB_8888 포맷은 알파 채녈과 빨강, 초록, 파랑(RGB) 채널을 8비트씩 사용하여 각 픽셀을 표현하는 방식이다.
2. bitmap.eraseColor(calculateColorBaseOnDepth(waveSize.get(i))); :
위에서 생성한 비트맵 객체의 모든 픽셀을 특정한 색으로 지우기 위해 eraseColor() 메서드를 호출한다.
calculateColorBaseOnDepth(waveSize.get(i))함수는 waveSize 리스트에서 값을 가져와 해당 값을 기반으로 색상을 계산하는 함수이다. 이렇게 계산된 색상으로 비트맵을 채워진다.
3. BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromBitmap(bitmap); : 
위에서 설정한 비트맵 객체를 사용하여 BitmapDescriptor 객체를 생성한다. 이 객체는 마커를 위한 아이콘을 나타내며, 이 아이콘을 지도 상에 마커로 표시할 때 사용한다. BitmapDescriptorFactory.fromBitmap() 메서드를 사용하여 비트맵에서 아이콘을 생성한다.

이 코드의 목적은 지도 상에 특정한 크기의 커스텀 마커 아이콘을 만들고, 이 아이콘을 markerIcon 변수에 저장하는 것이다. 이후 이 아이콘을 사용하여 지도 상에 마커를 표시할 수 있게 된다.

---

```
if (currentLocation != null){
    currentLocationMarker = gMap.addMarker(new MarkerOptions().position(currentLocation).title("현재 위치"));
    gMap.moveCamera(CameraUpdateFactory.newLatLng(position));
}
```
이 코드는 Google Maps API를 사용하는 현재 위치 정보가 유효한 경우에만 지도 상에 현재 위치를 나타내는 마커(Marker)를 추가하고, 해당 위치로 지도를 이동하는 역할을 한다.
1. if(currentLocation != null){...} :
이 부분은 currentLocation 변수가 null이 아닌 경우에만 안의 코드 블록을 실행한다. currentLocation은 지리적인 좌표 정보를 나타내는 변수로, 위도와 경도를 포함하는 LatLng 객체이다. 즉, 현재 위치 정보가 유효한 경우에만 해당 코드 블록이 실행된다.
2. currentLocationMarker = gMap.addMarker(new MarkerOptions().position(currentLocation).title("현재 위치")); :
이 부분에서는 gMap 객체(지도)에 마커를 추가한다.
currentLocation 변수의 위치에 마커를 추가하고, 마커의 타이틀을 현재 위치로 설정한다. 마커를 추가한 결과는 currentLocationMarker변수에 저장된다. 이 변수는 이후에 마커를 제어할 때 사용될 수 있다.
3. gMap.moveCamera(CameraUpdateFactory.newLatLng(position)); :
마지막으로, 지도의 카메라를 현재 위치로 이동시키는 코드이다.

위 코드의 목적은 현재 위치 정보가 유효한 경우에 지도 상에 "현재 위치"라는 타이틀을 가진 마커를 추가하고, 해당 위치로 지도의 카메라를 이동시키는 것이다.

---

```
fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
    return;
}
fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location->{
    if(location!=null){
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        gMap.addMarker(new MarkerOptions(),position(currentLocation).title("현재 위치"));
        gMap.moveCamera(CameraUpdateFactory,newLatLng(currentLocation));
        }
    });
}
```
위 코드는 안드로이드 애플리케이션에서 Fused Location Provider를 사용하여 현재 위치를 얻고, 해당 위치에 마커를 추가하여 지도를 이동하는 역할을 한다. Fused Location Provider는 안드로이드 위치 서비스 중 하나로, GPS, 네트워크, 센서 데이터 등을 결합하여 정확한 위치 정보를 제공하는 역할을 한다.
1. LocationServices.getFusedLocationProviderClient(this); :
Fused Location Provider를 초기화하여 fusedLocationProviderClient 객체를 생성한다. 이 객체를 사용하여 위치 정보를 가져올 수 있다.
2. 위치 권한 확인:
ActivityCompat.checkSelfPermission를 사용하여 앱이 위치 권한을 가지고 있는지 확인한다. 만약 위치 권한이 없다면 if문 안에 사용자에게 위치 권한을 요청하는 로직을 구현해야한다. 위치 권한이 없으면 함수를 종료한다.
3. 위치 정보 가져오기 : 
fusedLocationProviderClient.getLastLocation().addOnSuccessListener를 사용하여 최근 위치 정보를 가져온다.
addOnSuccessListener는 위치 정보를 성공적으로 가져온 경우에 호출되는 리스너이다.
4. 위치 정보 처리 : 
location 객체가 null이 아닌 경우 location 객체에서 위도와 경도를 가져와서 currentLocation 변수에 LatLng 객체를 저장한다. 그리고 gMap(Google Maps 객체)에 현재 위치를 나타내는 마커를 추가하고, 지도의 카메라를 해당 위치로 이동시킨다.

현재 위치 정보를 가져와서 지도 상에 마커를 표시하는 작업을 수행한다. 위치 권한을 확인하고, 위치 정보를 얻는 비동기적인 작업을 수행하는 코드이다.

---
```
private void onDataLodaed(){
    mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
    mapFrag.getMapAsync(this);
}
```
위 코드는 Adroid 애플리케이션에서 지도를 사용하기 위해 GoogleMaps API를 초기화하고 비동기 방식으로 지도를 가져오는 역할을 한다.
1. mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map); :
이 부분은 레이아웃 XML 파일에 정의된 지도 프래그먼트의 인스턴스를 가져온다. 프래그먼트는 안드로이드 액티비티 내에 다른 UI요소와 함께 조합될 수 있는 독자적인 UI요소이다. 'R.id.map'은 레이아웃 파일에서 지도 프래그먼트를 찾기 위해 사용되는 리소스 ID이다.
2. mapFrag.getMapAsync(this); :
이 부분은 비동기 방식으로 지도를 가져오도록 요청하는 부분이다. getMapAsync() 메서드는 지도를 가져오기 위해 Google Maps API에 요청하며, this 인자로 현재 클래스(또는 해당 메서드가 속한 클래스)를 콜백으로 전달합니다. 이렇게 하면 지도가 준비되었을때 this로 전달할 콜백 메서드(onMapReady())가 자동으로 호출된다.

예를 들어, onMapReady() 라는 메서드가 현재 클래스에 존재한다면, 지도가 가져와지면 자동으로 onMapReady() 메서드가 호출되어 지도를 조작하거나 초기화하는 작업을 수행할 수 있다. 이를 통해 지도가 준비되었을 때 필요한 동작을 수행하도록 코드를 구성할 수 있다.

---
```
private LocationRequese
