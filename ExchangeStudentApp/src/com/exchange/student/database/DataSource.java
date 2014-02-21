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

import com.exchange.student.bean.AddressBean;
import com.exchange.student.bean.ChatBean;
import com.exchange.student.bean.CountryBean;
import com.exchange.student.bean.StudentBean;
import com.exchange.student.bean.UniversityBean;
import com.exchange.student.bean.UserBean;

public class DataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private static MessageDigest md;

	public DataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
		database = dbHelper.getWritableDatabase();
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
		values.put("username", newUser.getUsername());
		values.put("password", encryptPassword(newUser.getPassword()));
		values.put("FK_HAS_PROFILE",
				newUser.getUserProfileId() != null ? newUser.getUserProfileId()
						: new Integer(1));
		values.put("FK_HAS_GROUP",
				newUser.getGroupId() != null ? newUser.getGroupId()
						: new Integer(1));
		values.put("FK_PARTICIPATE_IN_CHAT",
				newUser.getChatId() != null ? newUser.getChatId()
						: new Integer(1));
		values.put("FK_HAS_STUDENT",
				newUser.getStudentId() != null ? newUser.getStudentId()
						: new Integer(1));

		long insertId = database.insertOrThrow(MySQLiteHelper.TABLE_USER, null,
				values);

		UserBean newUserCreated = null;

		try {

			Cursor cursor = database.query(MySQLiteHelper.TABLE_USER,
					MySQLiteHelper.allColumnsUser,
					MySQLiteHelper.COLUMN_USER_ID + " = " + insertId, null,
					null, null, null);
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

		Cursor cursor = database.query(MySQLiteHelper.TABLE_USER,
				MySQLiteHelper.allColumnsUser, null, null, null, null, null);

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
			Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE,
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
		Cursor cursor = database.query(MySQLiteHelper.TABLE_USER,
				MySQLiteHelper.allColumnsUser, null, null, null, null, null);

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
							&& encryptPassword(password).equals(
									user.getPassword())) {
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

	private ChatBean cursorToChat(Cursor cursor) {
		ChatBean chat = new ChatBean();
		chat.setChatId(cursor.getLong(0));
		chat.setFkUser1(cursor.getLong(1));
		chat.setFkUser2(cursor.getLong(2));
		return chat;
	}

	private StudentBean cursorToStudent(Cursor cursor) {
		StudentBean student = new StudentBean();
		student.setStudentId(cursor.getLong(0));
		student.setLastName(cursor.getString(1));
		student.setMiddleName(cursor.getString(2));
		student.setFirstName(cursor.getString(3));
		// student.setDateOfBirth(cursor.get(4));
		/*
		 * student.(cursor.getLong(5)); student.setStudentId(cursor.getLong(6));
		 * student.setStudentId(cursor.getLong(7));
		 * student.setCountryId(cursor.getLong(2));
		 */
		return student;
	}

	private UniversityBean cursorToUniversity(Cursor cursor) {
		UniversityBean university = new UniversityBean();
		university.setUniversityId(cursor.getLong(0));
		university.setName(cursor.getString(1));
		university.setCountryId(cursor.getLong(2));
		return university;
	}

	private AddressBean cursorToAddress(Cursor cursor) {
		AddressBean address = new AddressBean();
		address.setAddressId(cursor.getLong(0));
		address.setStreetName(cursor.getString(1));
		address.setStreetNumber(cursor.getString(2));
		address.setAptNumber(cursor.getLong(3));
		address.setCity(cursor.getString(4));
		address.setZipCode(cursor.getString(5));
		address.setState(cursor.getString(6));
		address.setCountry(getCountry(cursor.getLong(7)));

		return address;
	}

	private CountryBean cursorToCountry(Cursor cursor) {
		CountryBean country = new CountryBean();
		country.setCountryId(cursor.getLong(0));
		country.setName(cursor.getString(1));
		return country;
	}

	/**
	 * Insert a new ADDRESS into the database
	 * 
	 * @param newStudent
	 *            New university to be registered
	 * @return UniversityBean new student created
	 */
	public AddressBean createAddress(AddressBean addressBean) {
		ContentValues values = new ContentValues();
		AddressBean newAddressCreated = null;

			values.put("str_Name", addressBean.getStreetName());
			values.put("str_Number", addressBean.getStreetNumber());
			values.put("apt_Number", addressBean.getAptNumber());
			values.put("city", addressBean.getCity());
			values.put("zip_Code", addressBean.getZipCode());
			values.put("state", addressBean.getState());
			values.put("FK_COUNTRY", addressBean.getCountry().getCountryId());
	
			long insertId = database.insert(MySQLiteHelper.TABLE_ADDRESS, null,
					values);
			Cursor cursor = database.query(MySQLiteHelper.TABLE_ADDRESS,
					MySQLiteHelper.allColumnsAddress,
					MySQLiteHelper.COLUMN_ADDRESS_ID + " = " + insertId, null,
					null, null, null);
	
			cursor.moveToFirst();
			newAddressCreated = cursorToAddress(cursor);
			cursor.close();
		return newAddressCreated;
	}

	/**
	 * Insert a new STUDENT into the database
	 * 
	 * @param newStudent
	 *            New university to be registered
	 * @return UniversityBean new student created
	 */
	public StudentBean createStudent(StudentBean newStudent) {
		ContentValues values = new ContentValues();

		values.put("last_name", newStudent.getLastName());		
		values.put("middle_name", newStudent.getMiddleName());
		values.put("first_name", newStudent.getFirstName());
		values.put("FK_ADDRESS", newStudent.getAddressBean().getAddressId());
		//newStudent.getDateOfBirth().toString()
		values.put("dob", "1990-10-10");
		values.put("FK_UNIVERSITY_FOREIGN", Long.valueOf(1));
		values.put("FK_UNIVERSITY_HOME", Long.valueOf(1));
		values.put("FK_ADDRESS", newStudent.getAddressBean().getAddressId());		
		values.put("FK_INTEREST", Long.valueOf(1));

		long insertId = database.insert(MySQLiteHelper.TABLE_STUDENT, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_STUDENT,
				MySQLiteHelper.allColumnsStudent,
				MySQLiteHelper.COLUMN_STUDENT_ID + " = " + insertId, null,
				null, null, null);

		cursor.moveToFirst();
		StudentBean newUniversityCreated = cursorToStudent(cursor);
		cursor.close();
		return newUniversityCreated;
	}

	/**
	 * Insert a new UNIVERSITY into the database
	 * 
	 * @param newUniversity
	 *            New university to be registered
	 * @return UniversityBean new university created
	 */
	public UniversityBean createUniversity(UniversityBean newUniversity) {
		ContentValues values = new ContentValues();

		values.put("name", newUniversity.getName());
		values.put("FK_COUNTRY", newUniversity.getCountry().getCountryId());

		long insertId = database.insert(MySQLiteHelper.TABLE_UNIVERSITY, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_UNIVERSITY,
				MySQLiteHelper.allColumnsUser,
				MySQLiteHelper.COLUMN_UNIVERSITY_ID + " = " + insertId, null,
				null, null, null);

		cursor.moveToFirst();
		UniversityBean newUniversityCreated = cursorToUniversity(cursor);
		cursor.close();
		return newUniversityCreated;
	}

	/**
	 * Get all users registered on the database
	 * 
	 * @return List<UniversityBean> - list of universities
	 */
	public List<UniversityBean> getAllUniversities() {
		List<UniversityBean> universities = new ArrayList<UniversityBean>();

		try {

			Cursor cursor = database.query(MySQLiteHelper.TABLE_UNIVERSITY,
					MySQLiteHelper.allColumnsUniversity, null, null, null,
					null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				UniversityBean university = cursorToUniversity(cursor);
				universities.add(university);
				cursor.moveToNext();
			}

			// make sure to close the cursor
			cursor.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return universities;
	}

	/**
	 * Get all users registered on the database
	 * 
	 * @return List<UniversityBean> - list of universities
	 */
	public List<ChatBean> getAllChats() {
		List<ChatBean> chats = new ArrayList<ChatBean>();

		try {

			Cursor cursor = database
					.query(MySQLiteHelper.TABLE_CHAT,
							MySQLiteHelper.allColumnsChat, null, null, null,
							null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				ChatBean chat = cursorToChat(cursor);
				chats.add(chat);
				cursor.moveToNext();
			}

			// make sure to close the cursor
			cursor.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return chats;
	}

	/**
	 * get single address
	 */
	public CountryBean getCountry(long id) {
		String selectQuery = "SELECT  * FROM " + MySQLiteHelper.TABLE_COUNTRY
				+ " WHERE " + MySQLiteHelper.COLUMN_COUNTRY_ID + " = " + id;

		Cursor cursor = database.rawQuery(selectQuery, null);

		if (cursor != null) {
			cursor.moveToFirst();

			CountryBean countryFound = cursorToCountry(cursor);

			return countryFound;
		} else {
			return null;
		}
	}

	public CountryBean getCountryByName(String name) {
		String selectQuery = "SELECT * FROM " + MySQLiteHelper.TABLE_COUNTRY
				+ " WHERE " + " COUNTRY.name " + " like '" + name + "'";

		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if (cursor != null) {
			cursor.moveToFirst();

			CountryBean countryFound = cursorToCountry(cursor);

			return countryFound;
		} else {
			return null;
		}
	}

	/**
	 * get single address
	 */
	public AddressBean getAddress(long id) {
		String selectQuery = "SELECT  * FROM " + MySQLiteHelper.TABLE_ADDRESS
				+ " WHERE " + MySQLiteHelper.COLUMN_ADDRESS_ID + " = " + id;

		Cursor cursor = database.rawQuery(selectQuery, null);

		if (cursor != null) {
			cursor.moveToFirst();

			AddressBean adressFound = cursorToAddress(cursor);

			return adressFound;
		} else {
			return null;
		}
	}

	/**
	 * get single user
	 */
	public UserBean getUser(long id) {
		String selectQuery = "SELECT  * FROM " + MySQLiteHelper.TABLE_USER
				+ " WHERE " + MySQLiteHelper.COLUMN_USER_ID + " = " + id;

		Cursor cursor = database.rawQuery(selectQuery, null);

		if (cursor != null) {
			cursor.moveToFirst();

			UserBean userFound = cursorToUser(cursor);

			return userFound;
		} else {
			return null;
		}
	}

	/**
	 * Get all users registered on the database
	 * 
	 * @return List<UniversityBean> - list of universities
	 */
	public List<CountryBean> getAllCountries() {
		List<CountryBean> countries = new ArrayList<CountryBean>();

		try {

			Cursor cursor = database.query(MySQLiteHelper.TABLE_COUNTRY,
					MySQLiteHelper.allColumnsCountry, null, null, null, null,
					null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				CountryBean country = cursorToCountry(cursor);
				countries.add(country);
				cursor.moveToNext();
			}

			// make sure to close the cursor
			cursor.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return countries;
	}

	/**
	 * Delete an existing university from database
	 * 
	 * @param university
	 *            university to be deleted
	 */
	public void deleteUniversity(UniversityBean university) {
		long id = university.getUniversityId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_UNIVERSITY,
				MySQLiteHelper.COLUMN_UNIVERSITY_ID + " = " + id, null);
	}
}