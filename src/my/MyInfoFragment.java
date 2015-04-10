package my;

import util.SPUtil;
import activity.SettingPasswordActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunhuirong.jpayapp.R;

public class MyInfoFragment extends Fragment {
	
	private TextView username;
	private TextView settings;
	
	private TextView exit;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_my, container, false);
		username = (TextView)v.findViewById(R.id.tv_username);
		settings = (TextView)v.findViewById(R.id.tv_settings);
		
		exit = (TextView) v.findViewById(R.id.bt_exit);
		
		username.setText(SPUtil.getCurrentUserInfo(getActivity()).getUserName());
		settings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), SettingPasswordActivity.class);
				startActivity(i);
			}
		});
		
		
		exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SPUtil.logOut(getActivity());
				getActivity().finish();
			}
		});
		return v;
	}
	
}
