package com.hx.android.ehentai.data;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

import com.hx.android.ehentai.model.Comic;
import com.hx.android.ehentai.net.WebManager;

public class ComicManager {

	private WebManager mWebManager;
	private OnGetMoreComicBegin mOnGetMoreComicBegin;
	private OnGetMoreComicEnd mOnGetMoreComicEnd;
	private OnLoadComicPageUrlBegin mOnLoadComicPageUrlBegin;
	private OnLoadComicPageUrlEnd mOnLoadComicPageUrlEnd;

	public ComicManager(Context context) {
		mWebManager = new WebManager(context);
	}
	
	public void firstPageStart(boolean val){
		if(val) mWebManager.setPage(0);
	}

	public void beginGetMoreComic() {
		LoadComicAsyncTask asyncTask = new LoadComicAsyncTask();
		if (mOnGetMoreComicBegin != null)
			mOnGetMoreComicBegin.getMoreComicBegin();
		asyncTask.execute();
	}

	public void setOnGetMoreComicBegin(OnGetMoreComicBegin onGetMoreComicBegin) {
		mOnGetMoreComicBegin = onGetMoreComicBegin;
	}

	public void setOnGetMoreComicEnd(OnGetMoreComicEnd onGetMoreComicEnd) {
		mOnGetMoreComicEnd = onGetMoreComicEnd;
	}

	public void beginLoadComicPages(Comic comic) {
		mOnLoadComicPageUrlBegin.loadComicPageUrlBegin();
		new LoadComicPageUrlAsyncTask().execute(comic);
	}

	public void setOnLoadComicPageUrlBegin(
			OnLoadComicPageUrlBegin onLoadComicPageUrlBegin) {
		mOnLoadComicPageUrlBegin = onLoadComicPageUrlBegin;
	}

	public void setOnLoadComicPageUrlEnd(
			OnLoadComicPageUrlEnd onLoadComicPageUrlEnd) {
		mOnLoadComicPageUrlEnd = onLoadComicPageUrlEnd;
	}

	public static interface OnGetMoreComicBegin {
		void getMoreComicBegin();
	}

	public static interface OnGetMoreComicEnd {
		void getMoreComicEnd(List<Comic> commics);
	}

	private class LoadComicAsyncTask extends
			AsyncTask<Void, Integer, List<Comic>> {

		@Override
		protected List<Comic> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return mWebManager.loadMoreComic();
		}

		@Override
		protected void onPostExecute(List<Comic> params) {
			if (mOnGetMoreComicEnd != null)
				mOnGetMoreComicEnd.getMoreComicEnd(params);
		}
	}

	public static interface OnLoadComicPageUrlBegin {
		void loadComicPageUrlBegin();
	}

	public static interface OnLoadComicPageUrlEnd {
		void loadComicPageUrlEnd(List<String> imageUrls);
	}

	private class LoadComicPageUrlAsyncTask extends
			AsyncTask<Comic, Integer, List<String>> {

		@Override
		protected List<String> doInBackground(Comic... params) {
			// TODO Auto-generated method stub
			List<String> ret = new LinkedList<String>();
			mWebManager.getAllImageUrl(params[0].firstPageUrl, ret);
			return ret;
		}

		protected void onPostExecute(List<String> result) {
			mOnLoadComicPageUrlEnd.loadComicPageUrlEnd(result);
		}
	}
}
