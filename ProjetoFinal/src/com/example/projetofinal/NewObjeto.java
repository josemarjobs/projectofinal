package com.example.projetofinal;

import com.example.projetofinal.db.DataSource;
import com.example.projetofinal.model.Comodo;
import com.example.projetofinal.model.Objeto;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewObjeto extends Activity {
	private long comodoId;
	DataSource source;

	private EditText tvNome, tvCodigo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_objeto);
		source = new DataSource(this);
		comodoId = getIntent().getExtras().getLong("COMODO_ID");
		tvNome = (EditText) findViewById(R.id.editTextNomeObjeto);
		tvCodigo = (EditText) findViewById(R.id.editTextCodigoObjeto);
	}

	public void salvar(View view) {

		Objeto objeto = new Objeto(tvNome.getText().toString(), "DESLIGADO");
		objeto.setCodigo(tvCodigo.getText().toString());
		Comodo c = new Comodo();
		c.setId(comodoId);
		objeto.setComodoId(c);
		source.open();

		objeto = source.createObjeto(objeto);
		Toast.makeText(this,
				"Objeto: " + objeto.getId() + ", adicionado com sucesso",
				Toast.LENGTH_SHORT).show();

		source.close();

		this.finish();
	}

	public void cancel(View view) {
		this.finish();
	}
}
