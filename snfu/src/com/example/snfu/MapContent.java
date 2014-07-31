package com.example.snfu;

import android.app.Activity;
import android.content.Intent;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;

public class MapContent extends AsyncTask<Void, Void, Void> {
	//안드로이드에서 AsyncTask는 쓰레드 관리와 UI Thread와의 통신을 원활하게 도와주는 Wrapper Class이다.
	//쓰레드를 쓰기 위해서 AsyncTask 클래스를 상속받음
	
	//xml의 name 부분을 저장하기 위한 객체 선언
	Vector<String> name_vec = new Vector<String>();

	//검색창에서 사용자가 입력한 값을 받아오기 위해 클래스 선언
	MainActivity mainActivity = new MainActivity();
	
	//입력한 값을 저장하는 변수 선언

	//utf-8값으로 변환한 값 저장하는 변수 선언
	String search;
	String uri;
	String api_key = "5158794961746e6735334f58704844";
	
	//검색값을 UTF-8값으로 바꿔주어 uri에 추가하고 그 값을 반환하는 함수.
	//MainActivity에서 실행시켜주는 함수이다.
	public String nameencode(String search_st){
		try {
			//위치 변환할 주소 인코딩
			search = URLEncoder.encode(search_st,"UTF-8");
			//연결할 사이트 주소 선택
			uri = "http://openAPI.seoul.go.kr:8088/"
					+api_key
					+"/xml/SearchCulturalFacilitiesNameService/1/5/"
					+search
					+"/";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uri;
	}
	
	//웹사이트에 연결하기 위해서 url 클래스를 적용
	URL url;

	//xml에서 읽어드려서 저장할 변수
	String tag_name = "",name="";
	
	//제대로 데이터가 읽어졌는지를 판단해주는 변수
	boolean flag = false;
	
	//실제 사이트에 접속해서 데이터를 추출하는 부분
	@Override
	protected Void doInBackground(Void... params){
		try{
			//안드로이드에서 xml문서를 읽고 파싱하는 객체를 선언
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			//네임스페이스 사용여부
			factory.setNamespaceAware(true);
			//실제 sax형태로 데이터를 파싱하는 객체 선언
			XmlPullParser xpp = factory.newPullParser();
			
			//웹사이트에 접속
			url = new URL(uri);
			//웹사이트를 통해서 읽어드린 xml문서를 안드로이드에 저장
			InputStream in = null;
			in = url.openStream();
			//xml문서를 읽고 파싱하는 객체에 넘겨줌
			xpp.setInput(in,"UTF-8");
			
			//row 태그가 아니라면
			boolean isInRowTag = false;
			//이벤트 타입을 얻어옴
			int eventType = xpp.getEventType();
			//문서의 끝까지 읽어 드리면서 name과 addr을 추출해냄
			while(eventType != XmlPullParser.END_DOCUMENT){
				if(eventType==XmlPullParser.START_TAG){
					//태그명을 읽어드림
					tag_name = xpp.getName();
					if(tag_name.equals("row")){
						isInRowTag = true;
					}
				}
				else if(eventType==XmlPullParser.TEXT){
					//태그명이 name이거나 또는 addr일 때 읽어옴
					
					if(tag_name.equals("FAC_NAME")&&isInRowTag){
						name += xpp.getText(); //name에 해당하는 모든 텍스트를 읽어드림(+=)

//						검색 결과가 " [] " 포함해서 출력되는 문제가 있음. 해결해야함.
//						name = name.replace("[","");
//						name = name.replace("]","");
					}
					}else if(eventType==XmlPullParser.END_TAG){
						//태그명을 읽어드림
						tag_name=xpp.getName();
						
						//endtag일 경우에만 벡터에 저장
						if(tag_name.equals("row")){
							//벡터에 저장
							name_vec.add(name);
							
							//변수 초기화
							name="";
							
							isInRowTag=true;
						}
					}
					//다음 이벤트 타입을 저장
					eventType = xpp.next();
				}
				//모든 데이터가 저장되었다면
				flag=true; //true: 지정된 xml파일을 읽고 필요한 데이터를 추출해서 저장 완료된 상태	
//			}
		}catch(Exception e){
			
		}
		return null;
	}
	
}
