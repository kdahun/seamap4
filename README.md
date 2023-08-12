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
    
```
---
