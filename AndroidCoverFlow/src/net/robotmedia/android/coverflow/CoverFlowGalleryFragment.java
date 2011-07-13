package net.robotmedia.android.coverflow;

import net.robotmedia.android.coverflow.CoverFlowGallery.ImageAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CoverFlowGalleryFragment extends Fragment  {

	private CoverFlowGallery mCoverFlow;
	private ImageAdapter mCoverFlowAdapter;
	private int mCoverFlowStrategyId;
	
//	private ArrayList<ViewGroup> mCoverFlowItemsInfoViews;
	
	private ListView mInfoListView;
	private ArrayAdapter<ViewGroup> mInfoViewsAdapter;
	
	
	private TextView mCoverText;

	
	private FragmentControllerInterface mListener;
	 
	
	public static CoverFlowGalleryFragment getNewInstance(ImageAdapter adapter, int coverflowStrategyId, ArrayAdapter<ViewGroup> infoViews )
    {
		CoverFlowGalleryFragment fragment = new CoverFlowGalleryFragment();
		fragment.setAdapter(adapter);
		fragment.setStrategy(coverflowStrategyId);
		fragment.setInfoViews(infoViews);
        return fragment;
    }
	
	private void setInfoViews(ArrayAdapter<ViewGroup> infoViews )
	{
		mInfoViewsAdapter = infoViews;
	}
	
	private void setAdapter(ImageAdapter coverFlowAdapter)
	{
		mCoverFlowAdapter = coverFlowAdapter;	
	}
	
	public void setStrategy(int strategyId)
	{
		if(mCoverFlow == null)
			mCoverFlowStrategyId = strategyId;
		else
			mCoverFlow.setStrategy(strategyId);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
        try {
            mListener = (FragmentControllerInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement CoverFlowInterface");
        }
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.coverflow_fragment_lyt, container, false);
		
		mCoverFlow = (CoverFlowGallery) contentView.findViewById(R.id.cover_flow);
		mCoverFlow.setAdapter(mCoverFlowAdapter);
		mCoverFlow.setStrategy(mCoverFlowStrategyId);
		
		mCoverText = (TextView) contentView.findViewById(R.id.cover_text);
		
		mInfoListView = (ListView) contentView.findViewById(R.id.info_list);
		mInfoListView.setAdapter(mInfoViewsAdapter);
		
		
		// TODO: Move to XML
		mCoverFlow.setSpacing(-50); //getResources().getDimension(R.dimen.coverflow_spacing)));
		mCoverFlow.setAnimationDuration(800);
		mCoverFlow.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				// Perform here local stuff
				
				// Call controller Activity to perform business logic
				mListener.onItemClick(parent, v, position, id);
			}
		});
		
		mCoverFlow.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) 
			{
				if(mInfoViewsAdapter.getCount() > 0)
				{
					LinearLayout infoItem = (LinearLayout)mInfoViewsAdapter.getItem(position);
					TextView tv = (TextView)infoItem.findViewById(R.id.info);
					
					tv.setText("Page "+(position));
					mInfoListView.bringChildToFront(infoItem);
				}
				
				mCoverText.setText("Page "+(position));
				
				mListener.onItemSelected(parent, v, position, id);
			}
			
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
		return contentView;
	}
	
}
