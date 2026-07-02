/**
 *Clase DossierObservacion.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.mongodb;

public class DossierObservacion {
	private String fecha;
	private String texto;
	private String autor;

	public DossierObservacion() {
	}

	public DossierObservacion(String fecha, String texto, String autor) {
		this.fecha = fecha;
		this.texto = texto;
		this.autor = autor;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String f) {
		this.fecha = f;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String t) {
		this.texto = t;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String a) {
		this.autor = a;
	}
}
