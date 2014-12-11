package com.hx.android.ehentai.net;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hx.android.ehentai.util.FileManager;
import com.hx.android.ehentai.util.NetWorkHelper;
import com.hx.android.ehentai.util.Path;

import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageLoader {

	private boolean stop;
	private Map<ImageView, String> loadList;

	private static ImageLoader instance;

	public static ImageLoader getInstance() {
		if (instance == null)
			instance = new ImageLoader();
		return instance;
	}

	private ImageLoader() {
		FileManager.createDir(Path.LAZY_LOAD_PATH);
		FileManager.createDir(Path.CACHE_PATH);
		stop = false;
		loadList = new LinkedHashMap<ImageView, String>();
	}

	public void clearCache() {
		FileManager.clearDir(Path.LAZY_LOAD_PATH);
	}

	public void ShowImage(ImageView imageView, String imgUrl) {
		File f = new File(Path.LAZY_LOAD_PATH
				+ String.valueOf(imgUrl.hashCode()));
		NetWorkHelper.httpRequestFile(imgUrl, f);
		imageView.setImageBitmap(FileManager.convertToBitmap(f));
	}

	public void ShowImageAsync(ImageView imageView, String imgUrl) {
		File f = new File(Path.LAZY_LOAD_PATH
				+ String.valueOf(imgUrl.hashCode()));
		if (!f.exists())
			new AsyncLoadImage(imageView, imgUrl, f).execute();
		else
			imageView.setImageBitmap(FileManager.convertToBitmap(f));
	}

	private class AsyncLoadImage extends AsyncTask<Void, Integer, Void> {

		private ImageView mImageView;
		private String mImgUrl;
		private File mFile;

		public AsyncLoadImage(ImageView imageView, String imgUrl, File file) {
			mImageView = imageView;
			mImgUrl = imgUrl;
			mFile = file;
		}

		@Override
		protected Void doInBackground(Void... params) {
			if(mFile == null)
				
			NetWorkHelper.httpRequestFile(mImgUrl, mFile);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mImageView.setImageBitmap(FileManager.convertToBitmap(mFile));
		}

	}
}
