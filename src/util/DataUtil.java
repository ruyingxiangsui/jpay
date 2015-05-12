package util;

import java.util.ArrayList;

public class DataUtil {
	static ArrayList<String> list;

	/**
	 * 暂时在本地
	 * 
	 * */
	public static ArrayList<String> getFAQS() {
		if (list == null) {
			list = new ArrayList<String>();
			list.add("充值:指金额从远程账户转到本地钱包");
			list.add("兑现:指金额从本地钱包转到远程账户");
		}

		return list;
	}


}
