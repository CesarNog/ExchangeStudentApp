/**
 * @author cesarnog
 */
package com.exchange.student.activity;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.exchange.student.R;
import com.exchange.student.database.DataSource;
import com.exchange.student.fragment.FindUniversityFragment;
import com.exchange.student.fragment.GroupDiscussionsFragment;
import com.exchange.student.fragment.HomeFragment;
import com.exchange.student.fragment.UniversityNews;
import com.exchange.student.slidingmenu.adapter.NavDrawerListAdapter;
import com.exchange.student.slidingmenu.model.NavDrawerItem;
import com.facebook.Session;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.plus.PlusClient;

/**
 * Main activity of the Exchange Student app
 */
public class MainActivity extends FragmentActivity implements
		View.OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {
	private static int backCounter = 0;
	protected boolean calledFrom3Party;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ListView chatDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private Fragment fragment = null;

	private int atualMenuPosition = 0;

	private DataSource datasource;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;

	private String[] navChatTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private ArrayList<NavDrawerItem> navDrawerItemsChat;
	private NavDrawerListAdapter adapter;
	private NavDrawerListAdapter adapterChat;

	private PlusClient mPlusClient;
	
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	
	private final static String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mPlusClient = new PlusClient.Builder(this, this, this).setActions(
				"http://schemas.google.com/AddActivity",
				"http://schemas.google.com/BuyActivity").build();
		setContentView(R.layout.activity_main);

		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F7DB7E")));

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// load chat items
		navChatTitles = getResources().getStringArray(R.array.nav_drawer_chat);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		chatDrawerList = (ListView) findViewById(R.id.chat_menu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		navDrawerItemsChat = new ArrayList<NavDrawerItem>();

		navDrawerItemsChat.add(new NavDrawerItem(navChatTitles[0], navMenuIcons
				.getResourceId(2, -1)));

		navDrawerItemsChat.add(new NavDrawerItem(navChatTitles[1], navMenuIcons
				.getResourceId(2, -1)));

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		// Find Universities
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		// Group Discussions, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(3, -1)));
		// Universities news
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(5, -1), true, "50+"));

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		chatDrawerList.setOnItemClickListener(new SlideMenuClickListenerChat());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);

		// setting the nav drawer list adapter
		adapterChat = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItemsChat);

		mDrawerList.setAdapter(adapter);

		chatDrawerList.setAdapter(adapterChat);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}

		datasource = new DataSource(this);
		
		// Check device for Play Services APK.
	    if (checkPlayServices()) {
	        // If this check succeeds, proceed with normal processing.
	        // Otherwise, prompt user to get valid Play Services APK.
	        
	    }
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListenerChat implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			mDrawerLayout.closeDrawer(chatDrawerList);
			return true;
		} else {
			switch (item.getItemId()) {
			case R.id.action_search:
				mDrawerLayout.closeDrawer(mDrawerList);
				mDrawerLayout.closeDrawer(chatDrawerList);

				return true;
			case R.id.action_chat:
				mDrawerLayout.closeDrawer(mDrawerList);
				mDrawerLayout.openDrawer(chatDrawerList);
				return true;
			case R.id.action_about:
				mDrawerLayout.closeDrawer(mDrawerList);
				mDrawerLayout.closeDrawer(chatDrawerList);
				return true;
			case R.id.logoff:
				logoff();
				return true;

			default:
				return super.onOptionsItemSelected(item);
			}
		}
	}

	public void logoff() {
		// Google+
		if (mPlusClient.isConnected()) {
			mPlusClient.clearDefaultAccount();
			mPlusClient.disconnect();
			mPlusClient.connect();
		}
		// Facebook
		Session session = Session.getActiveSession();
		if (session != null) {
			if (!session.isClosed()) {
				session.closeAndClearTokenInformation();
				// clear your preferences if saved
			}
		} else {
			session = new Session(getApplicationContext());
			Session.setActiveSession(session);
			session.closeAndClearTokenInformation();
			// clear your preferences if saved
		}

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		intent = new Intent(getApplicationContext(), LoginActivity.class);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

		// if CHAT nav drawer is opened, hide the action items
		boolean drawerChatOpen = mDrawerLayout.isDrawerOpen(chatDrawerList);

		menu.findItem(R.id.action_chat).setVisible(!drawerOpen);
		menu.findItem(R.id.action_search).setVisible(!drawerOpen);

		menu.findItem(R.id.action_search).setVisible(!drawerChatOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	@SuppressWarnings("null")
	private void displayView(int position) {
		// update the main content by replacing fragments
		ListFragment listFragment = null;
		atualMenuPosition = position;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			listFragment = new FindUniversityFragment();
			break;
		case 2:
			fragment = new GroupDiscussionsFragment();
			break;
		case 3:
			fragment = new UniversityNews();
			break;
		default:
			break;
		}

		if (listFragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, listFragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
			mDrawerLayout.closeDrawer(chatDrawerList);
		} else if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity: ",
					"Error in creating fragment -> " + fragment.getId());
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {
		backCounter--;
		// max 3 backpressed, after exit the app
		if (backCounter < 0) {
			backCounter = 0;
			if (this.calledFrom3Party) {
				super.onBackPressed();
				return;
			}
			if (atualMenuPosition != 0) {
				displayView(0);
			} else {

				// custom dialog
				final Dialog dialog = new Dialog(this);
				dialog.setContentView(R.layout.logoff_dialog);
				dialog.setTitle("Do you want to sign out?");

				// set the custom dialog components - text, image and button
				TextView text = (TextView) dialog
						.findViewById(R.id.questionLogoff);
				text.setText("Do you really want to sign out?");

				Button dialogButtonYes = (Button) dialog
						.findViewById(R.id.dialogButtonYes);
				// if button is clicked, close the custom dialog
				dialogButtonYes.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						logoff();
					}
				});

				Button dialogButtonNo = (Button) dialog
						.findViewById(R.id.dialogButtonNo);
				// if button is clicked, close the custom dialog
				dialogButtonNo.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

				dialog.show();

			}

		} else {
			super.onBackPressed();
		}
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}

	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
		checkPlayServices();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onStart() {
		super.onStart();
		mPlusClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mPlusClient.disconnect();
	}
	
	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	            Log.i(TAG, "This device is not supported.");
	            finish();
	        }
	        return false;
	    }
	    return true;
	}

}
