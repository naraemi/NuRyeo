package com.example.snfu;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class CulturalContent extends AsyncTask<Void, Void, Void> {

	Vector<String> num_vec = new Vector<String>();
	Vector<String> name_vec = new Vector<String>();
	Vector<String> board_vec = new Vector<String>();
	Vector<String> gu_vec = new Vector<String>();
	Vector<String> addr_vec = new Vector<String>();

	
	Vector<String> search;
//	String uri1, uri2, uri3;
	String api_key = "5158794961746e6735334f58704844";

	URL url;

	String tag_name = "";
	String num="",name="",board="",gu="",addr="";
	
	
	boolean flag = false;
	
	String uri1 = "http://openAPI.seoul.go.kr:8088/"
			+api_key
			+"/xml/ListCulturalAssetsInfo/1/500";
	String uri2 = "http://openAPI.seoul.go.kr:8088/"
			+api_key
			+"/xml/ListCulturalAssetsInfo/501/1000";
	String uri3 = "http://openAPI.seoul.go.kr:8088/"
			+api_key
			+"/xml/ListCulturalAssetsInfo/1001/1482";	

	@Override
	protected Void doInBackground(Void... params){
		try{
			//안드로이드에서 xml문서를 읽고 파싱하는 객체를 선언
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			//네임스페이스 사용여부
			factory.setNamespaceAware(true);
			//실제 sax형태로 데이터를 파싱하는 객체 선언
			XmlPullParser xpp = factory.newPullParser();
			
			for(int i=1;i<=3;i++){
				String uri="uri"+i;
				//웹사이트에 접속
				url = new URL(uri1);
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
						
						if(tag_name.equals

("MANAGE_NUM")&&isInRowTag){
							num += xpp.getText(); 
						}
						else if(tag_name.equals

("NAME_KOR")&&isInRowTag){
							name += xpp.getText(); 
						}
						else if(tag_name.equals

("BOARD_KOR")&&isInRowTag){
							board += xpp.getText(); 
						}
						else if(tag_name.equals

("STAND_REGION")&&isInRowTag){
							gu += xpp.getText(); 
						}
						else if(tag_name.equals

("STAND_ADDR")&&isInRowTag){
							addr += xpp.getText(); 
						}
						
						}else if(eventType==XmlPullParser.END_TAG){
							//태그명을 읽어드림
							tag_name=xpp.getName();
							
							//endtag일 경우에만 벡터에 저장
							if(tag_name.equals("row")){
								num_vec.add(num);
								name_vec.add(name);
								board_vec.add(board);
								gu_vec.add(gu);
								addr_vec.add(addr);
								
								num="";
								name="";
								board="";
								gu="";
								addr="";
								
								isInRowTag=true;
							}
						}
						//다음 이벤트 타입을 저장
						eventType = xpp.next();
					}	
			}
				//모든 데이터가 저장되었다면
				flag=true; //true: 지정된 xml파일을 읽고 필요한 데이터를 추출해서 저장 


		}catch(Exception e){
			
		}
		return null;
	}
	
}
