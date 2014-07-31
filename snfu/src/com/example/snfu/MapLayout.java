package com.example.snfu;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;

public class MapLayout extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//커스텀바 정의
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.listbar);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);	
		
		HorizontialListView listview = (HorizontialListView) findViewById(R.id.listview);
		listview.setAdapter(mAdapter);
	}
	
	//상단바 정의
	private static String[] dataObjects = new String[]{
	"전체",        
	"공연장",    		
	"박물관",    
	"미술관",    
	"문화재",    
	"★즐겨찾기★"}; 
	
//	@Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) { // 백 버튼
//        	onBackPressed();
//        }
//        return true;
//    }


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
		View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.mapactionbar, null);            
		Button button = (Button)retval.findViewById(R.id.button_map);
		button.setText(dataObjects[position]); 
		return retval;        
		}            
	};	 
}
