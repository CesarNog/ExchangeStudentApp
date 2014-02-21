/**
 * @author cesarnog
 */
package com.exchange.student.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.exchange.student.bean.UserBean;

/**
 * Creation of entity USER.
 */
public class UserInformationStoreHelper {

	/**
	 * Database version
	 */
	private static final int DATABASE_VERSION = 1;

	/**
	 * Table name.
	 */
	private static final String USER_TABLE_NAME = "USER";

	/**
	 * Id column name of USER.
	 */
	private static final String USER_UNIQUE_ID = "id";

	/**
	 * Query para criar tabela.
	 */
	private static final String USER_TABLE_CREATE = "CREATE TABLE [USER] ("
			+ "[id] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,"
			+ " [ssn] VARCHAR(30)  NULL,"
			+ "[FK_HAS_PROFILE] INTEGER NOT NULL,"
			+ "  [FK_HAS_GROUP] INTEGER NOT NULL,"
			+ "[FK_PARTICIPATE_IN_CHAT] INTEGER NOT NULL,"
			+ "[FK_HAS_STUDENT] INTEGER NOT NULL,"
			+ " FOREIGN KEY(FK_HAS_PROFILE) REFERENCES PROFILE(id),"
			+ " FOREIGN KEY(FK_HAS_STUDENT) REFERENCES STUDENT(id),"
			+ " FOREIGN KEY(FK_HAS_GROUP) REFERENCES GROUPDISC(id),"
			+ " FOREIGN KEY(FK_PARTICIPATE_IN_CHAT) REFERENCES CHAT(id)"
			+ " );";

	private static DatabaseHelper mOpenHelper;

	/**
	 * Construtor padrão, deve ser sobrescrito para que crie a tabela quando um objeto é instanciado.
	 *
	 * @param context
	 *            Contexto da aplicação.
	 */
	public UserInformationStoreHelper(Context context) {
		if (mOpenHelper == null) {
			mOpenHelper = new DatabaseHelper(context);
		}
	}

	/**
	 * This class helps open, create, and upgrade the database file.
	 */
	static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, USER_TABLE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(USER_TABLE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	/**
	 * Select all elements of the table
	 *
	 * @return the cursor for the DB selection
	 */
	public Cursor selectUserInformation() {
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor cursor = db.query(USER_TABLE_NAME, // Table Name
				null, // Columns to return
				null, // SQL WHERE
				null, // Selection Args
				null, // SQL GROUP BY
				null, // SQL HAVING
				null); // SQL ORDER BY
		cursor.moveToFirst();
		return cursor;
	}

	/**
	 * Return the SSN of the user
	 *
	 * @return If user have a SSN registered, returns the same, if not, returns a void string.
	 */
	public String findUserSSN() {
		String userSSN = "";
		Cursor cursor = selectUserInformation();
		if (cursor.getCount() > 0) {
			userSSN = cursor.getString(cursor.getColumnIndex(
					"ssn"));
		}
		cursor.close();
		return userSSN;
	}

	/**
	 * Método que verifica se o aparelho já está cadastrado. Caso não esteja, registra.
	 *
	 * @param uniqueId
	 *            Chave única para registro (caso ainda não tenha sido registrado).
	 * @return Chave única do aparelho.
	 */
	public String insertUniqueId(String uniqueId, UserBean user) {
		Cursor cursor = selectUserInformation();
		if (cursor.getCount() == 0) {
			insert(user, uniqueId);
		} else {
			uniqueId = cursor.getString(cursor.getColumnIndex(USER_UNIQUE_ID));
		}
		cursor.close();
		return uniqueId;
	}

	/**
	 * Método que atualiza a tabela de informações de usuário com o DDD informado (se foi informado).
	 *
	 * @param areaCode
	 *            DDD padrão inserido pelo usuário.
	 * @return DDD padrão inserido pelo usuário.
	 */
	public UserBean updateUser(UserBean user) {
		Cursor cursor = selectUserInformation();
		String uniqueId = cursor.getString(cursor.getColumnIndex(USER_UNIQUE_ID));
		cursor.close();

		ContentValues values = new ContentValues();
		values.put(USER_UNIQUE_ID, uniqueId);
		values.put("ssn", user.getSsn());
		values.put("FK_HAS_PROFILE", user.getUserProfileId());
		values.put("FK_HAS_GROUP", user.getGroupId());
		values.put("FK_PARTICIPATE_IN_CHAT", user.getChatId());
		values.put("FK_HAS_STUDENT", user.getStudentId());
		
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		db.update(USER_TABLE_NAME, values, null, null);

		return user;
	}

	/**
	 * Insert a USER at database
	 *
	 * @param uniqueId
	 *            Unique new user id.
	 */
    private static void insert(UserBean user, String uniqueId) {
		ContentValues values = new ContentValues();
		values.put(USER_UNIQUE_ID, uniqueId);
		values.put("ssn", user.getSsn());
		values.put("FK_HAS_PROFILE", user.getUserProfileId());
		values.put("FK_HAS_GROUP", user.getGroupId());
		values.put("FK_PARTICIPATE_IN_CHAT", user.getChatId());
		values.put("FK_HAS_STUDENT", user.getStudentId());
		
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		db.insertOrThrow(USER_TABLE_NAME, null, values);
	}
}
