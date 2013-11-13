package com.example.projetofinal;

import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetofinal.db.DataSource;
import com.example.projetofinal.model.Comodo;
import com.example.projetofinal.model.Objeto;

public class ComodoActivity extends MyActivity {
	private DataSource source;
	private Comodo comodo;
	private LayoutInflater inflater;
	private ListView listaDeObjetos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comodo);
		inflater = getLayoutInflater();

		listaDeObjetos = (ListView) findViewById(R.id.listViewObjetos);

		source = new DataSource(getApplicationContext());
		comodo = new Comodo();
		comodo.setId(getIntent().getExtras().getLong("COMODO_ID"));

		setUpObjetosList();

		registerForContextMenu(listaDeObjetos);

		TextView tv = (TextView) findViewById(R.id.tituloComodo);
		tv.setText(comodo.getNome());

	}

	public void switchEstado(View view) {

		Button btn = (Button) view;

		long id = btn.getId();

		// open the datasource
		source.open();
		// pega todos os dados do objeto
		Objeto o = source.findObjeto(id);
		o.setComodoId(comodo);

		System.out.println("Object in switch: " + o.getId() + ", "
				+ o.getCodigo());

		sendData(o.getCodigo());

		if ("OFF".equals(btn.getText())) {
			o.setEstado("DESLIGADO");
			btn.setText("ON");
			btn.setBackgroundColor(Color.CYAN);
			Toast.makeText(this, "DESLIGANDO, Codigo: " + o.getCodigo(), 3000)
					.show();
		} else {
			o.setEstado("LIGADO");
			btn.setText("OFF");
			btn.setBackgroundColor(Color.RED);
			Toast.makeText(this, "LIGANDO, Codigo: " + o.getCodigo(), 3000)
					.show();
		}

		source.alterObjeto(o);
		source.close();

		System.out
				.println("POS: " + o.getNome() + ", Estado: " + o.getEstado());
	}

	private void setUpObjetosList() {
		source.open();
		comodo = source.findComodoAndObjetos(comodo.getId());
		source.close();

		ArrayAdapter<Objeto> adapter = new ObjetosArrayAdapter(this,
				comodo.getObjetos());

		listaDeObjetos.setAdapter(adapter);
	}

	@Override
	public void onResume() {
		setUpObjetosList();
		super.onResume();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		getMenuInflater().inflate(R.menu.comodo_context_menu, menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.comodo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_add) {
			Intent intent = new Intent(this, NewObjeto.class);
			intent.putExtra("COMODO_ID", comodo.getId());
			startActivity(intent);
		}
		return true;
	}

	/**
	 * Adapter para a lista de objetos pertencentes ao comodo
	 */
	private class ObjetosArrayAdapter extends ArrayAdapter<Objeto> {

		private List<Objeto> objetos;

		public ObjetosArrayAdapter(Context context, List<Objeto> objetos) {
			super(context, R.id.textNomeObjeto, objetos);
			this.objetos = objetos;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = inflater
					.inflate(R.layout.list_row_objeto, parent, false);

			final Objeto o = objetos.get(position);

			TextView tv = (TextView) row.findViewById(R.id.textNomeObjeto);
			//
			// tv.setOnLongClickListener(new OnLongClickListener() {
			//
			// @Override
			// public boolean onLongClick(View v) {
			//
			// Builder builder = new Builder(getApplicationContext());
			// builder.setTitle("Remover Objeto");
			// builder.setMessage("Tem certeza que deseja remover o objeto?");
			// builder.setPositiveButton("Sim", new OnClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface arg0, int arg1) {
			// DataSource source = new DataSource(
			// getApplicationContext());
			// source.open();
			// source.deleteObjeto(o);
			// source.close();
			// setUpObjetosList();
			//
			// }
			// });
			//
			// builder.setNegativeButton("Nao", new OnClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// dialog.dismiss();
			// }
			// });
			// builder.create().show();
			// return true;
			// }
			// });

			Button btn = (Button) row.findViewById(R.id.btnSwitch);

			tv.setText(o.getNome());

			btn.setId((int) o.getId());

			if ("LIGADO".equals(o.getEstado())) {
				btn.setText("OFF");
				btn.setBackgroundColor(Color.RED);
			} else {
				btn.setText("ON");
				btn.setBackgroundColor(Color.CYAN);
			}
			return row;
		}
	}
}
