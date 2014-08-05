package com.example.snfu;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.AsyncTask;

public class DataContent extends AsyncTask<Void, Void, Void> {

	
	String FAC_DESC="";//시설 설명
	String IMG_ADD="";//이미지 주소
	String FAC_NAME="";

	String search;
	String uri;
	String api_key = "5158794961746e6735334f58704844";

	public String nameencode(String search_st){
		try {
			//위치 변환할 주소 인코딩
			search = URLEncoder.encode(search_st,"UTF-8");
			//연결할 사이트 주소 선택
			uri = "http://openAPI.seoul.go.kr:8088/"
					+api_key
					+"/xml/SearchCulturalFacilitiesDetailService/1/1/"
					+search
					+"/";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uri;
	}

	URL url;

	String tag_name = "";
	
	boolean flag = false;
	

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
					
					if(tag_name.equals("FAC_DESC")&&isInRowTag){
						FAC_DESC += xpp.getText(); 
					}
					else if(tag_name.equals("MAIN_IMG")&&isInRowTag){
						IMG_ADD += xpp.getText(); 
					}
					else if (tag_name.equals("FAC_NAME")&&isInRowTag){
						FAC_NAME+= xpp.getText(); 
					}
					
					}else if(eventType==XmlPullParser.END_TAG){
						//태그명을 읽어드림
						tag_name=xpp.getName();
						
						//endtag일 경우에만 벡터에 저장
						if(tag_name.equals("row")){
							
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
