package activity;

import util.SPUtil;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import application.JPayApplication;

import com.yunhuirong.jpayapp.R;

public class SettingPasswordActivity extends Activity {

	private Button back;
	private EditText old_pass;
	private EditText new_pass;
	private EditText re_pass;
	private Button confirm;
	JPayApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_password);
		app = (JPayApplication) getApplication();
		back = (Button) findViewById(R.id.bt_setting_password__back);
		old_pass = (EditText) findViewById(R.id.et_input_old_pass);
		new_pass = (EditText) findViewById(R.id.et_input_new_pass);
		re_pass = (EditText) findViewById(R.id.et_reinput_new_pass);

		confirm = (Button) findViewById(R.id.bt_confirm_setting_pass);

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(
						SettingPasswordActivity.this.getCurrentFocus()
								.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				SettingPasswordActivity.this.finish();
			}
		});
		confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (check(old_pass.getText().toString(), new_pass.getText()
						.toString(), re_pass.getText().toString())) {
					saveNewPass(re_pass.getText().toString());
					InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(
							SettingPasswordActivity.this.getCurrentFocus()
									.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
					Toast.makeText(getApplicationContext(), "设置成功！",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "设置失败！请检查输入！",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	private boolean check(String old_pass, String new_pass, String renew_pass) {
		if (new_pass != null && new_pass.equals(renew_pass)) {
			return SPUtil.checkAccountPass(app, SPUtil.getCurrentUserInfo(app)
					.getUserName(), old_pass);
		}Log.d("pass", "fas");
		return false;
	}

	private void saveNewPass(String pass) {
		SPUtil.updateAccountPass((JPayApplication) getApplication(), SPUtil
				.getCurrentUserInfo((JPayApplication) getApplication())
				.getUserName(), pass);
	}

}
