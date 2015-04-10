package huankuan;

import java.util.UUID;

import util.SPUtil;
import android.app.Activity;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yunhuirong.jpayapp.R;

import engine.JPayEngine;
import entity.Command;

public class HuanKuanActivity extends Activity implements
		LoyaltyCardReader.AccountCallback, JPayEngine {

	public static final String TAG = "CardReader";
	public static int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A
			| NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
	public LoyaltyCardReader mLoyaltyCardReader;

	private EditText etAccount;
	private Button back;
	private Button confirm;

	Context ctx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_huankuan);
		ctx = getApplicationContext();
		back = (Button) findViewById(R.id.huankuan_back);
		etAccount = (EditText) findViewById(R.id.et_huankuan_count);
		confirm = (Button) findViewById(R.id.confirm_huankuan);
		mLoyaltyCardReader = new LoyaltyCardReader(HuanKuanActivity.this, this);
		int count = SPUtil.getCommand(ctx).getTransCount();
		String state = SPUtil.getCommand(ctx).getTransState();
		if((count>0)&& (!state.equals(STATE_DONE))){
			etAccount.setText(count+"");
			etAccount.setEnabled(false);
		}
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				disableReaderMode();
				HuanKuanActivity.this.finish();
			}
		});

		confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (etAccount.getText().toString() == null
						|| etAccount.getText().toString().trim().equals("")) {
					Toast.makeText(ctx, "请输入金额", Toast.LENGTH_SHORT).show();
					return;
				}
				etAccount.setEnabled(false);
				Command cmd = new Command();
				cmd = SPUtil.getCommand(ctx);
				if (cmd.getTransCode().equals("")) {
					cmd.setTransCode(UUID.randomUUID() + "");
					cmd.setHuanKuanCard(SPUtil.getCurrentUserInfo(
							ctx).getUserName());
					;
					cmd.setTransCount(Integer.parseInt(etAccount.getText()
							.toString().trim()));
					SPUtil.setCommand(ctx, cmd);
				}
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

	@Override
	public void onSended(String account) {
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				etAccount.setText("0");
				etAccount.setEnabled(true);
			}
		});
	}
}
