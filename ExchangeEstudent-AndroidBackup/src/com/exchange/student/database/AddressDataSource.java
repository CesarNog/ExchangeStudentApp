package com.exchange.student.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.exchange.student.bean.AddressBean;
import com.exchange.student.bean.UserBean;

public class AddressDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;

	private String[] allColumns = { "id", "str_Name", "str_Number",
			"apt_Number", "city", "zip_Code", "state", "FK_STUDENT",
			"FK_COUNTRY", "FK_UNIVERSITY" };

	private String[] loginColumns = { "username", "password" };

	public AddressDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
		// dbHelper.deleteDatabase();
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
	public AddressBean createAddress(AddressBean newAddress) {
		ContentValues values = new ContentValues();
		values.put("str_Name", newAddress.getStreetName());
		values.put("str_Number", newAddress.getStreetNumber());
		values.put("apt_Number", newAddress.getAptNumber());
		values.put("city", newAddress.getCity());
		values.put("zip_Code", newAddress.getZipCode());
		values.put("state", newAddress.getState());

		long insertId = database.insert(MySQLiteHelper.TABLE_ADDRESS, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_ADDRESS,
				allColumns,
				MySQLiteHelper.COLUMN_ADDRESS_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		AddressBean newAddressCreated = cursorToAddress(cursor);
		cursor.close();
		return newAddressCreated;
	}

	public void deleteAddress(AddressBean address) {
		long id = address.getAddressId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_USER,
				MySQLiteHelper.COLUMN_ADDRESS_ID + " = " + id, null);
	}

	/**
	 * Get all address registered on the database
	 * 
	 * @return List<AddressBean> - list of users
	 */
	public List<AddressBean> getAllAddresses() {
		List<AddressBean> addresses = new ArrayList<AddressBean>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_USER, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			AddressBean address = cursorToAddress(cursor);
			addresses.add(address);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return addresses;
	}

	private AddressBean cursorToAddress(Cursor cursor) {
		AddressBean address = new AddressBean();
		if (cursor.getCount() > 0) {
			address.setAddressId(cursor.getInt(0));
			address.setStreetName(cursor.getString(1));
			address.setStreetNumber(cursor.getString(2));
			address.setAptNumber(cursor.getLong(3));
			address.setCity(cursor.getString(4));
			address.setZipCode(cursor.getString(5));
			address.setState(cursor.getString(6));
		}

		return address;
	}

}