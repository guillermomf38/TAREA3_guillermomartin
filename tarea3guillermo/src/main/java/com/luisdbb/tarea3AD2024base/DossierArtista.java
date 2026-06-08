/**
 *Clase DossierArtista.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;

@Document(collection = "dossiers")
public class DossierArtista {
	
	@Id
	private String id;

	private Long idArtista;
	private String nombre;
	private String nacionalidad;
	private String email;
	private List<String> especialidades = new ArrayList<>();
	private String apodo;

	private List<DossierEspectaculo> trayectoria = new ArrayList<>();
	private List<DossierEvaluacion> evaluaciones = new ArrayList<>();
	private List<DossierObservacion> observaciones = new ArrayList<>();

	public DossierArtista() {
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getIdArtista() {
		return idArtista;
	}

	public void setIdArtista(Long id) {
		this.idArtista = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String n) {
		this.nombre = n;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String n) {
		this.nacionalidad = n;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String e) {
		this.email = e;
	}

	public List<String> getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(List<String> e) {
		this.especialidades = e;
	}

	public String getApodo() {
		return apodo;
	}

	public void setApodo(String a) {
		this.apodo = a;
	}

	public List<DossierEspectaculo> getTrayectoria() {
		return trayectoria;
	}

	public void setTrayectoria(List<DossierEspectaculo> t) {
		this.trayectoria = t;
	}

	public List<DossierEvaluacion> getEvaluaciones() {
		return evaluaciones;
	}

	public void setEvaluaciones(List<DossierEvaluacion> e) {
		this.evaluaciones = e;
	}

	public List<DossierObservacion> getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(List<DossierObservacion> o) {
		this.observaciones = o;
	}
}
