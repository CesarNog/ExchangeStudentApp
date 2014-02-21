package com.exchange.student.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.exchange.student.R;
import com.exchange.student.bean.StudentBean;
import com.exchange.student.bean.UserBean;
import com.exchange.student.database.UserDataSource;
import com.exchange.student.utils.PreferencesUtils;

public class NewUserActivity extends Activity {
	private UserDataSource datasource;
	
	ImageView imgBack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.register_user);


		datasource = new UserDataSource(this);
		// Log.w(Environment.getDataDirectory().toString(),
		// "SAVING DATABASE AT: ");
		// System.out.println(Environment.getDataDirectory().toString());
		// System.out.println(MySQLiteHelper.isSDCardWriteable() ?
		// "SD CARD READABLE" : "NO SDCARD READABLE");
		datasource.open();
	}

	// Will be called via the onClick attribute
	// of the buttons in register_user.xml
	public void onClick(View view) {

		EditText username = (EditText) findViewById(R.id.username);

		ViewGroup group = (ViewGroup) findViewById(R.id.formNewUser);

		@SuppressWarnings("unchecked")
		UserBean user = null;
		switch (view.getId()) {
		case R.id.submit:
			int nextInt = new Random().nextInt(3);

			if (validateFields(group)) {
				UserBean userCreated = userFormTransfer();

					Intent intent = null;
					intent = new Intent(getApplicationContext(),
							MainActivity.class);
					PreferencesUtils.FIRST_ACTIVITY = MainActivity.class;
					startActivity(intent);
					overridePendingTransition(R.anim.slide_out_right,
							R.anim.slide_in_right);
					finish();			
			}

			break;
		case R.id.reset:

			clearForm(group);
			if (username.requestFocus()) {
				getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			}

			break;
		}
	}

	/**
	 * Transfer field values to a UserBean
	 * 
	 * @param group
	 */
	private UserBean userFormTransfer() {
		UserBean userTransfered = new UserBean();
		StudentBean studentTransfered = new StudentBean();

		EditText username = (EditText) findViewById(R.id.username);
		EditText password = (EditText) findViewById(R.id.password);
		EditText repeatedPassword = (EditText) findViewById(R.id.repeatedPassword);
		EditText ssn = (EditText) findViewById(R.id.ssn);
		EditText firstName = (EditText) findViewById(R.id.firstName);
		EditText middleName = (EditText) findViewById(R.id.middleName);
		EditText lastName = (EditText) findViewById(R.id.lastName);
		EditText dob = (EditText) findViewById(R.id.dob);

		userTransfered.setUsername(username.getText().toString());

		if (repeatedPassword.getText().toString()
				.equals(password.getText().toString())) {
			userTransfered.setPassword(password.getText().toString());
		}
		userTransfered.setSsn(ssn.getText().toString());

		studentTransfered.setFirstName(firstName.getText().toString());

		studentTransfered.setMiddleName(middleName.getText().toString());

		studentTransfered.setLastName(lastName.getText().toString());

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		try {
			Date date = formatter.parse(dob.getText().toString());
			System.out.println(date);
			System.out.println(formatter.format(date));
			studentTransfered.setDateOfBirth(date);
		} catch (java.text.ParseException e) {
			System.out.println(e.getMessage());
		}
		
		datasource.createUser(userTransfered);
		return userTransfered;
	}

	/**
	 * Clear all the editTexts inputs
	 * 
	 * @param group
	 */
	private void clearForm(ViewGroup group) {
		for (int i = 0, count = group.getChildCount(); i < count; ++i) {
			View view = group.getChildAt(i);
			if (view instanceof EditText) {
				((EditText) view).setText("");
			}

			if (view instanceof ViewGroup
					&& (((ViewGroup) view).getChildCount() > 0))
				clearForm((ViewGroup) view);
		}
	}

	private void listenFields(ViewGroup group) {
		for (int i = 0, count = group.getChildCount(); i < count; ++i) {
			View view = group.getChildAt(i);
			if (view instanceof EditText) {
				((EditText) view)
						.setOnFocusChangeListener(new View.OnFocusChangeListener() {
							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								String input;
								EditText editText;
								if (!hasFocus) {
									editText = (EditText) v;
									input = editText.getText().toString();
									if (!input.isEmpty()) {
										editText.setError(null);
									}
								}
							}
						});
			}
		}
	}

	/**
	 * Validate fields - new user form
	 * 
	 * @param group
	 */
	private boolean validateFields(ViewGroup group) {
		listenFields(group);
		EditText username = (EditText) findViewById(R.id.username);
		EditText password = (EditText) findViewById(R.id.password);
		EditText repeatedPassword = (EditText) findViewById(R.id.repeatedPassword);
		EditText ssn = (EditText) findViewById(R.id.ssn);
		EditText firstName = (EditText) findViewById(R.id.firstName);
		EditText middleName = (EditText) findViewById(R.id.middleName);
		EditText lastName = (EditText) findViewById(R.id.lastName);
		EditText dob = (EditText) findViewById(R.id.dob);

		int countErrors = 0;

		for (int i = 0, count = group.getChildCount(); i < 3; ++i) {
			View view = group.getChildAt(i);
			if (view instanceof EditText) {
				if (((EditText) view).getText().toString().isEmpty()) {
					((EditText) view).setError("Required");
					countErrors++;
				}
			}
		}
		
		if(!password.getText().toString().equals(repeatedPassword.getText().toString())){
			countErrors++;		
			
			CharSequence text = "Password and Repeated password must be the same!";
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(group.getContext(), text, duration);
			toast.show();
			repeatedPassword.setText(null);
			repeatedPassword.requestFocus();
		}
		if (countErrors == 0) {
			return true;
		}
		return false;

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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_ENTER && event.getRepeatCount() == 0) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}

}