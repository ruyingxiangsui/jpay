package shoukuan;

import java.util.Random;

import android.app.Activity;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yunhuirong.jpayapp.R;

import engine.JPayEngine;

public class ShouKuanActivity extends Activity implements CardService.AccountCallback,JPayEngine{
	
	private Button back;
	private TextView tvReceivedCount;
	
	Context ctx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoukuan);
		back = (Button) findViewById(R.id.shoukuan_back);
		ctx = getApplicationContext();
		tvReceivedCount = (TextView) findViewById(R.id.tv_received_count);
		
		CardService.setCallBack(ctx, this);
		
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ShouKuanActivity.this.finish();
			}
		});
	}

	@Override
	public void onReceived(final String count) {
		this.runOnUiThread(new Runnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				tvReceivedCount.setText(count);
				NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				Builder note = new Notification.Builder(ctx)
						.setContentTitle("收款成功!")
						.setContentText("收款" + count + "元")
						.setSmallIcon(R.drawable.ic_launcher)
						.setLights(Color.rgb(0, 255, 0), 5000, 5000)
						.setDefaults(Notification.DEFAULT_SOUND);
				// 第一个参数设置不同值时会显示多个notification
				nm.notify(new Random().nextInt(), note.getNotification());
			}
		});
	}
}
