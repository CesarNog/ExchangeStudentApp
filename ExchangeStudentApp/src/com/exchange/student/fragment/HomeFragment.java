package com.exchange.student.fragment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.exchange.student.R;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeFragment extends Fragment implements LocationListener {

	private GoogleMap mMap;

	// location manager
	private LocationManager locManager;

	// user marker
	private Marker userMarker;

	private Marker[] placeMarkers;

	// instance variables for Marker icon drawable resources
	private int userIcon;

	// university icon
	private int universityIcon;

	private final int MAX_PLACES = 10;
	private MarkerOptions[] places;
	
	private Boolean mapPositioned = false;

	public HomeFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// get drawable IDs
		userIcon = R.drawable.blue_point;
		universityIcon = R.drawable.university_marker;

		placeMarkers = new Marker[MAX_PLACES];

		View viewHome = inflater.inflate(R.layout.fragment_home, container,
				false);

		if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()
				.getApplicationContext()) == 0) {

			if (mMap == null) {
				try {
					mMap = ((MapFragment) getFragmentManager()
							.findFragmentById(R.id.map)).getMap();
				} catch (Exception e) {
					// TODO: handle exception
				}

				if (mMap != null) {
					// ok - proceed
					mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					
					// mMap.setMyLocationEnabled(true);
					mMap.getUiSettings().setMyLocationButtonEnabled(true);

					// update location
					updatePlaces();

				}
			}
		}

		return viewHome;
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

	@Override
	public void onDetach() {
		super.onDetach();
	}

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
					HttpResponse placesResponse = placesClient
							.execute(placesGet);
					// check response status
					StatusLine placeSearchStatus = placesResponse
							.getStatusLine();
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
		
		@Override
		protected void onPostExecute(String result) {
			// parse place data returned from Google Places
			// remove existing markers
			if (placeMarkers != null) {
				for (int pm = 0; pm < placeMarkers.length; pm++) {
					if (placeMarkers[pm] != null)
						placeMarkers[pm].remove();
				}
			}
			if (result != null) {
				try {
					// parse JSON
					// create JSONObject, pass string returned from doInBackground
					JSONObject resultObject = new JSONObject(result);
					// get "results" array
					JSONArray placesArray = resultObject.getJSONArray("results");
					// marker options for each place returned
					places = new MarkerOptions[placesArray.length()];
					// loop through places
					for (int p = 0; p < placesArray.length(); p++) {
						// parse each place
						// if any values are missing we won't show the marker
						boolean missingValue = false;
						LatLng placeLL = null;
						String placeName = "";
						String vicinity = "";
						int currIcon = userIcon;
						try {
							// attempt to retrieve place data values
							missingValue = false;
							// get place at this index
							JSONObject placeObject = placesArray.getJSONObject(p);
							// get location section
							JSONObject loc = placeObject.getJSONObject("geometry")
									.getJSONObject("location");
							// read lat lng
							placeLL = new LatLng(Double.valueOf(loc
									.getString("lat")), Double.valueOf(loc
									.getString("lng")));
							// get types
							JSONArray types = placeObject.getJSONArray("types");
							// loop through types
							for (int t = 0; t < types.length(); t++) {
								// what type is it
								String thisType = types.get(t).toString();
								// check for particular types - set icons
								if (thisType.contains("university")) {
									currIcon = universityIcon;
									break;
								}
							}
							// vicinity
							vicinity = placeObject.getString("vicinity");
							// name
							placeName = placeObject.getString("name");
						} catch (JSONException jse) {
							Log.v("PLACES", "missing value");
							missingValue = true;
							jse.printStackTrace();
						}
						// if values missing we don't display
						if (missingValue)
							places[p] = null;
						else
							places[p] = new MarkerOptions()
									.position(placeLL)
									.title(placeName)
									.icon(BitmapDescriptorFactory
											.fromResource(currIcon))
									.snippet(vicinity);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (places != null && placeMarkers != null) {
				for (int p = 0; p < places.length && p < placeMarkers.length; p++) {
					// will be null if a value was missing
					if (places[p] != null)
						placeMarkers[p] = mMap.addMarker(places[p]);

				}

			}
		}
	}

	private void updatePlaces() {
		// Location myLocation = mMap.getMyLocation();

		// get location manager
		locManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		// get last location
		Location lastLoc = locManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (lastLoc != null) {
			double lat = lastLoc.getLatitude();
			double lng = lastLoc.getLongitude();
			// create LatLng
			LatLng lastLatLng = new LatLng(lat, lng);

			// remove any existing marker
			if (userMarker != null)
				userMarker.remove();
			// create and set marker properties
			userMarker = mMap.addMarker(new MarkerOptions()
					.position(lastLatLng).title("You are here!")
					.icon(BitmapDescriptorFactory.fromResource(userIcon))
					.snippet("Your last recorded location"));

			if (mapPositioned == false) {
				// move to location
				mMap.animateCamera(
						CameraUpdateFactory.newLatLngZoom(lastLatLng, 14),
						2000, null);
				userMarker.showInfoWindow();
				mapPositioned = true;
			}		

			// build places - universities near query string
			String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/"
					+ "json?location="
					+ lat
					+ ","
					+ lng
					+ "&radius=50000"
					+ "&sensor=false"
					+ "&keyword=university"					 
					+ "&types=university"
					+ "&key=AIzaSyAHjSY3NWjUz7wmftj39lgZIh2wkbK35E4"; // ADD WEB
																		// API
																		// KEY

			// execute query
			new GetPlaces().execute(placesSearchStr);

			locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					50000, 100, this);
		} else {
			Toast.makeText(getActivity(),
					"Unable to fetch the current location", Toast.LENGTH_LONG)
					.show();
		}
	}


	@Override
	public void onLocationChanged(Location location) {
		Log.v("MainActivity", "location changed");
		//updatePlaces();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mMap != null) {
			locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					30000, 100, this);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mMap != null) {
			locManager.removeUpdates(this);
		}
	}

	@Override
	public void onProviderDisabled(String arg0) {
		Log.v("MainActivity", "provider disabled");
		Log.i("MainActivity", "provider disabled");
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.v("MainActivity", "provider enabled");
		Log.i("MainActivity", "provider disabled");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.v("MainActivity", "status changed");
	}

	@Override
	public void onDestroyView() {

		super.onDestroyView();

		try {

			MapFragment mapsFragment = (MapFragment) getFragmentManager()
					.findFragmentById(R.id.map);
			if (mapsFragment != null)
				getFragmentManager().beginTransaction().remove(mapsFragment)
						.commit();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
