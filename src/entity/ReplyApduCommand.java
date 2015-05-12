package entity;

public class ReplyApduCommand {
	/**
	 * 回复apdu 的指令类型
	 * 回复apdu 的交易号
	 * 回复apdu 的还款卡号
	 * 回复apdu 的还款金额
	 * key
	 * */
	private String type;
	private String code;
	private String card;
	private int money;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
}
