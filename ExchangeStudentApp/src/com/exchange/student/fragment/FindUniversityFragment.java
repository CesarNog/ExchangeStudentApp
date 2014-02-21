package com.exchange.student.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.exchange.student.R;
import com.exchange.student.bean.UniversityBean;
import com.exchange.student.database.DataSource;

public class FindUniversityFragment extends ListFragment {

	private DataSource datasource;

	private ImageView findAllUniversities;

	private StableArrayAdapter adapter;

	private View internalView;
	
	List<UniversityBean> listUniversities = null;

	public FindUniversityFragment() {
	}

	private class StableArrayAdapter extends ArrayAdapter<String> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		listUniversities = datasource.getAllUniversities();

		final ArrayList<String> list = new ArrayList<String>();
		if (listUniversities != null) {
			for (int i = 0; i < listUniversities.size(); ++i) {
				if (!listUniversities.get(i).getName().isEmpty()) {
					list.add(listUniversities.get(i).getName());
				}
			}
		}

		final ListView listview = (ListView) internalView
				.findViewById(android.R.id.list);

		adapter = new StableArrayAdapter(getActivity(),
				android.R.layout.simple_list_item_1, list);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				final String item = (String) parent.getItemAtPosition(position);
				view.animate().setDuration(1000).alpha(0)
						.withEndAction(new Runnable() {
							@Override
							public void run() {
								list.remove(item);
								adapter.notifyDataSetChanged();
								view.setAlpha(1);
							}
						});
			}

		});
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// do something with the data

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.list_universities, container,
				false);

		findAllUniversities = (ImageView) rootView
				.findViewById(R.id.key_find_all);

		/**
		 * Enabling Search Filter
		 */
		final EditText inputSearch = (EditText) rootView
				.findViewById(R.id.edit_text_search);

		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				adapter.getFilter().filter(cs);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});

		findAllUniversities.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.getFilter().filter(inputSearch.getText().toString());
				adapter.notifyDataSetChanged();
			};
		});

		internalView = rootView;

		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	// Will be called via the onClick attribute
	// of the buttons in list_universities.xml
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.key_find_all:

			EditText inputSearch = (EditText) view
					.findViewById(R.id.edit_text_search);
			adapter.getFilter().filter(inputSearch.getText().toString());

			break;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		datasource = new DataSource(activity);
	}
}
