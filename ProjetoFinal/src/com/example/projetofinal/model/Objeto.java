package com.example.projetofinal.model;

public class Objeto {

	private long id;
	private String codigo;
	private String nome;
	private String estado;
	private Comodo comodo;

	public Objeto() {
	}

	public Objeto(long id, String codigo, String nome, String estado, Comodo comodo) {
		this( nome, estado);
		this.codigo = codigo;
		this.id = id;
		this.comodo = comodo;
	}

	public Objeto( String nome, String estado) {
		this.nome = nome;
		this.estado = estado;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Comodo getComodo() {
		return comodo;
	}

	public void setComodoId(Comodo comodo) {
		this.comodo = comodo;
	}

	public long getId() {
		return id;
	}

	public void setId(long inserted) {
		this.id = inserted;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Objeto) {
			Objeto oo = (Objeto) o;
			return this.getId() == oo.getId();
		}
		return false;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

}
