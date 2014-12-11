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

		if (convertView == null)
			convertView = View.inflate(mContext, R.layout.comic_info, null);
		// TODO Auto-generated method stub
		ImageView imageView = (ImageView) convertView.findViewById(R.id.comic_cover);
		TextView textVuew = (TextView) convertView.findViewById(R.id.comic_title);
		Comic comic = mData.get(position);
		ImageLoader.getInstance().ShowImageAsync(imageView, comic.coverPath);
		textVuew.setText(comic.title);
		return convertView;
	}

}
