package com.exchange.student.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exchange.student.R;

public class GroupDiscussionsFragment extends Fragment {
	
	public GroupDiscussionsFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_community, container, false);
        
        /*Intent intent = new Intent(rootView.getContext(), ListViewLoader.class);
        startActivity(intent);*/         
         
        return rootView;
    }
}
