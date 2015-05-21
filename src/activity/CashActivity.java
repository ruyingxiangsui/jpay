package activity;

import http.JPayRequestListener;
import http.JpayApi;
import http.response.CashResp;

import java.text.SimpleDateFormat;

import util.SPUtil;
import util.TransUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import application.JPayApplication;

import com.yunhuirong.jpayapp.R;

import engine.JPayEngine;
import entity.TranstionItem;

@SuppressLint("SimpleDateFormat")
public class CashActivity extends Activity implements JPayEngine {

	private EditText editText;
	private Button button;
	private Button back;
	private JPayApplication app;
	private String current_account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cash);
		app = (JPayApplication) getApplication();
		current_account = SPUtil.getCurrentUserInfo((JPayApplication)this.getApplication()).getUserName();
		
		editText = (EditText) findViewById(R.id.cash_count_edittext);
		button = (Button) findViewById(R.id.cashing_btn);

		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startCashing();
			}
		});

		back = (Button) findViewById(R.id.cash_back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(CashActivity.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				CashActivity.this.finish();
			}
		});

	}

	private void startCashing() {
		final String str = editText.getText().toString();
		if (str == null || str.equals("")) {
			Toast.makeText(this, "请输入提现金额", Toast.LENGTH_SHORT).show();
		} else {

			int count = SPUtil.getAccountOverage((JPayApplication) getApplication(), current_account);

			if (count < Integer.valueOf(str)) {
				Toast.makeText(getApplicationContext(), "余额不足",
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			button.setClickable(false);
			JpayApi.cash(SPUtil.getCurrentUserInfo(app).getAccessToken(),  Integer.valueOf(str), "", new JPayRequestListener<CashResp>() {

				@Override
				public void onRequestSucceeded(CashResp data) {
					SimpleDateFormat sDateFormat = new SimpleDateFormat(
							"yyyy-MM-dd   hh:mm:ss");
					String time = sDateFormat.format(new java.util.Date());
					TranstionItem item = new TranstionItem();
					item.setTransCount(str);
					item.setTransType(TRANS_TYPE_CASH);
					item.setTransTime(time);
					item.setTransMyCard(current_account);

					SPUtil.updateAccountOverage((JPayApplication)getApplication(), current_account, TRANS_TYPE_CASH,  Integer.valueOf(str));
					TransUtil transUtil = new TransUtil(getApplicationContext());
					transUtil.insertTrans(item);
					

					Toast.makeText(getApplicationContext(), TRANS_TYPE_CASH + str + "成功!",
							Toast.LENGTH_SHORT).show();
					editText.setText("");

					button.setClickable(true);
				}

				@Override
				public void onRequestFailed(String ex) {
					// TODO Auto-generated method stub
					button.setClickable(true);
				}
			});
			
		}
	}
}
