package com.exchange.student.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.exchange.student.activity.LoginActivity;
import com.exchange.student.activity.MainActivity;
import com.exchange.student.bean.UserBean;

public abstract class PreferencesUtils {
	
	/**
	 * Chave para indicar a primeira tela a ser mostrada após o splash
	 */
	public static final boolean DEBUG_MODE_ON = false;

	/**
	 * Preferences file name.
	 */
	private static String PREFERENCES_NAME = "ExchangeStudentPreferences";

	/**
	 * Key that indicates first user access.
	 */
	public static final String KEY_FIRST_RUN = "KEY_FIRST_RUN";

	/**
	 * Chave para indicar qual o texto de filtro de contatos.
	 */
	public static final String KEY_CONTACTS_FILTER = "KEY_CONTACTS_FILTER";

	/**
	 * Chave para indicar a imagem de contato
	 */
	public static final String KEY_CONTACT_PHONE_ID = "KEY_CONTACT_PHONE_ID";

	/**
	 * Chave para indicar a primeira tela a ser mostrada após o splash
	 */
	public static final String KEY_FIRST_ACTIVITY = "KEY_FIRST_ACTIVITY";

	/**
	 * Para indicar que chamadas a activities subsequentes a primeira devem ser
	 * finalizadas Assim o back do celular tem historico de apenas -1
	 */
	public static Class<?> FIRST_ACTIVITY = MainActivity.class;

	/**
	 * Indicates if first access of the user
	 * */
	public static boolean isFirstRunFindContacts(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				PREFERENCES_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(KEY_FIRST_RUN, true);
	}

	/**
	 * Set the KEY_FIRST_RUN at SharedPreferences with value false, indicating
	 * that this will not be more the first activity to show
	 * */
	public static void setNotFirstRunFindContacts(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				PREFERENCES_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(KEY_FIRST_RUN, false);
		editor.commit();
	}

	public static boolean setUserLogged(Context context, UserBean userLogged) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				PREFERENCES_NAME, Context.MODE_PRIVATE);

		if (userLogged != null) {
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putLong("user_userId", userLogged.getUserId());
			editor.putString("user_username", userLogged.getUsername());
			editor.commit();
			return true;
		}
		return false;
	}

	public static void setTextFilter(Context context, String str) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				PREFERENCES_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(KEY_CONTACTS_FILTER, str);
		editor.commit();
	}

	public static String recoverTextFilter(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				PREFERENCES_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getString(KEY_CONTACTS_FILTER, "");
	}

	public static void setFirstActivity(Context context, String activity) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				PREFERENCES_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(KEY_FIRST_ACTIVITY, activity);
		editor.commit();
	}
	
	public static String recoverUniversity(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				PREFERENCES_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getString(KEY_FIRST_ACTIVITY,
				LoginActivity.class.toString());
	}

	public static String recoverFirstActivity(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				PREFERENCES_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getString(KEY_FIRST_ACTIVITY,
				LoginActivity.class.toString());
	}

	public static void setContactPhoneId(Context context, Integer id) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				PREFERENCES_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(KEY_CONTACT_PHONE_ID, id);
		editor.commit();
	}

	public static Integer recoverContactPhoneId(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				PREFERENCES_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(KEY_CONTACT_PHONE_ID, -1);
	}
}
