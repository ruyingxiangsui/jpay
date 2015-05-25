package activity;

import home.HomeActivity;
import http.JPayRequestListener;
import http.JpayApi;
import http.response.RegisterResp;
import http.response.SmsCodeResp;
import login.LoginActivity;
import util.SPUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import application.JPayApplication;

import com.yunhuirong.jpayapp.R;

import entity.CurrentUser;

public class RegisterActivity extends Activity {

	private Button btRegister;
	private TextView tvLogin;
	private Button btSmscode;
	private EditText etPhone;
	private EditText etPass;
	private EditText etsmsCode;
	private EditText etName;
	private EditText etBank;
	private EditText etID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		etPhone = (EditText) findViewById(R.id.et_register_phone);
		etPass = (EditText) findViewById(R.id.et_register_password);
		etsmsCode = (EditText) findViewById(R.id.sms_code);
		etName = (EditText) findViewById(R.id.et_register_real_name);
		etBank = (EditText) findViewById(R.id.et_register_bank);
		etID = (EditText) findViewById(R.id.et_register_id);

		btRegister = (Button) findViewById(R.id.bt_register);
		tvLogin = (TextView) findViewById(R.id.tv_login);
		btSmscode = (Button) findViewById(R.id.bt_get_sms_code);
		btSmscode.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String phone = etPhone.getText().toString();
				if (checkPhone(phone)) {
					JpayApi.getSmsCode(phone,
							new JPayRequestListener<SmsCodeResp>() {

								@Override
								public void onRequestSucceeded(SmsCodeResp data) {
									// TODO Auto-generated method stub
									etsmsCode.setText(data.smscode);
								}

								@Override
								public void onRequestFailed(String ex) {
									// TODO Auto-generated method stub
									Toast.makeText(getApplicationContext(),
											"获取短信验证码失败", Toast.LENGTH_SHORT)
											.show();
								}
							});
				}
			}
		});

		btRegister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkRegisterData() == null) {

					final String phone = etPhone.getText().toString();
					String password = etPass.getText().toString();
					String smscode = etsmsCode.getText().toString();
					String name = etName.getText().toString();
					String bank = etBank.getText().toString();
					String cid = etID.getText().toString();

					JpayApi.register(phone, smscode, name, password, cid, bank,
							null, null,
							new JPayRequestListener<RegisterResp>() {

								@Override
								public void onRequestSucceeded(RegisterResp data) {
									CurrentUser uf = new CurrentUser();
									uf.setUserName(phone);
									uf.setAccessToken(data.accessToken);
									SPUtil.setCurrentUserInfo(
											(JPayApplication)getApplication(), uf);
									Intent i = new Intent(
											RegisterActivity.this,
											HomeActivity.class);
									startActivity(i);
									RegisterActivity.this.finish();
								}

								@Override
								public void onRequestFailed(String ex) {
									Toast.makeText(getApplicationContext(),
											"注册失败！" + ex, Toast.LENGTH_SHORT)
											.show();
								}
							});
				} else {
					Toast.makeText(getApplicationContext(),
							checkRegisterData(), Toast.LENGTH_SHORT).show();
				}

			}
		});
		tvLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(RegisterActivity.this,
						LoginActivity.class);
				startActivity(i);
				RegisterActivity.this.finish();
			}
		});
	}

	private boolean checkPhone(String phone) {
		if (phone == null || phone.length() != 11 || !phone.startsWith("1")) {
			return false;
		}
		return true;
	}

	private String checkRegisterData() {

		if (etPhone.getText().toString() == null
				|| etPhone.getText().toString().equals("")) {
			return "手机号不能为空！";
		}
		if (etPass.getText().toString() == null
				|| etPass.getText().toString().equals("")) {
			return "密码不能为空！";
		}
		if (etsmsCode.getText().toString() == null
				|| etsmsCode.getText().toString().equals("")) {
			return "短信验证码不能为空！";
		}
		if (etName.getText().toString() == null
				|| etName.getText().toString().equals("")) {
			return "真实姓名不能为空！";
		}
		if (etBank.getText().toString() == null
				|| etBank.getText().toString().equals("")) {
			return "银行类型不能为空！";
		}
		if (etID.getText().toString() == null
				|| etID.getText().toString().equals("")) {
			return "身份证号不能为空！";
		}
		return null;
	}
}
