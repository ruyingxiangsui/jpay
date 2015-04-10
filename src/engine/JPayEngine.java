package engine;

public interface JPayEngine {
	
	static final String TEXT_TYPE = " TEXT ";
	static final String REAL_TYPE = " REAL ";
	static final String SPACE = " ";
	static final String COMMA_SEP = ",";

	static final int DATABASE_VERSION = 1;

	static final String DATABASE_NAME = "jpay_database";
	
	//交易表
	static final String TRANS_TABLE_NAME = "table_trans";
	static final String TRANS_COLUMN_TYPE = "trans_type";
	static final String TRANS_COLUMN_COUNT = "trans_count";
	static final String TRANS_COLUMN_TIME = "trans_time";
	static final String TRANS_COLUMN_MY_CARD = "trans_my_card";
	static final String TRANS_COLUMN_OTHER_CARD = "trans_other_card";
	
	//用户表
	static final String USER_TABLE_NAME = "table_user";
	static final String USER_CONLUMN_ACCOUNT = "user_account";
	static final String USER_CONLUMN_OVERAGE = "user_overage";
	static final String USER_CONLUMN_LOCAL_PASS = "user_local_password";
	
	
	//交易类型
	static final String TRANS_TYPE_CASH = "提现";
	static final String TRANS_TYPE_RECHARGE = "充值";
	static final String TRANS_TYPE_SHOUKUAN = "收款";
	static final String TRANS_TYPE_HUANKUAN = "还款";
	
	
	/**
	 * SharedPrefrences
	 * */
	//当前用户信息
	static final String SP_CURRENT_ACCOUNT_INFO = "sp_current_user_info";
	static final String CURRENT_ACCOUNT_NAME = "current_account_name";
	//本地账户信息
	static final String ACCOUNT_NAME = "account_name";
	static final String ACCOUNT_PASS = "account_pass";
	static final String ACCOUNT_OVERAGE = "account_overage";
	//保存ctime的文件(每个用户对应的键为该用户的名字,即手机号)
	static final String SP_CTIME = "sp_ctime";
	static final String DB_CTIME = "db_ctime";
	//当前指令
	static final String SP_CURRENT_COMMAND_INFO = "sp_current_command_info";
	static final String COMMAND_TRANS_CODE = "trans_code";
	static final String COMMAND_MYCARD = "my_card";
	static final String COMMAND_YOURCARD = "your_card";
	static final String COMMAND_TRANS_COUNT = "trans_count";
	static final String COMMAND_TRANS_STATE = "trans_state";
	
	
	static final String NULL = "";
	
	
	
	
	//reader/card
	public static final String JPAY_LOYALTY_CARD_AID = "F222222222";
	public static final String SELECT_APDU_HEADER = "00A40400";
	public static final byte[] SELECT_OK_SW = {(byte) 0x90, (byte) 0x00};
	public static final byte[] SELECT_READ_SW = {(byte) 0x90,(byte)0x01};
	public static final byte[] SELECT_DOING_SW = {(byte)0x90,(byte)0x02};
	public static final byte[] SELECT_DONE_SW = {(byte)0x90,(byte)0x03};
	public static final String TAG = "JPayLoyaltyCardReader";
	
	public static final String STATE_DOING = "DOIN";
	public static final String STATE_DONE = "DONE";
	public static final String STATE_OK = "OK";
	public static final String TRANS_UUID = "trans_uuid";
	
	
}
