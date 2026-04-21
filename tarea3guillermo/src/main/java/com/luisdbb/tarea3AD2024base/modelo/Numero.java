/**
 *Clase Numero.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "numero")
public class Numero {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private int orden;

	@Column(nullable = false)
	private String nombre;

	@Column(nullable = false)
	private double duracion;

	@ManyToOne
	@JoinColumn(name = "id_espectaculo", nullable = false)
	private Espectaculo espectaculo;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "numero_artista", joinColumns = @JoinColumn(name = "id_numero"), inverseJoinColumns = @JoinColumn(name = "id_artista"))
	private List<Artista> artistas = new ArrayList<>();

	public Numero() {
	}

	public Numero(int orden, String nombre, double duracion,
			Espectaculo espectaculo) {
		this.orden = orden;
		this.nombre = nombre;
		this.duracion = duracion;
		this.espectaculo = espectaculo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getDuracion() {
		return duracion;
	}

	public void setDuracion(double duracion) {
		this.duracion = duracion;
	}

	public Espectaculo getEspectaculo() {
		return espectaculo;
	}

	public void setEspectaculo(Espectaculo espectaculo) {
		this.espectaculo = espectaculo;
	}

	public List<Artista> getArtistas() {
		return artistas;
	}

	public void setArtistas(List<Artista> artistas) {
		this.artistas = artistas;
	}

}