/**
 *Clase DossierEspectaculo.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.mongodb;

import java.util.ArrayList;
import java.util.List;

public class DossierEspectaculo {
	private Long idEspectaculo;
	private String nombreEspectaculo;
	private List<DossierNumero> numeros = new ArrayList<>();

	public DossierEspectaculo() {
	}

	public DossierEspectaculo(Long idEspectaculo, String nombreEspectaculo) {
		this.idEspectaculo = idEspectaculo;
		this.nombreEspectaculo = nombreEspectaculo;
	}

	public Long getIdEspectaculo() {
		return idEspectaculo;
	}

	public void setIdEspectaculo(Long id) {
		this.idEspectaculo = id;
	}

	public String getNombreEspectaculo() {
		return nombreEspectaculo;
	}

	public void setNombreEspectaculo(String n) {
		this.nombreEspectaculo = n;
	}

	public List<DossierNumero> getNumeros() {
		return numeros;
	}

	public void setNumeros(List<DossierNumero> n) {
		this.numeros = n;
	}

}
