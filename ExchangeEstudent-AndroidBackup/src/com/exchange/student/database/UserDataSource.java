package com.exchange.student.database;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.exchange.student.bean.UserBean;

public class UserDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private static MessageDigest md;

	private String[] allColumns = { "_id", "ssn", "username", "password",
			"FK_HAS_PROFILE", "FK_HAS_GROUP", "FK_PARTICIPATE_IN_CHAT",
			"FK_HAS_STUDENT" };

	public UserDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void remove() {

	}

	/**
	 * Insert a new USER into the database
	 * 
	 * @param newUser
	 *            New user to be registered
	 * @return UserBean new user created
	 */
	public UserBean createUser(UserBean newUser) {
		ContentValues values = new ContentValues();
		values.put("ssn", newUser.getSsn());
		values.put("username", newUser.getUsername());
		values.put("password", encryptPassword(newUser.getPassword()));	
		values.put("FK_HAS_PROFILE", newUser.getUserProfileId()!=null ? newUser.getUserProfileId() : new Integer(1));
		values.put("FK_HAS_GROUP", newUser.getGroupId()!=null ? newUser.getGroupId() : new Integer(1));
		values.put("FK_PARTICIPATE_IN_CHAT", newUser.getChatId()!=null ? newUser.getChatId() : new Integer(1));
		values.put("FK_HAS_STUDENT", newUser.getStudentId()!=null ? newUser.getStudentId() : new Integer(1) );
	
		long insertId = database
				.insertOrThrow(MySQLiteHelper.TABLE_USER, null, values);

		UserBean newUserCreated = null;

		try {

			Cursor cursor = database.query(MySQLiteHelper.TABLE_USER,
					allColumns, MySQLiteHelper.COLUMN_USER_ID + " = "
							+ insertId, null, null, null, null);
			cursor.moveToFirst();
			newUserCreated = cursorToUser(cursor);
			cursor.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return newUserCreated;
	}

	public void deleteUser(UserBean user) {
		long id = user.getUserId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_USER,
				MySQLiteHelper.COLUMN_USER_ID + " = " + id, null);
	}

	/**
	 * Get all users registered on the database
	 * 
	 * @return List<Userbean> - list of users
	 */
	public List<UserBean> getAllUsers() {
		List<UserBean> users = new ArrayList<UserBean>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_USER, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			UserBean user = cursorToUser(cursor);
			users.add(user);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return users;
	}

	private UserBean cursorToUser(Cursor cursor) {
		UserBean user = new UserBean();
		if (cursor.getCount() > 0) {
			user.setUserId(cursor.getLong(0));
			user.setSsn(cursor.getString(1));
			user.setUsername(cursor.getString(2));
			user.setPassword(cursor.getString(3));
		}

		return user;
	}

	/**
	 * Encrypt password to MD5
	 * 
	 * @param password
	 *            The password to be encrypted
	 */
	private static final String encryptPassword(final String password) {
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] passBytes = password.getBytes();
			md.reset();
			byte[] digested = md.digest(passBytes);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < digested.length; i++) {
				sb.append(Integer.toHexString(0xff & digested[i]));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(UserDataSource.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return null;

	}

	/**
	 * Verify login of user
	 * 
	 * @param username
	 *            Username given
	 * @param password
	 *            Password given
	 * @param context
	 *            Application context
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public UserBean verifyLogin(String username, String password,
			Context context) throws UnsupportedEncodingException {
		Cursor cursor = database.query(MySQLiteHelper.TABLE_USER, allColumns,
				null, null, null, null, null);

		if (username.isEmpty() || password.isEmpty()) {
			CharSequence text = "Please fill up username and password fields.";
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		} else {

			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					UserBean user = cursorToUser(cursor);
					if (username.equals(user.getUsername())
							&& encryptPassword(password).equals(user.getPassword())) {
						return user;
					}
					cursor.moveToNext();
				}
				CharSequence text = "No user has found with the username/password combination!";
				int duration = Toast.LENGTH_LONG;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		}
		return null;
	}
}