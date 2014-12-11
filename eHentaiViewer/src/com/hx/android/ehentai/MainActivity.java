package com.hx.android.ehentai;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.hx.android.ehentai.adapter.ComicListAdapter;
import com.hx.android.ehentai.data.ComicManager;
import com.hx.android.ehentai.data.ComicManager.OnGetMoreComicEnd;
import com.hx.android.ehentai.model.Comic;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends Activity {
	private ListView comicListView;
	private ComicListAdapter mAdapter;
	private Button moreBtn;
	private List<Comic> mCommics;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setOverflowShowingAlways();

		initViews();
	}

	private void initViews() {
		moreBtn = (Button) this.findViewById(R.id.more);
		comicListView = (ListView) this.findViewById(R.id.main_list);
		final ComicManager comicMan = new ComicManager(this);
		comicMan.beginGetMoreComic();
		final ProgressDialog dialog = ProgressDialog.show(this, "Hentai",
				"Loading");
		comicMan.setOnGetMoreComicEnd(new OnGetMoreComicEnd() {

			@Override
			public void getMoreComicEnd(List<Comic> commics) {
				// TODO Auto-generated method stub
				if (mCommics == null)
					mCommics = new ArrayList<Comic>();
				mCommics.addAll(commics);
				if (mAdapter == null) {
					mAdapter = new ComicListAdapter(mCommics, MainActivity.this);
					comicListView.setAdapter(mAdapter);
				}else{
					mAdapter.notifyDataSetChanged();
				}
				
				dialog.dismiss();
			}
		});

		moreBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				comicMan.beginGetMoreComic();
				dialog.show();
			}

		});
		
		comicListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, ViewerActivity.class);
				intent.putExtra("comic", mCommics.get(position));
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		ActionBar actionBar = this.getActionBar();
		actionBar.setTitle("Comic List");
		actionBar.setDisplayHomeAsUpEnabled(false);
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
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:

			return false;
		case R.id.setting:

			break;
		case R.id.quit:

			break;
		default:
			break;
		}

		return true;
	}

}
