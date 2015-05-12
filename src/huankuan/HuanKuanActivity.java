package huankuan;

import java.util.Random;

import util.SPUtil;
import android.app.Activity;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import application.JPayApplication;

import com.yunhuirong.jpayapp.R;

import engine.JPayEngine;
import entity.Command;

/**
 * 模拟卡充当收款对象
 * 
 * 
 * */
public class HuanKuanActivity extends Activity implements
		CardService.AccountCallback, JPayEngine {

	public static final String TAG = "Card";

	private EditText etAccount;
	private Button back;
	private Button confirm;
	private String current_account;
	JPayApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_huankuan);
		app = (JPayApplication)getApplication();
		current_account = SPUtil.getCurrentUserInfo(app).getUserName();
		back = (Button) findViewById(R.id.huankuan_back);
		etAccount = (EditText) findViewById(R.id.et_huankuan_count);
		etAccount.setHint("余额 "+SPUtil.getAccountOverage(app, current_account)+"元");
		confirm = (Button) findViewById(R.id.confirm_huankuan);
		int count = SPUtil.getHuanCommand(app).getTransCount();
		String state = SPUtil.getHuanCommand(app).getTransState();
		if ((count > 0) && (!state.equals(STATE_DONE))) {
			// 有未结束的订单
			etAccount.setText(count + "");
			etAccount.setEnabled(false);
			confirm.setEnabled(false);
		} else {
			// 清除当前还款交易记录
			SPUtil.resetHuanCommand(app);
		}
		// 初始化卡
		CardService.setCallBack(app, this);

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				HuanKuanActivity.this.finish();
			}
		});

		confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (etAccount.getText().toString() == null
						|| etAccount.getText().toString().trim().equals("")
						|| etAccount.getText().toString().trim().equals("0")) {
					Toast.makeText(app, "请输入金额", Toast.LENGTH_SHORT).show();
					return;
				}else if(Integer.parseInt(etAccount.getText().toString())>MAX_HUANKUAN_COUNT){
					Toast.makeText(app, "单笔交易额最大为500￥", Toast.LENGTH_SHORT).show();
					return;
				}
				else if (Integer.parseInt(etAccount.getText().toString()) > SPUtil
						.getAccountOverage(app, current_account)) {
					Toast.makeText(app, "余额不足", Toast.LENGTH_SHORT).show();
					return;
				}
				etAccount.setEnabled(false);
				Command cmd = SPUtil.getHuanCommand(app);

				cmd.setTransCode("");
				cmd.setHuanKuanCard(SPUtil.getCurrentUserInfo(app)
						.getUserName());
				;
				cmd.setTransState(STATE_INIT);
				cmd.setTransCount(Integer.parseInt(etAccount.getText()
						.toString().trim()));
				confirm.setEnabled(false);
				// 生成当前的交易记录
				SPUtil.setHuanCommand(app, cmd);

				// 金额在收款方来收取时减少
				/*
				 * SPUtil.updateAccountOverage(ctx,
				 * SPUtil.getCurrentUserInfo(ctx) .getUserName(),
				 * TRANS_TYPE_HUANKUAN, cmd .getTransCount());
				 */

			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onHuankuanReceived(final String count) {
		this.runOnUiThread(new Runnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				// 通知(对方已收到还款)
				// 清除当前用户当前交易记录
				NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				Builder note = new Notification.Builder(app)
						.setContentTitle("还款成功!")
						.setContentText("还款" + count + "元")
						.setSmallIcon(R.drawable.ic_launcher)
						.setLights(Color.rgb(0, 255, 0), 5000, 5000)
						.setDefaults(Notification.DEFAULT_SOUND);
				// 第一个参数设置不同值时会显示多个notification
				nm.notify(new Random().nextInt(), note.getNotification());
				SPUtil.resetHuanCommand(app);
				etAccount.setEnabled(true);
				etAccount.setText("");
				confirm.setEnabled(true);
				
				etAccount.setHint("余额 "+SPUtil.getAccountOverage(app, current_account)+"元");
			}
		});
	}
}
