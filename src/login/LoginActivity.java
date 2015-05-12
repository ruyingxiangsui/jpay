package login;

import home.HomeActivity;
import http.JPayRequestListener;
import http.JpayApi;
import http.response.LoginResp;
import util.SPUtil;
import activity.RegisterActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import application.JPayApplication;

import com.yunhuirong.jpayapp.R;

import entity.CurrentUser;

public class LoginActivity extends Activity {

	private EditText tv_username;
	private EditText tv_password;
	private Button bt_login;
	private TextView tv_register;
	private ProgressBar pb_progress;
	private String name;
	private String pass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		tv_username = (EditText) findViewById(R.id.et_login_username);
		tv_password = (EditText) findViewById(R.id.et_login_password);
		bt_login = (Button) findViewById(R.id.bt_login);
		tv_register = (TextView) findViewById(R.id.tv_register);
		pb_progress = (ProgressBar) findViewById(R.id.pb_login);
		
		bt_login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hideKeyBoard();
				name = tv_username.getText().toString();
				pass = tv_password.getText().toString();
				if(name==null||name.equals("")){
					return;
				}
				if(pass==null||pass.equals("")){
					return;
				}
				pb_progress.setVisibility(View.VISIBLE);
				JpayApi.login(name, pass, new JPayRequestListener<LoginResp>() {
					
					@Override
					public void onRequestSucceeded(LoginResp data) {
						CurrentUser uf = new CurrentUser();
						uf.setUserName(name);
						SPUtil.setCurrentUserInfo((JPayApplication)getApplication(), uf);
						
						Intent i = new Intent(LoginActivity.this,HomeActivity.class);
						startActivity(i);
						LoginActivity.this.finish();
						
					}
					
					@Override
					public void onRequestFailed(String ex) {
						Toast.makeText(LoginActivity.this, ex, Toast.LENGTH_SHORT).show();
						
					}
				});
				
				
			}
		});
		tv_register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(i);
				LoginActivity.this.finish();
			}
		});
	}
	private void hideKeyBoard(){
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(
				LoginActivity.this.getCurrentFocus()
						.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
}
