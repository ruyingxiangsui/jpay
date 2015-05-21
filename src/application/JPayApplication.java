package application;

import android.app.Application;

public class JPayApplication extends Application {
	static {
		System.loadLibrary("JpayJni");
	}


	/**
	 * 获取ctime
	 * 
	 * */
	public native String getCtime(String file);

	/**
	 * 获取AES算法的密钥
	 * 
	 * @param context
	 * @return 密钥的byte数组
	 * */
	public native byte[] getKey();

	/**
	 * 获取AES算法的IV
	 * 
	 * @param context
	 * @return 密钥的byte数组
	 * */
	public native byte[] getIv();

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

}
