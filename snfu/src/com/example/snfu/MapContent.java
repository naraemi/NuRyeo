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
	//�ȵ���̵忡�� AsyncTask�� ������ ������ UI Thread���� ����� ��Ȱ�ϰ� �����ִ� Wrapper Class�̴�.
	//�����带 ���� ���ؼ� AsyncTask Ŭ������ ��ӹ���
	
	//xml�� name �κ��� �����ϱ� ���� ��ü ����
	Vector<String> name_vec = new Vector<String>();

	//�˻�â���� ����ڰ� �Է��� ���� �޾ƿ��� ���� Ŭ���� ����
	MainActivity mainActivity = new MainActivity();
	
	//�Է��� ���� �����ϴ� ���� ����

	//utf-8������ ��ȯ�� �� �����ϴ� ���� ����
	String search;
	String uri;
	String api_key = "5158794961746e6735334f58704844";
	
	//�˻����� UTF-8������ �ٲ��־� uri�� �߰��ϰ� �� ���� ��ȯ�ϴ� �Լ�.
	//MainActivity���� ��������ִ� �Լ��̴�.
	public String nameencode(String search_st){
		try {
			//��ġ ��ȯ�� �ּ� ���ڵ�
			search = URLEncoder.encode(search_st,"UTF-8");
			//������ ����Ʈ �ּ� ����
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
	
	//������Ʈ�� �����ϱ� ���ؼ� url Ŭ������ ����
	URL url;

	//xml���� �о����� ������ ����
	String tag_name = "",name="";
	
	//����� �����Ͱ� �о��������� �Ǵ����ִ� ����
	boolean flag = false;
	
	//���� ����Ʈ�� �����ؼ� �����͸� �����ϴ� �κ�
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
					
					if(tag_name.equals("FAC_NAME")&&isInRowTag){
						name += xpp.getText(); //name�� �ش��ϴ� ��� �ؽ�Ʈ�� �о�帲(+=)

//						�˻� ����� " [] " �����ؼ� ��µǴ� ������ ����. �ذ��ؾ���.
//						name = name.replace("[","");
//						name = name.replace("]","");
					}
					}else if(eventType==XmlPullParser.END_TAG){
						//�±׸��� �о�帲
						tag_name=xpp.getName();
						
						//endtag�� ��쿡�� ���Ϳ� ����
						if(tag_name.equals("row")){
							//���Ϳ� ����
							name_vec.add(name);
							
							//���� �ʱ�ȭ
							name="";
							
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
