package login;

import util.SPUtil;
import home.HomeActivity;
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

import com.yunhuirong.jpayapp.R;

import entity.UserInfo;

public class LoginActivity extends Activity {

	private EditText tv_username;
	private EditText tv_password;
	private Button bt_login;
	private TextView tv_register;
	private ProgressBar pb_progress;
	
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
				String name = tv_username.getText().toString();
				String pass = tv_password.getText().toString();
				if(name==null||name.equals("")){
					return;
				}
				if(pass==null||pass.equals("")){
					return;
				}
				UserInfo uf = new UserInfo();
				uf.setUserName(name);
				uf.setLocalWalletId(name);
				SPUtil.setCurrentUserInfo(LoginActivity.this, uf);
				pb_progress.setVisibility(View.VISIBLE);
				Intent i = new Intent(LoginActivity.this,HomeActivity.class);
				startActivity(i);
				LoginActivity.this.finish();
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
