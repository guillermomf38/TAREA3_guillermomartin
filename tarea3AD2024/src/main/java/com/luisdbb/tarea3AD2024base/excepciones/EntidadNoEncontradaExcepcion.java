/**
 *Clase EntidadNoEncontradaExcepcion.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.excepciones;

public class EntidadNoEncontradaExcepcion extends RuntimeException{

	public EntidadNoEncontradaExcepcion (String mensaje) {
		super(mensaje);
	}
}
