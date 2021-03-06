package adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunhuirong.jpayapp.R;

import engine.JPayEngine;
import entity.TranstionItem;

public class LocalTranstionsAdapter extends BaseAdapter implements JPayEngine{

	ArrayList<TranstionItem> data;
	LayoutInflater inflater;

	public LocalTranstionsAdapter(ArrayList<TranstionItem> data, Context ctx) {
		this.data = data;
		if (this.data == null) {
			this.data = new ArrayList<TranstionItem>();
		}
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
			view = inflater.inflate(R.layout.item_transtion, parent, false);
			holder = new ViewHolder();
			holder.type = (TextView) view
					.findViewById(R.id.transtion_item_type);
			holder.time = (TextView) view
					.findViewById(R.id.transtion_item_time);
			holder.count = (TextView) view
					.findViewById(R.id.transtion_item_count);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		String str = data.get(position).getTransType();
		holder.type.setText(str);
		holder.time.setText(data.get(position).getTransTime());

		if (str.equals(TRANS_TYPE_CASH) || str.equals(TRANS_TYPE_HUANKUAN)) {
			holder.count.setTextColor(Color.RED);
			holder.count.setText("-" + data.get(position).getTransCount());
		} else {
			holder.count.setTextColor(Color.BLACK);
			holder.count.setText("+" + data.get(position).getTransCount());
		}
		return view;
	}

	private class ViewHolder {
		public TextView type;
		public TextView time;
		public TextView count;
	}

}
