package com.hx.android.ehentai;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hx.android.ehentai.R;
import com.hx.android.ehentai.adapter.LocalComicListAdapter;
import com.hx.android.ehentai.data.ComicManager;
import com.hx.android.ehentai.model.Comic;

public class LocalComicFragment extends Fragment implements IChangeData,
		OnItemClickListener, PullToRefreshBase.OnRefreshListener2<ListView>,
		OnClickListener {
	private View mRootView;
	private Button mDelBtn;
	private PullToRefreshListView mListView;
	private LocalComicListAdapter mAdapter;
	private ComicManager mComicMan;
	private List<Comic> mComics;
	private List<String> mDelList;
	private boolean isEditMode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mComicMan = new ComicManager(this.getActivity());
		isEditMode = false;
		mDelList = new ArrayList<String>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.local_comic_fragment, container,
				false);

		mDelBtn = (Button) mRootView.findViewById(R.id.del_btn);
		mDelBtn.setOnClickListener(this);

		mListView = (PullToRefreshListView) mRootView
				.findViewById(R.id.local_comic_list);
		mListView.hideTopIndicatorArrowShow();

		mComics = mComicMan.getLocalComic();
		mAdapter = new LocalComicListAdapter(mComics, this.getActivity());
		mListView.setMode(Mode.PULL_FROM_START);
		mListView.setAdapter(mAdapter);

		mListView.setOnRefreshListener(this);

		mListView.setOnItemClickListener(this);

		return mRootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void changeData(List<Comic> comics) {
		mComics = comics;
		mAdapter.notifyDataSetChanged();
	}

	class loadComicAsyncTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			mComics = mComicMan.getLocalComic();
			return null;
		}

		@Override
		protected void onPostExecute(Void params) {
			mAdapter.setData(mComics);
			mAdapter.notifyDataSetChanged();
			mListView.onRefreshComplete();
		}

	}

	public void startEdit() {
		mDelBtn.setVisibility(View.VISIBLE);
		isEditMode = true;
	}

	public void quitEdit() {
		for (int i = 0; i < mListView.getChildCount();i++){
			mListView.getChildAt(i).setBackgroundColor(Color.argb(0, 255, 255, 255));
		}
		mDelList.clear();
		mDelBtn.setVisibility(View.GONE);
		isEditMode = false;
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Comic comic = mComics.get(position - 1);
		if (isEditMode) {
			if (mDelList.contains(comic.firstPageUrl)) {
				view.setBackgroundColor(Color.argb(0, 255, 255, 255));
				mDelList.remove(comic.firstPageUrl);
			} else {
				view.setBackgroundColor(Color.argb(127, 0, 0, 0));
				mDelList.add(comic.firstPageUrl);
			}
			mDelBtn.setText(String.valueOf(mDelList.size()));
			return;
		}
		Intent intent = new Intent(getActivity(), ViewerActivity.class);
		intent.putExtra("comic", comic);
		startActivity(intent);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// if(mListView.isRefreshing()) return;
		if (isEditMode)
			return;
		new loadComicAsyncTask().execute();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

	}

	@Override
	public void onClick(View v) {
		mComicMan.deleteComics(mDelList);
		mComics = mComicMan.getLocalComic();
		mAdapter.setData(mComics);
		quitEdit();
	}
	
	public boolean handleKeyBack(){
		if(isEditMode){
			quitEdit();
			return true;
		}
		return false;
	}
}
