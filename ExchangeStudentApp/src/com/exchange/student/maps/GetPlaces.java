package com.exchange.student.maps;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class GetPlaces extends AsyncTask<String, Void, String> {
	
	@Override
	protected String doInBackground(String... placesURL) {
		// fetch places

		// build result as string
		StringBuilder placesBuilder = new StringBuilder();
		// process search parameter string(s)
		for (String placeSearchURL : placesURL) {
			HttpClient placesClient = new DefaultHttpClient();
			try {
				// try to fetch the data

				// HTTP Get receives URL string
				HttpGet placesGet = new HttpGet(placeSearchURL);
				// execute GET with Client - return response
				HttpResponse placesResponse = placesClient.execute(placesGet);
				// check response status
				StatusLine placeSearchStatus = placesResponse.getStatusLine();
				// only carry on if response is OK
				if (placeSearchStatus.getStatusCode() == 200) {
					// get response entity
					HttpEntity placesEntity = placesResponse.getEntity();
					// get input stream setup
					InputStream placesContent = placesEntity.getContent();
					// create reader
					InputStreamReader placesInput = new InputStreamReader(
							placesContent);
					// use buffered reader to process
					BufferedReader placesReader = new BufferedReader(
							placesInput);
					// read a line at a time, append to string builder
					String lineIn;
					while ((lineIn = placesReader.readLine()) != null) {
						placesBuilder.append(lineIn);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return placesBuilder.toString();
	}
}