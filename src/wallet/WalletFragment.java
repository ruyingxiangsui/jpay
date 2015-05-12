package wallet;

import util.SPUtil;
import activity.CashActivity;
import activity.FAQActivity;
import activity.LocalTransactionsActivity;
import activity.RechargeActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import application.JPayApplication;

import com.yunhuirong.jpayapp.R;

public class WalletFragment extends Fragment {

	private Button localTransactionsBtn;
	private TextView localWalletCountTv;

	private Button cashBtn;
	private Button rechargeBtn;
	private TextView faqs;

	private String current_account;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		current_account = SPUtil.getCurrentUserInfo((JPayApplication)getActivity().getApplication()).getUserName();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_wallet, container, false);
		localTransactionsBtn = (Button) v
				.findViewById(R.id.bt_all_transactions);
		localWalletCountTv = (TextView) v.findViewById(R.id.localWalletCount);
		cashBtn = (Button) v.findViewById(R.id.cashBtn);
		rechargeBtn = (Button) v.findViewById(R.id.rechargeBtn);
		faqs = (TextView) v.findViewById(R.id.FAQ);

		localWalletCountTv.setText(getLocalWalletCount());
		
		/** 显示本地交易记录 */
		localTransactionsBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(),
						LocalTransactionsActivity.class);
				startActivity(i);
			}
		});
		
		/** 兑现*/
		cashBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), CashActivity.class);
				startActivity(i);
			}
		});
		
		/** 充值*/
		rechargeBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), RechargeActivity.class);
				startActivity(i);
			}
		});

		/** 疑问*/
		faqs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), FAQActivity.class);
				startActivity(i);
			}
		});
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		localWalletCountTv.setText(getLocalWalletCount());
	}
	
	public String getLocalWalletCount(){
		
		return "￥"+SPUtil.getAccountOverage((JPayApplication)getActivity().getApplication(), current_account);
	}
	
	
}
