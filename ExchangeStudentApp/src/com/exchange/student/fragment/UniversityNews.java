package com.exchange.student.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exchange.student.R;
import com.exchange.student.activity.ListViewLoader;

public class UniversityNews extends Fragment {
	
	public UniversityNews(){}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_universities_news, container, false);
               
        return rootView;
    }
}
