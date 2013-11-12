package com.example.projetofinal.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {

	private static final String LOGTAG = "PROJETOFINAL";

	private static final String DATABASE_NAME = "projetofinal.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_COMODOS = "comodos";
	public static final String COLUMN_COMODOS_ID = "comodoId";
	public static final String COLUMN_COMODOS_NOME = "nome";
	public static final String COLUMN_COMODOS_DESC = "descricao";

	public static final String TABLE_OBJETOS = "objetos";
	public static final String COLUMN_OBJETOS_ID = "objetoId";
	public static final String COLUMN_OBJETOS_NOME = "nome";
	public static final String COLUMN_OBJETOS_ESTADO = "estado";
	public static final String COLUMN_OBJETOS_COMODO_ID = "comodoID";

	private static final String TABLE_COMODOS_CREATE = "CREATE TABLE "
			+ TABLE_COMODOS + " (" + COLUMN_COMODOS_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_COMODOS_NOME
			+ " STRING, " + COLUMN_COMODOS_DESC + " TEXT )";

	private static final String TABLE_OBJETOS_CREATE = "CREATE TABLE "
			+ TABLE_OBJETOS + " (" + COLUMN_OBJETOS_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_OBJETOS_NOME
			+ " STRING, " + COLUMN_OBJETOS_ESTADO + " STRING, "
			+ COLUMN_OBJETOS_COMODO_ID + " INTEGER )";

	public DBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_COMODOS_CREATE);
		db.execSQL(TABLE_OBJETOS_CREATE);

		Log.i(LOGTAG, "Table has been created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMODOS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBJETOS);
		
		onCreate(db);

		Log.i(LOGTAG, "Database has been upgraded from " + 
				oldVersion + " to " + newVersion);
	}

}
