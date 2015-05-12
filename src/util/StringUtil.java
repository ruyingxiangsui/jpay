package util;

import java.io.ByteArrayOutputStream;
import java.util.Random;

public class StringUtil {

	private static String HEX = "0123456789ABCDEF";

	/**
	 * 将字符串转换为对应的16进制码
	 * 
	 * */
	public static String StringToHexString(String str) {

		byte[] bytes = str.getBytes();
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(HEX.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(HEX.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}

	/**
	 * 将16进制的字符串转为对应的字符串
	 * 
	 * */
	public static String HexStringToString(String str) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(str.length() / 2);
		// 将每2位16进制整数组装成一个字节
		for (int i = 0; i < str.length(); i += 2)
			baos.write((HEX.indexOf(str.charAt(i)) << 4 | HEX.indexOf(str
					.charAt(i + 1))));
		return new String(baos.toByteArray());
	}

	public static byte[] HexStringToByteArray(String s)
			throws IllegalArgumentException {
		int len = s.length();
		if (len % 2 == 1) {
			throw new IllegalArgumentException(
					"Hex string must have even number of characters");
		}
		byte[] data = new byte[len / 2]; // Allocate 1 byte per 2 hex characters
		for (int i = 0; i < len; i += 2) {
			// Convert each character into a integer (base-16), then bit-shift
			// into place
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public static String ByteArrayToHexString(byte[] bytes) {
		final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] hexChars = new char[bytes.length * 2]; // Each byte has two hex
														// characters (nibbles)
		int v;
		for (int j = 0; j < bytes.length; j++) {
			v = bytes[j] & 0xFF; // Cast bytes[j] to int, treating as unsigned
									// value
			hexChars[j * 2] = hexArray[v >>> 4]; // Select hex character from
													// upper nibble
			hexChars[j * 2 + 1] = hexArray[v & 0x0F]; // Select hex character
														// from lower nibble
		}
		return new String(hexChars);
	}

	public static String getUuidString() {
		int length =32;
		String base = "ABCDEF0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/*
	 * public static String getFormatStr(String phone) { String str =
	 * "000000000000" + phone; return str.substring(phone.length()); }
	 */

}
