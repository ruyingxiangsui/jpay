package activity;

import home.HomeActivity;
import login.LoginActivity;
import util.SPUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import application.JPayApplication;

import com.yunhuirong.jpayapp.R;

public class EnterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_enter);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (checkUser() == null || checkUser().equals("")) {
					Intent i = new Intent(EnterActivity.this,
							LoginActivity.class);
					startActivity(i);
				} else {
					Intent i = new Intent(EnterActivity.this,
							HomeActivity.class);
					startActivity(i);
				}
				EnterActivity.this.finish();
				overridePendingTransition(R.anim.fadein, R.anim.fadeout);
			}
		}, 1500);

	}

	public String checkUser() {
		return SPUtil.getCurrentUserInfo((JPayApplication)getApplication()).getUserName();
	}

}
