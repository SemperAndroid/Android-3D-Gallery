package net.robotmedia.android.coverflow;

import net.robotmedia.utils.DataCache;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public abstract class CoverFlowImageAdapter extends ArrayAdapter<BitmapDrawable>
{
	protected static final int	            CACHE_SIZE	= 30;

	protected Context	                    mContext;
	protected DataCache<Integer, ImageView>	mDataCache;

	public CoverFlowImageAdapter(Context context)
	{
		super(context, 0x0);
		mContext = context;
		mDataCache = new DataCache<Integer, ImageView>(CACHE_SIZE);
	}

	public CoverFlowImageAdapter(Context context, int cacheSize)
	{
		super(context, 0x0);
		mContext = context;
		mDataCache = new DataCache<Integer, ImageView>(cacheSize);
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		return getImage(position);
	}

	protected ImageView getImage(int position)
	{
		ImageView i = mDataCache.get(Integer.valueOf(position));
		if (i != null)
		{
			return i;
		}
		i = createImage(position);
		mDataCache.put(Integer.valueOf(position), i);
		return i;
	}

	protected abstract ImageView createImage(int position);

	protected abstract int getThumbnailWidth();

	protected abstract int getThumbnailHeight();
}
