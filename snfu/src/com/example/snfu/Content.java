package com.example.snfu;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Content extends Activity {
	String desc = "";
	String imgAdd = "";
	TextView des_tv;
	ImageView imgView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.data);
		setContentView(R.layout.web);
		des_tv = (TextView) findViewById(R.id.des_tv);
		// 이전 액티비티에서 넘어온 데이터를 받음
		Intent intent = getIntent();

		// 데이터 저장
		String data = intent.getExtras().getString("content");

		// System.out.println("data:"+data);
		// 텍스트뷰 객체 선언
		/*
		TextView des_tv = new TextView(this);
		// 텍스트뷰에 데이터를 붙임

		// 보여질 내용일 많아질경우를 위해 스크롤뷰 생성
		ScrollView sv = new ScrollView(this);
		// 스크롤뷰에 텍스트뷰를 붙임
		sv.addView(des_tv);

		// 스크롤뷰를 액티비티에 붙임
		setContentView(sv);
		*/

		DataContent datacontent = new DataContent();
		datacontent.nameencode(data);//data
		datacontent.execute(null, null, null);
		desc = "";
		imgAdd = "";

		while (true) {
			try {
				Thread.sleep(1000); // 0.1초마다 실행
				if (datacontent.flag == true) {
					desc = datacontent.FAC_DESC;
					imgAdd = datacontent.IMG_ADD;
					break; // 반복문 종료
				}
			} catch (Exception e) {
			}
		}

		// 이미지와 설명 띄우기
		/*Bitmap imgBitmap = GetImageFromURL(imgAdd);

		if (imgBitmap != null) {
			//ImageView imgView = new ImageView(this);
			ImageView imgView = (ImageView) findViewById(R.id.des_iv);
			imgView.setImageBitmap(imgBitmap);
		}
*/
		WebView web = (WebView) findViewById(R.id.webapp);
		// 자바스크립트 허용
		web.getSettings().setJavaScriptEnabled(true);
		 
		// 스크롤바 없애기
		web.setHorizontalScrollBarEnabled(false);
		web.setVerticalScrollBarEnabled(false);
		web.setBackgroundColor(1);

		web.loadData(desc, "text/html; charset=utf-8", null );
		//des_tv.setText(Html.fromHtml(desc));
		ImageView imgView = (ImageView) findViewById(R.id.des_iv);
		Picasso.with(getBaseContext()).load(imgAdd).into(imgView);
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