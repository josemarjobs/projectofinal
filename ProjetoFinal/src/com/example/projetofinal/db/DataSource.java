package com.example.projetofinal.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.projetofinal.model.Comodo;
import com.example.projetofinal.model.Objeto;

public class DataSource {

	public static final String LOGTAG = "PROJETOFINAL";

	private Context context;
	private DBOpenHelper dbOpenHelper;
	private SQLiteDatabase database;

	private static final String[] comodosColumns = {
			DBOpenHelper.COLUMN_COMODOS_ID, DBOpenHelper.COLUMN_COMODOS_NOME,
			DBOpenHelper.COLUMN_COMODOS_DESC };

	private static final String[] objetosColumns = {
			DBOpenHelper.COLUMN_OBJETOS_ID, DBOpenHelper.COLUMN_OBJETOS_NOME,
			DBOpenHelper.COLUMN_OBJETOS_ESTADO,
			DBOpenHelper.COLUMN_OBJETOS_COMODO_ID };

	public DataSource(Context context) {
		this.context = context;
		this.dbOpenHelper = new DBOpenHelper(context);
	}

	public void open() {
		Log.i(LOGTAG, "Database opened");
		this.database = this.dbOpenHelper.getWritableDatabase();
	}

	public void close() {
		Log.i(LOGTAG, "Database closed");
		this.database.close();
	}

	public Objeto createObjeto(Objeto objeto) {
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.COLUMN_OBJETOS_NOME, objeto.getNome());
		values.put(DBOpenHelper.COLUMN_OBJETOS_ESTADO, objeto.getEstado());
		values.put(DBOpenHelper.COLUMN_OBJETOS_COMODO_ID, objeto.getComodo()
				.getId());

		long inserted = this.database.insert(DBOpenHelper.TABLE_OBJETOS, null,
				values);
		objeto.setId(inserted);
		return objeto;
	}

	public Comodo createComodo(Comodo comodo) {
		ContentValues values = createComodoValues(comodo);

		long insertedId = this.database.insert(DBOpenHelper.TABLE_COMODOS,
				null, values);
		comodo.setId(insertedId);
		return comodo;
	}

	public boolean deleteComodo(Comodo comodo) {
		String where = DBOpenHelper.COLUMN_COMODOS_ID + "=" + comodo.getId();
		int result = database.delete(DBOpenHelper.TABLE_COMODOS, where, null);
		return (result == 1);
	}

	public boolean alterComodo(Comodo comodo) {
		String where = DBOpenHelper.COLUMN_COMODOS_ID + "=" + comodo.getId();
		ContentValues values = createComodoValues(comodo);

		int updated = database.update(DBOpenHelper.TABLE_COMODOS, values,
				where, null);
		return (updated == 1);
	}

	public boolean alterObjeto(Objeto objeto) {
		String where = DBOpenHelper.COLUMN_OBJETOS_ID + "=" + objeto.getId();

		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.COLUMN_OBJETOS_NOME, objeto.getNome());
		values.put(DBOpenHelper.COLUMN_OBJETOS_ESTADO, objeto.getEstado());
		values.put(DBOpenHelper.COLUMN_OBJETOS_COMODO_ID, objeto.getComodo()
				.getId());

		int updated = database.update(DBOpenHelper.TABLE_OBJETOS, values,
				where, null);
		return (updated == 1);
	}

	private ContentValues createComodoValues(Comodo comodo) {
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.COLUMN_COMODOS_NOME, comodo.getNome());
		values.put(DBOpenHelper.COLUMN_COMODOS_DESC, comodo.getDescricao());
		return values;
	}

	public Comodo findComodo(long id) {
		List<Comodo> comodos = findAllComodos();
		for (Comodo comodo : comodos) {
			if (comodo.getId() == id) {
				return comodo;
			}
		}
		return null;
	}

	public List<Comodo> findAllComodos() {
		Cursor cursor = database.query(DBOpenHelper.TABLE_COMODOS,
				comodosColumns, null, null, null, null, null);
		return cursorToComodosList(cursor);
	}

	public Comodo findComodoAndObjetos(long id) {
		String query = "SELECT " + DBOpenHelper.TABLE_OBJETOS + ".* FROM "
				+ DBOpenHelper.TABLE_OBJETOS + " WHERE "
				+ DBOpenHelper.COLUMN_OBJETOS_COMODO_ID + " = " + id;

		Comodo comodo = this.findComodo(id);
		// Cursor cursor = database.query(DBOpenHelper.TABLE_OBJETOS,
		// objetosColumns, null, null, null, null, null);

		Cursor cursor = this.database.rawQuery(query, null);

		List<Objeto> objetos = new ArrayList<Objeto>();
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Objeto o = cursorToObjeto(cursor);
				o.setComodoId(comodo);
				objetos.add(o);
			}
		}
		comodo.setObjetos(objetos);
		return comodo;
	}

	private List<Comodo> cursorToComodosList(Cursor c) {
		List<Comodo> comodos = new ArrayList<Comodo>();

		if (c.getCount() > 0) {
			while (c.moveToNext()) {
				Comodo comodo = cursorToComodo(c);
				comodos.add(comodo);
			}
		}
		return comodos;
	}

	private Objeto cursorToObjeto(Cursor cursor) {
		Objeto o = new Objeto();
		o.setId(cursor.getLong(cursor
				.getColumnIndex(DBOpenHelper.COLUMN_OBJETOS_ID)));
		o.setNome(cursor.getString(cursor
				.getColumnIndex(DBOpenHelper.COLUMN_OBJETOS_NOME)));
		o.setEstado(cursor.getString(cursor
				.getColumnIndex(DBOpenHelper.COLUMN_OBJETOS_ESTADO)));
		return o;
	}

	private Comodo cursorToComodo(Cursor c) {
		Comodo comodo = new Comodo();
		comodo.setId(c.getLong(c.getColumnIndex(DBOpenHelper.COLUMN_COMODOS_ID)));
		comodo.setNome(c.getString(c
				.getColumnIndex(DBOpenHelper.COLUMN_COMODOS_NOME)));
		comodo.setDescricao(c.getString(c
				.getColumnIndex(DBOpenHelper.COLUMN_COMODOS_DESC)));
		return comodo;
	}

}
