package com.exchange.student.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.exchange.student.R;
import com.exchange.student.bean.UserBean;
import com.exchange.student.database.UniversityDataSource;

public class FindUniversityFragment extends ListFragment {

	private UniversityDataSource datasource;

	private ImageView findAllUniversities;

	private ListView listView;

	private ArrayAdapter<String> adapter;
	
	public FindUniversityFragment() {
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		datasource = new UniversityDataSource(getActivity().getApplicationContext());
				
		String[] values = new String [] {"Unifei", "UMKC", "University of Utah", "California State University"};
		
		/*List<UniversityBean> universities = datasource.getAllUniversities();
		
		if (universities != null) {
			int count = 0;
			for (UniversityBean universityBean : universities) {
				values[count] = universityBean.getName().toString();
				count++;
			}		
		}*/			
		
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, values);
		setListAdapter(adapter);	

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

		//listView = (ListView) rootView.findViewById();
		
		/**
         * Enabling Search Filter
         * */
		final EditText inputSearch = (EditText) rootView.findViewById(R.id.edit_text_search);
		
		inputSearch.addTextChangedListener(new TextWatcher() {
             
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                adapter.getFilter().filter(cs);   
            }
             
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
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
			};
		});

		return rootView;
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Log.w(Environment.getDataDirectory().toString(),
		// "SAVING DATABASE AT: ");
		// System.out.println(Environment.getDataDirectory().toString());
		// System.out.println(MySQLiteHelper.isSDCardWriteable() ?
		// "SD CARD READABLE" : "NO SDCARD READABLE");		
	}

	// Will be called via the onClick attribute
	// of the buttons in list_universities.xml
	public void onClick(View view) {
		// EditText searchUniversitiesField = (EditText)
		// findViewById(R.id.username);

		@SuppressWarnings("unchecked")
		UserBean user = null;
		switch (view.getId()) {
		case R.id.key_find_all:

			break;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		datasource = new UniversityDataSource(activity);
	}

	/**
	 * Find all the universities
	 */
	protected void findUniversities() {
		// verificar pra não buscar se não tiver contatos
	}

}
