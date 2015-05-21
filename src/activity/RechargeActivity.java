package activity;

import http.JPayRequestListener;
import http.JpayApi;
import http.response.RechargeResp;

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
public class RechargeActivity extends Activity implements JPayEngine {

	private EditText editText;
	private Button rechargingBtn;
	private Button back;

	private String current_account;
	private JPayApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recharge);
		app = (JPayApplication) getApplication();
		current_account = SPUtil.getCurrentUserInfo(
				(JPayApplication) getApplication()).getUserName();

		editText = (EditText) findViewById(R.id.recharge_count_edittext);
		rechargingBtn = (Button) findViewById(R.id.recharging_btn);
		rechargingBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startRecharging();
			}
		});

		back = (Button) findViewById(R.id.recharge_back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(
						RechargeActivity.this.getCurrentFocus()
								.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				RechargeActivity.this.finish();
			}
		});

	}

	private void startRecharging() {
		final String str = editText.getText().toString();
		if (str == null || str.equals("")) {
			Toast.makeText(this, "请输入充值金额!", Toast.LENGTH_SHORT).show();
		} else {
			rechargingBtn.setClickable(false);
			JpayApi.recharge(SPUtil.getCurrentUserInfo(app).getAccessToken(),
					Integer.parseInt(str), "",
					new JPayRequestListener<RechargeResp>() {

						@Override
						public void onRequestSucceeded(RechargeResp data) {
							// 更新本地余额,插入交易记录
							SPUtil.updateAccountOverage(
									(JPayApplication) getApplication(),
									current_account, TRANS_TYPE_RECHARGE,
									Integer.parseInt(str));

							TransUtil transUtil = new TransUtil(
									getApplicationContext());
							SimpleDateFormat sDateFormat = new SimpleDateFormat(
									"yyyy-MM-dd   hh:mm:ss");
							String time = sDateFormat
									.format(new java.util.Date());
							TranstionItem item = new TranstionItem();
							item.setTransType(TRANS_TYPE_RECHARGE);
							item.setTransTime(time);
							item.setTransCount(str);
							item.setTransMyCard(current_account);
							transUtil.insertTrans(item);

							Toast.makeText(getApplicationContext(), "充值成功!",
									Toast.LENGTH_SHORT).show();
							editText.setText("");
							rechargingBtn.setClickable(true);
						}

						@Override
						public void onRequestFailed(String ex) {
							Toast.makeText(getApplicationContext(),
									"充值失败!" + ex, Toast.LENGTH_SHORT).show();
							rechargingBtn.setClickable(true);
						}
					});

		}
	}

}
