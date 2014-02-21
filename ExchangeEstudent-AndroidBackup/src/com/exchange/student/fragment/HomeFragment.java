package com.exchange.student.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exchange.student.R;
import com.exchange.student.activity.ListViewLoader;

public class HomeFragment extends Fragment {

	private OnItemSelectedListener listener;

	public HomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View listView = getActivity().findViewById(R.id.list_of_carriers);
		
		  // Create a new TextView and set its text to the fragment's section
        // number argument value.
        /*View V = inflater.inflate(R.layout.main, container, false);

        ListView LV = (ListView) this.getActivity().findViewById(R.id.mainList);
        String List[] = new String[2];
        List[0] = "tst1";
        List[1] ="tst2";
        ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_2, List);

        LV.setAdapter(aa);

        return V;*/

		 return inflater.inflate(R.layout.fragment_home, container, false);
	}

	public void setText(String item) {
		TextView view = (TextView) getView().findViewById(R.id.txtLabel);
		view.setText(item);
	}

	public interface OnItemSelectedListener {
	}

	// May also be triggered from the Activity
	public void updateDetail() {
		// create a string, just for testing
		String newTime = String.valueOf(System.currentTimeMillis());

		// Inform the Activity about the change based
		// interface defintion
		// listener.onRssItemSelected(newTime);
	}

}
