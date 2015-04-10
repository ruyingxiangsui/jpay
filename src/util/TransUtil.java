package util;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dbhelper.JPayDBHelper;
import engine.JPayEngine;
import entity.Command;
import entity.TranstionItem;

public class TransUtil implements JPayEngine {

	private JPayDBHelper helper = null;
	private SQLiteDatabase db = null;
	private Context c;

	public TransUtil(Context context) {
		helper = new JPayDBHelper(context);
		c = context;
	}

	public boolean insertTrans(TranstionItem item) {
		// 添加交易记录
		db = helper.getWritableDatabase();
		db.execSQL(
				"insert into " + TRANS_TABLE_NAME + " values(?,?,?,?,?)",
				new String[] { item.getTransType(), item.getTransCount(),
						item.getTransTime(), item.getTransMyCard(),
						item.getTransOtherCard() });
		// 更改当前余额
		SPUtil.updateAccountOverage(c, item.getTransMyCard(),
				item.getTransType(), Integer.parseInt(item.getTransCount()));

		DBClose();

		return true;
	}

	
	
	/**
	 * 优化函数：最好能够达到分页的方式
	 * 
	 * 
	 * */
	public ArrayList<TranstionItem> getTrans(String account) {
		ArrayList<TranstionItem> tmps = new ArrayList<TranstionItem>();
		Cursor c = getCursor(account);
		while (c.moveToNext()) {
			TranstionItem tmp = new TranstionItem();
			tmp.setTransType(c.getString(c.getColumnIndex(TRANS_COLUMN_TYPE)));
			tmp.setTransCount(c.getString(c.getColumnIndex(TRANS_COLUMN_COUNT)));
			tmp.setTransTime(c.getString(c.getColumnIndex(TRANS_COLUMN_TIME)));
			tmps.add(tmp);
		}
		// 及时关闭Cursor
		c.close();
		return tmps;
	}

	public boolean isExistTrans(Command cmd) {
		Cursor c = getReadableDatabase().rawQuery(
				"select * from " + TRANS_TABLE_NAME + " where "
						+ TRANS_COLUMN_MY_CARD + " = '" + cmd.getShouKuanCard()
						+ "'" + " and " + TRANS_COLUMN_OTHER_CARD + "= '"
						+ cmd.getHuanKuanCard() + "'",null);
		boolean flag = c.getCount()>0;
		c.close();
		return flag;
	}

	public SQLiteDatabase getReadableDatabase() {
		return helper.getReadableDatabase();
	}

	public Cursor getCursor(String account) {
		Cursor c = getReadableDatabase().rawQuery(
				"select * from " + TRANS_TABLE_NAME + " where "
						+ TRANS_COLUMN_MY_CARD + " = '" + account + "'"
						+ " order by " + TRANS_COLUMN_TIME + " desc", null);
		return c;
	}

	public void DBClose() {
		db.close();
	}
}
