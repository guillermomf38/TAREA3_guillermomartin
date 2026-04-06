/**
 *Clase Coordinacion.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "coordinacion")
public class Coordinacion extends Persona {

	@Column(nullable = false)
	private boolean senior = false;

	private LocalDate fechasenior;

	@OneToMany(mappedBy = "coordinador")
	private List<Espectaculo> espectaculosdirige = new ArrayList<>();

	public Coordinacion() {
	}

	public Coordinacion(String email, String nombre, String nacionalidad,
			Credenciales credenciales, boolean senior, LocalDate fechasenior) {
		super(email, nombre, nacionalidad, credenciales);
		this.senior = senior;
		this.fechasenior = fechasenior;
	}

	public boolean isSenior() {
		return senior;
	}

	public void setSenior(boolean senior) {
		this.senior = senior;
	}

	public LocalDate getFechasenior() {
		return fechasenior;
	}

	public void setFechasenior(LocalDate fechasenior) {
		this.fechasenior = fechasenior;
	}

	public List<Espectaculo> getEspectaculosdirige() {
		return espectaculosdirige;
	}

	public void setEspectaculosdirige(List<Espectaculo> espectaculosdirige) {
		this.espectaculosdirige = espectaculosdirige;
	}

}