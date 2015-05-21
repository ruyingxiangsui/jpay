package activity;

import http.JPayRequestListener;
import http.JpayApi;
import http.response.PaymentsResp;

import java.util.ArrayList;

import util.SPUtil;
import adapter.NetTransationsAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import application.JPayApplication;

import com.yunhuirong.jpayapp.R;

import entity.Payment;

public class NetTransActivity extends Activity {

	private ListView listView;
	private Button button;
	private TextView nextPage;
	NetTransationsAdapter adapter;
	String current_account;
	JPayApplication app;
	ArrayList<Payment> pays = new ArrayList<Payment>();
	static int current_page = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_net_trans);
		app = (JPayApplication) getApplication();
		current_account = SPUtil.getCurrentUserInfo(app).getUserName();

		listView = (ListView) findViewById(R.id.listview_net_transtions);
		nextPage = (TextView) findViewById(R.id.next_page);
		nextPage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				current_page++;
				getNetTrans(current_page);
			}
		});
		button = (Button) findViewById(R.id.trans_net_back);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				NetTransActivity.this.finish();
			}
		});
		adapter = new NetTransationsAdapter(pays, getApplicationContext());
		listView.setAdapter(adapter);
		getNetTrans(current_page);
	}

	private void getNetTrans(int page) {
		JpayApi.getPayments(SPUtil.getCurrentUserInfo(app).getAccessToken(),
				page, new JPayRequestListener<PaymentsResp>() {

					@Override
					public void onRequestSucceeded(PaymentsResp data) {
							pays.addAll(data.data);
							adapter.notifyDataSetChanged();
					}

					@Override
					public void onRequestFailed(String ex) {

					}
				});

	}

}
