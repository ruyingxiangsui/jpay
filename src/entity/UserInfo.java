package entity;

public class UserInfo {
	private String userName;
	private String localWalletId;//设置成手机号？

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLocalWalletId() {
		return localWalletId;
	}

	public void setLocalWalletId(String localWalletId) {
		this.localWalletId = localWalletId;
	}

}
