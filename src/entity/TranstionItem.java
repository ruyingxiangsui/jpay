package entity;

public class TranstionItem {
	// 交易类型、交易时间、交易金额
	private String transType;
	private String transTime;
	private String transCount;
	private String transMyCard;
	private String transOtherCard;
	
	public TranstionItem() {
		
	}
	
	public TranstionItem(String transType, String transTime, String transCount,
			String transMyCard, String transOtherCard) {
		this.transType = transType;
		this.transTime = transTime;
		this.transCount = transCount;
		this.transMyCard = transMyCard;
		this.transOtherCard = transOtherCard;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	public String getTransCount() {
		return transCount;
	}

	public void setTransCount(String transCount) {
		this.transCount = transCount;
	}

	public String getTransMyCard() {
		return transMyCard;
	}

	public void setTransMyCard(String transMyCard) {
		this.transMyCard = transMyCard;
	}

	public String getTransOtherCard() {
		return transOtherCard;
	}

	public void setTransOtherCard(String transOtherCard) {
		this.transOtherCard = transOtherCard;
	}

}
