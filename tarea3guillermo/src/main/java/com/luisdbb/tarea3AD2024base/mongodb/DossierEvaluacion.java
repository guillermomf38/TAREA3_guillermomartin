/**
 *Clase DossierEvaluacion.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.mongodb;

public class DossierEvaluacion {

	private String fecha;
	private Long idPersonaRealizadaPor;
	private String rol;
	private String comentario;
	private String nivel;

	public DossierEvaluacion() {
	}

	public DossierEvaluacion(String fecha, Long idPersona, String rol,
			String comentario, String nivel) {
		this.fecha = fecha;
		this.idPersonaRealizadaPor = idPersona;
		this.rol = rol;
		this.comentario = comentario;
		this.nivel = nivel;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String f) {
		this.fecha = f;
	}

	public Long getIdPersonaRealizadaPor() {
		return idPersonaRealizadaPor;
	}

	public void setIdPersonaRealizadaPor(Long id) {
		this.idPersonaRealizadaPor = id;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String r) {
		this.rol = r;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String c) {
		this.comentario = c;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String n) {
		this.nivel = n;
	}
}
