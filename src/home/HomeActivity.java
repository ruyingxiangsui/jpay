package home;

import java.util.ArrayList;
import java.util.List;

import view.ChangeColorIconWithText;
import adapter.SectionsPagerAdapter;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.yunhuirong.jpayapp.R;

public class HomeActivity extends FragmentActivity implements OnClickListener {

	private ViewPager mViewPager;
	private SectionsPagerAdapter mPagerAdapter;
	private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();

	private ChangeColorIconWithText tabJiaoyi;
	private ChangeColorIconWithText tabWallet;
	private ChangeColorIconWithText tabMyInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		initTabs();
		initViewPager();
		checkNFC();
	}

	private void checkNFC() {
		NfcManager manager = (NfcManager) getSystemService(Context.NFC_SERVICE);
		NfcAdapter adapter = manager.getDefaultAdapter();
		if (adapter != null) {
			if (!adapter.isEnabled()) { // adapter存在，能启用
				Toast.makeText(this, "请在设置中启用NFC功能", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "您的手机不支持NFC功能", Toast.LENGTH_LONG).show();
		}
	}

	private void initTabs() {
		tabJiaoyi = (ChangeColorIconWithText) findViewById(R.id.indicator_jiaoyi);
		tabWallet = (ChangeColorIconWithText) findViewById(R.id.indicator_wallet);
		tabMyInfo = (ChangeColorIconWithText) findViewById(R.id.indicator_my);

		mTabIndicators.add(tabJiaoyi);
		mTabIndicators.add(tabWallet);
		mTabIndicators.add(tabMyInfo);

		tabJiaoyi.setOnClickListener(this);
		tabWallet.setOnClickListener(this);
		tabMyInfo.setOnClickListener(this);

		tabJiaoyi.setIconAlpha(1.0f);
	}

	private void initViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				if (positionOffset > 0) {
					ChangeColorIconWithText left = mTabIndicators.get(position);
					ChangeColorIconWithText right = mTabIndicators
							.get(position + 1);
					left.setIconAlpha(1 - positionOffset);
					right.setIconAlpha(positionOffset);
				}

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Override
	public void onClick(View v) {

		resetOtherTabs();

		switch (v.getId()) {
		case R.id.indicator_jiaoyi:
			mTabIndicators.get(0).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(0, false);
			break;
		case R.id.indicator_wallet:
			mTabIndicators.get(1).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(1, false);
			break;
		case R.id.indicator_my:
			mTabIndicators.get(2).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(2, false);
			break;

		}

	}

	/**
	 * 重置其他的TabIndicator的颜色
	 */
	private void resetOtherTabs() {
		for (int i = 0; i < mTabIndicators.size(); i++) {
			mTabIndicators.get(i).setIconAlpha(0);
		}
	}

}
