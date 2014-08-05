package com.example.snfu;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.AsyncTask;

public class DataContent extends AsyncTask<Void, Void, Void> {

	
	String FAC_DESC="";//�ü� ����
	String IMG_ADD="";//�̹��� �ּ�
	String FAC_NAME="";

	String search;
	String uri;
	String api_key = "5158794961746e6735334f58704844";

	public String nameencode(String search_st){
		try {
			//��ġ ��ȯ�� �ּ� ���ڵ�
			search = URLEncoder.encode(search_st,"UTF-8");
			//������ ����Ʈ �ּ� ����
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
			//�ȵ���̵忡�� xml������ �а� �Ľ��ϴ� ��ü�� ����
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			//���ӽ����̽� ��뿩��
			factory.setNamespaceAware(true);
			//���� sax���·� �����͸� �Ľ��ϴ� ��ü ����
			XmlPullParser xpp = factory.newPullParser();
			
			//������Ʈ�� ����
			url = new URL(uri);
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
						//�±׸��� �о�帲
						tag_name=xpp.getName();
						
						//endtag�� ��쿡�� ���Ϳ� ����
						if(tag_name.equals("row")){
							
							isInRowTag=true;
						}
					}
					//���� �̺�Ʈ Ÿ���� ����
					eventType = xpp.next();
				}
				//��� �����Ͱ� ����Ǿ��ٸ�
				flag=true; //true: ������ xml������ �а� �ʿ��� �����͸� �����ؼ� ���� �Ϸ�� ����	
//			}
		}catch(Exception e){
			
		}
		return null;
	}
	
}
