/**
 *Clase Artista.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "artista")
public class Artista extends Persona {

	private String apodo;

	@ElementCollection
	@Enumerated(EnumType.STRING)
	private List<Especialidad> especialidades = new ArrayList<>();

	@ManyToMany(mappedBy = "artistas")
	private List<Numero> numeros = new ArrayList<>();

	public Artista() {
	}

	public Artista(String email, String nombre, String nacionalidad,
			Credenciales credenciales, String apodo,
			List<Especialidad> especialidades) {
		super(email, nombre, nacionalidad, credenciales);
		this.apodo = apodo;
		this.especialidades = especialidades;
	}

	public String getApodo() {
		return apodo;
	}

	public void setApodo(String apodo) {
		this.apodo = apodo;
	}

	public List<Especialidad> getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(List<Especialidad> especialidades) {
		this.especialidades = especialidades;
	}

	public List<Numero> getNumeros() {
		return numeros;
	}

	public void setNumeros(List<Numero> numeros) {
		this.numeros = numeros;
	}

}
