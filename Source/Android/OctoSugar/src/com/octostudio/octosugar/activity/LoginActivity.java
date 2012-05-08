package com.octostudio.octosugar.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;  
import android.content.DialogInterface;  
import android.content.Intent;
import android.os.Bundle;  
import android.util.Log;
import android.view.Gravity;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.widget.Button;  
import android.widget.EditText;  
import android.widget.PopupWindow; 
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import android.util.Log;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.octostudio.octosugar.*;

public class LoginActivity extends SugarActivity {
	String PassWord;
	String Account;
	
	ProgressDialog m_Dialog;
	AlertDialog.Builder ad;
	View LoginView;
	LoginActivity mMain;
	AlertDialog dlg;
	/*
	* �����û���½�ĶԻ���
	* ��½�������������ť
	* 1��ťΪ��½
	* 2��ťΪ����½����
	* */
	private void CreateLoginAlert()
	{
		LayoutInflater inflater = LayoutInflater.from(this); 
		LoginView = inflater.inflate(R.layout.loginview, null); 

		ad =new AlertDialog.Builder(this);
		ad.setTitle("�˺ŵ�½");
		ad.setView(LoginView);
		dlg= ad.create();
		/*
		*/
		dlg.setButton("��½", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface arg0, int arg1) {
				EditText password= (EditText)LoginView.findViewById(R.id.txt_password);
				EditText account =(EditText)LoginView.findViewById(R.id.txt_username);
				PassWord=password.getText().toString();
				Account=account.getText().toString();
				
				//���ɵ�½�Ի���
				m_Dialog=ProgressDialog.show(mMain, "��ȴ�...", "����Ϊ���½...",true);
				mRedrawHandler.sleep(100); 
			}
			});

		dlg.show(); 
		
	}
	/*
	*��ʱ�߳�����֤��
	* */
	private RefreshHandler mRedrawHandler = new RefreshHandler();
	class RefreshHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			try{
				//��������ӿڣ�ʵ�ֵ�½ָ��
				Boolean flags= true;///UserDataServiceHelper.Login(Account, PassWord); 
				if(flags) 
				{
		            // ������ʽ��ͼ
		            Intent intent = new Intent(LoginActivity.this, OctoSugarActivity.class);

					//�����½��Ϣ
					SaveUserDate();
					//��ʾ��½�ɹ�
					Toast.makeText(mMain, "��½�ɹ�", Toast.LENGTH_SHORT).show();
					SugarContext.isLogin = true;
					SugarContext.sessionId = 0x1234;
					
		            // ���µ�Activity
		            startActivity(intent);
		            
					mMain.finish();
					
					
				}else
				{
					//ʧ�� ��ʾ������Ϣ
					Toast.makeText(mMain, "��½ʧ��", Toast.LENGTH_SHORT).show();
					dlg.show();
					LoginView.findViewById(R.id.txt_loginerror).setVisibility(View.VISIBLE);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace(); 
			}
			finally{
				m_Dialog.dismiss(); 
			}
		}
		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mMain = this;
        if(!LoadUserDate())
        	CreateLoginAlert();
    }
    
    /**
     * �����û���Ϣ
     */
	private void SaveUserDate(){
	     //���������ļ�
	    SharedPreferences sp = getSharedPreferences(SugarContext.PREFS_NAME, 0);
	     //д�������ļ�
	    Editor spEd = sp.edit();
	    
		spEd.putBoolean("isSave", true);
		spEd.putString("name", Account);
		spEd.putString("password", PassWord);
	    spEd.commit();
	}	
	/**
	   * �����Ѽ�ס���û���Ϣ
	   */
	private boolean LoadUserDate(){
	   SharedPreferences sp = getSharedPreferences(SugarContext.PREFS_NAME, 0);
	  
	   if(sp.getBoolean("isSave", false)){
		   Account = sp.getString("name", "");
		   PassWord = sp.getString("password", "");
		   if(!("".equals(Account)&&"".equals(PassWord))){
				//���ɵ�½�Ի���
				m_Dialog=ProgressDialog.show(mMain, "��ȴ�...", "����Ϊ���½...",true);
				mRedrawHandler.sleep(100); 
				return true;
		   }else{
			   	return false;
		   }
		}else{
			return false;
		}
	}
}
