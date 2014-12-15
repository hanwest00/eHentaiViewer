package com.hx.android.ehentai.adapter;

import java.util.List;

import com.hx.android.ehentai.R;
import com.hx.android.ehentai.model.Comic;
import com.hx.android.ehentai.net.ImageLoader;
import com.hx.android.ehentai.util.FileManager;
import com.hx.android.ehentai.util.Path;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ComicListAdapter extends BaseAdapter {
	private List<Comic> mData;
	private Context mContext;
	private ImageLoader lazyLoad;

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
			convertView.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) convertView.getTag();
		
		Comic comic = mData.get(position);
		LayoutParams lp = viewHolder.imageView.getLayoutParams();
		ImageLoader.getInstance().ShowImageAsync(viewHolder.imageView, comic.coverPath,
				lp.width, lp.height);
		viewHolder.title.setText(comic.title);
		return convertView;
	}

	public final class ViewHolder {
		public ImageView imageView;
		public TextView title;
	}
}
