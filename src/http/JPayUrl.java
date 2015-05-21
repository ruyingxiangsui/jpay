package http;

public class JPayUrl {

	//加载本地so库?
	
	static protected String getSmsCodeUrl() {
		return "http://qserver.chinacloudapp.cn:6060/nfc/smscode/";
	}

	static protected String getLoginUrl() {
		return "http://qserver.chinacloudapp.cn:6060/nfc/login/";
	}

	static protected String getRegisterUrl() {
		return "http://qserver.chinacloudapp.cn:6060/nfc/users";
	}

	static protected String getCashUrl() {
		return "http://qserver.chinacloudapp.cn:6060/nfc/payments/cash";
	}

	static protected String getRechargeUrl() {
		return "http://qserver.chinacloudapp.cn:6060/nfc/payments/recharge";
	}

	static protected String getTransUrl() {
		return "http://qserver.chinacloudapp.cn:6060/nfc/payments";
	}

	static protected String getBalanceUrl() {
		return "http://qserver.chinacloudapp.cn:6060/nfc/balance";
	}
}
