/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package shoukuan;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Random;

import util.SPUtil;
import util.StringUtil;
import util.TransUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.util.Log;
import application.JPayApplication;

import com.google.gson.Gson;

import engine.JPayEngine;
import entity.ApduCommand;
import entity.Command;
import entity.ReplyApduCommand;
import entity.TranstionItem;

/**
 * Callback class, invoked when an NFC card is scanned while the device is
 * running in reader mode.
 *
 * Reader mode can be invoked by calling NfcAdapter
 */
@SuppressLint("SimpleDateFormat")
public class LoyaltyCardReader implements NfcAdapter.ReaderCallback, JPayEngine {

	// Weak reference to prevent retain loop. mAccountCallback is responsible
	// for exiting
	// foreground mode before it becomes invalid (e.g. during onPause() or
	// onStop()).
	private WeakReference<AccountCallback> mAccountCallback;

	private static Gson gson = new Gson();
	private static Command tmp = new Command();
	private static ReplyApduCommand replyCmd = new ReplyApduCommand();
	private JPayApplication app;
	private static ApduCommand apduCmd = new ApduCommand();
	private static TransUtil transUtil;

	public interface AccountCallback {
		public void onSuccessed(String account);
	}

	public LoyaltyCardReader(JPayApplication app, AccountCallback accountCallback) {
		mAccountCallback = new WeakReference<AccountCallback>(accountCallback);
		this.app = app;
		transUtil = new TransUtil(app);
	}

	@Override
	public void onTagDiscovered(Tag tag) {
		IsoDep isoDep = IsoDep.get(tag);
		Log.e("reader", "find new tag...");
		// 先读取当前指令状态
		tmp = SPUtil.getShouCommand(app);
		Log.e("reader", "reader 当前指令:" + gson.toJson(tmp));
		if (isoDep != null) {
			try {
				// Connect to the remote NFC device
				isoDep.connect();
				byte[] result0 = isoDep
						.transceive(BuildSelectApdu(JPAY_LOYALTY_CARD_AID));
				int resultLength0 = result0.length;
				byte[] statusWord0 = { result0[resultLength0 - 2],
						result0[resultLength0 - 1] };
				Log.e("reader", "reader select:"
						+ ByteArrayToHexString(statusWord0));
				// 如果选择卡成功
				if (Arrays.equals(SELECT_OK_SW, statusWord0)) {

					if (tmp.getTransState().equals(STATE_DONE)) {
						// send donejson
						Log.e("reader", "reader:send done json");
						apduCmd.setCode(tmp.getTransCode());
						apduCmd.setShou(tmp.getShouKuanCard());
						apduCmd.setType(tmp.getTransState());
						apduCmd.setKey(JPAY_KEY);
						String hexStr = StringUtil.StringToHexString(gson
								.toJson(apduCmd));
						isoDep.transceive(BuildGetApdu(hexStr));

						return;// 不行换goto
					} else {
						// send json
						apduCmd.setCode(tmp.getTransCode());
						apduCmd.setShou(tmp.getShouKuanCard());
						apduCmd.setKey(JPAY_KEY);
						apduCmd.setType(tmp.getTransState());
						Log.e("reader", "reader:" + gson.toJson(apduCmd));
						String hexStr = StringUtil.StringToHexString(gson
								.toJson(apduCmd));
						byte[] result = isoDep.transceive(BuildGetApdu(hexStr));
						int resultLength = result.length;
						byte[] statusWord = { result[resultLength - 2],
								result[resultLength - 1] };
						byte[] payload = Arrays
								.copyOf(result, resultLength - 2);
						Log.e("reader", "reader:"
								+ ByteArrayToHexString(payload));
						if (Arrays.equals(SELECT_OK_SW, statusWord)) {
							String str = new String(payload);
							replyCmd = gson.fromJson(str,
									ReplyApduCommand.class);
							tmp.setHuanKuanCard(replyCmd.getCard());
							tmp.setTransCount(replyCmd.getMoney());
							tmp.setTransState(STATE_DONE);
							SPUtil.setShouCommand(app, tmp);
							
							/** 更新本地余额,添加交易记录 */
							SPUtil.updateAccountOverage(app, SPUtil
									.getCurrentUserInfo(app).getUserName(),
									TRANS_TYPE_SHOUKUAN, tmp.getTransCount());
							SimpleDateFormat sDateFormat = new SimpleDateFormat(
									"yyyy-MM-dd   hh:mm:ss");
							String time = sDateFormat
									.format(new java.util.Date());
							transUtil.insertTrans(new TranstionItem(
									TRANS_TYPE_SHOUKUAN, time, tmp
											.getTransCount()+"", tmp
											.getShouKuanCard(), tmp
											.getHuanKuanCard()));
							/**显示通知*/
							mAccountCallback.get().onSuccessed(
									tmp.getTransCount() + "");

							apduCmd.setCode(tmp.getTransCode());
							apduCmd.setShou(tmp.getShouKuanCard());
							apduCmd.setType(tmp.getTransState());
							apduCmd.setKey(JPAY_KEY);
							String hexstr = StringUtil.StringToHexString(gson
									.toJson(apduCmd));
							isoDep.transceive(BuildGetApdu(hexstr));
						}
						return;
					}
				} else {

					Log.e("reader", "reader select not ok");
				}
			} catch (IOException e) {
				Log.e(TAG, "Error communicating with card: " + e.toString());
			}
		}
	}

	public static byte[] BuildGetApdu(String aid) {

		return HexStringToByteArray("00B00000"
				+ String.format("%02X", aid.length() / 2) + aid);
	}

	public static String getTransCodeString(int length) { // length表示生成字符串的长度
		String base = "ABCDEF0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static byte[] BuildSelectApdu(String aid) {
		// Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH |
		// DATA]
		return HexStringToByteArray(SELECT_APDU_HEADER
				+ String.format("%02X", aid.length() / 2) + aid);
	}

	public static String ByteArrayToHexString(byte[] bytes) {
		final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for (int j = 0; j < bytes.length; j++) {
			v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static byte[] HexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public static byte[] ConcatArrays(byte[] first, byte[]... rest) {
		int totalLength = first.length;
		for (byte[] array : rest) {
			totalLength += array.length;
		}
		byte[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (byte[] array : rest) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}

}
