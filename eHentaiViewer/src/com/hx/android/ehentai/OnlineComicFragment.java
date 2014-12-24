package com.hx.android.ehentai;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.hx.android.ehentai.R;
import com.hx.android.ehentai.adapter.ComicListAdapter;
import com.hx.android.ehentai.data.ComicManager;
import com.hx.android.ehentai.data.ComicManager.OnGetMoreComicEnd;
import com.hx.android.ehentai.model.Comic;

public class OnlineComicFragment extends Fragment implements IChangeData {
	private View mRootView;
	private PullToRefreshListView mListView;
	private List<Comic> mComics;
	private ComicListAdapter mAdapter;
	private ComicManager mComicMan;
	private ProgressDialog mDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.online_comic_fragment, container,
				false);
		mListView = (PullToRefreshListView) mRootView
				.findViewById(R.id.online_comic_list);
		
		mComicMan = new ComicManager(this.getActivity());
		
		mDialog = new ProgressDialog(this.getActivity());
		mDialog.setTitle("Hentai");
		mDialog.setMessage("Loading");

		mComicMan.setOnGetMoreComicEnd(new OnGetMoreComicEnd() {

			@Override
			public void getMoreComicEnd(List<Comic> commics) {
				// TODO Auto-generated method stub
				if (mComics == null)
					mComics = new ArrayList<Comic>();
				mComics.addAll(commics);
				if (mAdapter == null) {
					mAdapter = new ComicListAdapter(mComics, getActivity());
					mListView.setAdapter(mAdapter);
				} else {
					mAdapter.notifyDataSetChanged();
				}
				
				mListView.onRefreshComplete();
				mDialog.dismiss();
			}
		});

		mListView.setMode(Mode.BOTH);
		mListView.hideTopIndicatorArrowShow();
		mListView.hideBottomIndicatorArrowShow();
		mListView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase refreshView) {
						mComicMan.firstPageStart(true);
						mComics.clear();
						mComicMan.beginGetMoreComic();
						mDialog.show();
					}

					@Override
					public void onPullUpToRefresh(PullToRefreshBase refreshView) {
						mComicMan.beginGetMoreComic();
						mDialog.show();
					}

				});

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), ViewerActivity.class);
				intent.putExtra("comic", mComics.get(position - 1));
				startActivity(intent);
			}
		});

		return mRootView;
	}
	
	public boolean isDataEmpty()
	{
		return mComics == null || mComics.size() == 0;
	}

	public void loadOnlineComic() {
		mDialog.show();
		mComicMan.beginGetMoreComic();
	}

	@Override
	public void changeData(List<Comic> comics) {
		mComics = comics;
		mAdapter.notifyDataSetChanged();
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
}
