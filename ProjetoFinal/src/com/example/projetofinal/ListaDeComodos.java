package com.example.projetofinal;

import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetofinal.db.DataSource;
import com.example.projetofinal.model.Comodo;

public class ListaDeComodos extends BaseActivity {
	private final String LOGTAG = "PROJETOFINAL";

	private LayoutInflater inflater;
	private ListView listaDeComodos;
	private List<Comodo> comodos;
	private DataSource source;

	private Comodo currentComodo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comodos);

		source = new DataSource(getApplicationContext());

		inflater = getLayoutInflater();

		listaDeComodos = (ListView) findViewById(R.id.listViewComodos);

		setUpComodosList();
		registerForContextMenu(listaDeComodos);
		listaDeComodos.setOnItemClickListener(new ItemClickHandler());
		listaDeComodos.setOnItemLongClickListener(new ItemLongClick());
	}

	private void setUpComodosList() {
		source.open();
		comodos = source.findAllComodos();
		source.close();

		ArrayAdapter<Comodo> adapter = new ComodosArrayAdapter(this, comodos);

		listaDeComodos.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comodos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_add) {
			Intent intent = new Intent(this, NewComodoActivity.class);
			startActivity(intent);
		}
		if (item.getItemId() == R.id.action_disconnect) {
			disconnectDevice();
		}
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.comodos_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.action_edit) {
			Intent i = new Intent(this, NewComodoActivity.class);
			i.putExtra("COMODO_ID", currentComodo.getId());
			startActivity(i);
		}
		if (item.getItemId() == R.id.action_delete) {

			Builder builder = new Builder(this);
			builder.setMessage("Tem certeza que deseja eliminar o(a) "
					+ currentComodo.getNome() + "?");
			builder.setCancelable(true);
			builder.setTitle("Eliminar Comodo");
			builder.setPositiveButton("Sim", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(getApplicationContext(),
							"Deleting Item: " + currentComodo.getNome(),
							Toast.LENGTH_SHORT).show();

					source.open();
					source.deleteComodo(currentComodo);
					source.close();

					setUpComodosList();
					dialog.dismiss();
				}
			});
			builder.setNegativeButton("Nao", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		}

		return true;
	}

	@Override
	protected void onResume() {
		Log.i(LOGTAG, "Lista de comodos sendo visualisadas de novo");
		setUpComodosList();
		super.onResume();
	}

	/**
	 * 
	 * @author josemarmagalhaes Adapter para os comodos da casa
	 */
	private class ComodosArrayAdapter extends ArrayAdapter<Comodo> {
		private List<Comodo> comodos;

		public ComodosArrayAdapter(Context context, List<Comodo> comodos) {
			super(context, R.id.textViewComodo, comodos);
			this.comodos = comodos;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = inflater
					.inflate(R.layout.list_row_comodo, parent, false);

			TextView tvNome = (TextView) row.findViewById(R.id.textViewComodo);
			tvNome.setText(comodos.get(position).getNome());
			TextView tvDescricao = (TextView) row
					.findViewById(R.id.textViewDescricaoComodo);
			tvDescricao.setText(comodos.get(position).getDescricao());

			return row;
		}
	}

	private class ItemClickHandler implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long arg3) {
			Comodo comodo = comodos.get(position);
			Toast.makeText(getApplicationContext(),
					"Nome: " + comodo.getNome(), Toast.LENGTH_SHORT).show();
			Intent i = new Intent(getApplicationContext(), ComodoActivity.class);
			i.putExtra("COMODO_ID", comodo.getId());
			startActivity(i);
		}
	}

	private class ItemLongClick implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> adapter, View view,
				int position, long arg3) {
			currentComodo = comodos.get(position);
			return false;
		}

	}
}
