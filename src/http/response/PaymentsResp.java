package http.response;

import java.util.ArrayList;

public class PaymentsResp extends JpayBaseResp {
	public String action;
	public ArrayList<Payment> data;
}
class Payment{
	public int amount;
	public String bankref;
	public String status;
	public String paymentid;
	public String date;
	public String action;
	public String note;
	
}
