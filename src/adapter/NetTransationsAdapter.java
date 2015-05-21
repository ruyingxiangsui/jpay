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
import entity.Payment;

public class NetTransationsAdapter  extends BaseAdapter implements JPayEngine{

	ArrayList<Payment> data;
	LayoutInflater inflater;

	public NetTransationsAdapter(ArrayList<Payment> data, Context ctx) {
		this.data = data;
		if (this.data == null) {
			this.data = new ArrayList<Payment>();
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
		String str;
		if(data.get(position).amount>0){
			str = TRANS_TYPE_RECHARGE;
		}else{
			str = TRANS_TYPE_CASH;
		}
		holder.type.setText(str);
		holder.time.setText(data.get(position).date);

		if (str.equals(TRANS_TYPE_CASH) || str.equals(TRANS_TYPE_HUANKUAN)) {
			holder.count.setTextColor(Color.RED);
			holder.count.setText("-" + data.get(position).amount);
		} else {
			holder.count.setTextColor(Color.BLACK);
			holder.count.setText("+" + data.get(position).amount);
		}
		return view;
	}

	private class ViewHolder {
		public TextView type;
		public TextView time;
		public TextView count;
	}

}
