package com.example.snfu;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener ,OnItemClickListener{
	TextView tv;
	EditText editText;
	String search_st="";
	String Gu;
//현재위치값 불러오기
	private LocationManager locManager;
	Geocoder geoCoder;
	private Location myLocation = null;
	double latPoint = 0;
	double lngPoint = 0;
	//
	Geocoder mCoder;
	static double lat,lon;
	//
	/*파싱하는 부분 정의*/
	Vector<String> name_vec = new Vector<String>();
	Vector<String> code_vec = new Vector<String>();
	Vector<String> add_vec = new Vector<String>();
	Vector<String> codename_vec = new Vector<String>(); 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);	

		final HorizontialListView listview = (HorizontialListView) findViewById(R.id.listview);
		listview.setAdapter(mAdapter);
		
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, this);
	    geoCoder = new Geocoder(this, Locale.KOREAN); 
	    GetLocations();

		tv = (TextView)findViewById(R.id.textView_main);
		tv.setText("현재위치 주변의 정보");
		search_st = GetLocations();

		
/*		2. 사용자가 직접 검색을 함
*/
		// 검색창에서 사용자가 입력한 값 받아오기
		editText=(EditText)findViewById(R.id.editText1);
		
		// 검색 이미지 눌렀을 때 이벤트
		// xml을 파싱한다.
		findViewById(R.id.imageView_search).setOnClickListener(new View.OnClickListener(){	
			
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
//		 		1) xml 검색(문화공간정보 명칭 검색 api)				
//		 		2) 결과 listView에 추가
//				3) "현재위치 주변의 데이트 정보" 텍스트 뷰 -> "검색결과"로 바꾸기

				if(editText.length()==0){ //만약 검색결과가 없을 경우
					tv.setText("검색어를 입력해주세요.");
					//리스트를 초기화 시키고
					name_vec.clear();
					//초기화된 리스트 출력한다.(공백)
					listview.setAdapter(mAdapter);
				}				
				else{
					MapContent mapcontent = new MapContent();
					//검색창에 입력된 텍스트를 mapcontent에 넘겨주는 코드
					//nameencode에선 입력받은 텍스트를 utf-8로 변환하여 uri값을 반환한다.
					mapcontent.nameencode(editText.getText().toString());
					//mapcontent 실행
					mapcontent.execute(null,null,null);				
					//리스트 초기화
					name_vec.clear();
					
					while(true){
						try{
							Thread.sleep(1000); //0.1초마다 실행
							if(mapcontent.flag==true){
								name_vec=mapcontent.name_vec;
								break; //반복문 종료
							}
						}catch (Exception e){
							
						}
					}
					if(name_vec.size()==0){
						tv.setText("검색결과 없음");						
						//리스트 초기화
						name_vec.clear();	
						//초기화된 리스트 출력(공백)
						listview.setAdapter(mAdapter);
					}else{
						tv.setText("▼ 검색결과 ▼");						
						//리스트 다시 출력
						listview.setAdapter(mAdapter);
					}
				}
				//키보드 아래로 내리기
				InputMethodManager imm= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
				
			}
		});
		
		findViewById(R.id.loc_icon).setOnClickListener(new View.OnClickListener(){	
			
			public void onClick(View v) {
				search_st = GetLocations();
				Log.d("location", "button pressed");

				if(search_st.length()==0){ //만약 검색결과가 없을 경우
					tv.setText("다시시도해주세요.");
					//리스트를 초기화 시키고
					name_vec.clear();
					code_vec.clear();
					//초기화된 리스트 출력한다.(공백)
					listview.setAdapter(mAdapter);
				}				
				else{
					LocContent loccontent = new LocContent();
					loccontent.nameencode(search_st);
					loccontent.execute(null,null,null);				
					name_vec.clear();
					code_vec.clear();
					
					while(true){
						try{
							Thread.sleep(1000); //0.1초마다 실행
							if(loccontent.flag==true){
								name_vec=loccontent.name_vec;
								code_vec=loccontent.code_vec;
								break; //반복문 종료
							}
						}catch (Exception e){
							
						}
					}
					if(name_vec.size()==0){
						tv.setText("검색결과 없음");						
						//리스트 초기화
						name_vec.clear();	
						code_vec.clear();
						//초기화된 리스트 출력(공백)
						listview.setAdapter(mAdapter);
					}else{
						tv.setText("▼"+search_st+" 검색결과 ▼");						
						//리스트 다시 출력
						listview.setAdapter(mAdapter);
					}
				}				
			}
		});
		
		listview.setOnItemClickListener(this);
		
	}
	

	//지역 버튼을 눌렀을 때의 이벤트 처리.
	public void onClick(View v){
		switch (v.getId()){
		case R.id.button_gangnam:
			getAdd("강남구");
			Intent intent = new Intent(MainActivity.this, MapLayout.class);
			startActivity(intent);
			
			//인텐트 실행 시 발생하는 애니메이션을 제거한다.(액션바 때문에..)
			overridePendingTransition(0,0);
			//finish();
			
		case R.id.button_gangbuk:
			
			break;
		}
		
	}
	public void getAdd(String local)
	{
		//선택한 버튼 -> 주소와 시설이름 저장하는 함수
		LocContent loccontent = new LocContent();
		loccontent.nameencode(local);//getString(v.getId())); //
		loccontent.execute(null,null,null);				
		name_vec.clear();
		code_vec.clear();
		//codename_vec.clear();
		add_vec.clear();
		
		while(true){
			try{
				Thread.sleep(1000); //0.1초마다 실행
				if(loccontent.flag==true){
					name_vec=loccontent.name_vec;
					code_vec=loccontent.code_vec;
					add_vec=loccontent.add_vec;
					break; //반복문 종료
				}
			}
			catch (Exception e){
				
			}}
		//주소 -> 좌표 변환
		mCoder = new Geocoder(this);
		String testadd = "서울특별시 강서구 등촌로51나길 29";
		List<Address> addr=null;
		//double tes11 = testadd.getLatitude();
			try {
				addr = mCoder.getFromLocationName(testadd,5);
			} 
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			if(addr.size()==0){
				return;
				}
			else{
				for(int i = 0; i<addr.size();++i)
				{
					Address lating = addr.get(i);
					lat=lating.getLatitude();
					lon=lating.getLongitude();
				}
				}
		
		/*String testadd = "서울특별시 강서구 등촌로51나길 29 (등촌동)";
		받아온 주소를 좌표로 변환
		latPoint = testadd.getLatitude();
		lngPoint = myLocation.getLongitude();*/
	
	}

		
	
	// 하단의 가로 스크롤뷰 정의	
    private BaseAdapter mAdapter = new BaseAdapter() {         
    	  
    	public int getCount() {
    		//벡터의 길이만큼 반환한다.
    		return name_vec.size();
    		}     
    	   
    	public Object getItem(int position) {            
    		return null;        
    		}         
    	
    
    	public long getItemId(int position) {            
    		code_vec.elementAt(position);
    		
    		return 0;        
    		}         
    	
       
    	public View getView(int position, View convertView, ViewGroup parent) {
    		View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, null);            
    		Button button = (Button)retval.findViewById(R.id.button_list);
    		//벡터 리스트에 저장되어있는 값을 순서대로 String값으로 출력한다.
    		//button.setText(name_vec.toString());  //벡터 v.removeElement()
    		
    			button.setText(name_vec.elementAt(position)); 
    		
    		
    		return retval;        
    		}            
    	};	 
    	
    	
    	 public String GetLocations() {

 			if (myLocation != null) {
 				latPoint = myLocation.getLatitude();
 				lngPoint = myLocation.getLongitude();
 				try {
 					
 					List<Address> addresses;
 					addresses = geoCoder.getFromLocation(latPoint, lngPoint, 1);
 					
 					for(Address addr: addresses){
 					
 						Gu = addr.getLocality();	
 					}
 					
 				} catch (IOException e) {
 					e.printStackTrace();
 				}				
 			}
 			//초기화시켜주기
 			latPoint = 0;
			lngPoint = 0;
 			return Gu;
 		}
     public void onLocationChanged(Location location) {
 			Log.d("location", "location changed");
 			myLocation = location;
 			
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

//검색결과창에서 설명으로 넘어가는거!
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			String content = code_vec.get(arg2);
		
			
			if(content.length()==0){ //만약 검색결과가 없을 경우
				tv.setText("앗! 뭔가 잘못되써용!");
				//리스트를 초기화 시키고
				name_vec.clear();
				code_vec.clear();
				
			}				
			else{
				LocContent loccontent = new LocContent();
				loccontent.nameencode(search_st);
				loccontent.execute(null,null,null);				
				name_vec.clear();
				code_vec.clear();
				
				while(true){
					try{
						Thread.sleep(1000); //0.1초마다 실행
						if(loccontent.flag==true){
							name_vec=loccontent.name_vec;
							code_vec=loccontent.code_vec;
							break; //반복문 종료
						}
					}
					catch (Exception e){
						
					}
				}
			
	        //System.out.println(content);
	        //새로운 화면을 띄우기 위한 클래스 작성
	       // Intent intent = new Intent().setClass(this,Content.class);
	        Intent intent = new Intent(MainActivity.this, Content.class);
	        //새로운 화면에 데이터를 전달
	        intent.putExtra("content",content);
	        //새로운 화면으로 전환
	        startActivity(intent);
	    
			}
		}

	

}
