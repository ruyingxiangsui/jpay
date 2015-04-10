package util;

import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import engine.JPayEngine;
import entity.Command;
import entity.UserInfo;

public class SPUtil implements JPayEngine {

	/**
	 * 得到当前用户信息
	 * 
	 * */
	public static UserInfo getCurrentUserInfo(Context c) {
		SharedPreferences sp = c.getSharedPreferences(SP_CURRENT_ACCOUNT_INFO,
				Activity.MODE_PRIVATE);
		UserInfo uf = new UserInfo();
		uf.setUserName(sp.getString(CURRENT_ACCOUNT_NAME, NULL));
		return uf;
	}

	/**
	 * 设置当前用户信息
	 * 
	 * */
	public static void setCurrentUserInfo(Context c, UserInfo uf) {
		SharedPreferences sp = c.getSharedPreferences(SP_CURRENT_ACCOUNT_INFO,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(CURRENT_ACCOUNT_NAME, uf.getUserName());
		editor.commit();
	}

	/**
	 * 退出
	 * 
	 * */
	public static void logOut(Context c) {
		SharedPreferences sp = c.getSharedPreferences(SP_CURRENT_ACCOUNT_INFO,
				Activity.MODE_PRIVATE);
		sp.edit().clear().commit();
	}

	/**
	 * 获取用户的余额信息
	 * */
	public static int getAccountOverage(Context c, String account) {
		return c.getSharedPreferences(account, Activity.MODE_PRIVATE).getInt(
				ACCOUNT_OVERAGE, 0);
	}

	/**
	 * 更新余额信息
	 * 
	 * */
	public static void updateAccountOverage(Context c, String account,
			String type, int count) {
		SharedPreferences sp = c.getSharedPreferences(account,
				Activity.MODE_PRIVATE);
		int over = sp.getInt(ACCOUNT_OVERAGE, 0);
		if (type.equals(TRANS_TYPE_CASH) || type.equals(TRANS_TYPE_HUANKUAN)) {
			count = (-1) * count;
		}
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(ACCOUNT_OVERAGE, over + count);
		editor.commit();

	}

	/**
	 * 更新密码
	 * 
	 * */
	public static void updateAccountPass(Context c, String account, String pass) {
		SharedPreferences sp = c.getSharedPreferences(account,
				Activity.MODE_PRIVATE);
		sp.edit().putString(ACCOUNT_PASS, pass).commit();
	}

	/**
	 * 验证本地支付密码
	 * */
	public static boolean checkAccountPass(Context c, String account,
			String pass) {

		return c.getSharedPreferences(account, Activity.MODE_PRIVATE)
				.getString(ACCOUNT_PASS, NULL).equals(pass);
	}

	/**
	 * 每次更改数据库、xml文件时更新 内容为文件的ctime(文件内容或目录改变时也会修改ctime)
	 * 
	 * */
	public static void updateCTime(Context c, String filepath) {
		// 数据库的ctime
		SharedPreferences sp = c.getSharedPreferences("files_ctime",
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("db_ctime", "db_time_"
				+ getFileCTime(c.getDatabasePath("table_trans")
						.getAbsolutePath()));
		editor.putString("sp_ctime", getFileCTime("") + "");
		editor.commit();
	}

	/**
	 * 核对当前用户的本地数据是否修改过
	 * 
	 * */
	public static boolean checkAccountCTime(Context c, String account) {

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
	 * 获取当前指令
	 * 
	 * */
	public static Command getCommand(Context ctx) {

		SharedPreferences sp = ctx.getSharedPreferences(getCurrentUserInfo(ctx)
				.getUserName(), Activity.MODE_PRIVATE);
		Command cmd = new Command();
		cmd.setTransCode(sp.getString(COMMAND_TRANS_CODE, ""));
		cmd.setHuanKuanCard(sp.getString(COMMAND_MYCARD, ""));
		cmd.setShouKuanCard(sp.getString(COMMAND_YOURCARD, ""));
		cmd.setTransCount(sp.getInt(COMMAND_TRANS_COUNT, 0));
		cmd.setTransState(sp.getString(COMMAND_TRANS_STATE, ""));

		return cmd;
	}

	/**
	 * 设置当前指令
	 * 
	 * */
	public static void setCommand(Context ctx, Command cmd) {
		SharedPreferences sp = ctx.getSharedPreferences(getCurrentUserInfo(ctx)
				.getUserName(), Activity.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(COMMAND_TRANS_CODE, cmd.getTransCode());
		editor.putString(COMMAND_MYCARD, cmd.getHuanKuanCard());
		editor.putString(COMMAND_YOURCARD, cmd.getShouKuanCard());
		editor.putInt(COMMAND_TRANS_COUNT, cmd.getTransCount());
		editor.putString(COMMAND_TRANS_STATE, cmd.getTransState());

		editor.commit();

	}

	/**
	 * 重置当前指令
	 * 
	 * */
	public static void resetCommand(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(getCurrentUserInfo(ctx)
				.getUserName(), Activity.MODE_PRIVATE);
		sp.edit().clear().commit();
	}

	/**
	 * 获取当前交易使用的UUID
	 * 
	 * */
	public static String getCurrentTransUUID(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(getCurrentUserInfo(ctx)
				.getUserName(), Activity.MODE_PRIVATE);
		return sp.getString(TRANS_UUID, UUID.randomUUID() + "");
	}

	/**
	 * 清除当前交易使用的UUID
	 * 
	 * */
	public static void clearCurrentTransUUID(Context ctx) {
		ctx.getSharedPreferences(getCurrentUserInfo(ctx).getUserName(),
				Activity.MODE_PRIVATE).edit().remove(TRANS_UUID);

	}
}
