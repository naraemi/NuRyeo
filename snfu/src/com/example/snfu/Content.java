package com.example.snfu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
/* Content.java : �˻����â���� ���õ� �ü��� ����� �̹����� �޾ƿ��� �Լ�
 * 8/2 update - �ʿ���� �ּ�/�ڵ� ����
 * 8/5 name���� �߰� -> �ü��� �޾ƿͼ� tv�� �ü��̸� ����, ��� ���̸� �����
 * */
public class Content extends Activity {
	String desc = "";
	String imgAdd = "";
	String name="";
	TextView des_tv;
	ImageView imgView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.data);
		setContentView(R.layout.web);
		des_tv = (TextView) findViewById(R.id.des_tv);
		// ���� ��Ƽ��Ƽ���� �Ѿ�� �����͸� ����
		Intent intent = getIntent();

		// ������ ����
		String data = intent.getExtras().getString("content");

		DataContent datacontent = new DataContent();
		datacontent.nameencode(data);//data
		datacontent.execute(null, null, null);
		desc = "";
		imgAdd = "";name="";

		while (true) {
			try {
				Thread.sleep(1000); // 0.1�ʸ��� ����
				if (datacontent.flag == true) {
					desc = datacontent.FAC_DESC;
					imgAdd = datacontent.IMG_ADD;
					name = datacontent.FAC_NAME;
					break; // �ݺ��� ����
				}
			} catch (Exception e) {
			}
		}

		// �̹����� ���� ����
		
		WebView web = (WebView) findViewById(R.id.webapp);
		// �ڹٽ�ũ��Ʈ ���
		web.getSettings().setJavaScriptEnabled(true);
		 
		// ��ũ�ѹ� ���ֱ�
		web.setHorizontalScrollBarEnabled(false);
		web.setVerticalScrollBarEnabled(false);
		web.setBackgroundColor(1);
		TextView tv = (TextView) findViewById(R.id.nametextView1);
		
		
		tv.setText(name);
		web.loadData(desc, "text/html; charset=utf-8", null );
		ImageView imgView = (ImageView) findViewById(R.id.des_iv);
		Picasso.with(getBaseContext()).load(imgAdd).into(imgView);
		desc = "";
		imgAdd = "";name="";
	

	}

}