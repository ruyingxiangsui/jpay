package http.response;

import java.util.ArrayList;

import entity.Payment;

public class PaymentsResp extends JpayBaseResp {
	public String action;
	public ArrayList<Payment> data;
}

