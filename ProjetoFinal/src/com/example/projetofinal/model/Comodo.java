package com.example.projetofinal.model;

import java.util.ArrayList;
import java.util.List;

public class Comodo {

	private long id;
	private String nome;
	private String descricao;
	private List<Objeto> objetos = new ArrayList<Objeto>();

	public Comodo() {
	}

	public Comodo(String nome, String descricao) {
		this.nome = nome;
		this.setDescricao(descricao);
	}
	
	public boolean adicionaObjeto(Objeto objeto){
		return this.objetos.add(objeto);
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Objeto> getObjetos() {
		return objetos;
	}

	public void setObjetos(List<Objeto> objetos) {
		this.objetos = objetos;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
