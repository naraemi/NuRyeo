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
		
		//Ŀ���ҹ� ����
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.listbar);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);	
		
		HorizontialListView listview = (HorizontialListView) findViewById(R.id.listview);
		listview.setAdapter(mAdapter);
	}
	
	//��ܹ� ����
	private static String[] dataObjects = new String[]{
	"��ü",        
	"������",    		
	"�ڹ���",    
	"�̼���",    
	"��ȭ��",    
	"�����ã���"}; 
	
//	@Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) { // �� ��ư
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
