package com.exchange.student.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.exchange.student.activity.LoginActivity;
import com.exchange.student.activity.MainActivity;
import com.exchange.student.bean.UserBean;

public abstract class PreferencesUtils {

	/**
	 * Allow open the application directly
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
			if(userLogged.getUserId()!=null){
			editor.putLong("user_userId", userLogged.getUserId());
			editor.putString("user_username", userLogged.getUsername());
			editor.putBoolean("USER_LOGGED", true);
			editor.commit();
			}
			return true;
		}
		return false;
	}

	public static boolean isUserAuthenticated(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				PREFERENCES_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean("USER_LOGGED", false);
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
}
