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
 * */
public class MainActivity extends Activity implements LocationListener,
		OnItemClickListener {
	TextView tv;
	EditText editText;
	String search_st = "";
	String Gu;
	static int data_num = 0; // 0:�ʱⰪ ����� � �޾ƿð������� ���� ����
	// ������ġ�� �ҷ�����
	private LocationManager locManager;
	Geocoder geoCoder;
	private Location myLocation = null;
	double latPoint = 0;
	double lngPoint = 0;
	//
	Geocoder mCoder;
	double lat, lon;
	static Vector<Double> lat_vec = new Vector<Double>();
	static Vector<Double> lon_vec = new Vector<Double>();
	//
	/* �Ľ��ϴ� �κ� ���� */
	static 	Vector<String> name_vec = new Vector<String>();
	Vector<String> code_vec = new Vector<String>();
	Vector<String> add_vec = new Vector<String>();
	Vector<String> codename_vec = new Vector<String>();

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
		editText = (EditText) findViewById(R.id.editText1);

		// �˻� �̹��� ������ �� �̺�Ʈ
		// xml�� �Ľ��Ѵ�.
		findViewById(R.id.imageView_search).setOnClickListener(
				new View.OnClickListener() {

					public void onClick(View v) {

						// TODO Auto-generated method stub
						// 1) xml �˻�(��ȭ�������� ��Ī �˻� api)
						// 2) ��� listView�� �߰�
						// 3) "������ġ �ֺ��� ����Ʈ ����" �ؽ�Ʈ �� -> "�˻����"�� �ٲٱ�

						if (editText.length() == 0) { // ���� �˻������ ���� ���
							tv.setText("�˻�� �Է����ּ���.");
							// ����Ʈ�� �ʱ�ȭ ��Ű��
							name_vec.clear();
							// �ʱ�ȭ�� ����Ʈ ����Ѵ�.(����)
							listview.setAdapter(mAdapter);
						} else {
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
										break; // �ݺ��� ����
									}
								} catch (Exception e) {

								}
							}
							if (name_vec.size() == 0) {
								tv.setText("�˻���� ����");
								// ����Ʈ �ʱ�ȭ
								name_vec.clear();
								// �ʱ�ȭ�� ����Ʈ ���(����)
								listview.setAdapter(mAdapter);
							} else {
								tv.setText("�� �˻���� ��");
								// ����Ʈ �ٽ� ���
								listview.setAdapter(mAdapter);
							}
						}
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
							code_vec.clear();
							// �ʱ�ȭ�� ����Ʈ ����Ѵ�.(����)
							listview.setAdapter(mAdapter);
						} else {
							LocContent loccontent = new LocContent();
							// data_num=5;
							loccontent.nameencode(search_st, 5);
							loccontent.execute(null, null, null);
							name_vec.clear();
							code_vec.clear();
							// data_num=0;

							while (true) {
								try {
									Thread.sleep(1000); // 0.1�ʸ��� ����
									if (loccontent.flag == true) {
										name_vec = loccontent.name_vec;
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
								code_vec.clear();
								// �ʱ�ȭ�� ����Ʈ ���(����)
								listview.setAdapter(mAdapter);
							} else {
								tv.setText("��" + search_st + " �˻���� ��");
								// ����Ʈ �ٽ� ���
								listview.setAdapter(mAdapter);
							}
						}
					}
				});

		listview.setOnItemClickListener(this);

	}

	// ���� ��ư�� ������ ���� �̺�Ʈ ó��.
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_gangnam:
			getAdd("������"); // "������"�� �ü��������� �Ľ�, vec�� ����
			transAdd(); // ������ ��ȭ������ �ּҵ��� ��ǥ������ ����
		//	MapLayout veccontent = new MapLayout();
		//	veccontent.latvectorcopy(lat_vec);
		//	veccontent.lonvectorcopy(lon_vec);
			Intent intent = new Intent(this, MapLayout.class);
			startActivity(intent);

			// ����Ʈ ���� �� �߻��ϴ� �ִϸ��̼��� �����Ѵ�.(�׼ǹ� ������..)
			overridePendingTransition(0, 0);
			// finish();

		case R.id.button_gangbuk:

			break;
		}

	}

	public void getAdd(String local) {
		// ������ ���� ��ư -> �ּҿ� �ü��̸� �����ϴ� �Լ�
		LocContent loccontent = new LocContent();
		data_num = 15;
		loccontent.nameencode(local, data_num);// getString(v.getId())); //
		loccontent.execute(null, null, null);
		name_vec.clear();
		code_vec.clear();
		// codename_vec.clear();
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
		String testadd = "����Ư���� ������ �б�����12�� 51, ��ݺ��� 3�� ";// "����Ư���� ������ ���̷�51���� 29";
		
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
			name_vec.clear();
			code_vec.clear();

		} else {
			LocContent loccontent = new LocContent();
			loccontent.nameencode(search_st, 1);
			loccontent.execute(null, null, null);
			name_vec.clear();
			code_vec.clear();

			while (true) {
				try {
					Thread.sleep(1000); // 0.1�ʸ��� ����
					if (loccontent.flag == true) {
						name_vec = loccontent.name_vec;
						code_vec = loccontent.code_vec;
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
