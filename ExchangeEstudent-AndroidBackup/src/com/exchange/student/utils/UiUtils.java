package com.exchange.student.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.exchange.student.R;

/**
 * Classe de métodos de uso comum em vários pontos da aplicação.
 * 
 * @author cesarnog
 * 
 */
public final class UiUtils {
	

	/**
	 * Dialog about us
	 * 
	 * @param act
	 *            Activity that calls the dialog
	 */
	public static void dialogAboutUs(final Activity act) {
		AlertDialog.Builder builder;
		LayoutInflater inflater = (LayoutInflater) act.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_about_us, (ViewGroup) act.findViewById(R.id.about_us_layout));

		builder = new AlertDialog.Builder(act);
		builder.setView(layout);
		builder.setTitle(act.getText(R.string.about_us_title));

		builder.setCancelable(true).setNeutralButton(act.getText(R.string.btn_back), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	/**
	 * Quick message for  the user.
	 * 
	 * @param act
	 *            Activity that will call the message.
	 * @param neeededAreacodeMessage
	 *            Message to be showed.
	 */
	public static void quickMessage(Activity act, String stringMessage) {
		Toast.makeText(act.getApplicationContext(), stringMessage, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Long message for  the user.
	 * 
	 * @param act
	 *            Activity that will call the message.
	 * @param neeededAreacodeMessage
	 *            Message to be showed.
	 */
	public static void longMessage(Activity act, String stringMessage) {
		Toast.makeText(act.getApplicationContext(), stringMessage, Toast.LENGTH_LONG).show();
	}

	public static void editContact(Activity act, Integer contactId) {
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setData(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId));
		act.startActivity(intent);
	}

}
