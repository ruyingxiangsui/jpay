package activity;

import util.SPUtil;
import util.TransUtil;
import adapter.LocalTranstionsAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.yunhuirong.jpayapp.R;

public class LocalTransactionsActivity extends Activity {

	private ListView listView;
	private Button button;
	LocalTranstionsAdapter adapter;
	TransUtil transUtil;
	String current_account;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local_transactions);
		
		current_account = SPUtil.getCurrentUserInfo(this).getUserName();
		
		listView = (ListView) findViewById(R.id.listview_local_transtions);
		transUtil = new TransUtil(this);
		adapter = new LocalTranstionsAdapter(transUtil.getTrans(current_account), this);
		
		listView.setAdapter(adapter);
		
		button = (Button) findViewById(R.id.trans_back);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LocalTransactionsActivity.this.finish();
			}
		});
	}

}
