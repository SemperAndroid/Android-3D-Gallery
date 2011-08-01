package net.robotmedia.android.coverflow;

import android.content.Context;

public class CoverFlowFactory
{
	public static final int	        COVER_FLOW_TYPE_HORIZONTAL	= 0;
	public static final int	        COVER_FLOW_TYPE_CIRCULAR	= 1;

	private static CoverFlowFactory	mInstance;

	public static CoverFlowFactory getInstance()
	{
		if (mInstance == null)
			mInstance = new CoverFlowFactory();
		return mInstance;
	}

	private CoverFlowFactory()
	{
	}

	public CoverFlowGalleryFragment getCoverFlowGalleryFragment(Context aContext,
	        int coverFlowType, CoverFlowImageAdapter adapter)
	{
		return CoverFlowGalleryFragment.getNewInstance(adapter, coverFlowType);
	}

	public CoverFlowGalleryFragment getCoverFlowGalleryFragment(Context aContext,
	        CoverFlowImageAdapter adapter)
	{
		return getCoverFlowGalleryFragment(aContext, COVER_FLOW_TYPE_HORIZONTAL, adapter);
	}

	public CoverFlowGalleryFragment getCoverFlowGalleryFragment(Context aContext)
	{
		ReflectionImageAdapter adapter = new ReflectionImageAdapter(aContext);
		return getCoverFlowGalleryFragment(aContext, COVER_FLOW_TYPE_HORIZONTAL, adapter);
	}

	public CoverFlowGallery buildCoverFlowWidget(Context aContext, int coverFlowType)
	{
		CoverFlowGallery coverFlow;
		switch (coverFlowType)
		{
			case COVER_FLOW_TYPE_HORIZONTAL:
				coverFlow = new HorizontalCoverFlowGallery(aContext);
				break;
			case COVER_FLOW_TYPE_CIRCULAR:
				coverFlow = new CircularCoverFlowGallery(aContext);
				break;

			// TODO: Add your cover flow implementation types here

			default:
				coverFlow = new HorizontalCoverFlowGallery(aContext);
				break;
		}
		return coverFlow;
	}
}
