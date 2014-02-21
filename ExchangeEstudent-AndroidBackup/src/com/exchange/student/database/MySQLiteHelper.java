/** @author cesarnog */
package com.exchange.student.database;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	// The Android's default system path of your application database.
	private static String DATABASE_PATH;

	private static final String DATABASE_NAME = "exchangestudent.db";
	private static final int DATABASE_VERSION = 1;

	private final Context internalContext;

	public Context getInternalContext() {
		return internalContext;
	}

	public static final String TABLE_USER = "USER";
	public static final String TABLE_STUDENT = "STUDENT";
	public static final String TABLE_UNIVERSITY = "UNIVERSITY";
	public static final String TABLE_CHAT = "CHAT";
	public static final String TABLE_MESSAGE = "MESSAGE";
	public static final String TABLE_GROUP = "GROUP_DISK";
	public static final String TABLE_PROFILE = "GROUP_DISK";
	public static final String TABLE_ADDRESS = "ADDRESS";

	public static final String COLUMN_USER_ID = "_id";
	public static final String COLUMN_STUDENT_ID = "_id";
	public static final String COLUMN_UNIVERSITY_ID = "_id";
	public static final String COLUMN_CHAT_ID = "_id";
	public static final String COLUMN_PROFILE_ID = "_id";
	public static final String COLUMN_ADDRESS_ID = "_id";
	
	// Database creation sql statement
	private static final String DATABASE_CREATE_USER = "CREATE TABLE [USER] ( "
			+ " [_id] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,"
			+ " [ssn] VARCHAR(30)  NULL," + " [username] VARCHAR(30) NOT NULL,"
			+ " [password] VARCHAR(30) NOT NULL,"
			+ " [FK_HAS_PROFILE] INTEGER NOT NULL,"
			+ " [FK_HAS_GROUP] INTEGER NOT NULL,"
			+ " [FK_PARTICIPATE_IN_CHAT] INTEGER NOT NULL,"
			+ " [FK_HAS_STUDENT] INTEGER NOT NULL,"
			+ " FOREIGN KEY(FK_HAS_PROFILE) REFERENCES PROFILE(id),"
			+ " FOREIGN KEY(FK_HAS_STUDENT) REFERENCES STUDENT(id),"
			+ " FOREIGN KEY(FK_HAS_GROUP) REFERENCES GROUPDISC(id),"
			+ " FOREIGN KEY(FK_PARTICIPATE_IN_CHAT) REFERENCES CHAT(id)" + " )";

	private static final String DATABASE_CREATE_STUDENT = "CREATE TABLE [STUDENT] ( "
			+ "[_id] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ " [last_name] VARCHAR(30)  NULL,"
			+ " [middle_Name] VARCHAR(30)  NULL,"
			+ " [first_Name] VARCHAR(30)  NULL,"
			+ " [dob] DATE  NULL,"
			+ " FK_UNIVERSITY_FOREIGN integer null,"
			+ " FK_UNIVERSITY_HOME integer null,"
			+ " FK_ADDRESS integer not null,"
			+ " FK_INTEREST integer not null,"
			+ " FOREIGN KEY (FK_UNIVERSITY_FOREIGN) REFERENCES UNIVERSITY(id),"
			+ " FOREIGN KEY (FK_UNIVERSITY_HOME) REFERENCES UNIVERSITY(id),"
			+ " FOREIGN KEY (FK_ADDRESS) REFERENCES UNIVERSITY(id),"
			+ " FOREIGN KEY (FK_INTEREST) REFERENCES UNIVERSITY(id)" + " )";

	private static final String DATABASE_CREATE_ADDRESS = "CREATE TABLE [ADDRESS] ( "
			+ "[_id] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL, "
			+ "[str_Name] VARCHAR(30)  NULL, "
			+ "[str_Number] VARCHAR(30)  NULL, "
			+ "[apt_Number] NUMERIC  NULL, "
			+ "[city] VARCHAR(30)  NULL, "
			+ "[zip_Code] VARCHAR(20)  NULL, "
			+ "[state] VARCHAR(30)  NULL, "
			+ "[FK_STUDENT] INTEGER  NOT NULL, "
			+ "[FK_COUNTRY] INTEGER  NOT NULL, "
			+ "[FK_UNIVERSITY] INTEGER  NOT NULL " + ")";
	
	private static final String DATABASE_CREATE_UNIVERSITY = "CREATE TABLE [UNIVERSITY] ( "
			+ "[_id] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
			+ "[name] VARCHAR(30)  NULL, "
			+ "[FK_COUNTRY] integer  NOT NULL " + ")";

	
	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		/*try {
			createDataBase();			

		} catch (IOException ioe) {
			throw new Error("Unable to create database: " + ioe.getMessage());
		}

		try {
			openDataBase();
		} catch (SQLException sqle) {
			throw sqle;

		}
		// dbHelper.deleteDatabase();*/
		this.internalContext = context;
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {
				System.out.println(e.getMessage());
				throw new Error("Error copying database");

			}
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;
		
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
			DATABASE_PATH = "/data/com.exchange.student/databases/";
		} else {
			DATABASE_PATH = "/data/data/com.exchange.student/databases/";
		}

		DATABASE_PATH = "/data/data/com.exchange.student/databases/";
		
		try {
			String myPath = DATABASE_PATH + DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// database does't exist yet.

		}

		if (checkDB != null) {

			//checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		try {
			InputStream myInput = internalContext.getAssets().open(DATABASE_NAME);

		// Path to the just created empty db
		String outFileName = DATABASE_PATH + DATABASE_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
		
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	

	}

	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = DATABASE_PATH + DATABASE_NAME;
		/*exchangeStudentDatabase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);*/

	}

	@Override
	public synchronized void close() {

	/*	if (exchangeStudentDatabase != null)
			//exchangeStudentDatabase.close();*/

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		
		 executeSQLScript(database, "exchangeDbCreation.sql");
		
		 /*database.execSQL(DATABASE_CREATE_USER);
		 database.execSQL(DATABASE_CREATE_ADDRESS);
		 database.execSQL(DATABASE_CREATE_UNIVERSITY);
		 database.execSQL(DATABASE_CREATE_STUDENT);		*/
	}

	/**
	 * When the database needs to be upgraded, use this.
	 */
	public void cleanDatabase(SQLiteDatabase db) {

		/* Drop previsouly tables if already created */

		db.execSQL("DELETE * FROM " + TABLE_USER);
		db.execSQL("DELETE * FROM " + TABLE_STUDENT);
		db.execSQL("DELETE * FROM " + TABLE_UNIVERSITY);
		db.execSQL("DELETE * FROM " + TABLE_CHAT);
		db.execSQL("DELETE * FROM " + TABLE_MESSAGE);
		db.execSQL("DELETE * FROM " + TABLE_GROUP);
	}

	/**
	 * When the database needs to be upgraded, use this.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");

		/* Drop previsouly tables if already created */

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNIVERSITY);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);

		onCreate(db);
	}

	/**
	 * Execute SQL scripts by a .sql script file
	 * 
	 * @param database
	 *            Database Type
	 * @param dbname
	 *            Database name
	 */
	private void executeSQLScript(SQLiteDatabase database, String dbname) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];
		int len;
		AssetManager assetManager = internalContext.getAssets();
		InputStream inputStream = null;
		
		String sqlStatement = null;

		try {
			inputStream = assetManager.open(dbname);
			while ((len = inputStream.read(buf)) != -1) {
				outputStream.write(buf, 0, len);
			}
			outputStream.close();
			inputStream.close();

			String[] createScript = outputStream.toString().split(";");
			for (int i = 0; i < createScript.length; i++) {
				sqlStatement = createScript[i].trim();
				
				// TODO You may want to parse out comments here
				if (sqlStatement.length() > 0) {
					database.execSQL(sqlStatement + ";");
				}
			}
		} catch (IOException e) {
			// TODO Handle Script Failed to Load
		} catch (SQLException e) {
			// TODO Handle Script Failed to Execute
			System.out.println(e.getMessage() + sqlStatement);
		}
	}

	/**
	 * Verify is SDCard is Writeable
	 * 
	 * @return
	 */
	public static boolean isSDCardWriteable() {
		boolean rc = false;

		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			rc = true;
		}

		return rc;
	}

	public void deleteDatabase() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USER, "1", new String[] {});
		// db.delete(TABLE_UNIVERSITY, "1", new String[] {});
		Log.d("Database stuff", "Database table succesfully deleted");
		//db.close();
	}

}