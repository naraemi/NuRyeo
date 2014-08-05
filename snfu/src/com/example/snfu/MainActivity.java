package com.example.snfu;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
/*
 * �����Լ�
 * 8/2 ��Ʈ��ũ ������ Ȯ�ε��� ���� ��� ����ó��(������ġ��������)
 * 8/5 '��Ÿ'�� ����X �ּ� ��ȯ �Լ����� �����߻� -> �ذ�
 * 		������,���ϱ� click�̺�Ʈ Ȱ��ȭ-> �ʱ� ��ǥ�������� �ڵ����� but_click�Լ��� ����
 *     map�� �ߵ��� ����
 * */
public class MainActivity extends Activity implements LocationListener,
		OnItemClickListener {
	TextView tv;
	EditText editText;
	String search_st = "";
	String Gu;
	static int data_num = 0; // 0:�ʱⰪ / ����� � �޾ƿð������� ���� ����
	// ������ġ�� �ҷ�����
	private LocationManager locManager;
	Geocoder geoCoder;
	private Location myLocation = null;
	double latPoint = 0;
	double lngPoint = 0;
	//
	static double loc_lat=0.0,loc_lon=0.0; //��ư�� ���� �ٲ�� �� ���� �ʱⰪ
	Geocoder mCoder;
	double lat, lon;
	static Vector<Double> lat_vec = new Vector<Double>(); //�ּҺ�ȯ ��ǥ
	static Vector<Double> lon_vec = new Vector<Double>();//�ּҺ�ȯ ��ǥ
	//
	/* �Ľ��ϴ� �κ� ���� */
	static 	Vector<String> name_vec = new Vector<String>();
	static 	Vector<String> sname_vec = new Vector<String>();//�˻��� �����
	static 	Vector<String> lname_vec = new Vector<String>(); //������ġ �ֺ���
	Vector<String> code_vec = new Vector<String>();
	Vector<String> code_vec2 = new Vector<String>();
	static 	Vector<String> name_vec2 = new Vector<String>();
	Vector<String> add_vec = new Vector<String>();
	Vector<String> codename_vec = new Vector<String>();

//	String r_name=""; String r_code=""; // �˻� ��� �ü���,�ü��ڵ�
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);

		final HorizontialListView listview = (HorizontialListView) findViewById(R.id.listview);
		listview.setAdapter(mAdapter);

		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
				1000, 5, this);
		geoCoder = new Geocoder(this, Locale.KOREAN);
		GetLocations();

		tv = (TextView) findViewById(R.id.textView_main);
		tv.setText("������ġ �ֺ��� ����");
		search_st = GetLocations();

		/*
		 * 2. ����ڰ� ���� �˻��� ��
		 */
		// �˻�â���� ����ڰ� �Է��� �� �޾ƿ���
		editText = (EditText) findViewById(R.id.editText_main);

		// �˻� �̹��� ������ �� �̺�Ʈ
		// xml�� �Ľ��Ѵ�.
		findViewById(R.id.imageView_search).setOnClickListener(
				new View.OnClickListener() {

					public void onClick(View v) {

						// TODO Auto-generated method stub
						// 1) xml �˻�(��ȭ�������� ��Ī �˻� api)
						// 2) ��� listView�� �߰�
						// 3) "������ġ �ֺ��� ����Ʈ ����" �ؽ�Ʈ �� -> "�˻����"�� �ٲٱ�
						name_vec.clear();
						if (editText.length() == 0) { // ���� �˻������ ���� ���
							tv.setText("�˻�� �Է����ּ���.");
							// ����Ʈ�� �ʱ�ȭ ��Ű��
							name_vec.clear();
							// �ʱ�ȭ�� ����Ʈ ����Ѵ�.(����)
							listview.setAdapter(mAdapter);
						} else {
							//
							// ��ȭ ���� api �˻�
							//
							mapContent_search();
							
							//
							// ��ȭ�� api �˻�
							//
							if (name_vec.size() == 0) {
								//���� ��ȭ���� api�˻��� ���� �� ������ ���ٸ�, ��ȭ�� api�˻��� �Ѵ�.							
								culturalContent_search();
								
//								//�׷��� ������ ������ �˻���� ������ ���
//								if(name_vec.size()==0){
//									tv.setText("�˻���� ����");
//									// ����Ʈ �ʱ�ȭ
//									name_vec.clear();
//									// �ʱ�ȭ�� ����Ʈ ���(����)
//									listview.setAdapter(mAdapter);
//								}else{//������ �ִٸ� �� ������ ���								
									for(int i=0;i<name_vec.size();i++){
										// elementAt���� �޾ƿ��� \n������ �޾ƿ����� ������ �־���..
										// �׷��� std.length()-1���༭ \n�� ����
										String std1=name_vec.elementAt(i);
										String std2=name_vec.elementAt(i);
										std1=std1.substring(0, std1.length()-1);
										if(std1.equals(editText.getText().toString())){
											name_vec.clear();
											name_vec.add(std2);
											break;
										}
										else if(i==name_vec.size()-1&&!std1.equals(editText.getText().toString())){
											name_vec.clear();
										}
									}
								}
						}
								if(name_vec.size()==0){
									tv.setText("�˻���� ����");
									// ����Ʈ �ʱ�ȭ
									name_vec.clear();
									// �ʱ�ȭ�� ����Ʈ ���(����)
									listview.setAdapter(mAdapter);
								}else{
									tv.setText("�� �˻���� ��");
									// ����Ʈ �ٽ� ���
									listview.setAdapter(mAdapter);	
								}
//							}
						// Ű���� �Ʒ��� ������
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(editText.getWindowToken(),
								0);
					}
				});
		
		findViewById(R.id.loc_icon).setOnClickListener( //������ġ�� �ü��˻�
				new View.OnClickListener() {

					public void onClick(View v) {
						search_st = GetLocations();
						Log.d("location", "button pressed");

						if (search_st.length() == 0) { // ���� �˻������ ���� ���
							tv.setText("�� ��Ʈ��ũ ���� ���¸� Ȯ�����ּ���. ��");
							// ����Ʈ�� �ʱ�ȭ ��Ű��
							name_vec.clear();
						//	name_vec.clear();
							code_vec.clear();
							// �ʱ�ȭ�� ����Ʈ ����Ѵ�.(����)
							listview.setAdapter(mAdapter);
						} else {
							LocContent loccontent = new LocContent();
							data_num=5;
							loccontent.nameencode(search_st, data_num);
							loccontent.execute(null, null, null);
							name_vec.clear();
						//	name_vec.clear();
							code_vec.clear();
							// data_num=0;

							while (true) {
								try {
									Thread.sleep(1000); // 0.1�ʸ��� ����
									if (loccontent.flag == true) {
										name_vec = loccontent.name_vec;
									//	name_vec= loccontent.name_vec; //�����
										code_vec = loccontent.code_vec;
										break; // �ݺ��� ����
									}
								} catch (Exception e) {

								}
							}
							if (name_vec.size() == 0) {
									tv.setText("�˻���� ����");
									// ����Ʈ �ʱ�ȭ
									name_vec.clear();
									//name_vec.clear();
									code_vec.clear();
									// �ʱ�ȭ�� ����Ʈ ���(����)
							}
							else {
								tv.setText("��" + search_st + " �˻���� ��");
								// ����Ʈ �ٽ� ���
								listview.setAdapter(mAdapter);
							}
						}
					}
				});

		listview.setOnItemClickListener(this);

	}
	
	public void mapContent_search(){
		MapContent mapcontent = new MapContent();
		// �˻�â�� �Էµ� �ؽ�Ʈ�� mapcontent�� �Ѱ��ִ� �ڵ�
		// nameencode���� �Է¹��� �ؽ�Ʈ�� utf-8�� ��ȯ�Ͽ� uri���� ��ȯ�Ѵ�.
		mapcontent
				.nameencode(editText.getText().toString());
		// mapcontent ����
		mapcontent.execute(null, null, null);
		// ����Ʈ �ʱ�ȭ
		name_vec.clear();

		while (true) {
			try {
				Thread.sleep(1000); // 0.1�ʸ��� ����
				if (mapcontent.flag == true) {
					name_vec = mapcontent.name_vec;
					code_vec=mapcontent.fac_code_vec;
					break; // �ݺ��� ����
				}
			} catch (Exception e) {

			}
		}
	}
	
	public void culturalContent_search(){
		CulturalContent culturalcontent = new CulturalContent();
		//culturalcontent ����
		culturalcontent.execute(null,null,null);
		// ����Ʈ �ʱ�ȭ
		name_vec.clear();
		
		while(true){
			try{
				Thread.sleep(1000); //0.1�ʸ��� ����
				if(culturalcontent.flag==true){
					name_vec=culturalcontent.name_vec;
					break; //�ݺ��� ����
				}
			}catch (Exception e){
				
			}
		}
	}

	// ���� ��ư�� ������ ���� �̺�Ʈ ó��.
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_gangnam:
			//������û[127.0475020, 37.5173050]
			loc_lon=127.0475020; loc_lat=37.5173050;
			but_click("������");
			break;
		
		case R.id.button_gangdong:
			//������û 127.1237708&y=37.5301260
			loc_lon=127.1237708; loc_lat=37.5301260;
			but_click("������");
			break;
		case R.id.button_gangbuk:
			//127.0254820&y=37.6397480
			loc_lon=127.0254820; loc_lat=37.6397480;
			but_click("���ϱ�");
			break;
		case R.id.button_nowon:
			loc_lon=127.1237708; loc_lat=37.5301260;
			but_click("�����");
			break;
		}

	}
public void but_click(String str)
{
	getAdd(str); // "������"�� �ü��������� �Ľ�, vec�� ����
	transAdd(); // ������ ��ȭ������ �ּҵ��� ��ǥ������ ����
	Intent intent = new Intent(this, MapLayout.class);
	startActivity(intent);

	// ����Ʈ ���� �� �߻��ϴ� �ִϸ��̼��� �����Ѵ�.(�׼ǹ� ������..)
	overridePendingTransition(0, 0);
	// finish();

}
	public void getAdd(String local) {
		// ������ ���� ��ư -> �ּҿ� �ü��̸� �����ϴ� �Լ�
		LocContent loccontent = new LocContent();
		data_num = 20;
		loccontent.nameencode(local, data_num);// getString(v.getId())); //
		loccontent.execute(null, null, null);
		name_vec.clear();
		code_vec.clear();
		codename_vec.clear();
		add_vec.clear();

		while (true) {
			try {
				Thread.sleep(1000); // 0.1�ʸ��� ����
				if (loccontent.flag == true) {
					name_vec = loccontent.name_vec;
					code_vec = loccontent.code_vec;
					add_vec = loccontent.add_vec;
					break; // �ݺ��� ����
				}
			} catch (Exception e) {

			}
		}

	}

	public void transAdd() {

		// �ּ� -> ��ǥ ��ȯ
		mCoder = new Geocoder(this);
		String testadd = "";// "����Ư���� ������ ���̷�51���� 29";
		data_num = add_vec.size();
		lat_vec.clear(); lon_vec.clear();
		List<Address> addr = null;
		// double tes11 = testadd.getLatitude();
		for (int location = 0; location < data_num ; ++location) {

			try {
				testadd = add_vec.elementAt(location);
				// Ư������ ���� ���ڿ� ����
				Pattern regex = Pattern.compile(",");
				Pattern regex2 = Pattern.compile("\\(");//Ư�����ڴ� �տ� \\�� �־���Դϴ�
				Matcher regexMatcher = regex.matcher(testadd);
				Matcher regexMatcher2 = regex2.matcher(testadd);

				if (regexMatcher.find()) {
					String[] temp = testadd.split(",");
					testadd = temp[0];
				}
				if (regexMatcher2.find()) {
					String[] temp = testadd.split("\\(");
					testadd = temp[0];
				}

				addr = mCoder.getFromLocationName(testadd, 1);
				testadd="";//�ʱ�ȭ
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			// ����� ���� ���� ���
			if (addr.size() == 0) {
				lat_vec.addElement(0.0);
				lon_vec.addElement(0.0);
				// return;
			} else {
				Address lating = addr.get(0);

				lat = lating.getLatitude();
				lat_vec.addElement(lat);
				lon = lating.getLongitude();
				lon_vec.addElement(lon);

			}
			// return;
		}

	}

	// �ϴ��� ���� ��ũ�Ѻ� ����
	private BaseAdapter mAdapter = new BaseAdapter() {

		public int getCount() {
			// ������ ���̸�ŭ ��ȯ�Ѵ�.
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
			View retval = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.listitem, null);
			Button button = (Button) retval.findViewById(R.id.button_list);
			// ���� ����Ʈ�� ����Ǿ��ִ� ���� ������� String������ ����Ѵ�.
			// button.setText(name_vec.toString()); //���� v.removeElement()

			String std = name_vec.elementAt(position);
			button.setText(std.substring(0, std.length()-1));

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

				for (Address addr : addresses) {

					Gu = addr.getLocality();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			Gu="";//����ó��
		}
		// �ʱ�ȭ�����ֱ�
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

	// �˻����â���� �������� �Ѿ�°�!
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		String content = code_vec.get(arg2);

		if (content.length() == 0) { // ���� �˻������ ���� ���
			tv.setText("��! ���� �߸��ǽ��!");
			// ����Ʈ�� �ʱ�ȭ ��Ű��
			//name_vec.clear();
			//code_vec.clear();

		} else {
			LocContent loccontent = new LocContent();
			loccontent.nameencode(search_st, 1);
			loccontent.execute(null, null, null);
		//	name_vec.clear();
		//	code_vec.clear();

			while (true) {
				try {
					Thread.sleep(1000); // 0.1�ʸ��� ����
					if (loccontent.flag == true) {
						name_vec2 = loccontent.name_vec;
						code_vec2 = loccontent.code_vec;
						break; // �ݺ��� ����
					}
				} catch (Exception e) {

				}
			}

			// System.out.println(content);
			// ���ο� ȭ���� ���� ���� Ŭ���� �ۼ�
			// Intent intent = new Intent().setClass(this,Content.class);
			Intent intent = new Intent(MainActivity.this, Content.class);
			// ���ο� ȭ�鿡 �����͸� ����
			intent.putExtra("content", content);
			// ���ο� ȭ������ ��ȯ
			startActivity(intent);

		}
	}

}
