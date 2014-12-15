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

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

	public void ShowImage(ImageView imageView, String imgUrl, int w, int h) {
		File f = new File(Path.LAZY_LOAD_PATH
				+ String.valueOf(imgUrl.hashCode()));
		NetWorkHelper.httpRequestFile(imgUrl, f);
		imageView.setImageBitmap(FileManager.convertToBitmap(
				f.getAbsolutePath(), w, h));
	}

	public void ShowImageAsync(ImageView imageView, String imgUrl, int w, int h) {
		File f = new File(Path.LAZY_LOAD_PATH
				+ String.valueOf(imgUrl.hashCode()));
		if (!f.exists())
			new AsyncLoadImage(imageView, imgUrl, f, w, h).execute();
		else
			setImageAndAutoSize(imageView, FileManager.convertToBitmap(f.getPath(),
					w, h));
	}
	
	private void setImageAndAutoSize(ImageView imageView, Bitmap src)
	{
		/*if(src == null) return;
		LayoutParams lp = imageView.getLayoutParams();
		lp.height = src.getHeight();
		lp.width = src.getWidth();
		imageView.setLayoutParams(lp);*/
		imageView.setImageBitmap(src);
	}

	private class AsyncLoadImage extends AsyncTask<Void, Integer, Void> {

		private ImageView mImageView;
		private String mImgUrl;
		private File mFile;
		private int mWidth;
		private int mHeight;

		public AsyncLoadImage(ImageView imageView, String imgUrl, File file,
				int w, int h) {
			mImageView = imageView;
			mImgUrl = imgUrl;
			mFile = file;
			mWidth = w;
			mHeight = h;
		}

		@Override
		protected Void doInBackground(Void... params) {
			if(!NetWorkHelper.httpRequestFile(mImgUrl, mFile))
				NetWorkHelper.httpRequestFile(mImgUrl, mFile);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			setImageAndAutoSize(mImageView, FileManager.convertToBitmap(
					mFile.getPath(), mWidth, mHeight));
		}

	}
}
