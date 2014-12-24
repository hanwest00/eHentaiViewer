package com.hx.android.ehentai;

import java.util.List;

import com.hx.android.ehentai.data.ComicManager;
import com.hx.android.ehentai.data.ComicManager.OnLoadComicPageUrlBegin;
import com.hx.android.ehentai.data.ComicManager.OnLoadComicPageUrlEnd;
import com.hx.android.ehentai.model.Comic;
import com.hx.android.ehentai.net.ImageLoader;
import com.hx.android.ehentai.util.FileManager;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class ViewerActivity extends ActionBarActivity {

	private ScrollView mScrollView;
	private LinearLayout mLinearLayout;
	private Comic mComic;
	private int mCurrLoad;
	private List<String> mImageUrls;
	private List<byte[]> mImages;
	private LinearLayout.LayoutParams imageLayoutParams;
	private DisplayMetrics screenDM;
	private boolean isLocal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewer);
		this.getSupportActionBar().hide();
		mComic = (Comic) this.getIntent().getSerializableExtra("comic");
		mCurrLoad = 0;
		isLocal = false;
		initView();
	}

	private void initView() {
		mScrollView = (ScrollView) this.findViewById(R.id.viewr_main_scroll);
		mScrollView.setVerticalScrollBarEnabled(false);
		mLinearLayout = (LinearLayout) this.findViewById(R.id.viewr_main);

		screenDM = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(screenDM);
		imageLayoutParams = new LinearLayout.LayoutParams(screenDM.widthPixels,
				screenDM.heightPixels);
		final ProgressDialog dialog = new ProgressDialog(ViewerActivity.this);
		dialog.setTitle("Load Pages");
		dialog.setMessage("Loading");
		
		mScrollView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_UP:
					if (v.getMeasuredHeight() <= v.getScrollY() + v.getHeight()) {
						// 滚动到底部
						if (isLocal)
							showNextLocalImage();
						else
							showNextImage();
					}
					break;

				default:
					break;
				}
				return false;
			}

		});
		
		
		ComicManager comicMan = new ComicManager(this);
		mImages = comicMan.loadLocalComicPages(mComic);
		if (mImages.size() > 0) {
			isLocal = true;
			showNextLocalImage();
			return;
		}

		comicMan.setOnLoadComicPageUrlBegin(new OnLoadComicPageUrlBegin() {

			@Override
			public void loadComicPageUrlBegin() {
				dialog.show();
			}

		});

		comicMan.setOnLoadComicPageUrlEnd(new OnLoadComicPageUrlEnd() {

			@Override
			public void loadComicPageUrlEnd(List<String> imageUrls) {
				mImageUrls = imageUrls;
				showNextImage();
				dialog.dismiss();
			}

		});

		comicMan.beginLoadComicPages(mComic);
	}

	private void showNextImage() {
		if (mImageUrls.size() <= mCurrLoad)
			return;
		String url = mImageUrls.get(mCurrLoad);
		ImageView imageView = new ImageView(this);
		// imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setLayoutParams(imageLayoutParams);
		this.mLinearLayout.addView(imageView);
		imageView.setTag(url);
		ImageLoader.getInstance().ShowImageAsync(imageView, url,
				screenDM.widthPixels, screenDM.heightPixels);
		mCurrLoad++;
	}

	private void showNextLocalImage() {
		if (mImages.size() <= mCurrLoad)
			return;
		ImageView imageView = new ImageView(this);
		// imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setLayoutParams(imageLayoutParams);
		this.mLinearLayout.addView(imageView);

		imageView.setImageBitmap(FileManager.convertToBitmap(
				mImages.get(mCurrLoad), screenDM.widthPixels,
				screenDM.heightPixels));
		mCurrLoad++;
	}
}
