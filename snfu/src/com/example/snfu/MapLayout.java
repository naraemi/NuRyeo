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

	public static final String API_KEY = "33d492644495def2a212c177e1cbedf6";// ���̹�����
	NMapView mMapView = null;
	NMapController mMapController = null;
	LinearLayout MapContainer;
	
	// �����ڵ�
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
		
		// Ŀ���ҹ� ����
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.listbar);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);

		HorizontialListView listview = (HorizontialListView) findViewById(R.id.listview);
		listview.setAdapter(mAdapter);

		// �ʰ��� �ڵ� ����
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
		
	

		
		
		
		// ���������� ���ҽ��� �����ϱ� ���� ��ü
		NMapViewerResourceProvider mMapViewerResourceProvider = null;
		// �������� ������
		NMapOverlayManager mOverlayManager;
		OnStateChangeListener onPOIdataStateChangeListener = null;
		/******************* �������� ���� �ڵ� ���� ********************/
		// �������� ���ҽ� ������ü �Ҵ�
		mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

		// �������� ������ �߰�
		mOverlayManager = new NMapOverlayManager(this, mMapView,
				mMapViewerResourceProvider);

		// �������̵��� �����ϱ� ���� id�� ����
		int markerId = NMapPOIflagType.PIN;

		// ǥ���� ��ġ �����͸� �����Ѵ�. -- ������ ���ڰ� �������̸� �ν��ϱ� ���� id��
		NMapPOIdata poiData = new NMapPOIdata(datanum, mMapViewerResourceProvider);
		poiData.beginPOIdata(datanum);
		// poiData.addPOIitem(127.0630205, 37.5091300, "��ġ1", markerId, 0);
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
		// poiData.addPOIitem(lon, lat, "�ڿ��� �����丮��", markerId, 0);
		// poiData.addPOIitem(127.061, 37.51, "��ġ2", markerId, 0);
		poiData.endPOIdata();

		// ��ġ �����͸� ����Ͽ� �������� ����
		NMapPOIdataOverlay poiDataOverlay = mOverlayManager
				.createPOIdataOverlay(poiData, null);

		// id���� 0���� ������ ��� �������̰� ǥ�õǰ� �ִ� ��ġ�� ������ �߽ɰ� ZOOM�� �缳��
		poiDataOverlay.showAllPOIdata(0);

		// �������� ���û��� or ��� ���õǴ� ���
		poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
		// �������� �̺�Ʈ ���
		mOverlayManager
				.setOnCalloutOverlayListener((OnCalloutOverlayListener) this);
		/******************* �������� ���� �ڵ� �� ********************/
		/*
		 * String slat_temp =
		 * ""+addr.get(0).getLatitude();//testadd.getLatitude(); String
		 * slon_temp = ""+addr.get(0).getLongitude();// slat =
		 * slat_temp.replace(".", ""); // ��ȯ�� ��ǥ���� . �� ���� slon =
		 * slon_temp.replace(".", ""); slat = slat.substring(0,8); // ���ڸ��� �ϳ���
		 * ���� ������ ���������� ���. slon = slon.substring(0,9); // �������� // GeoPoint pt =
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

	// ��ܹ� ����
	private static String[] dataObjects = new String[] { "��ü", "������", "�ڹ���",
			"�̼���", "��ȭ��", "�����ã���" };

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) { // �� ��ư
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
			// �����ʱ�ȭ�� ����! ���ð��� ���� ����Ǿ�ߵ� ������->������û[127.0475020, 37.5173050]
			mMapController.setMapCenter(new NGeoPoint(127.0475020, 37.5173050),
					11); // 11�� �ܷ��� 1�� �����ϸ� �ѹݵ�������
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
