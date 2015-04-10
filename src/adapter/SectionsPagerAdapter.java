package adapter;

import jiaoyi.JiaoYiFragment;
import my.MyInfoFragment;
import wallet.WalletFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

	private Fragment[] fragments;

	public SectionsPagerAdapter(FragmentManager fm) {
		super(fm);
		if (fragments == null) {
			fragments = new Fragment[] { new JiaoYiFragment(), new WalletFragment(),
					new MyInfoFragment(), };
		}
	}

	@Override
	public Fragment getItem(int pos) {
		return fragments[pos];
	}

	@Override
	public int getCount() {
		return fragments.length;
	}

}
