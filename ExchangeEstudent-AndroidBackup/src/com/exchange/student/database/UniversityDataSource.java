package com.exchange.student.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.exchange.student.bean.UniversityBean;

public class UniversityDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { "_id", "name", "FK_COUNTRY" };

	public UniversityDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
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
				allColumns, MySQLiteHelper.COLUMN_UNIVERSITY_ID + " = "
						+ insertId, null, null, null, null);

		cursor.moveToFirst();
		UniversityBean newUniversityCreated = cursorToUniversity(cursor);
		cursor.close();
		return newUniversityCreated;
	}

	public void deleteUniversity(UniversityBean university) {
		long id = university.getUniversityId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_UNIVERSITY,
				MySQLiteHelper.COLUMN_UNIVERSITY_ID + " = " + id, null);
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
					allColumns, null, null, null, null, null);

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

	private UniversityBean cursorToUniversity(Cursor cursor) {
		UniversityBean university = new UniversityBean();
		university.setUniversityId(cursor.getLong(0));
		university.setName(cursor.getString(1));
		university.setCountryId(cursor.getLong(2));
		return university;
	}

}