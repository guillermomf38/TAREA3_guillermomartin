/**
 *Clase ResolucionIndicencia.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;


public class ResolucionIncidencia implements Serializable {
	
	private Long id;

	private LocalDateTime fechahoraResolucion;
	private String accionesRealizadas;
	private Long idPersonaResuelve;

	private Long idIncidencia;

	public ResolucionIncidencia() {
	}

	public ResolucionIncidencia(String accionesRealizadas,
			Long idPersonaResuelve, Long idIncidencia) {
		this.fechahoraResolucion = LocalDateTime.now();
		this.accionesRealizadas = accionesRealizadas;
		this.idPersonaResuelve = idPersonaResuelve;
		this.idIncidencia = idIncidencia;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getFechahoraResolucion() {
		return fechahoraResolucion;
	}

	public String getAccionesRealizadas() {
		return accionesRealizadas;
	}

	public void setAccionesRealizadas(String acciones) {
		this.accionesRealizadas = acciones;
	}

	public Long getIdPersonaResuelve() {
		return idPersonaResuelve;
	}

	public void setIdPersonaResuelve(Long id) {
		this.idPersonaResuelve = id;
	}

	public Long getIdIncidencia() {
		return idIncidencia;
	}

	public void setIdIncidencia(Long id) {
		this.idIncidencia = id;
	}

}
