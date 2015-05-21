package http;

import http.response.BalanceResp;
import http.response.CashResp;
import http.response.LoginResp;
import http.response.LogoutResp;
import http.response.PaymentsResp;
import http.response.RechargeResp;
import http.response.RegisterResp;
import http.response.SmsCodeResp;

import java.util.HashMap;

public class JpayApi extends JPayUrl {

	private static final String API_KEY_VALUE = "1234567890";
	private static final String AUTH_PREFIX = "Bearer ";

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
		headers.put("apikey", API_KEY_VALUE);

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
		headers.put("apikey", API_KEY_VALUE);

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
		headers.put("apikey", API_KEY_VALUE);

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
	public static void recharge(String accessToken, int amount, String note,
			JPayRequestListener<RechargeResp> listener) {
		String url = getRechargeUrl();

		// 生成头信息
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("apikey", API_KEY_VALUE);
		headers.put("Authorization", AUTH_PREFIX + accessToken);
		// 生成参数
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("amount", amount + "");
		params.put("note", note);

		// 设置请求
		JpayRequest<RechargeResp> request = new JpayRequest<RechargeResp>(url,
				headers, params, RequestType.POST) {
		};
		// 执行请求
		JPayExcuter<RechargeResp> excuter = new JPayExcuter<RechargeResp>(
				request, listener);
		excuter.execute();
	}

	/**
	 * 兑现
	 * 
	 * */
	public static void cash(String accessToken, int amount, String note,
			JPayRequestListener<CashResp> listener) {
		String url = getCashUrl();

		// 生成头信息
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("apikey", API_KEY_VALUE);
		headers.put("Authorization", AUTH_PREFIX + accessToken);
		// 生成参数
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("amount", amount + "");
		params.put("note", note);

		// 设置请求
		JpayRequest<CashResp> request = new JpayRequest<CashResp>(url, headers,
				params, RequestType.POST) {
		};
		// 执行请求
		JPayExcuter<CashResp> excuter = new JPayExcuter<CashResp>(request,
				listener);
		excuter.execute();
	}

	/**
	 * 获取交易记录
	 * 
	 * */
	public static void getPayments(String accessToken, int cursor,
			JPayRequestListener<PaymentsResp> listener) {
		String url = getTransUrl();
		// 生成头信息
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("apikey", API_KEY_VALUE);
		headers.put("Authorization", AUTH_PREFIX + accessToken);

		// 生成参数
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cursor", cursor + "");

		// 设置请求
		JpayRequest<PaymentsResp> request = new JpayRequest<PaymentsResp>(url,
				headers, params, RequestType.GET) {
		};
		// 执行请求
		JPayExcuter<PaymentsResp> excuter = new JPayExcuter<PaymentsResp>(
				request, listener);
		excuter.execute();
	}

	/**
	 * 获取账户余额
	 * 
	 * */
	public static void getBalance(String accessToken,
			JPayRequestListener<BalanceResp> listener) {
		String url = getBalanceUrl();
		// 生成头信息
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("apikey", API_KEY_VALUE);
		headers.put("Authorization", AUTH_PREFIX + accessToken);
		HashMap<String, String> params = new HashMap<String, String>();
		// 设置请求
		JpayRequest<BalanceResp> request = new JpayRequest<BalanceResp>(url,
				headers, params, RequestType.GET) {
		};
		// 执行请求
		JPayExcuter<BalanceResp> excuter = new JPayExcuter<BalanceResp>(
				request, listener);
		excuter.execute();
	}

	/**
	 * 退出
	 * 
	 * */
	@SuppressWarnings("unused")
	public static void logOut(String accessToken, String phone,
			JPayRequestListener<LogoutResp> listener) {
		String url = getLoginUrl() + "/" + phone;

		// 生成头信息
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("apikey", API_KEY_VALUE);
		headers.put("Authorization", AUTH_PREFIX + accessToken);
		JpayRequest<LogoutResp> request = new JpayRequest<LogoutResp>(url,
				headers, new HashMap<String, String>(), RequestType.PUT) {
		};
		JPayExcuter<LogoutResp> executor = new JPayExcuter<LogoutResp>(request,
				listener);
	}
}
