/**
 *Clase ValidacionExcepcion.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.excepciones;

public class ValidacionExcepcion extends RuntimeException {
	public ValidacionExcepcion(String mensaje) {
        super(mensaje);
    }
}
