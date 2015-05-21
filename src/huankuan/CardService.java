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

package huankuan;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import util.AesUtil;
import util.SPUtil;
import util.StringUtil;
import util.TransUtil;
import android.annotation.SuppressLint;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;
import application.JPayApplication;

import com.google.gson.Gson;

import engine.JPayEngine;
import entity.ApduCommand;
import entity.Command;
import entity.ReplyApduCommand;
import entity.TranstionItem;

@SuppressLint("SimpleDateFormat")
public class CardService extends HostApduService implements JPayEngine {

	private static final byte[] UNKNOWN_CMD_SW = HexStringToByteArray("0000");
	// private static final byte[] SELECT_APDU =
	// BuildSelectApdu(JPAY_LOYALTY_CARD_AID);

	private static final Gson gson = new Gson();
	private static Command tmp = new Command();
	private static TransUtil transUtil;
	private static ApduCommand apduCmd;
	private static ReplyApduCommand replyCmd = new ReplyApduCommand();
	private static JPayApplication app;
	private static WeakReference<AccountCallback> mAccountCallback;

	public interface AccountCallback {
		public void onHuankuanReceived(String count);
	}

	public static void setCallBack(JPayApplication ap,
			AccountCallback accountCallback) {
		mAccountCallback = new WeakReference<AccountCallback>(accountCallback);
		transUtil = new TransUtil(ap);
		app = ap;
	}

	@Override
	public void onDeactivated(int reason) {
		Log.i(TAG, "deactive...");
	}

	@Override
	public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
		tmp = SPUtil.getHuanCommand((JPayApplication) getApplication());
		try {
			// 如果是SELECT指令
			if (Arrays.equals(BuildSelectApdu(JPAY_LOYALTY_CARD_AID),
					commandApdu)) {
				// SELECT APPLICATION
				Log.e("card", "card:SELECT 指令通过,返回ok");
				return SELECT_OK_SW;
			}
			// READ BINARY 指令
			else if ((commandApdu[1] == (byte) 0xB0)
					&& (commandApdu[2] == (byte) 0x00)) {
				apduCmd = checkApdu(commandApdu);

				// 如果指令通过并且本地交易指令的状态为未完成
				if (apduCmd != null && !STATE_DONE.equals(tmp.getTransState())) {

					/** 本地当前交易指令交易号与收到的交易号相同,并且收到收款方的确认 */
					if (tmp.getTransCode().equals(apduCmd.getCode())
							&& STATE_DONE.equals(apduCmd.getType())) {
						tmp.setTransState(STATE_DONE);
						SPUtil.setHuanCommand(
								(JPayApplication) getApplication(), tmp);
						if (tmp.getTransCount() > 0) {
							mAccountCallback.get().onHuankuanReceived(
									tmp.getTransCount() + "");
						}

					}
					/** 本地当前交易指令交易号为空且交易金額不为0,并且收到收款方收款请求 */
					else if ((tmp.getTransCode() == null || tmp.getTransCode()
							.equals(""))
							&& tmp.getTransCount() > 0
							&& !STATE_DONE.equals(apduCmd.getType())) {
						replyCmd.setCard(tmp.getHuanKuanCard());
						replyCmd.setCode(tmp.getTransCode());
						replyCmd.setMoney(tmp.getTransCount());
						replyCmd.setType(STATE_OK);

						tmp.setTransCode(apduCmd.getCode());
						tmp.setShouKuanCard(apduCmd.getShou());

						SPUtil.setHuanCommand(
								(JPayApplication) getApplication(), tmp);

						/** 本地余额减少,并添加交易记录 */
						SPUtil.updateAccountOverage(
								(JPayApplication) getApplication(),
								tmp.getHuanKuanCard(), TRANS_TYPE_HUANKUAN,
								tmp.getTransCount());
						SimpleDateFormat sDateFormat = new SimpleDateFormat(
								"yyyy-MM-dd   hh:mm:ss");
						String time = sDateFormat.format(new java.util.Date());
						transUtil.insertTrans(new TranstionItem(
								TRANS_TYPE_HUANKUAN, time, tmp.getTransCount()
										+ "", tmp.getHuanKuanCard(), tmp
										.getShouKuanCard()));

						return ConcatArrays(
								AesUtil.encrypt_AES(gson.toJson(replyCmd),
										app.getKey(), app.getIv()).getBytes(),
								SELECT_OK_SW);

					}
					/** 本地当前交易指令交易号与收到的交易号相同,并且收到收款方的收款请求 */
					else if (tmp.getTransCode().equals(apduCmd.getCode())
							&& !STATE_DONE.equals(apduCmd.getType())) {
						replyCmd.setCard(tmp.getHuanKuanCard());
						replyCmd.setCode(tmp.getTransCode());
						replyCmd.setMoney(tmp.getTransCount());
						replyCmd.setType(STATE_OK);
						return ConcatArrays(AesUtil.encrypt_AES(gson.toJson(replyCmd),
								app.getKey(), app.getIv()).getBytes(),
								SELECT_OK_SW);
					}
					/** 本地当前交易指令交易号为空,但收到收款方的完成收款信号 */
					else if ((tmp.getTransCode() == null || tmp.getTransCode()
							.equals(""))
							&& STATE_DONE.equals(apduCmd.getType())) {

						return UNKNOWN_CMD_SW;
					} else {
						tmp.setTransState(STATE_DONE);
						SPUtil.setHuanCommand(
								(JPayApplication) getApplication(), tmp);
						mAccountCallback.get().onHuankuanReceived(
								tmp.getTransCount() + "");
						return UNKNOWN_CMD_SW;
					}
				} else {

				}
			}
		} catch (Exception e) {
			Log.e("card", e.toString());
		}
		Log.e("card", "card:无法识别的指令,返回unknown.");
		return UNKNOWN_CMD_SW;

	}

	// 对传过来的apdu命令进行验证
	public ApduCommand checkApdu(byte[] commandApdu) {
		String str = ByteArrayToHexString(commandApdu);

		Log.e("card", "card checking:" + str);
		/**
		 * 报文头:"00A40400" 2位 报文数据长度:"XXXX" 尾部为JSON数据
		 * 
		 * */
		try {
			String sHead = str.substring(0, 8);
			String sDataLen = str.substring(8, 10);
			String hexStr = str.substring(10);

			String raw = StringUtil.HexStringToString(hexStr);
			String json = AesUtil.decrypt_AES(raw, app.getKey(), app.getIv());
			int len = Integer.parseInt(sDataLen, 16);
			if (GET_APDU_HEADER.equals(sHead) && (len * 2 == hexStr.length())) {
				ApduCommand apduCmd = gson.fromJson(json, ApduCommand.class);
				return apduCmd;
			}
			Log.e("card", json);
		} catch (Exception e) {
			Log.e("card", "Card: checking fail." + e.toString());
		}
		Log.e("Card", "Card: checking fail.");
		return null;
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
