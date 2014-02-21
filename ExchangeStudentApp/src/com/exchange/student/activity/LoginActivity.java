package com.exchange.student.activity;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.exchange.student.R;
import com.exchange.student.bean.UserBean;
import com.exchange.student.database.DataSource;
import com.exchange.student.utils.PreferencesUtils;
import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;

@SuppressWarnings("deprecation")
public class LoginActivity extends Activity implements View.OnClickListener,
		ConnectionCallbacks, OnConnectionFailedListener {
	private DataSource datasource;

	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
	private static final String TAG = LoginActivity.class.getSimpleName();;
	@SuppressWarnings("unused")
	private static final int REQUEST_CODE_RESOLVE_MISSING_GP = 9001;

	private ProgressDialog mConnectionProgressDialog;
	private PlusClient mPlusClient;
	private ConnectionResult mConnectionResult;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mPlusClient = new PlusClient.Builder(this, this, this).setActions(
				"http://schemas.google.com/AddActivity",
				"http://schemas.google.com/BuyActivity").build();

		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"com.exchange.student", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash:",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.login);

		// Spinner progress bar to be displayed if the user clicks the Google+
		// sign-in button before resolving connection errors.
		mConnectionProgressDialog = new ProgressDialog(this);
		mConnectionProgressDialog
				.setMessage(getString(R.string.signing_in_status));

		findViewById(R.id.sign_in_button).setOnClickListener(this);

		// checkIfDisableLoginFields();

		datasource = new DataSource(this);

		facebookLoginAuthButtonListen();

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

	public void checkIfDisableLoginFields() {
		if (PreferencesUtils.isUserAuthenticated(getApplicationContext())) {
			EditText username = (EditText) findViewById(R.id.username);
			EditText password = (EditText) findViewById(R.id.password);
			Button signinBtn = (Button) findViewById(R.id.signin);
			username.setActivated(false);
			username.setFocusable(false);
			password.setActivated(false);
			password.setFocusable(false);
			signinBtn.setActivated(false);
			signinBtn.setClickable(false);
		}
	}

	/**
	 * Listen for action at the facebook button
	 */
	public void facebookLoginAuthButtonListen() {

		LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
		authButton.setOnErrorListener(new OnErrorListener() {

			@Override
			public void onError(FacebookException error) {
				Log.i("LoginActivity", "Facebook error: " + error.getMessage());
			}
		});
		// set permission list, Don't forget to add email
		authButton.setReadPermissions(Arrays.asList("basic_info", "email"));
		// session state call back event
		authButton.setSessionStatusCallback(new Session.StatusCallback() {

			@Override
			public void call(Session session, SessionState state,
					Exception exception) {

				if (session.isOpened()) {
					Log.d("Access facebook Token: ", session.getAccessToken());
					Request.executeMeRequestAsync(session,
							new Request.GraphUserCallback() {
								@Override
								public void onCompleted(GraphUser user,
										Response response) {
									if (user != null) {
										UserBean userBean = new UserBean();
										userBean.setUserId(Long.valueOf(user
												.getId()));
										userBean.setUsername(user.getUsername());
										PreferencesUtils.setUserLogged(
												getApplicationContext(),
												userBean);
										Toast.makeText(getApplicationContext(), "You are logged by Facebook", Toast.LENGTH_LONG).show();
										callMainActivity();
									}
								}
							});
				}
			}
		});

	}

	/**
	 * Call the MainActivity
	 */
	public void callMainActivity() {
		Intent intent = null;
		intent = new Intent(getApplicationContext(), MainActivity.class);
		PreferencesUtils.FIRST_ACTIVITY = MainActivity.class;
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		finish();
	}

	// Will be called via the onClick attribute
	// of the buttons in main.xml
	public void onClick(View view) {
		EditText username = (EditText) findViewById(R.id.username);
		EditText password = (EditText) findViewById(R.id.passwordUser);

		switch (view.getId()) {
		/* Google Plus button */
		case R.id.sign_in_button:
			if (!mPlusClient.isConnected()) {
				if (mConnectionResult == null) {
					mConnectionProgressDialog.show();
				} else {
					try {
						mConnectionResult.startResolutionForResult(this,
								REQUEST_CODE_RESOLVE_ERR);
					} catch (SendIntentException e) {
						// Try connecting again.
						mConnectionResult = null;
						mPlusClient.connect();
					}
				}
			}
			break;
		case R.id.signin:
			if (!PreferencesUtils.DEBUG_MODE_ON) {
				try {
					if (PreferencesUtils.setUserLogged(
							getApplicationContext(),
							datasource.verifyLogin(username.getText()
									.toString().trim(), password.getText()
									.toString().trim(), view.getContext()))) {

						Toast.makeText(getApplicationContext(), "You are logged using The Exchange Student account.", Toast.LENGTH_LONG).show();
						callMainActivity();
						finish();
					}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Intent intent = null;
				intent = new Intent(getApplicationContext(), MainActivity.class);
				PreferencesUtils.FIRST_ACTIVITY = MainActivity.class;
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_left);
				finish();
			}
			break;
		case R.id.signup:
			Intent intent = null;
			intent = new Intent(getApplicationContext(), NewUserActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			// finish();
			break;
		case R.id.reset:
			username.setText(null);
			password.setText(null);
			if (username.requestFocus()) {
				getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			}
			break;
		}

	}

	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {		
		if (requestCode == REQUEST_CODE_RESOLVE_ERR && resultCode == RESULT_OK) {
			mConnectionResult = null;
			mPlusClient.connect();
		}
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
		//super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (mConnectionProgressDialog.isShowing()) {
			// The user clicked the sign-in button already. Start to resolve
			// connection errors. Wait until onConnected() to dismiss the
			// connection dialog.
			mConnectionProgressDialog.dismiss();

			if (result.hasResolution()) {
				try {
					result.startResolutionForResult(this,
							REQUEST_CODE_RESOLVE_ERR);
				} catch (SendIntentException e) {
					mPlusClient.connect();
				}
			}
		}

		// Save the intent so that we can start an activity when the user clicks
		// the sign-in button.
		mConnectionResult = result;
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// We've resolved any connection errors.
		mConnectionProgressDialog.dismiss();
		Toast.makeText(this, "You are logged by Google+", Toast.LENGTH_LONG).show();
		Intent intent = null;
		intent = new Intent(getApplicationContext(), MainActivity.class);
		PreferencesUtils.FIRST_ACTIVITY = MainActivity.class;
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		finish();
	}

	@Override
	public void onDisconnected() {
		Log.d(TAG, "disconnected");
	}

}