/**
 *Clase PersonaService.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package com.luisdbb.tarea3AD2024base.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luisdbb.tarea3AD2024base.excepciones.EntidadNoEncontradaExcepcion;
import com.luisdbb.tarea3AD2024base.excepciones.ValidacionExcepcion;
import com.luisdbb.tarea3AD2024base.modelo.Artista;
import com.luisdbb.tarea3AD2024base.modelo.Coordinacion;
import com.luisdbb.tarea3AD2024base.modelo.Credenciales;
import com.luisdbb.tarea3AD2024base.modelo.Especialidad;
import com.luisdbb.tarea3AD2024base.modelo.Perfiles;
import com.luisdbb.tarea3AD2024base.modelo.Persona;
import com.luisdbb.tarea3AD2024base.repositorios.ArtistaRepository;
import com.luisdbb.tarea3AD2024base.repositorios.CoordinacionRepository;
import com.luisdbb.tarea3AD2024base.repositorios.PersonaRepository;

@Service
public class PersonaService {
	@Autowired
	private PersonaRepository personaRepository;

	@Autowired
	private ArtistaRepository artistaRepository;

	@Autowired
	private CoordinacionRepository coordinacionRepository;

	@Autowired
	private CredencialesService credencialesService;

	@Autowired
	private NacionalidadService nacionalidadService;

	public void registrarArtista(String nombre, String email,
			String nacionalidad, String credNombre, String credPassword,
			String apodo, List<Especialidad> especialidades) {

		validarDatosPersonales(nombre, email, nacionalidad);

		if (especialidades == null || especialidades.isEmpty()) {
			throw new ValidacionExcepcion(
					"El artista debe tener al menos una especialidad");
		}

		Credenciales credenciales = credencialesService
				.crearCredenciales(credNombre, credPassword, Perfiles.ARTISTA);

		Artista artista = new Artista(email, nombre, nacionalidad, credenciales,
				apodo, especialidades);
		artistaRepository.save(artista);
	}

	public void registrarCoordinacion(String nombre, String email,
			String nacionalidad, String credNombre, String credPassword,
			boolean senior, LocalDate fechasenior) {

		validarDatosPersonales(nombre, email, nacionalidad);

		if (senior && fechasenior == null) {
			throw new ValidacionExcepcion(
					"Si es senior debe indicar la fecha desde cuando");
		}

		Credenciales credenciales = credencialesService.crearCredenciales(
				credNombre, credPassword, Perfiles.COORDINACION);

		Coordinacion coordinacion = new Coordinacion(email, nombre,
				nacionalidad, credenciales, senior,
				senior ? fechasenior : null);
		coordinacionRepository.save(coordinacion);
	}

	public void modificarDatosPersonales(Long id, String nombre, String email,
			String nacionalidad) {
		Persona persona = personaRepository.findById(id)
				.orElseThrow(() -> new EntidadNoEncontradaExcepcion(
						"Persona no encontrada"));

		if (!persona.getEmail().equals(email)
				&& personaRepository.findByEmail(email) != null) {
			throw new ValidacionExcepcion("Ese email ya esta registrado");
		}

		persona.setNombre(nombre);
		persona.setEmail(email);
		persona.setNacionalidad(nacionalidad);
		personaRepository.save(persona);
	}

	public void modificarArtista(Long id, String apodo,
			List<Especialidad> especialidades) {
		Artista artista = artistaRepository.findById(id)
				.orElseThrow(() -> new EntidadNoEncontradaExcepcion(
						"Artista no encontrado"));

		if (especialidades == null || especialidades.isEmpty()) {
			throw new ValidacionExcepcion(
					"El artista debe tener al menos una especialidad");
		}

		artista.setApodo(apodo);
		artista.setEspecialidades(especialidades);
		artistaRepository.save(artista);
	}

	public void modificarCoordinacion(Long id, boolean senior,
			LocalDate fechasenior) {
		Coordinacion coordinacion = coordinacionRepository.findById(id)
				.orElseThrow(() -> new EntidadNoEncontradaExcepcion(
						"Coordinacion no encontrada"));

		if (senior && fechasenior == null) {
			throw new ValidacionExcepcion(
					"Si es senior debe indicar la fecha desde cuando");
		}

		coordinacion.setSenior(senior);
		coordinacion.setFechasenior(senior ? fechasenior : null);
		coordinacionRepository.save(coordinacion);
	}

	public Artista verFichaArtista(Long id) {
		return artistaRepository.findById(id)
				.orElseThrow(() -> new EntidadNoEncontradaExcepcion(
						"Artista no encontrado"));
	}
	public Artista buscarArtistaPorUsuario(String nombreUsuario) {
	    return artistaRepository.findByCredenciales_Nombre(nombreUsuario);
	}

	public List<Persona> listarPersonas() {
	    return personaRepository.findByCredenciales_PerfilNot(Perfiles.ADMIN);
	}

	public List<Artista> listarArtistas() {
		return artistaRepository.findAll();
	}

	public List<Coordinacion> listarCoordinaciones() {
		return coordinacionRepository.findAll();
	}

	private void validarDatosPersonales(String nombre, String email,
			String nacionalidad) {
		if (nombre == null || nombre.isBlank()) {
			throw new ValidacionExcepcion("El nombre no puede estar vacio");
		}
		if (email == null || email.isBlank()) {
			throw new ValidacionExcepcion("El email no puede estar vacio");
		}
		if (nacionalidad == null || nacionalidad.isBlank()) {
			throw new ValidacionExcepcion(
					"La nacionalidad no puede estar vacia");
		}
		if (personaRepository.findByEmail(email) != null) {
			throw new ValidacionExcepcion("Ese email ya esta registrado");
		}
		if (!nacionalidadService.esValida(nacionalidad)) {
			throw new ValidacionExcepcion("La nacionalidad no es valida");
		}
	}
}
