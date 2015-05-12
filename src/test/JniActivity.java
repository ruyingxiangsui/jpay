package test;

import util.AesUtil;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import application.JPayApplication;

import com.yunhuirong.jpayapp.R;

public class JniActivity extends Activity {

	Button jiami;
	Button jiemi;
	EditText text;
	byte[] b_key;
	byte[] b_iv;
	static {
		System.loadLibrary("JpayJni");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jni);

		jiami = (Button) findViewById(R.id.jiami);
		jiemi = (Button) findViewById(R.id.jiemi);
		text = (EditText) findViewById(R.id.text);
		b_key = ((JPayApplication) getApplication()).getKey();
		b_iv = ((JPayApplication) getApplication()).getIv();
		jiami.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String str = text.getText().toString();
				try {
					text.setText(AesUtil.encrypt_AES(str, b_key, b_iv));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		jiemi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String str = text.getText().toString();
				try {
					boolean flag = ((JPayApplication)getApplication()).checkCtime("", "");
					text.setText(AesUtil.decrypt_AES(str, b_key, b_iv));
					//checkAPP(getApplicationContext());
					Toast.makeText(getApplicationContext(), flag+"", Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}
	
	
	int checkAPP(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(),
	                        PackageManager.GET_SIGNATURES);
	        Signature[] signs = packageInfo.signatures;
	        Signature sign = signs[0];
	         
	        int hashcode = sign.hashCode();
	        Log.i("test", "hashCode : " + hashcode);
	        return hashcode == 1532883504 ? 1 : 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return -1;
	}
	
}
