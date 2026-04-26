/**
 *Clase CredencialesService.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package com.luisdbb.tarea3AD2024base.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luisdbb.tarea3AD2024base.excepciones.ValidacionExcepcion;
import com.luisdbb.tarea3AD2024base.modelo.Credenciales;
import com.luisdbb.tarea3AD2024base.modelo.Perfiles;
import com.luisdbb.tarea3AD2024base.repositorios.CredencialesRepository;

@Service
public class CredencialesService {

	@Autowired
	private CredencialesRepository credencialesRepository;

	@Autowired
	private SesionService sesionService;

	public void login(String nombre, String password) {

		if (nombre == null || nombre.isBlank()) 
		{
			throw new ValidacionExcepcion("El nombre de usuario no puede estar vacio");
		}
		if (password == null || password.isBlank()) 
		{
			throw new ValidacionExcepcion("La contrasena no puede estar vacia");
		}

		Credenciales credenciales = credencialesRepository.findByNombreAndPassword(nombre, password);

		if (credenciales == null) 
		{
			throw new ValidacionExcepcion("Usuario o contrasena incorrectos");
		}

		sesionService.iniciarSesion(credenciales);
	}

	public void logout() {
		sesionService.cerrarSesion();
	}

	public Credenciales crearCredenciales(String nombre, String password,Perfiles perfil) 
	{

		nombre = nombre.toLowerCase().trim();

		if (nombre.isBlank()) 
		{
			throw new ValidacionExcepcion("El nombre de usuario no puede estar vacio");
		}
		if (password == null || password.isBlank()) 
		{
			throw new ValidacionExcepcion("La contrasena no puede estar vacia");
		}
		if (nombre.contains(" ")) 
		{
			throw new ValidacionExcepcion("El nombre de usuario no puede contener espacios");
		}
		if (password.contains(" ")) 
		{
			throw new ValidacionExcepcion("La contrasena no puede contener espacios");
		}
		if (nombre.length() <= 2) 
		{
			throw new ValidacionExcepcion("El nombre de usuario debe tener mas de 2 caracteres");
		}
		if (password.length() <= 2) 
		{
			throw new ValidacionExcepcion("La contrasena debe tener mas de 2 caracteres");
		}
		if (!nombre.matches("[a-z]+")) 
		{
			throw new ValidacionExcepcion("El nombre solo puede contener letras sin tildes ni dieresis");
		}
		if (credencialesRepository.findByNombre(nombre) != null) 
		{
			throw new ValidacionExcepcion("Ese nombre de usuario ya existe");
		}

		return new Credenciales(nombre, password, perfil);
	}
}