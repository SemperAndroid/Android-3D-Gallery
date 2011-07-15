package net.robotmedia.android.coverflow;

import android.content.Context;

public class CoverFlowFactory
{
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
	
	public CoverFlowGalleryFragment getCoverFlowGalleryFragment(Context aContext, CoverFlowGallery coverFlowWidget, CoverFlowImageAdapter adapter)
	{		
		return CoverFlowGalleryFragment.getNewInstance(adapter, coverFlowWidget);		
	}
	
	public CoverFlowGalleryFragment getCoverFlowGalleryFragment(Context aContext, CoverFlowImageAdapter adapter)
	{		
		HorizontalCoverFlowGallery coverFlow = new HorizontalCoverFlowGallery(aContext);
		return getCoverFlowGalleryFragment(aContext, coverFlow, adapter);
	}

	public CoverFlowGalleryFragment getCoverFlowGalleryFragment(Context aContext)
	{
		ReflectionImageAdapter adapter = new ReflectionImageAdapter(aContext);
		HorizontalCoverFlowGallery coverFlow = new HorizontalCoverFlowGallery(aContext);
		return getCoverFlowGalleryFragment(aContext, coverFlow, adapter); 
	}
}
