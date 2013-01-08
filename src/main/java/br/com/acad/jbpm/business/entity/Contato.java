package br.com.acad.jbpm.business.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

//@Entity 
public class Contato {

	@Id
	@GeneratedValue
	private Integer id;
	
	private String nome;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	
}
