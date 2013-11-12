package com.example.projetofinal;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetofinal.db.DataSource;
import com.example.projetofinal.model.Comodo;
import com.example.projetofinal.model.Objeto;

public class ComodoActivity extends BaseActivity {
	private DataSource source;
	private Comodo comodo;
	private LayoutInflater inflater;
	private ListView listaDeObjetos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comodo);
		inflater = getLayoutInflater();

		listaDeObjetos = (ListView) findViewById(R.id.listViewObjetos);

		source = new DataSource(getApplicationContext());
		comodo = new Comodo();
		comodo.setId(getIntent().getExtras().getLong("COMODO_ID"));

		setUpObjetosList();

		TextView tv = (TextView) findViewById(R.id.tituloComodo);
		tv.setText(comodo.getNome());

		// connect(false);

		Toast.makeText(this, "Socket: " + selectedDevice, Toast.LENGTH_LONG)
				.show();
	}

	public void switchEstado(View view) {
		
		Switch switch1 = (Switch) view;
		long id = switch1.getId();

		Objeto o = new Objeto();
		o.setId(id);

		int pos = this.comodo.getObjetos().indexOf(o);
		o = this.comodo.getObjetos().get(pos);
		if ("DESLIGADO".equals(o.getEstado())) {
			o.setEstado("LIGADO");
		} else {
			o.setEstado("DESLIGADO");
		}

		source.open();
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
	protected void onResume() {
		setUpObjetosList();
		super.onResume();
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
			super(context, R.id.switchObjeto, objetos);
			this.objetos = objetos;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = inflater
					.inflate(R.layout.list_row_objeto, parent, false);
			Switch switch1 = (Switch) row.findViewById(R.id.switchObjeto);
			Objeto o = objetos.get(position);
			switch1.setText(o.getNome());
			switch1.setId((int) o.getId());
			switch1.setActivated(false);
			if ("LIGADO".equals(o.getEstado())) {
				switch1.toggle();
			}
			return row;
		}
	}
}
