package com.example.snfu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
/* Content.java : 검색결과창에서 선택된 시설의 설명과 이미지를 받아오는 함수
 * 8/2 update - 필요없는 주석/코드 정리
 * 8/5 name변수 추가 -> 시설명 받아와서 tv에 시설이름 띄우기, 상단 앱이름 지우기
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
		// 이전 액티비티에서 넘어온 데이터를 받음
		Intent intent = getIntent();

		// 데이터 저장
		String data = intent.getExtras().getString("content");

		DataContent datacontent = new DataContent();
		datacontent.nameencode(data);//data
		datacontent.execute(null, null, null);
		desc = "";
		imgAdd = "";name="";

		while (true) {
			try {
				Thread.sleep(1000); // 0.1초마다 실행
				if (datacontent.flag == true) {
					desc = datacontent.FAC_DESC;
					imgAdd = datacontent.IMG_ADD;
					name = datacontent.FAC_NAME;
					break; // 반복문 종료
				}
			} catch (Exception e) {
			}
		}

		// 이미지와 설명 띄우기
		
		WebView web = (WebView) findViewById(R.id.webapp);
		// 자바스크립트 허용
		web.getSettings().setJavaScriptEnabled(true);
		 
		// 스크롤바 없애기
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