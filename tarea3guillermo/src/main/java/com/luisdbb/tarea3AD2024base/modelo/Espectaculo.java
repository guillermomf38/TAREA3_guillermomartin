/**
 *Clase Espectaculo.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "espectaculo")
public class Espectaculo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false, length = 25)
	private String nombre;

	@Column(nullable = false)
	private LocalDate fechaini;

	@Column(nullable = false)
	private LocalDate fechafin;

	@ManyToOne
	@JoinColumn(name = "id_coord", nullable = false)
	private Coordinacion coordinador;

	@OneToMany(mappedBy = "espectaculo", cascade = CascadeType.ALL,  orphanRemoval = true, fetch = FetchType.EAGER)
	@OrderBy("orden ASC")
	private List<Numero> numeros = new ArrayList<>();

	public Espectaculo() {
	}

	public Espectaculo(String nombre, LocalDate fechaini, LocalDate fechafin,
			Coordinacion coordinador) {
		this.nombre = nombre;
		this.fechaini = fechaini;
		this.fechafin = fechafin;
		this.coordinador = coordinador;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFechaini() {
		return fechaini;
	}

	public void setFechaini(LocalDate fechaini) {
		this.fechaini = fechaini;
	}

	public LocalDate getFechafin() {
		return fechafin;
	}

	public void setFechafin(LocalDate fechafin) {
		this.fechafin = fechafin;
	}

	public Coordinacion getCoordinador() {
		return coordinador;
	}

	public void setCoordinador(Coordinacion coordinador) {
		this.coordinador = coordinador;
	}

	public List<Numero> getNumeros() {
		return numeros;
	}

	public void setNumeros(List<Numero> numeros) {
		this.numeros = numeros;
	}

}