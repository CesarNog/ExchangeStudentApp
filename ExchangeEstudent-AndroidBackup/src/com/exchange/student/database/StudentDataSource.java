package com.exchange.student.database;

import java.util.ArrayList;
import java.util.List;

import com.exchange.student.bean.UserBean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Datasource provider for Student entity
 * @author cesarnog
 *
 */
public class StudentDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { "id", "ssn", "FK_HAS_PROFILE",
			"FK_HAS_GROUP", "FK_PARTICIPATE_IN_CHAT", "FK_HAS_STUDENT" };
	
	private String[] loginColumns = { "username", "password" };
		
	public StudentDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
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
		values.put("FK_HAS_PROFILE", newUser.getUserProfileId());
		values.put("FK_HAS_GROUP", newUser.getGroupId());
		values.put("FK_PARTICIPATE_IN_CHAT", newUser.getChatId());
		values.put("FK_HAS_STUDENT", newUser.getStudentId());

		long insertId = database
				.insert(MySQLiteHelper.TABLE_USER, null, values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_USER, allColumns,
				MySQLiteHelper.COLUMN_USER_ID + " = " + insertId, null, null,
				null, null);
		cursor.moveToFirst();
		UserBean newUserCreated = cursorToUser(cursor);
		cursor.close();
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
		user.setUserId(cursor.getLong(0));
		user.setSsn(cursor.getString(1));
		return user;
	}
	
	public boolean verifyLogin(String username, String password) {
		Cursor cursor = database.query(MySQLiteHelper.TABLE_USER, loginColumns,
				null, null, null, null, null);

		if(cursor!=null){
			return true;
		}
		
		return false;
	}
}