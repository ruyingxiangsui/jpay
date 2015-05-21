package activity;

import home.HomeActivity;
import login.LoginActivity;
import util.SPUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;
import application.JPayApplication;

import com.yunhuirong.jpayapp.R;

import entity.CurrentUser;

public class EnterActivity extends Activity {
	JPayApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_enter);
		app = (JPayApplication) getApplication();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (checkUser() == null || checkUser().equals("")) {
					Intent i = new Intent(EnterActivity.this,
							LoginActivity.class);
					startActivity(i);
				} else {
					if (SPUtil.checkAccountCTime(app, checkUser())) {
						Intent i = new Intent(EnterActivity.this,
								HomeActivity.class);
						startActivity(i);
					} else {
						Toast.makeText(app, "当前用户本地数据已损坏！", Toast.LENGTH_SHORT)
								.show();
						// 清除当前用户，以及该用户的本地数据
						SPUtil.clearAccountLocalData(app,checkUser());
						SPUtil.setCurrentUserInfo(app, new CurrentUser());
						EnterActivity.this.finish();
					}
				}
				EnterActivity.this.finish();
				overridePendingTransition(R.anim.fadein, R.anim.fadeout);
			}
		}, 1500);

	}

	public String checkUser() {
		if (SPUtil.getCurrentUserInfo(app) == null) {
			return null;
		}
		return SPUtil.getCurrentUserInfo(app).getUserName();
	}

}
