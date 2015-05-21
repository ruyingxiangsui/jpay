package util;

import application.JPayApplication;
import engine.JPayEngine;
import entity.Command;
import entity.CurrentUser;

public class SPUtil implements JPayEngine {

	/**
	 * 得到当前用户信息
	 * 
	 * */
	public static CurrentUser getCurrentUserInfo(JPayApplication app) {
		return SPAdapter.getCurrentUserInfo(app);
	}

	/**
	 * 设置当前用户信息
	 * 
	 * */
	public static void setCurrentUserInfo(JPayApplication app, CurrentUser cu) {
		SPAdapter.setCurrentUserInfo(app, cu);
	}

	/**
	 * 清除用户本地数据
	 * 
	 * */
	public static void clearAccountLocalData(JPayApplication app, String account) {
		SPAdapter.clearAccountLocalData(app,account);
	}

	/**
	 * 退出
	 * 
	 * */
	public static void logOut(JPayApplication app) {
		SPAdapter.logOut(app);
		;
	}

	/**
	 * 获取用户的余额信息
	 * */
	public static int getAccountOverage(JPayApplication app, String account) {
		return SPAdapter.getAccountOverage(app, account);
	}

	/**
	 * 更新余额信息
	 * 
	 * */
	public static void updateAccountOverage(JPayApplication app,
			String account, String type, int count) {
		SPAdapter.updateAccountOverage(app, account, type, count);
		// 更新ctime
		updateCTime(app, account);
	}

	/**
	 * 更新密码
	 * 
	 * */
	public static void updateAccountPass(JPayApplication app, String account,
			String pass) {
		SPAdapter.updateAccountPass(app, account, pass);
		// 更新ctime
		updateCTime(app, account);
	}

	/**
	 * 验证本地支付密码
	 * */
	public static boolean checkAccountPass(JPayApplication app, String account,
			String pass) {

		return SPAdapter.checkAccountPass(app, account, pass);
	}

	/**
	 * 每次更改数据库、xml文件时更新 内容为文件的ctime(文件内容或目录改变时也会修改ctime)
	 * 
	 * */
	public static void updateCTime(JPayApplication app, String account) {
		SPAdapter.updateCTime(app, account);
	}

	/**
	 * 核对当前用户的本地数据是否修改过
	 * 
	 * */
	public static boolean checkAccountCTime(JPayApplication app, String account) {

		return SPAdapter.checkAccountCTime(app, account);
	}

	/**
	 * 获取当前收款指令
	 * 
	 * */
	public static Command getShouCommand(JPayApplication app) {
		return SPAdapter.getShouCommand(app, getCurrentUserInfo(app)
				.getUserName());
	}

	/**
	 * 设置当前收款指令
	 * 
	 * */
	public static void setShouCommand(JPayApplication app, Command cmd) {
		SPAdapter.setShouCommand(app, cmd, getCurrentUserInfo(app)
				.getUserName());
		// 更新ctime
		updateCTime(app, getCurrentUserInfo(app).getUserName());
	}

	/**
	 * 获取当前还款指令
	 * 
	 * */
	public static Command getHuanCommand(JPayApplication app) {
		return SPAdapter.getHuanCommand(app, getCurrentUserInfo(app)
				.getUserName());
	}

	/**
	 * 设置当前还款指令
	 * 
	 * */
	public static void setHuanCommand(JPayApplication app, Command cmd) {
		SPAdapter.setHuanCommand(app, cmd, getCurrentUserInfo(app)
				.getUserName());
		// 更新ctime
		updateCTime(app, getCurrentUserInfo(app).getUserName());

	}

	/**
	 * 重置当前还款指令
	 * 
	 * */
	public static void resetHuanCommand(JPayApplication app) {
		SPAdapter.resetHuanCommand(app, getCurrentUserInfo(app).getUserName());
		// 更新ctime
		updateCTime(app, getCurrentUserInfo(app).getUserName());
	}

	/**
	 * 重置当前收款指令
	 * 
	 * */
	public static void resetShouCommand(JPayApplication app) {
		SPAdapter.resetShouCommand(app, getCurrentUserInfo(app).getUserName());
		// 更新ctime
		updateCTime(app, getCurrentUserInfo(app).getUserName());
	}

}
