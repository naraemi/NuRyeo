package com.example.snfu;

import java.util.Vector;

import android.graphics.Rect;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.NMapView.OnMapStateChangeListener;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager.OnCalloutOverlayListener;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay.OnStateChangeListener;

public class MapLayout extends NMapActivity implements
		OnMapStateChangeListener, OnCalloutOverlayListener, LocationListener {

	public static final String API_KEY = "33d492644495def2a212c177e1cbedf6";// 네이버지도
	NMapView mMapView = null;
	NMapController mMapController = null;
	LinearLayout MapContainer;
	
	// 지오코딩
	static double lat, lon;
	LocationManager mLocMan;
	Geocoder mCoder;

	public Vector<Double> lat_vec = new Vector<Double>();
	public Vector<Double> lon_vec = new Vector<Double>();
	public Vector<String> name_vec = new Vector<String>();
	int datanum;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		lon_vec=MainActivity.lon_vec;
		lat_vec=MainActivity.lat_vec;
		name_vec=MainActivity.name_vec;
		datanum=MainActivity.data_num;
		
		// 커스텀바 정의
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.listbar);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);

		HorizontialListView listview = (HorizontialListView) findViewById(R.id.listview);
		listview.setAdapter(mAdapter);

		// 맵관련 코드 시작
		MapContainer = (LinearLayout) findViewById(R.id.MapContainer);
		// create map view
		mMapView = new NMapView(this);
		mMapController = mMapView.getMapController();
		// set a registered API key for Open MapViewer Library
		mMapView.setApiKey(API_KEY);

		MapContainer.addView(mMapView);

		// initialize map view
		mMapView.setClickable(true);

		mMapView.setBuiltInZoomControls(true, null);
		// use map controller to zoom in/out, pan and set map center, zoom level
		// etc.
		mMapView.setOnMapStateChangeListener(this);
		// mMapView.setOnMapViewTouchEventListener(this);
		
	

		
		
		
		// 오버레이의 리소스를 제공하기 위한 객체
		NMapViewerResourceProvider mMapViewerResourceProvider = null;
		// 오버레이 관리자
		NMapOverlayManager mOverlayManager;
		OnStateChangeListener onPOIdataStateChangeListener = null;
		/******************* 오버레이 관련 코드 시작 ********************/
		// 오버레이 리소스 관리객체 할당
		mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

		// 오버레이 관리자 추가
		mOverlayManager = new NMapOverlayManager(this, mMapView,
				mMapViewerResourceProvider);

		// 오버레이들을 관리하기 위한 id값 생성
		int markerId = NMapPOIflagType.PIN;

		// 표시할 위치 데이터를 지정한다. -- 마지막 인자가 오버레이를 인식하기 위한 id값
		NMapPOIdata poiData = new NMapPOIdata(datanum, mMapViewerResourceProvider);
		poiData.beginPOIdata(datanum);
		// poiData.addPOIitem(127.0630205, 37.5091300, "위치1", markerId, 0);
		/*
		 * MainActivity content = new MainActivity(); lon =
		 * content.lon_vec.get(i); lat = content.lat_vec.get(i);
		 */
		for (int i = 0; i < datanum; ++i)
		{
			lon = (Double) lon_vec.get(i);
			lat = (Double) lat_vec.get(i);
			String temp = name_vec.get(i);
			poiData.addPOIitem(lon, lat, temp, markerId, 0);
		}
		// poiData.addPOIitem(lon, lat, "코엑스 오디토리움", markerId, 0);
		// poiData.addPOIitem(127.061, 37.51, "위치2", markerId, 0);
		poiData.endPOIdata();

		// 위치 데이터를 사용하여 오버레이 생성
		NMapPOIdataOverlay poiDataOverlay = mOverlayManager
				.createPOIdataOverlay(poiData, null);

		// id값이 0으로 지정된 모든 오버레이가 표시되고 있는 위치로 지도의 중심과 ZOOM을 재설정
		poiDataOverlay.showAllPOIdata(0);

		// 아이템의 선택상태 or 마룽선 선택되는 경우
		poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
		// 오버레이 이벤트 등록
		mOverlayManager
				.setOnCalloutOverlayListener((OnCalloutOverlayListener) this);
		/******************* 오버레이 관련 코드 끝 ********************/
		/*
		 * String slat_temp =
		 * ""+addr.get(0).getLatitude();//testadd.getLatitude(); String
		 * slon_temp = ""+addr.get(0).getLongitude();// slat =
		 * slat_temp.replace(".", ""); // 변환한 좌표에서 . 을 뺀다 slon =
		 * slon_temp.replace(".", ""); slat = slat.substring(0,8); // 끝자리수 하나를
		 * 빼야 지도가 정상적으로 뜬다. slon = slon.substring(0,9); // 마찬가지 // GeoPoint pt =
		 * new GeoPoint(Integer.parseInt(slat),Integer.parseInt(slon));
		 */

	}

	public void transAdd() {

	}

	public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay,
			NMapPOIitem item) {
		Toast.makeText(MapLayout.this, item.getTitle(), Toast.LENGTH_LONG)
				.show();
	}

	// 상단바 정의
	private static String[] dataObjects = new String[] { "전체", "공연장", "박물관",
			"미술관", "문화재", "★즐겨찾기★" };

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) { // 백 버튼
			onBackPressed();
		}
		return true;
	}

	private BaseAdapter mAdapter = new BaseAdapter() {

		public int getCount() {
			return dataObjects.length;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View retval = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.mapactionbar, null);
			Button button = (Button) retval.findViewById(R.id.button_map);
			button.setText(dataObjects[position]);
			return retval;
		}
	};

	public void onMapInitHandler(NMapView mapview, NMapError errorInfo) {
		// TODO Auto-generated method stub
		if (errorInfo == null) {
			// 지도초기화면 설정! 선택값에 따라 변경되어야되 강남구->강남구청[127.0475020, 37.5173050]
			mMapController.setMapCenter(new NGeoPoint(127.0475020, 37.5173050),
					11); // 11은 줌레벨 1로 설정하면 한반도가보임
		} else {
			android.util.Log.e("NMAP",
					"onMapInitHandler: error=" + errorInfo.toString());
		}

	}

	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay arg0,
			NMapOverlayItem arg1, Rect arg2) {
		return new NMapCalloutBasicOverlay(arg0, arg1, arg2);
	}

	public void onAnimationStateChange(NMapView arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public void onMapCenterChange(NMapView arg0, NGeoPoint arg1) {
		// TODO Auto-generated method stub

	}

	public void onMapCenterChangeFine(NMapView arg0) {
		// TODO Auto-generated method stub

	}

	public void onZoomLevelChange(NMapView arg0, int arg1) {
		// TODO Auto-generated method stub

	}
}
