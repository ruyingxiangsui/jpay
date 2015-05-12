package util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;
import android.util.Log;

public class AesUtil {
	/**
	 * 
	 * 加密
	 * 
	 * @param src
	 *            要加密的字符串
	 * @param b_key
	 *            加密的key
	 * @param b_iv
	 *            加密使用的向量
	 * 
	 * @return 加密后的内容
	 * */
	public static String encrypt_AES(String src, byte[] b_key, byte[] b_iv)
			throws Exception {
		if (b_key == null) {
			return "key不能为空!";
		}
		/*
		 * if (key.length != 16) { return "key长度不为16位！"; }
		 */

		SecretKeySpec keyspec = new SecretKeySpec(b_key, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
		IvParameterSpec iv = new IvParameterSpec(b_iv);

		cipher.init(Cipher.ENCRYPT_MODE, keyspec, iv);
		byte[] encrypted = cipher.doFinal(src.getBytes());

		return Base64.encodeToString(encrypted, Base64.DEFAULT);
	}

	/**
	 * 
	 * 解密
	 * 
	 * @param src
	 *            要解密的字符串
	 * @param b_key
	 *            解密的key
	 * @param b_iv
	 *            解密使用的向量
	 * 
	 * @return 解密得到的原内容
	 * */
	public static String decrypt_AES(String src, byte[] b_key, byte[] b_iv)
			throws Exception {
		if (b_key == null) {
			return "key不能为空！";
		}

		SecretKeySpec keyspec = new SecretKeySpec(b_key, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec(b_iv);

		cipher.init(Cipher.DECRYPT_MODE, keyspec, iv);
		byte[] encrypted = Base64.decode(src, Base64.DEFAULT);
		byte[] original = cipher.doFinal(encrypted);
		String ostr = new String(original);

		return ostr;
	}

}
