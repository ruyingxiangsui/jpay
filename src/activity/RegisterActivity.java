package activity;

import util.SPUtil;
import home.HomeActivity;
import login.LoginActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yunhuirong.jpayapp.R;

import entity.UserInfo;

public class RegisterActivity extends Activity {

	private Button btRegister;
	private TextView tvLogin;
	
	private EditText etPhone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		btRegister = (Button) findViewById(R.id.bt_register);
		tvLogin = (TextView) findViewById(R.id.tv_login);
		
		etPhone = (EditText) findViewById(R.id.et_register_phone);
		
		btRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String phone = etPhone.getText().toString();
				if(phone==null||phone.trim().equals("")){
					return;
				}
				UserInfo uf = new UserInfo();
				uf.setLocalWalletId(phone);
				uf.setUserName(phone);
				SPUtil.setCurrentUserInfo(RegisterActivity.this, uf);
				Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
				startActivity(i);
				RegisterActivity.this.finish();
			}
		});
		tvLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
				startActivity(i);
				RegisterActivity.this.finish();
			}
		});
	}

	
}
