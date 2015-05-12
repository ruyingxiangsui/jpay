package engine;

public interface JPayEngine {

	static final String TEXT_TYPE = " TEXT ";
	static final String REAL_TYPE = " REAL ";
	static final String SPACE = " ";
	static final String COMMA_SEP = ",";

	static final int DATABASE_VERSION = 1;
	static final int MAX_HUANKUAN_COUNT = 500;

	static final String DATABASE_NAME = "jpay_database";

	// 交易表
	static final String TRANS_TABLE_NAME = "table_trans";
	static final String TRANS_COLUMN_TYPE = "trans_type";
	static final String TRANS_COLUMN_COUNT = "trans_count";
	static final String TRANS_COLUMN_TIME = "trans_time";
	static final String TRANS_COLUMN_MY_CARD = "trans_my_card";
	static final String TRANS_COLUMN_OTHER_CARD = "trans_other_card";

	// 用户表
	static final String USER_TABLE_NAME = "table_user";
	static final String USER_CONLUMN_ACCOUNT = "user_account";
	static final String USER_CONLUMN_OVERAGE = "user_overage";
	static final String USER_CONLUMN_LOCAL_PASS = "user_local_password";

	// 交易类型
	static final String TRANS_TYPE_CASH = "提现";
	static final String TRANS_TYPE_RECHARGE = "充值";
	static final String TRANS_TYPE_SHOUKUAN = "收款";
	static final String TRANS_TYPE_HUANKUAN = "还款";

	/**
	 * SharedPrefrences
	 * */
	// 当前用户信息
	static final String SP_CURRENT_ACCOUNT_INFO = "sp_current_user_info";
	static final String CURRENT_ACCOUNT_NAME = "current_account_name";
	// 本地账户信息
	static final String ACCOUNT_NAME = "account_name";
	static final String ACCOUNT_PASS = "account_pass";
	static final String ACCOUNT_OVERAGE = "account_overage";
	// 保存ctime的文件(每个用户对应的键为该用户的名字,即手机号)
	static final String SP_CTIME = "sp_ctime";
	static final String DB_CTIME = "db_ctime";
	// 当前还款、收款指令
	static final String SP_CURRENT_COMMAND_INFO = "sp_current_command_info";
	static final String SHOU_COMMAND_TRANS_CODE = "shou_trans_code";
	static final String SHOU_COMMAND_HUANKUAN_CARD = "shou_huankuan_card";
	static final String SHOU_COMMAND_SHOUKUAN_CARD = "shou_shoukuan_card";
	static final String SHOU_COMMAND_TRANS_COUNT = "shou_trans_count";
	static final String SHOU_COMMAND_TRANS_STATE = "shou_trans_state";

	static final String HUAN_COMMAND_TRANS_CODE = "huan_trans_code";
	static final String HUAN_COMMAND_HUANKUAN_CARD = "huan_huankuan_card";
	static final String HUAN_COMMAND_SHOUKUAN_CARD = "huan_shoukuan_card";
	static final String HUAN_COMMAND_TRANS_COUNT = "huan_trans_count";
	static final String HUAN_COMMAND_TRANS_STATE = "huan_trans_state";

	static final String NULL = "";

	// reader/card
	static final String JPAY_LOYALTY_CARD_AID = "F222222222";
	static final String JPAY_KEY = "EF234322";
	static final String SELECT_APDU_HEADER = "00A40400";
	static final String GET_APDU_HEADER = "00B00000";
	static final byte[] SELECT_OK_SW = { (byte) 0x90, (byte) 0x00 };
	static final byte[] SELECT_READ_SW = { (byte) 0x90, (byte) 0x01 };
	static final byte[] SELECT_DOING_SW = { (byte) 0x90, (byte) 0x02 };
	static final byte[] SELECT_FINISH_SW = { (byte) 0x90, (byte) 0x03 };
	static final String TAG = "JPayLoyaltyCardReader";

	static final String STATE_DOING = "doin";
	static final String STATE_DONE = "done";
	static final String STATE_OK = "ok";
	static final String STATE_INIT = "init";
	static final String TRANS_UUID = "trans_uuid";

	// apdu指令类型
	static final String APDU_TYPE_REQUEST = "AA00";
	static final String APDU_TYPE_DOING = "AA01";
	static final String APDU_TYPE_DONE = "AA02";

}
