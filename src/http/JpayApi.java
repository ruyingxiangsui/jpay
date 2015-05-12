package http;

import java.util.HashMap;

import http.response.LoginResp;
import http.response.RegisterResp;
import http.response.SmsCodeResp;

public class JpayApi extends JPayUrl {

	/**
	 * 获取短信验证码
	 * 
	 * 
	 * listener为回调方法
	 * */
	public static void getSmsCode(String phone,
			JPayRequestListener<SmsCodeResp> listener) {
		String url = getSmsCodeUrl() + phone;
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("apikey", "1234567890");

		JpayRequest<SmsCodeResp> request = new JpayRequest<SmsCodeResp>(url,
				headers, new HashMap<String, String>(), RequestType.POST) {
		};

		JPayExcuter<SmsCodeResp> excuter = new JPayExcuter<SmsCodeResp>(
				request, listener);
		excuter.execute();
	}

	/**
	 * 登录
	 * 
	 * */
	public static void login(String phone, String pass,
			JPayRequestListener<LoginResp> listener) {
		// 根据电话号码生成url
		String url = getLoginUrl() + phone;

		// 生成头信息
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("apikey", "1234567890");

		// 生成参数
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("password", pass);

		// 设置请求
		JpayRequest<LoginResp> request = new JpayRequest<LoginResp>(url,
				headers, params, RequestType.POST) {
		};
		// 执行请求
		JPayExcuter<LoginResp> excuter = new JPayExcuter<LoginResp>(request,
				listener);
		excuter.execute();

	}

	/**
	 * 开户注册
	 * 
	 * */
	public static void register(String phone, String smscode, String name,
			String password, String cid, String bank, String device,
			String origin, JPayRequestListener<RegisterResp> listener) {

		// url
		String url = getRegisterUrl();

		// headers
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("apikey", "1234567890");

		// body
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("phone", phone);
		params.put("smscode", smscode);
		params.put("name", name);
		params.put("password", password);
		params.put("cid", cid);
		params.put("bank", bank);
		params.put("device", device);
		params.put("origin", origin);

		// http请求
		JpayRequest<RegisterResp> request = new JpayRequest<RegisterResp>(url,
				headers, params, RequestType.POST) {
		};

		// 执行请求
		JPayExcuter<RegisterResp> excuter = new JPayExcuter<RegisterResp>(
				request, listener);
		excuter.execute();
	}

	/**
	 * 充值
	 * 
	 * */
	public static void recharge() {
		String url = getRechargeUrl();
	}

	/**
	 * 兑现
	 * 
	 * */
	public static void cash() {
		String url = getCashUrl();
	}

	/**
	 * 获取交易记录
	 * 
	 * */
	public static void getTrans() {
		String url = getTransUrl();
	}

	/**
	 * 获取账户余额
	 * 
	 * */
	public static void getBalance() {
		String url = getBalanceUrl();
	}

}
