package com.hx.android.ehentai.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.hx.android.ehentai.model.Comic;
import com.hx.android.ehentai.net.WebManager;
import com.hx.android.ehentai.util.FileManager;
import com.hx.android.ehentai.util.NetWorkHelper;
import com.hx.android.ehentai.util.Path;

public class ComicManager {

	private WebManager mWebManager;
	private OnGetMoreComicBegin mOnGetMoreComicBegin;
	private OnGetMoreComicEnd mOnGetMoreComicEnd;
	private OnLoadComicPageUrlBegin mOnLoadComicPageUrlBegin;
	private OnLoadComicPageUrlEnd mOnLoadComicPageUrlEnd;
	private OnDownloadComic mOnDownloadComic;
	private static SQLiteDatabase mDB;

	public ComicManager(Context context) {
		mWebManager = new WebManager(context);
		if (mDB == null) {
			DatabaseHelper dbHelper = new DatabaseHelper(context, 1);
			mDB = dbHelper.getWritableDatabase();
		}
		FileManager.createDir(Path.COMIC_DIR);
	}

	public void firstPageStart(boolean val) {
		if (val)
			mWebManager.setPage(0);
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

	public List<byte[]> loadLocalComicPages(Comic comic) {
		List<byte[]> ret = new ArrayList<byte[]>();
		File comicFile = new File(String.format("%s%s", Path.COMIC_DIR,
				comic.firstPageUrl.hashCode()));
		// File comicFile = new File(String.format("%s%s", Path.COMIC_DIR,
		// "1590384686"));
		if (comicFile.exists()) {
			byte[] temp = FileManager.readAllBytes(comicFile);
			if (temp != null) {
				int headerStart = 0;
				int currSplit = 0;
				for (int i = 0; i < temp.length; i++) {
					if (temp[i] == 58/* char ':' */) {
						currSplit = i;
						byte[] headerBytes = new byte[currSplit - headerStart];
						for (int j = 0; j < headerBytes.length; j++) {
							headerBytes[j] = temp[headerStart + j];
						}
						String header = new String(headerBytes);
						int length = Integer.parseInt(header);
						byte[] imgBytes = new byte[length];
						System.arraycopy(temp, currSplit + 1, imgBytes, 0,
								length);
						ret.add(imgBytes);
						headerStart = i = currSplit + 1 + length;
					}
				}
			}
		}
		return ret;
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

	public void setOnDownloadComic(OnDownloadComic onDownloadComic) {
		mOnDownloadComic = onDownloadComic;
	}

	public void addComicToDB(Comic comic) {
		if (isComicExists(comic))
			mDB.update("comic", comic.convertTo(), "firstPageUrl = ?",
					new String[] { comic.firstPageUrl });
		else
			mDB.insert("comic", null, comic.convertTo());
	}

	public void deleteComics(List<String> firstPageUrls) {

		if (firstPageUrls == null || firstPageUrls.size() == 0)
			return;
		StringBuilder sb = new StringBuilder();
		
		removeComicFile(firstPageUrls.get(0));
		sb.append("'");
		sb.append(firstPageUrls.get(0));
		sb.append("'");

		for (int i = 1; i < firstPageUrls.size(); i++) {
			sb.append(",'");
			sb.append(firstPageUrls.get(i));
			sb.append("'");
			
			removeComicFile(firstPageUrls.get(i));
		}

		mDB.execSQL(String.format(
				"delete from comic where firstPageUrl in (%s)", sb.toString()));
	}

	public boolean removeComicFile(String firstPageUrl) {
		File comicFile = new File(String.format("%s%s", Path.COMIC_DIR,
				firstPageUrl.hashCode()));
		if(!comicFile.exists()) return true;
		return comicFile.delete();
	}

	public static boolean isComicExists(Comic comic) {
		Cursor c = mDB.rawQuery(
				"select count(1) from comic where firstPageUrl = ?",
				new String[] { comic.firstPageUrl });
		return (c.moveToNext() && c.getInt(0) > 0);
	}

	public List<Comic> getLocalComic() {
		List<Comic> ret = new LinkedList<Comic>();
		Cursor c = mDB.rawQuery("select * from comic", null);
		while (c.moveToNext()) {
			try {
				ret.add(Comic.convertFrom(c));
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return ret;
	}

	public static boolean hasDownloaded(Comic comic) {
		File f = new File(String.format("%s%s", Path.COMIC_DIR,
				comic.firstPageUrl.hashCode()));
		return f.exists() && isComicExists(comic);
	}

	public void downloadComicImages(Comic comic) {
		new downloadComicImagesAsyncTask().execute(comic);
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
			mWebManager.getAllImageUrlAndCache(params[0].firstPageUrl, ret);
			return ret;
		}

		protected void onPostExecute(List<String> result) {
			mOnLoadComicPageUrlEnd.loadComicPageUrlEnd(result);
		}
	}

	public static interface OnDownloadComic {
		void onStart();

		void onProgress(int curr, int proc);

		void onEnd();
	}

	public class downloadComicImagesAsyncTask extends
			AsyncTask<Comic, Integer, Integer> {
		public final static int STATUS_COMPLETE = 0;
		public final static int STATUS_ERROR = 1;

		@Override
		protected Integer doInBackground(Comic... params) {
			if (mOnDownloadComic != null)
				mOnDownloadComic.onStart();
			List<String> imageUrls = new LinkedList<String>();
			mWebManager.getAllImageUrlAndCache(params[0].firstPageUrl,
					imageUrls);

			try {
				File f = new File(String.format("%s%s", Path.COMIC_DIR,
						params[0].firstPageUrl.hashCode()));

				f.createNewFile();

				FileOutputStream outputStream = new FileOutputStream(f);
				FileChannel fileChannel = outputStream.getChannel();

				int count = imageUrls.size();
				int i = 1;
				for (String url : imageUrls) {
					byte[] buffer = NetWorkHelper.httpRequestFile(url);
					if (buffer == null || buffer.length == 0)
						buffer = NetWorkHelper.httpRequestFile(url);

					String header = String.format("%s:", buffer.length);
					fileChannel.write(
							ByteBuffer.wrap(header.getBytes("utf-8")),
							f.length());
					fileChannel.write(ByteBuffer.wrap(buffer), f.length());
					publishProgress(i, count);
					i++;
				}

				fileChannel.close();
				outputStream.close();
				return STATUS_COMPLETE;
			} catch (Exception e) {
				e.printStackTrace();
				return STATUS_ERROR;
			}
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			if (mOnDownloadComic != null)
				mOnDownloadComic.onProgress(progress[0], progress[1]);
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (mOnDownloadComic != null)
				mOnDownloadComic.onEnd();
		}
	}
}
