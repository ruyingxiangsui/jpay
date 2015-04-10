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

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Arrays;

import util.SPUtil;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.util.Log;

import com.google.gson.Gson;

import engine.JPayEngine;
import entity.Command;

/**
 * Callback class, invoked when an NFC card is scanned while the device is
 * running in reader mode.
 *
 * Reader mode can be invoked by calling NfcAdapter
 */
public class LoyaltyCardReader implements NfcAdapter.ReaderCallback, JPayEngine {

	// Weak reference to prevent retain loop. mAccountCallback is responsible
	// for exiting
	// foreground mode before it becomes invalid (e.g. during onPause() or
	// onStop()).
	private WeakReference<AccountCallback> mAccountCallback;

	private static Gson gson = new Gson();
	private static Command tmp = new Command();
	private Context ctx;

	public interface AccountCallback {
		public void onSended(String account);
	}

	public LoyaltyCardReader(Context ctx, AccountCallback accountCallback) {
		mAccountCallback = new WeakReference<AccountCallback>(accountCallback);
		this.ctx = ctx;
	}

	/**
	 * Callback when a new tag is discovered by the system.
	 *
	 * <p>
	 * Communication with the card should take place here.
	 *
	 * @param tag
	 *            Discovered tag
	 */
	@Override
	public void onTagDiscovered(Tag tag) {
		Log.i(TAG, "New tag discovered");

		IsoDep isoDep = IsoDep.get(tag);
		if (isoDep != null) {
			try {
				// Connect to the remote NFC device
				isoDep.connect();

				Log.i(TAG, "Requesting remote AID: " + JPAY_LOYALTY_CARD_AID);
				byte[] cmdReadSend = BuildSelectApdu(JPAY_LOYALTY_CARD_AID);
				// Send command to remote device
				byte[] bytesReadReceive = isoDep.transceive(cmdReadSend);
				Log.i(TAG, "Receiving: " + ByteArrayToHexString(bytesReadReceive ));
				int resultReadLength = bytesReadReceive.length;
				byte[] statusWordRead = {
						bytesReadReceive[resultReadLength - 2],
						bytesReadReceive[resultReadLength - 1] };
				byte[] cmdReadReceive = Arrays.copyOf(bytesReadReceive,
						resultReadLength - 2);
				
				if (Arrays.equals(SELECT_OK_SW, statusWordRead)) {
					Log.d("cao", "cao");
					// The remote NFC device will immediately respond with its
					// stored account number
					String strShouKuanCard = new String(cmdReadReceive, "UTF-8");
					

					// 生成ab之间的交易指令
					tmp = SPUtil.getCommand(ctx);
					if (tmp.getTransState().equals(STATE_DONE)) {
						// A读取xml中保存的当前指令，如果状态为DONE,则中止。
						SPUtil.resetCommand(ctx);
						mAccountCallback.get().onSended(
								tmp.getTransCount() + "");
						Log.d("cao2", "cao2");

					} else {
						Log.d("cao3", "cao3");
						// 否则A向B发送一条ab之间的交易记录、并将该记录写入xml,状态为DOING
						tmp.setTransState(STATE_DOING);
						tmp.setShouKuanCard(strShouKuanCard);
						SPUtil.setCommand(ctx, tmp);
						String str = gson.toJson(tmp);
						if((str.length()) % 2 == 1){
							str+=" ";
						}
						byte[] cmdTrans = BuildSelectApdu(str);
						byte[] bytesResultTrans = isoDep.transceive(cmdTrans);
						Log.d("SEND", "SEND:"+ByteArrayToHexString(cmdTrans));

						// A收到B的DOING后返回OK;收到DONE时,将xml中的指令设置为DONE(成功支付).
						
						if (Arrays.equals(SELECT_DOING_SW, bytesResultTrans)) {
							// 返回OK
							byte[] bytesResultOK = isoDep.transceive(SELECT_OK_SW);

							
							if (Arrays.equals(bytesResultOK, SELECT_DONE_SW)) {
								tmp = SPUtil.getCommand(ctx);
								tmp.setTransState(STATE_DONE);
								SPUtil.setCommand(ctx, tmp);

							}

						} else if (Arrays.equals(SELECT_DONE_SW, bytesResultTrans)) {
							// 收到DONE时,将xml中的指令设置为DONE(成功支付).
							tmp = SPUtil.getCommand(ctx);
							tmp.setTransState(STATE_DONE);
							SPUtil.setCommand(ctx, tmp);
						}
					}
				}
				
				

			} catch (IOException e) {
				Log.e(TAG, "Error communicating with card: " + e.toString());
			}
		}
	}

	/**
	 * Build APDU for SELECT AID command. This command indicates which service a
	 * reader is interested in communicating with. See ISO 7816-4.
	 *
	 * @param aid
	 *            Application ID (AID) to select
	 * @return APDU for SELECT AID command
	 */
	public static byte[] BuildSelectApdu(String aid) {
		// Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH |
		// DATA]
		return HexStringToByteArray(SELECT_APDU_HEADER
				+ String.format("%02X", aid.length() / 2) + aid);
	}

	/**
	 * Utility class to convert a byte array to a hexadecimal string.
	 *
	 * @param bytes
	 *            Bytes to convert
	 * @return String, containing hexadecimal representation.
	 */
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

	/**
	 * Utility class to convert a hexadecimal string to a byte string.
	 *
	 * <p>
	 * Behavior with input strings containing non-hexadecimal characters is
	 * undefined.
	 *
	 * @param s
	 *            String containing hexadecimal characters to convert
	 * @return Byte array generated from input
	 */
	public static byte[] HexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

}
