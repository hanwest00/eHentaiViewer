package com.hx.android.ehentai.adapter;

import java.text.DecimalFormat;
import java.util.List;

import com.hx.android.ehentai.R;
import com.hx.android.ehentai.data.ComicManager;
import com.hx.android.ehentai.data.ComicManager.OnDownloadComic;
import com.hx.android.ehentai.model.Comic;
import com.hx.android.ehentai.net.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ComicListAdapter extends BaseAdapter implements OnClickListener{
	private List<Comic> mData;
	private Context mContext;
	private int mCurrentPosition;

	public ComicListAdapter(List<Comic> comics, Context context) {
		mData = comics;
		mContext = context;
	}

	public void setData(List<Comic> data) {
		mData = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.comic_info, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.comic_cover);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.comic_title);
			viewHolder.downloadBtn = (Button) convertView
					.findViewById(R.id.comic_btn);
			
			convertView.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) convertView.getTag();
		
		Comic comic = mData.get(position);
		
		if(ComicManager.hasDownloaded(comic))
			viewHolder.downloadBtn.setVisibility(View.GONE);
		else
			viewHolder.downloadBtn.setVisibility(View.VISIBLE);
		
		mCurrentPosition = position;
		LayoutParams lp = viewHolder.imageView.getLayoutParams();
		viewHolder.imageView.setTag(comic.coverPath);
		ImageLoader.getInstance().ShowImageAsync(viewHolder.imageView, comic.coverPath,
				lp.width, lp.height);
		viewHolder.title.setText(comic.title);
		viewHolder.downloadBtn.setOnClickListener(this);
		return convertView;
	}

	public final class ViewHolder {
		public ImageView imageView;
		public TextView title;
		public Button downloadBtn;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		final Button obj = (Button) v;
		final Comic currComic = mData.get(mCurrentPosition);
		ComicManager comicMan = new ComicManager(mContext);
		
		obj.setText("Downloading...");
		comicMan.setOnDownloadComic(new OnDownloadComic(){
			private DecimalFormat mDecimalFormat = new DecimalFormat("#.00");
			private ComicManager mComicMan = new ComicManager(mContext);
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onProgress(int curr, int proc) {
				// TODO Auto-generated method stub
				obj.setText(mDecimalFormat.format(curr * 100 / proc));
			}

			@Override
			public void onEnd() {
				// TODO Auto-generated method stub
				obj.setText("");
				obj.setVisibility(View.GONE);
				mComicMan.addComicToDB(currComic);
			}
			
		});	
		
		comicMan.downloadComicImages(currComic);
	}
}
