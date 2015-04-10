package dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import engine.JPayEngine;

public class JPayDBHelper extends SQLiteOpenHelper implements JPayEngine {

	private static final String SQL_CREATE_TRANS_TABLE = "CREATE TABLE "

	+ TRANS_TABLE_NAME + " (" + TRANS_COLUMN_TYPE + SPACE + TEXT_TYPE
			+ COMMA_SEP + TRANS_COLUMN_COUNT + SPACE + TEXT_TYPE + COMMA_SEP
			+ TRANS_COLUMN_TIME + SPACE + TEXT_TYPE + COMMA_SEP
			+ TRANS_COLUMN_MY_CARD + SPACE + TEXT_TYPE + COMMA_SEP
			+ TRANS_COLUMN_OTHER_CARD + SPACE + TEXT_TYPE + ")";

	private static final String SQL_DELETE_TRANS_TABLE = "DROP TABLE IF EXISTS "
			+ TRANS_TABLE_NAME;

	private static final String SQL_CREATE_USER_TABLE = "CREATE TABLE "
			+ USER_TABLE_NAME + "(" + USER_CONLUMN_ACCOUNT + SPACE + TEXT_TYPE
			+ COMMA_SEP + USER_CONLUMN_OVERAGE + SPACE + TEXT_TYPE + COMMA_SEP
			+ USER_CONLUMN_LOCAL_PASS + SPACE + TEXT_TYPE + ")";

	private static final String SQL_DELETE_USER_TABLE = "DROP TABLE IF EXISTS "
			+ USER_TABLE_NAME;

	public JPayDBHelper(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TRANS_TABLE);
		db.execSQL(SQL_CREATE_USER_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_TRANS_TABLE);
		db.execSQL(SQL_DELETE_USER_TABLE);
		onCreate(db);
	}

}
