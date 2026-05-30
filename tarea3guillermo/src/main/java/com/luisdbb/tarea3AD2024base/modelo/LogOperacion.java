/**
 *Clase LogOperacion.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package com.luisdbb.tarea3AD2024base.modelo;


import java.io.Serializable;

public class LogOperacion implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private String fechaHora;
	private String usuario;
	private String tipoOperacion;
	private String resumen;

	public LogOperacion() {

	}

	public LogOperacion(Long id, String fechaHora, String usuario,
			String tipoOperacion, String resumen) {
		super();
		this.id = id;
		this.fechaHora = fechaHora;
		this.usuario = usuario;
		this.tipoOperacion = tipoOperacion;
		this.resumen = resumen;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

}
