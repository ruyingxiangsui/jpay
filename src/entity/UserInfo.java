package entity;

public class UserInfo {
	//用户基本信息
	public String username;
	public String localpass;// 本地支付密码
	public int account_overage;//余额

	//还款命令
	public String huan_shoukuan_card;
	public String huan_huankuan_card;
	public int huan_trans_count;
	public String huan_trans_code;
	public String huan_trans_state;

	//收款命令
	public String shou_shoukuan_card;
	public String shou_huankuan_card;
	public int shou_trans_count;
	public String shou_trans_code;
	public String shou_trans_state;
}
