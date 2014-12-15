package com.hx.android.ehentai.widget;

import java.util.List;

import com.hx.android.ehentai.R;
import com.hx.android.ehentai.model.Comic;
import com.hx.android.ehentai.net.WebManager;
import com.hx.android.ehentai.net.WebManager.ImageQuality;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;

public class ImageQuatilyActionProvider extends ActionProvider implements
		OnMenuItemClickListener {

	private OnQuatilyChangedListener mOnQuatilyChangedListener;
	private OnQuatilyChangeListener mOnQuatilyChangeListener;
	private Context mContext;
	
	public ImageQuatilyActionProvider(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	
	public void setOnQuatilyChangedListener(OnQuatilyChangedListener onQuatilyChangedListener)
	{
		mOnQuatilyChangedListener = onQuatilyChangedListener;
	}
	
	public void setOnQuatilyChangeListener(OnQuatilyChangeListener onQuatilyChangeListener)
	{
		mOnQuatilyChangeListener = onQuatilyChangeListener;
	}

	@Override
	public View onCreateActionView() {
		return null;
	}

	@Override
	public void onPrepareSubMenu(SubMenu subMenu) {
		super.onPrepareSubMenu(subMenu);
		subMenu.clear();
		subMenu.add("420X").setIcon(R.drawable.ic_launcher).setOnMenuItemClickListener(this);
		subMenu.add("780X").setIcon(R.drawable.ic_launcher).setOnMenuItemClickListener(this);
		subMenu.add("980X").setIcon(R.drawable.ic_launcher).setOnMenuItemClickListener(this);
	}

	@Override
	public boolean hasSubMenu() {
		return true;
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		ImageQuality iq;
		
		if(mOnQuatilyChangeListener != null)
			mOnQuatilyChangeListener.change();
		
		if(item.getTitle().equals("780X"))
			iq = ImageQuality.X780;
		else if(item.getTitle().equals("780X"))
			iq = ImageQuality.X980;
		else
			iq = ImageQuality.X420;
		
		new ChangeQuatilyAsyncTask().execute(iq);
		return true;
	}
	
	private class ChangeQuatilyAsyncTask extends AsyncTask<ImageQuality, Integer, List<Comic>>{

		@Override
		protected List<Comic> doInBackground(ImageQuality... params) {
			WebManager webMan = new WebManager(mContext);
			webMan.setImageQuality(params[0]);
			webMan.setPage(0);
			return webMan.loadMoreComic();
		}
		
		 protected void onPostExecute(List<Comic> result) {
	         if(mOnQuatilyChangedListener != null)
	        	 mOnQuatilyChangedListener.changed(result);
	     }

	}
	
	public interface OnQuatilyChangedListener {
		void changed(List<Comic> comics);
	}
	
	public interface OnQuatilyChangeListener {
		void change();
	}
}
