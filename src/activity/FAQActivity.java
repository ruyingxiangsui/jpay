package activity;

import java.util.ArrayList;

import util.DataUtil;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yunhuirong.jpayapp.R;
public class FAQActivity extends Activity {

	private ListView listView;
	private Button back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_faq);

		listView = (ListView) findViewById(R.id.listview_faqs);
		FAQSAdapter adapter = new FAQSAdapter(DataUtil.getFAQS(), this);
		listView.setAdapter(adapter);
		
		
		back = (Button) findViewById(R.id.faqs_back);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FAQActivity.this.finish();
			}
		});
		
	}

	class FAQSAdapter extends BaseAdapter {

		ArrayList<String> data;
		LayoutInflater inflater;

		public FAQSAdapter(ArrayList<String> data, Context ctx) {
			this.data = data;
			inflater = LayoutInflater.from(ctx);
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = inflater.inflate(R.layout.item_faqs, parent, false);
				holder = new ViewHolder();
				holder.content = (TextView) view
						.findViewById(R.id.faqs_item_content);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.content.setText(data.get(position));
			return view;
		}

		class ViewHolder {
			TextView title;
			TextView content;
		}

	}

}
