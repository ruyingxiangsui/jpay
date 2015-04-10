package activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.yunhuirong.jpayapp.R;

public class SettingPasswordActivity extends Activity {

	
	private Button back;
	private EditText old_pass;
	private EditText new_pass;
	private EditText re_pass;
	private Button confirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_password);
		back = (Button) findViewById(R.id.bt_setting_password__back);
		old_pass = (EditText) findViewById(R.id.et_input_old_pass);
		new_pass = (EditText) findViewById(R.id.et_input_new_pass);
		re_pass = (EditText) findViewById(R.id.et_reinput_new_pass);

		confirm = (Button) findViewById(R.id.bt_confirm_setting_pass);

		
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(SettingPasswordActivity.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				SettingPasswordActivity.this.finish();
				SettingPasswordActivity.this.finish();
			}
		});
		confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (check(old_pass.getText().toString(), new_pass.getText()
						.toString(), re_pass.getText().toString())) {
					saveNewPass(re_pass.getText().toString());
				}
			}
		});

	}

	private boolean check(String old_pass, String new_pass, String renew_pass) {
		return true;
	}
	
	private void saveNewPass(String pass){
		
	}

}
