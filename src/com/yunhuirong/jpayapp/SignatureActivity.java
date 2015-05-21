package com.yunhuirong.jpayapp;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

public class SignatureActivity extends Activity {

	private TextView tv_signature;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signature);
		tv_signature = (TextView) findViewById(R.id.showSignature);
		tv_signature.setText(getSignature() + "");
	}

	private long getSignature() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 64);
			return info.signatures[0].hashCode();
			/**-1642232357*/
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

	}

}
