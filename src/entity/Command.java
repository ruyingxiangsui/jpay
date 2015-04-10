package entity;

public class Command {
	/**
	 * 交易指令： 交易码、我的卡、别人的卡、交易金额
	 * 
	 * */
	private String transCode;
	private String huanKuanCard;
	private String shouKuanCard;
	private int transCount;
	private String transState;

	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	public String getHuanKuanCard() {
		return huanKuanCard;
	}

	public void setHuanKuanCard(String huanKuanCard) {
		this.huanKuanCard = huanKuanCard;
	}

	public String getShouKuanCard() {
		return shouKuanCard;
	}

	public void setShouKuanCard(String shouKuanCard) {
		this.shouKuanCard = shouKuanCard;
	}

	public int getTransCount() {
		return transCount;
	}

	public void setTransCount(int transCount) {
		this.transCount = transCount;
	}

	public String getTransState() {
		return transState;
	}

	public void setTransState(String transState) {
		this.transState = transState;
	}

}
