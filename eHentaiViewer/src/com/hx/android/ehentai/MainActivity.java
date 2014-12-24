package com.hx.android.ehentai;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.hx.android.ehentai.adapter.ComicListAdapter;
import com.hx.android.ehentai.data.ComicManager;
import com.hx.android.ehentai.data.ComicManager.OnGetMoreComicEnd;
import com.hx.android.ehentai.model.Comic;
import com.hx.android.ehentai.widget.ImageQuatilyActionProvider;
import com.hx.android.ehentai.widget.ImageQuatilyActionProvider.OnQuatilyChangeListener;
import com.hx.android.ehentai.widget.ImageQuatilyActionProvider.OnQuatilyChangedListener;

import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity implements TabListener,
		OnQuatilyChangeListener, OnQuatilyChangedListener {

	private ViewPager mViewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ProgressDialog progressDialog;
	private ActionBar mActionBar;
	private MenuItem mQuatilyItem;
	private MenuItem mEditItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setOverflowShowingAlways();
		initViews();
	}

	private void initViews() {

		mActionBar = this.getSupportActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mActionBar.setTitle("Comic");
		mActionBar.setDisplayHomeAsUpEnabled(false);

		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		mViewPager = (ViewPager) this.findViewById(R.id.main_pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						mActionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			Tab tab = mActionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this);
			mActionBar.addTab(tab);
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private void setOverflowShowingAlways() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKeyField.setAccessible(true);
			menuKeyField.setBoolean(config, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		mQuatilyItem = menu.findItem(R.id.action_provider);
		mQuatilyItem.setVisible(false);
		mEditItem = menu.findItem(R.id.action_edit);
		ImageQuatilyActionProvider actionProvider = (ImageQuatilyActionProvider) MenuItemCompat
				.getActionProvider(mQuatilyItem);
		actionProvider.setOnQuatilyChangeListener(this);
		actionProvider.setOnQuatilyChangedListener(this);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:

			return false;
		case R.id.action_edit:
			LocalComicFragment fragment = (LocalComicFragment) mSectionsPagerAdapter.getCurrentFragment1();
			fragment.startEdit();
			break;
		case R.id.quit:

			break;
		default:
			break;
		}

		return true;
	}

	@Override
	public void change() {
		if (progressDialog == null)
			progressDialog = ProgressDialog.show(this, "Change", "Loading");
	}

	@Override
	public void changed(List<Comic> comics) {
		// TODO Auto-generated method stub
		mSectionsPagerAdapter.changeFragmentData(comics);
		progressDialog.dismiss();
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		if (mQuatilyItem == null)
			return;
		if (arg0.getPosition() == 1) {
			mQuatilyItem.setVisible(true);
			mEditItem.setVisible(false);
			OnlineComicFragment curr = (OnlineComicFragment) mSectionsPagerAdapter
					.getCurrentFragment();
			if (curr.isDataEmpty())
				curr.loadOnlineComic();
		} else {
			mQuatilyItem.setVisible(false);
			mEditItem.setVisible(true);
		}

		mViewPager.setCurrentItem(arg0.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		private IChangeData mIChangeData;
		private IChangeData mIChangeData1;

		public SectionsPagerAdapter(FragmentManager fm) {

			super(fm);
		}

		public void changeFragmentData(List<Comic> comics) {
			mIChangeData.changeData(comics);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			switch (position) {
			case 0:
				LocalComicFragment lf = new LocalComicFragment();
				mIChangeData1 = lf;
				return lf;
			case 1:
				OnlineComicFragment of = new OnlineComicFragment();
				mIChangeData = of;
				return of;
			default:
				return null;
			}
		}

		public Fragment getCurrentFragment() {
			return (Fragment) mIChangeData;
		}
		
		public Fragment getCurrentFragment1() {
			return (Fragment) mIChangeData1;
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();

			switch (position) {
			case 0:
				return getString(R.string.title_local).toUpperCase(l);
			case 1:
				return getString(R.string.title_online).toUpperCase(l);
			}
			return null;

		}
	}
}
