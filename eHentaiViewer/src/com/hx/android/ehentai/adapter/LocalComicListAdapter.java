package com.hx.android.ehentai.adapter;

import java.util.List;

import com.hx.android.ehentai.R;
import com.hx.android.ehentai.adapter.ComicListAdapter.ViewHolder;
import com.hx.android.ehentai.model.Comic;
import com.hx.android.ehentai.net.ImageLoader;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LocalComicListAdapter extends BaseAdapter {
	private List<Comic> mData;
	private Context mContext;
	private int mCurrentPosition;

	public LocalComicListAdapter(List<Comic> comics, Context context) {
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
			convertView = View.inflate(mContext, R.layout.local_comic_info,
					null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.comic_cover);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.comic_title);

			convertView.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) convertView.getTag();

		Comic comic = mData.get(position);
		mCurrentPosition = position;
		LayoutParams lp = viewHolder.imageView.getLayoutParams();
		viewHolder.imageView.setTag(comic.coverPath);
		ImageLoader.getInstance().ShowImageAsync(viewHolder.imageView,
				comic.coverPath, lp.width, lp.height);
		viewHolder.title.setText(comic.title);
		return convertView;
	}

	public final class ViewHolder {
		public ImageView imageView;
		public TextView title;
	}

}
