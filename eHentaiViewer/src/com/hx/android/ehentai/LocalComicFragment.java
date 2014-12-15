package com.hx.android.ehentai;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hx.android.ehentai.R;
import com.hx.android.ehentai.model.Comic;

public class LocalComicFragment extends Fragment implements IChangeData {
	private View mRootView;
	private PullToRefreshListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.local_comic_fragment, container, false);
		mListView = (PullToRefreshListView) mRootView
				.findViewById(R.id.local_comic_list);
		mListView.hideTopIndicatorArrowShow();
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
		// TODO Auto-generated method stub

	}
}
