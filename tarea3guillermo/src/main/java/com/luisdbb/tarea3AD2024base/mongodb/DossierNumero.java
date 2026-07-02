/**
 *Clase DossierNumero.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.mongodb;

public class DossierNumero {
    private Long idNumero;
    private String nombreNumero;
    
	public DossierNumero() {
	
	}

	public DossierNumero(Long idNumero, String nombreNumero) {
	
		this.idNumero = idNumero;
		this.nombreNumero = nombreNumero;
	}

	public Long getIdNumero() {
		return idNumero;
	}

	public void setIdNumero(Long idNumero) {
		this.idNumero = idNumero;
	}

	public String getNombreNumero() {
		return nombreNumero;
	}

	public void setNombreNumero(String nombreNumero) {
		this.nombreNumero = nombreNumero;
	}
    

}
