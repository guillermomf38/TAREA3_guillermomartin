/**
 *Clase SesionService.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.services;

import org.springframework.stereotype.Service;

import com.luisdbb.tarea3AD2024base.modelo.Credenciales;
import com.luisdbb.tarea3AD2024base.modelo.Perfiles;

@Service
public class SesionService {

	private Credenciales usuarioActual = null;

	public void iniciarSesion(Credenciales credenciales) {
		this.usuarioActual = credenciales;
	}

	public void cerrarSesion() {
		this.usuarioActual = null;

	}

	public Credenciales getUsuarioActual() {
		return usuarioActual;
	}

	public boolean isInvitado() {
		return usuarioActual == null;
	}

	public boolean isAdmin() {
		return usuarioActual != null
				&& usuarioActual.getPerfil() == Perfiles.ADMIN;
	}

	public boolean isCoordinacion() {
		return usuarioActual != null
				&& usuarioActual.getPerfil() == Perfiles.COORDINACION;
	}

	public boolean isArtista() {
		return usuarioActual != null
				&& usuarioActual.getPerfil() == Perfiles.ARTISTA;
	}
}
