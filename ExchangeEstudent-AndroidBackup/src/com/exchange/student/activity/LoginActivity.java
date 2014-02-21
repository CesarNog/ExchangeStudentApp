package com.exchange.student.activity;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.exchange.student.R;
import com.exchange.student.database.UserDataSource;
import com.exchange.student.utils.PreferencesUtils;

public class LoginActivity extends Activity {
	private UserDataSource datasource;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.login);

		datasource = new UserDataSource(this);
		
				
		// Log.w(Environment.getDataDirectory().toString(),
		// "SAVING DATABASE AT: ");
		// System.out.println(Environment.getDataDirectory().toString());
		// System.out.println(MySQLiteHelper.isSDCardWriteable() ?
		// "SD CARD READABLE" : "NO SDCARD READABLE");
		//datasource.open();		

		EditText mTextView = (EditText) findViewById(R.id.password);

		/*mTextView
				.setOnEditorActionListener(new EditText.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView view, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							System.out
									.println("Validating USER REGISTER FORM...");
							validateAndSubmit();
							return true;
						}
						return false;
					}
				});*/
	}

	// Will be called via the onClick attribute
	// of the buttons in main.xml
	public void onClick(View view) throws UnsupportedEncodingException {
		EditText username = (EditText) findViewById(R.id.username);
		EditText password = (EditText) findViewById(R.id.password);
		switch (view.getId()) {
		case R.id.signin:
			if (!PreferencesUtils.DEBUG_MODE_ON) {
				if (PreferencesUtils.setUserLogged(getApplicationContext(),
						datasource.verifyLogin(username.getText().toString(),
								password.getText().toString(),
								view.getContext()))) {

					Intent intent = null;
					intent = new Intent(getApplicationContext(),
							MainActivity.class);
					PreferencesUtils.FIRST_ACTIVITY = MainActivity.class;
					startActivity(intent);
					overridePendingTransition(R.anim.slide_out_right,
							R.anim.slide_in_right);
					finish();
				}
			} else {
				Intent intent = null;
				intent = new Intent(getApplicationContext(),
						MainActivity.class);
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
			overridePendingTransition(R.anim.fade_in,
					R.anim.fade_out);
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

}