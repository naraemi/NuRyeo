package com.example.snfu;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class Content extends Activity {
	String desc = "";
	String imgAdd = "";
	TextView des_tv;
	ImageView imgView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data);
		
		des_tv = (TextView) findViewById(R.id.des_tv);
		// ���� ��Ƽ��Ƽ���� �Ѿ�� �����͸� ����
		Intent intent = getIntent();

		// ������ ����
		String data = intent.getExtras().getString("content");

		// System.out.println("data:"+data);
		// �ؽ�Ʈ�� ��ü ����
		
		/*TextView des_tv = new TextView(this);
		// �ؽ�Ʈ�信 �����͸� ����

		// ������ ������ ��������츦 ���� ��ũ�Ѻ� ����
		ScrollView sv = new ScrollView(this);
		// ��ũ�Ѻ信 �ؽ�Ʈ�並 ����
		sv.addView(des_tv);

		// ��ũ�Ѻ並 ��Ƽ��Ƽ�� ����
		setContentView(sv);
		*/

		DataContent datacontent = new DataContent();
		datacontent.nameencode(data);
		datacontent.execute(null, null, null);
		desc = "";
		imgAdd = "";

		while (true) {
			try {
				Thread.sleep(1000); // 0.1�ʸ��� ����
				if (datacontent.flag == true) {
					desc = datacontent.FAC_DESC;
					imgAdd = datacontent.IMG_ADD;
					break; // �ݺ��� ����
				}
			} catch (Exception e) {
			}
		}

		// �̹����� ���� ����
		Bitmap imgBitmap = GetImageFromURL(imgAdd);

		if (imgBitmap != null) {
			//ImageView imgView = new ImageView(this);
			ImageView imgView = (ImageView) findViewById(R.id.des_iv);
			imgView.setImageBitmap(imgBitmap);
		}

		des_tv.setText(desc);
		desc = "";
		imgAdd = "";
	

	}

	private Bitmap GetImageFromURL(String strImageURL) {
		Bitmap imgBitmap = null;
		try {
			URL url = new URL(strImageURL);

			URLConnection conn = url.openConnection();

			conn.connect();

			int nSize = conn.getContentLength();

			BufferedInputStream bis = new BufferedInputStream(
					conn.getInputStream(), nSize);

			imgBitmap = BitmapFactory.decodeStream(bis);

			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return imgBitmap;

	}
}