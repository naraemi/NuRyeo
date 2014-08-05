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
			//�ȵ���̵忡�� xml������ �а� �Ľ��ϴ� ��ü�� ����
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			//���ӽ����̽� ��뿩��
			factory.setNamespaceAware(true);
			//���� sax���·� �����͸� �Ľ��ϴ� ��ü ����
			XmlPullParser xpp = factory.newPullParser();
			
			for(int i=1;i<=3;i++){
				String uri="uri"+i;
				//������Ʈ�� ����
				url = new URL(uri1);
				//������Ʈ�� ���ؼ� �о�帰 xml������ �ȵ���̵忡 ����
				InputStream in = null;
				in = url.openStream();
				//xml������ �а� �Ľ��ϴ� ��ü�� �Ѱ���
				xpp.setInput(in,"UTF-8");
				
				//row �±װ� �ƴ϶��
				boolean isInRowTag = false;
				//�̺�Ʈ Ÿ���� ����
				int eventType = xpp.getEventType();
				//������ ������ �о� �帮�鼭 name�� addr�� �����س�
				while(eventType != XmlPullParser.END_DOCUMENT){
					if(eventType==XmlPullParser.START_TAG){
						//�±׸��� �о�帲
						tag_name = xpp.getName();
						if(tag_name.equals("row")){
							isInRowTag = true;
						}
					}
					else if(eventType==XmlPullParser.TEXT){
						//�±׸��� name�̰ų� �Ǵ� addr�� �� �о��
						
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
							//�±׸��� �о�帲
							tag_name=xpp.getName();
							
							//endtag�� ��쿡�� ���Ϳ� ����
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
						//���� �̺�Ʈ Ÿ���� ����
						eventType = xpp.next();
					}	
			}
				//��� �����Ͱ� ����Ǿ��ٸ�
				flag=true; //true: ������ xml������ �а� �ʿ��� �����͸� �����ؼ� ���� 


		}catch(Exception e){
			
		}
		return null;
	}
	
}
