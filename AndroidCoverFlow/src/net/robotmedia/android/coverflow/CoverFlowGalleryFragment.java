package net.robotmedia.android.coverflow;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;

public class CoverFlowGalleryFragment extends Fragment
{

	private CoverFlowGallery	                        mCoverFlow;
	private CoverFlowImageAdapter	                    mCoverFlowAdapter;
	private int	                                        mCoverFlowType;
	private View										mCurrentView;

	private CoverFlowGalleryFragmentControllerInterface	mControllerInterface;

	public static CoverFlowGalleryFragment getNewInstance(CoverFlowImageAdapter adapter, int coverFlowType)
	{
		CoverFlowGalleryFragment fragment = new CoverFlowGalleryFragment();
		fragment.setAdapter(adapter);
		fragment.setCoverFlowWidget(coverFlowType);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		try
		{
			mControllerInterface = (CoverFlowGalleryFragmentControllerInterface) activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString()
			        + " must implement CoverFlowInterface");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState)
	{
		FrameLayout contentView = new FrameLayout(getActivity());
		contentView.setId(1);
		contentView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		        LayoutParams.FILL_PARENT));

		mCoverFlow = CoverFlowFactory.getInstance().buildCoverFlowWidget(	getActivity(), mCoverFlowType);
		
		mCoverFlow.setAdapter(mCoverFlowAdapter);

		contentView.addView(mCoverFlow);

		mCoverFlow.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				// Call controller Activity to perform business logic
				mControllerInterface.onCoverFlowItemClick(parent, v, position, id);
			}
		});

		mCoverFlow.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View v, int position,
			        long id)
			{
			
				mControllerInterface.onCoverFlowItemSelected(parent, v, position, id);
			}

			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});

		return contentView;
	}

	private void setAdapter(CoverFlowImageAdapter coverFlowAdapter)
	{
		mCoverFlowAdapter = coverFlowAdapter;
	}

	private void setCoverFlowWidget(int coverflowType ) 
	{
		mCoverFlowType = coverflowType;
	}
	
	public String getCurrentImageId()
	{
		return "currentImage";
//		return mCurrentView.getBackground();
	}

}
