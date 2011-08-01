package net.robotmedia.android.coverflow;

import android.view.View;
import android.widget.AdapterView;

public interface CoverFlowGalleryFragmentControllerInterface {

	void onCoverFlowItemClick(AdapterView<?> parent, View v, int position,
			long id);
	
	void onCoverFlowItemSelected(AdapterView<?> parent, View v, int position, long id);
}
