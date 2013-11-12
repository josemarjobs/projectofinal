package com.example.projetofinal;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projetofinal.db.DataSource;
import com.example.projetofinal.model.Comodo;

public class NewComodoActivity extends Activity {
	private boolean edit = false;
	private DataSource source;
	private Comodo comodo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_comodo);
		source = new DataSource(getApplicationContext());

		if (getIntent().getExtras() != null) {
			long id = getIntent().getExtras().getLong("COMODO_ID");
			if (id != 0) {
				setTitle("Editanto Comodo");
				edit = true;
				Button b = (Button) findViewById(R.id.buttonSalvarComodo);
				b.setText("Atualizar");
				source.open();
				comodo = source.findComodo(id);
				source.close();
				EditText txtNome = (EditText) findViewById(R.id.editTextNomeComodo);
				EditText txtDescricao = (EditText) findViewById(R.id.editTextDescricaoComodo);
				txtNome.setText(comodo.getNome());
				txtDescricao.setText(comodo.getDescricao());
			}
		}

	}

	public void cancel(View v) {
		this.finish();
	}

	public void salvar(View v) {
		EditText txtNome = (EditText) findViewById(R.id.editTextNomeComodo);
		EditText txtDescricao = (EditText) findViewById(R.id.editTextDescricaoComodo);
		source.open();
		if (edit == true) {
			comodo.setNome(txtNome.getText().toString());
			comodo.setDescricao(txtDescricao.getText().toString());
			source.alterComodo(comodo);
			Toast.makeText(this, "Comodo alterado com sucesso",
					Toast.LENGTH_SHORT).show();
		} else {
			comodo = new Comodo(txtNome.getText().toString(), txtDescricao
					.getText().toString());
			source.createComodo(comodo);
			Toast.makeText(this, "Comodo adicionado com sucesso",
					Toast.LENGTH_SHORT).show();
		}
		source.close();
		this.finish();
	}
}
