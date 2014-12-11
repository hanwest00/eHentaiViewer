package com.hx.android.ehentai;

import java.util.List;
import com.hx.android.ehentai.data.ComicManager;
import com.hx.android.ehentai.data.ComicManager.OnLoadComicPageUrlBegin;
import com.hx.android.ehentai.data.ComicManager.OnLoadComicPageUrlEnd;
import com.hx.android.ehentai.model.Comic;
import com.hx.android.ehentai.net.ImageLoader;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class ViewerActivity extends Activity {

	private ScrollView mScrollView;
	private LinearLayout mLinearLayout;
	private Comic mComic;
	private int mCurrLoad;
	private List<String> mImageUrls;
	private LinearLayout.LayoutParams imageLayoutParams;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewer);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			this.getActionBar().hide();
		mComic = (Comic) this.getIntent().getSerializableExtra("comic");
		mCurrLoad = 0;
		initView();
	}

	private void initView() {
		mScrollView = (ScrollView) this.findViewById(R.id.viewr_main_scroll);
		mLinearLayout = (LinearLayout) this.findViewById(R.id.viewr_main);

		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		imageLayoutParams = new LinearLayout.LayoutParams(dm.widthPixels,
				dm.heightPixels);
		final ProgressDialog dialog = new ProgressDialog(ViewerActivity.this);
		dialog.setTitle("Load Pages");
		dialog.setMessage("Loading");
		ComicManager comicMan = new ComicManager(this);
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

		mScrollView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_UP:
					if (v.getMeasuredHeight() <= v.getScrollY() + v.getHeight()) {
						// 滚动到底部
						showNextImage();
					}
					break;

				default:
					break;
				}
				return false;
			}

		});

		comicMan.beginLoadComicPages(mComic);
	}

	private void showNextImage() {
		if (mImageUrls.size() <= mCurrLoad)
			return;
		String url = mImageUrls.get(mCurrLoad);
		ImageView imageView = new ImageView(this);
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setLayoutParams(imageLayoutParams);
		this.mLinearLayout.addView(imageView);
		ImageLoader.getInstance().ShowImageAsync(imageView, url);
		mCurrLoad++;
	}
}
