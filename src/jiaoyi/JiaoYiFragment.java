package jiaoyi;

import huankuan.HuanKuanActivity;
import shoukuan.ShouKuanActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yunhuirong.jpayapp.R;

public class JiaoYiFragment extends Fragment {
	Button huankuanBtn;
	Button shoukuanBtn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_jiaoyi, container,false);
		huankuanBtn = (Button) v.findViewById(R.id.btn_huankuan);
		shoukuanBtn = (Button) v.findViewById(R.id.btn_shoukuan);
		
		huankuanBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), HuanKuanActivity.class);
				startActivity(i);
			}
		});
		
		shoukuanBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), ShouKuanActivity.class);
				startActivity(i);
				
			}
		});
		
		return v;
		
		
		
	}
}
