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
	* 创建用户登陆的对话框
	* 登陆界面包含两个按钮
	* 1按钮为登陆
	* 2按钮为不登陆试玩
	* */
	private void CreateLoginAlert()
	{
		LayoutInflater inflater = LayoutInflater.from(this); 
		LoginView = inflater.inflate(R.layout.loginview, null); 

		ad =new AlertDialog.Builder(this);
		ad.setTitle("账号登陆");
		ad.setView(LoginView);
		dlg= ad.create();
		/*
		*/
		dlg.setButton("登陆", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface arg0, int arg1) {
				EditText password= (EditText)LoginView.findViewById(R.id.txt_password);
				EditText account =(EditText)LoginView.findViewById(R.id.txt_username);
				PassWord=password.getText().toString();
				Account=account.getText().toString();
				
				//生成登陆对话框
				m_Dialog=ProgressDialog.show(mMain, "请等待...", "正在为你登陆...",true);
				mRedrawHandler.sleep(100); 
			}
			});

		dlg.show(); 
		
	}
	/*
	*定时线程做验证用
	* */
	private RefreshHandler mRedrawHandler = new RefreshHandler();
	class RefreshHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			try{
				//调用网络接口，实现登陆指令
				Boolean flags= true;///UserDataServiceHelper.Login(Account, PassWord); 
				if(flags) 
				{
		            // 创建显式意图
		            Intent intent = new Intent(LoginActivity.this, OctoSugarActivity.class);

					//保存登陆信息
					SaveUserDate();
					//提示登陆成功
					Toast.makeText(mMain, "登陆成功", Toast.LENGTH_SHORT).show();
					SugarContext.isLogin = true;
					SugarContext.sessionId = 0x1234;
					
		            // 打开新的Activity
		            startActivity(intent);
		            
					mMain.finish();
					
					
				}else
				{
					//失败 显示错误信息
					Toast.makeText(mMain, "登陆失败", Toast.LENGTH_SHORT).show();
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
     * 保存用户信息
     */
	private void SaveUserDate(){
	     //载入配置文件
	    SharedPreferences sp = getSharedPreferences(SugarContext.PREFS_NAME, 0);
	     //写入配置文件
	    Editor spEd = sp.edit();
	    
		spEd.putBoolean("isSave", true);
		spEd.putString("name", Account);
		spEd.putString("password", PassWord);
	    spEd.commit();
	}	
	/**
	   * 载入已记住的用户信息
	   */
	private boolean LoadUserDate(){
	   SharedPreferences sp = getSharedPreferences(SugarContext.PREFS_NAME, 0);
	  
	   if(sp.getBoolean("isSave", false)){
		   Account = sp.getString("name", "");
		   PassWord = sp.getString("password", "");
		   if(!("".equals(Account)&&"".equals(PassWord))){
				//生成登陆对话框
				m_Dialog=ProgressDialog.show(mMain, "请等待...", "正在为你登陆...",true);
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
