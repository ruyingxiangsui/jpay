package util;

import java.io.File;

import com.google.gson.Gson;

import application.JPayApplication;
import engine.JPayEngine;
import entity.Command;
import entity.CurrentUser;
import entity.UserInfo;

public class SPAdapter implements JPayEngine {

	static Gson gson = new Gson();

	/**
	 * 得到当前用户信息
	 * 
	 * */
	public static CurrentUser getCurrentUserInfo(JPayApplication app) {
		CurrentUser ui = new CurrentUser();
		try {
			String str = FileHelper.readFile(app.getFilesDir() + File.separator
					+ "current_user");
			String des = AesUtil.decrypt_AES(str, app.getKey(), app.getIv());
			ui.setUserName(des);
		} catch (Exception e) {
			ui.setUserName("");
			e.printStackTrace();
		}
		return ui;
	}

	/**
	 * 设置当前用户信息
	 * 
	 * */
	public static void setCurrentUserInfo(JPayApplication app, CurrentUser cu) {
		try {
			String src = cu.getUserName();
			String des = AesUtil.encrypt_AES(src, app.getKey(), app.getIv());
			FileHelper.writeFile(app.getFilesDir() + "/current_user", des);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 退出
	 * 
	 * */
	public static void logOut(JPayApplication app) {
		FileHelper.writeFile(app.getFilesDir() + File.separator
				+ "current_user", "");
	}

	/**
	 * 获取用户的余额信息
	 * */
	public static int getAccountOverage(JPayApplication app, String account) {
		int count = 0;
		try {
			String src = FileHelper.readFile(app.getFilesDir() + File.separator
					+ account);
			if (src == null || src.equals("")) {
				count = 0;
			}
			String des = AesUtil.decrypt_AES(src, app.getKey(), app.getIv());
			UserInfo ui = gson.fromJson(des, UserInfo.class);
			count = ui.account_overage;
		} catch (Exception e) {
			count = 0;
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 更新余额信息
	 * 
	 * */
	public static void updateAccountOverage(JPayApplication app,
			String account, String type, int count) {

		try {
			String src = FileHelper.readFile(app.getFilesDir() + File.separator
					+ account);
			UserInfo ui;
			if (src == null || src.equals("")) {
				ui = new UserInfo();
				ui.uername = account;
			} else {
				String des = AesUtil
						.decrypt_AES(src, app.getKey(), app.getIv());
				ui = gson.fromJson(des, UserInfo.class);
			}
			if (TRANS_TYPE_CASH.equals(type)
					|| TRANS_TYPE_HUANKUAN.equals(type)) {
				count = -count;
			}
			ui.account_overage += count;

			FileHelper.writeFile(
					app.getFilesDir() + File.separator + account,
					AesUtil.encrypt_AES(gson.toJson(ui), app.getKey(),
							app.getIv()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新密码
	 * 
	 * */
	public static void updateAccountPass(JPayApplication app, String account,
			String pass) {
		try {
			String src = FileHelper.readFile(app.getFilesDir() + File.separator
					+ account);

			String des = AesUtil.decrypt_AES(src, app.getKey(), app.getIv());
			UserInfo ui = gson.fromJson(des, UserInfo.class);
			ui.localpass = pass;

			FileHelper.writeFile(
					app.getFilesDir() + File.separator + account,
					AesUtil.encrypt_AES(gson.toJson(ui), app.getKey(),
							app.getIv()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 验证本地支付密码
	 * */
	public static boolean checkAccountPass(JPayApplication app, String account,
			String pass) {
		try {
			String src = FileHelper.readFile(app.getFilesDir() + File.separator
					+ account);

			String des = AesUtil.decrypt_AES(src, app.getKey(), app.getIv());
			UserInfo ui = gson.fromJson(des, UserInfo.class);
			return ui.localpass == pass;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 每次更改数据库、xml文件时更新 内容为文件的ctime(文件内容或目录改变时也会修改ctime)
	 * 
	 * */
	public static void updateCTime(JPayApplication app, String filepath) {

	}

	/**
	 * 核对当前用户的本地数据是否修改过
	 * 
	 * */
	public static boolean checkAccountCTime(JPayApplication app, String account) {
		return false;
	}

	/**
	 * 需调用本地方法!!!!!
	 * 
	 * */
	public static final String getFileCTime(String path) {
		return "";
	}

	/**
	 * 获取当前收款指令
	 * 
	 * */
	public static Command getShouCommand(JPayApplication app, String account) {
		try {
			String src = FileHelper.readFile(app.getFilesDir() + File.separator
					+ account);

			String des = AesUtil.decrypt_AES(src, app.getKey(), app.getIv());
			UserInfo ui = gson.fromJson(des, UserInfo.class);
			Command cmd = new Command();
			cmd.setHuanKuanCard(ui.shou_huankuan_card);
			cmd.setShouKuanCard(ui.shou_shoukuan_card);
			cmd.setTransCode(ui.shou_trans_code);
			cmd.setTransCount(ui.shou_trans_count);
			cmd.setTransState(ui.shou_trans_state);

			return cmd;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 设置当前收款指令
	 * 
	 * */
	public static void setShouCommand(JPayApplication app, Command cmd,
			String account) {
		try {
			String src = FileHelper.readFile(app.getFilesDir() + File.separator
					+ account);

			String des = AesUtil.decrypt_AES(src, app.getKey(), app.getIv());
			UserInfo ui = gson.fromJson(des, UserInfo.class);
			ui.shou_huankuan_card = cmd.getHuanKuanCard();
			ui.shou_shoukuan_card = cmd.getShouKuanCard();
			ui.shou_trans_code = cmd.getTransCode();
			ui.shou_trans_count = cmd.getTransCount();
			ui.shou_trans_state = cmd.getTransState();

			FileHelper.writeFile(
					app.getFilesDir() + File.separator + account,
					AesUtil.encrypt_AES(gson.toJson(ui), app.getKey(),
							app.getIv()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取当前还款指令
	 * 
	 * */
	public static Command getHuanCommand(JPayApplication app, String account) {
		try {
			String src = FileHelper.readFile(app.getFilesDir() + File.separator
					+ account);

			String des = AesUtil.decrypt_AES(src, app.getKey(), app.getIv());
			UserInfo ui = gson.fromJson(des, UserInfo.class);
			Command cmd = new Command();
			cmd.setHuanKuanCard(ui.huan_huankuan_card);
			cmd.setShouKuanCard(ui.huan_shoukuan_card);
			cmd.setTransCode(ui.huan_trans_code);
			cmd.setTransCount(ui.huan_trans_count);
			cmd.setTransState(ui.huan_trans_state);
			return cmd;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 设置当前还款指令
	 * 
	 * */
	public static void setHuanCommand(JPayApplication app, Command cmd,
			String account) {
		try {
			String src = FileHelper.readFile(app.getFilesDir() + File.separator
					+ account);

			String des = AesUtil.decrypt_AES(src, app.getKey(), app.getIv());
			UserInfo ui = gson.fromJson(des, UserInfo.class);
			ui.huan_huankuan_card = cmd.getHuanKuanCard();
			ui.huan_shoukuan_card = cmd.getShouKuanCard();
			ui.huan_trans_code = cmd.getTransCode();
			ui.huan_trans_count = cmd.getTransCount();
			ui.huan_trans_state = cmd.getTransState();

			FileHelper.writeFile(
					app.getFilesDir() + File.separator + account,
					AesUtil.encrypt_AES(gson.toJson(ui), app.getKey(),
							app.getIv()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重置当前还款指令
	 * 
	 * */
	public static void resetHuanCommand(JPayApplication app, String account) {
		try {
			String src = FileHelper.readFile(app.getFilesDir() + File.separator
					+ account);

			String des = AesUtil.decrypt_AES(src, app.getKey(), app.getIv());
			UserInfo ui = gson.fromJson(des, UserInfo.class);
			ui.huan_huankuan_card = "";
			ui.huan_shoukuan_card = "";
			ui.huan_trans_code = "";
			ui.huan_trans_count = 0;
			ui.huan_trans_state = "";

			FileHelper.writeFile(
					app.getFilesDir() + File.separator + account,
					AesUtil.encrypt_AES(gson.toJson(ui), app.getKey(),
							app.getIv()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重置当前收款指令
	 * 
	 * */
	public static void resetShouCommand(JPayApplication app, String account) {
		try {
			String src = FileHelper.readFile(app.getFilesDir() + File.separator
					+ account);

			String des = AesUtil.decrypt_AES(src, app.getKey(), app.getIv());
			UserInfo ui = gson.fromJson(des, UserInfo.class);
			ui.shou_huankuan_card = "";
			ui.shou_shoukuan_card = "";
			ui.shou_trans_code = "";
			ui.shou_trans_count = 0;
			ui.shou_trans_state = "";

			FileHelper.writeFile(
					app.getFilesDir() + File.separator + account,
					AesUtil.encrypt_AES(gson.toJson(ui), app.getKey(),
							app.getIv()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
