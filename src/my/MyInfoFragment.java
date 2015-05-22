package my;

import http.JPayRequestListener;
import http.JpayApi;
import http.response.BalanceResp;
import util.SPUtil;
import activity.LocalTransactionsActivity;
import activity.SettingPasswordActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import application.JPayApplication;

import com.yunhuirong.jpayapp.R;

public class MyInfoFragment extends Fragment {
	
	private TextView username;
	private TextView settings;
	private TextView all_trans;
	private TextView balance;
	private TextView exit;
	
	JPayApplication app;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (JPayApplication) getActivity().getApplication();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_my, container, false);
		username = (TextView)v.findViewById(R.id.tv_username);
		settings = (TextView)v.findViewById(R.id.tv_settings);
		all_trans = (TextView)v.findViewById(R.id.all_trans);
		balance = (TextView)v.findViewById(R.id.balance);
		exit = (TextView) v.findViewById(R.id.bt_exit);
		
		username.setText(SPUtil.getCurrentUserInfo((JPayApplication)getActivity().getApplication()).getUserName());
		settings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), SettingPasswordActivity.class);
				startActivity(i);
			}
		});
		
		all_trans.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(),
						LocalTransactionsActivity.class);
				startActivity(i);
			}
		});
		balance.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				JpayApi.getBalance(SPUtil.getCurrentUserInfo(app).getAccessToken(),new JPayRequestListener<BalanceResp>() {

					@Override
					public void onRequestSucceeded(BalanceResp data) {
						Toast.makeText(app, data.balance, Toast.LENGTH_LONG).show();
					}

					@Override
					public void onRequestFailed(String ex) {
						Toast.makeText(app, "查询失败！\n"+ex, Toast.LENGTH_SHORT).show();
					}
				} );
			}
		});
		exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SPUtil.logOut((JPayApplication)getActivity().getApplication());
				getActivity().finish();
			}
		});
		return v;
	}
	
}
