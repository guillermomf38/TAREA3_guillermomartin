/**
 *Clase Incidencia.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Incidencia implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private LocalDateTime fechaHora;

	@Enumerated(EnumType.STRING)
	private TipoIncidencia tipo;

	@Column(length = 1000)
	private String descripcion;

	private boolean resuelta = false;

	private Long idPersonaReporta;
	private Long idEspectaculo;
	private Long idNumero;

	public Incidencia() {
	}

	public Incidencia(TipoIncidencia tipo, String descripcion,
			Long idPersonaReporta, Long idEspectaculo, Long idNumero) {
		this.fechaHora = LocalDateTime.now();
		this.tipo = tipo;
		this.descripcion = descripcion;
		this.resuelta = false;
		this.idPersonaReporta = idPersonaReporta;
		this.idEspectaculo = idEspectaculo;
		this.idNumero = idNumero;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(LocalDateTime fecha) {
		this.fechaHora = fecha;
	}

	public TipoIncidencia getTipo() {
		return tipo;
	}

	public void setTipo(TipoIncidencia tipo) {
		this.tipo = tipo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descrip) {
		this.descripcion = descrip;
	}

	public boolean isResuelta() {
		return resuelta;
	}

	public void setResuelta(boolean resuelta) {
		this.resuelta = resuelta;
	}

	public Long getIdPersonaReporta() {
		return idPersonaReporta;
	}

	public void setIdPersonaReporta(Long id) {
		this.idPersonaReporta = id;
	}

	public Long getIdEspectaculo() {
		return idEspectaculo;
	}

	public void setIdEspectaculo(Long id) {
		this.idEspectaculo = id;
	}

	public Long getIdNumero() {
		return idNumero;
	}

	public void setIdNumero(Long id) {
		this.idNumero = id;
	}
}
