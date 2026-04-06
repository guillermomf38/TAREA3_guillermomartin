/**
 *Clase EspectaculoService.java
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
import com.luisdbb.tarea3AD2024base.modelo.Espectaculo;
import com.luisdbb.tarea3AD2024base.modelo.Numero;
import com.luisdbb.tarea3AD2024base.repositorios.ArtistaRepository;
import com.luisdbb.tarea3AD2024base.repositorios.CoordinacionRepository;
import com.luisdbb.tarea3AD2024base.repositorios.EspectaculoRepository;
import com.luisdbb.tarea3AD2024base.repositorios.NumeroRepository;

@Service
public class EspectaculoService {

	@Autowired
	private EspectaculoRepository espectaculoRepository;

	@Autowired
	private NumeroRepository numeroRepository;

	@Autowired
	private CoordinacionRepository coordinacionRepository;

	@Autowired
	private ArtistaRepository artistaRepository;

	public List<Espectaculo> listarEspectaculos() {
		return espectaculoRepository.findAllByOrderByFechainiAsc();
	}

	public Espectaculo verEspectaculoCompleto(Long id) {
		return espectaculoRepository.findById(id)
				.orElseThrow(() -> new EntidadNoEncontradaExcepcion(
						"Espectaculo no encontrado"));
	}

	public Espectaculo crearEspectaculo(String nombre, LocalDate fechaini,
			LocalDate fechafin, Coordinacion coordinador) {
		validarEspectaculo(nombre, fechaini, fechafin, null);
		Espectaculo espectaculo = new Espectaculo(nombre, fechaini, fechafin,
				coordinador);
		return espectaculoRepository.save(espectaculo);
	}

	public Espectaculo modificarEspectaculo(Long id, String nombre,
			LocalDate fechaini, LocalDate fechafin, Coordinacion coordinador) {
		Espectaculo espectaculo = espectaculoRepository.findById(id)
				.orElseThrow(() -> new EntidadNoEncontradaExcepcion(
						"Espectaculo no encontrado"));

		validarEspectaculo(nombre, fechaini, fechafin, id);
		espectaculo.setNombre(nombre);
		espectaculo.setFechaini(fechaini);
		espectaculo.setFechafin(fechafin);
		espectaculo.setCoordinador(coordinador);
		return espectaculoRepository.save(espectaculo);
	}

	public Numero crearNumero(String nombre, double duracion,
			Espectaculo espectaculo) {
		validarNumero(nombre, duracion);
		int orden = siguienteOrden(espectaculo.getId());
		Numero numero = new Numero(orden, nombre, duracion, espectaculo);
		return numeroRepository.save(numero);
	}

	public Numero modificarNumero(Long id, String nombre, double duracion) {
		Numero numero = numeroRepository.findById(id).orElseThrow(
				() -> new EntidadNoEncontradaExcepcion("Numero no encontrado"));
		validarNumero(nombre, duracion);
		numero.setNombre(nombre);
		numero.setDuracion(duracion);
		return numeroRepository.save(numero);
	}

	public Numero asignarArtistas(Long idNumero, List<Artista> artistas) {
		if (artistas == null || artistas.isEmpty()) {
			throw new ValidacionExcepcion(
					"Debe asignar al menos un artista al numero");
		}
		Numero numero = numeroRepository.findById(idNumero).orElseThrow(
				() -> new EntidadNoEncontradaExcepcion("Numero no encontrado"));
		numero.setArtistas(artistas);
		return numeroRepository.save(numero);
	}

	public List<Numero> listarNumerosDeEspectaculo(Long idEspectaculo) {
		return numeroRepository
				.findByEspectaculoIdOrderByOrdenAsc(idEspectaculo);
	}

	public List<Artista> listarArtistas() {
		return artistaRepository.findAll();
	}

	public List<Coordinacion> listarCoordinadores() {
		return coordinacionRepository.findAll();
	}

	private void validarEspectaculo(String nombre, LocalDate fechaini,
			LocalDate fechafin, Long idPropio) {
		if (nombre == null || nombre.isBlank()) {
			throw new ValidacionExcepcion("El nombre no puede estar vacio");
		}
		if (nombre.length() > 25) {
			throw new ValidacionExcepcion(
					"El nombre no puede superar los 25 caracteres");
		}
		if (fechaini == null || fechafin == null) {
			throw new ValidacionExcepcion("Las fechas no pueden estar vacias");
		}
		if (fechafin.isBefore(fechaini)) {
			throw new ValidacionExcepcion(
					"La fecha de fin no puede ser anterior a la de inicio");
		}
		if (fechafin.isAfter(fechaini)) {
			throw new ValidacionExcepcion(
					"El periodo no puede ser superior a 1 ano");
		}
		Espectaculo existente = espectaculoRepository.findByNombre(nombre);
		if (existente != null && !existente.getId().equals(idPropio)) {
			throw new ValidacionExcepcion(
					"Ya existe un espectaculo con ese nombre");
		}
	}

	private void validarNumero(String nombre, double duracion) {
		if (nombre == null || nombre.isBlank()) {
			throw new ValidacionExcepcion(
					"El nombre del numero no puede estar vacio");
		}
		if (duracion <= 0) {
			throw new ValidacionExcepcion("La duracion debe ser mayor que 0");
		}
		double parteDecimal = duracion - Math.floor(duracion);
		if (parteDecimal != 0.0 && parteDecimal != 0.5) {
			throw new ValidacionExcepcion(
					"La duracion solo puede tener ,0 o ,5 como decimales");
		}
	}

	private int siguienteOrden(Long idEspectaculo) {
		return numeroRepository
				.findByEspectaculoIdOrderByOrdenAsc(idEspectaculo).size() + 1;
	}
}
