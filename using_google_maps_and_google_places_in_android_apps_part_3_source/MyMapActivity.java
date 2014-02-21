package your.package.name;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;

/*
 * MyMapActivity class forms part of Map application
 * in Mobiletuts+ tutorial series:
 * Using Google Maps and Google Places in Android Apps
 * 
 * This version of the class is for Part 3 of the series.
 * 
 * Sue Smith
 * March/April 2013
 */
public class MyMapActivity extends Activity {

	//instance variables for Marker icon drawable resources
	private int userIcon, foodIcon, drinkIcon, shopIcon, otherIcon;

	//the map
	private GoogleMap theMap;

	//location manager
	private LocationManager locMan;

	//user marker
	private Marker userMarker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_map);

		//get drawable IDs
		userIcon = R.drawable.yellow_point;
		foodIcon = R.drawable.red_point;
		drinkIcon = R.drawable.blue_point;
		shopIcon = R.drawable.green_point;
		otherIcon = R.drawable.purple_point;

		//find out if we already have it
		if(theMap==null){
			//get the map
			theMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.the_map)).getMap();
			//check in case map/ Google Play services not available
			if(theMap!=null){
				//ok - proceed
				theMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				//update location
				updatePlaces();

			}

		}
	}

	private void updatePlaces(){
		//get location manager
		locMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		//get last location
		Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		double lat = lastLoc.getLatitude();
		double lng = lastLoc.getLongitude();
		//create LatLng
		LatLng lastLatLng = new LatLng(lat, lng);

		//remove any existing marker
		if(userMarker!=null) userMarker.remove();
		//create and set marker properties
		userMarker = theMap.addMarker(new MarkerOptions()
		.position(lastLatLng)
		.title("You are here")
		.icon(BitmapDescriptorFactory.fromResource(userIcon))
		.snippet("Your last recorded location"));
		//move to location
		theMap.animateCamera(CameraUpdateFactory.newLatLng(lastLatLng), 3000, null);
		
		//build places query string
		String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
				"json?location="+lat+","+lng+
				"&radius=1000&sensor=true" +
				"&types=food|bar|store|museum|art_gallery"+
				"&key=your_key_here";//ADD KEY
	}
	
	private class GetPlaces extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... placesURL) {
			//fetch places
			
			//build result as string
			StringBuilder placesBuilder = new StringBuilder();
			//process search parameter string(s)
			for (String placeSearchURL : placesURL) {
				HttpClient placesClient = new DefaultHttpClient();
				try {
					//try to fetch the data
					
					//HTTP Get receives URL string
					HttpGet placesGet = new HttpGet(placeSearchURL);
					//execute GET with Client - return response
					HttpResponse placesResponse = placesClient.execute(placesGet);
					//check response status
					StatusLine placeSearchStatus = placesResponse.getStatusLine();
					//only carry on if response is OK
					if (placeSearchStatus.getStatusCode() == 200) {
						//get response entity
						HttpEntity placesEntity = placesResponse.getEntity();
						//get input stream setup
						InputStream placesContent = placesEntity.getContent();
						//create reader
						InputStreamReader placesInput = new InputStreamReader(placesContent);
						//use buffered reader to process
						BufferedReader placesReader = new BufferedReader(placesInput);
						//read a line at a time, append to string builder
						String lineIn;
						while ((lineIn = placesReader.readLine()) != null) {
							placesBuilder.append(lineIn);
						}
					}
				}
				catch(Exception e){ 
					e.printStackTrace(); 
				}
			}
			return placesBuilder.toString();
		}
	}

}
