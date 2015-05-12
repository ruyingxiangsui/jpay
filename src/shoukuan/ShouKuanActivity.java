package shoukuan;

import java.util.Random;

import util.SPUtil;
import util.StringUtil;
import android.app.Activity;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import application.JPayApplication;

import com.yunhuirong.jpayapp.R;

import engine.JPayEngine;
import entity.Command;

public class ShouKuanActivity extends Activity implements
		LoyaltyCardReader.AccountCallback, JPayEngine {

	public static int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A
			| NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;

	private Button back;
	private TextView tvReceivedCount;
	public LoyaltyCardReader mLoyaltyCardReader;
	private Button confirm;
	JPayApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoukuan);
		
		
		back = (Button) findViewById(R.id.shoukuan_back);
		app = (JPayApplication)getApplication();
		tvReceivedCount = (TextView) findViewById(R.id.tv_received_count);
		mLoyaltyCardReader = new LoyaltyCardReader(app, this);
		confirm = (Button) findViewById(R.id.bt_shoukuan_confirm);
		
		Command cmd = SPUtil.getShouCommand(app);
		// 如果当前交易指令为空或者执行完毕,则新建交易码
		if (cmd.getTransCode() == null || cmd.getTransCode().equals("")
				|| cmd.getTransState().equals(STATE_DONE)) {
			Command tmp = new Command();
			tmp.setTransCode(StringUtil.getUuidString());
			tmp.setShouKuanCard(SPUtil.getCurrentUserInfo(app).getUserName());
			tmp.setTransState(STATE_INIT);
			SPUtil.setShouCommand(app, tmp);
			tvReceivedCount.setText("new");
		}else{
			tvReceivedCount.setText("init...");
		}

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ShouKuanActivity.this.finish();
			}
		});
		
		
		confirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Command tmp = new Command();
				tmp.setTransCode(StringUtil.getUuidString());
				tmp.setShouKuanCard(SPUtil.getCurrentUserInfo(app).getUserName());
				tmp.setTransState(STATE_INIT);
				SPUtil.setShouCommand(app, tmp);
				tvReceivedCount.setText("new");
			}
		});
	}

	
	
	@Override
	public void onSuccessed(final String count) {
		this.runOnUiThread(new Runnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				tvReceivedCount.setText(count);
				// 通知用户收款成功
				NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				Builder note = new Notification.Builder(app)
						.setContentTitle("收款成功!")
						.setContentText("收款" + count + "元")
						.setSmallIcon(R.drawable.ic_launcher)
						.setLights(Color.rgb(0, 255, 0), 5000, 5000)
						.setDefaults(Notification.DEFAULT_SOUND);
				// 第一个参数设置不同值时会显示多个notification
				nm.notify(new Random().nextInt(), note.getNotification());
				
				/**/
				
				Command tmp = new Command();
				tmp.setTransCode(StringUtil.getUuidString());
				tmp.setShouKuanCard(SPUtil.getCurrentUserInfo(app).getUserName());
				tmp.setTransState(STATE_INIT);
				SPUtil.setShouCommand(app, tmp);
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		disableReaderMode();
	}

	@Override
	public void onResume() {
		super.onResume();
		enableReaderMode();
	}

	private void enableReaderMode() {
		NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
		if (nfc != null) {
			nfc.enableReaderMode(this, mLoyaltyCardReader, READER_FLAGS, null);
		}
	}

	private void disableReaderMode() {
		NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
		if (nfc != null) {
			nfc.disableReaderMode(this);
		}
	}

}
