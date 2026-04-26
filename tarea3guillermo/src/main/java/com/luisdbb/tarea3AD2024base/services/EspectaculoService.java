/**
 *Clase EspectaculoService.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package com.luisdbb.tarea3AD2024base.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

	public List<Espectaculo> listarEspectaculos() 
	{
		return espectaculoRepository.findAllByOrderByFechainiAsc();
	}

	public Espectaculo verEspectaculoCompleto(Long id)
	{
		return espectaculoRepository.findById(id).orElseThrow(() -> new EntidadNoEncontradaExcepcion("Espectaculo no encontrado"));
	}

	public Espectaculo crearEspectaculo(String nombre, LocalDate fechaini,LocalDate fechafin, Coordinacion coordinador) 
	{
		validarEspectaculo(nombre, fechaini, fechafin, null);
		Espectaculo espectaculo = new Espectaculo(nombre, fechaini, fechafin,coordinador);
		return espectaculoRepository.save(espectaculo);
	}

	public Espectaculo modificarEspectaculo(Long id, String nombre,LocalDate fechaini, LocalDate fechafin, Coordinacion coordinador)
	{
		Espectaculo espectaculo = espectaculoRepository.findById(id).orElseThrow(() -> new EntidadNoEncontradaExcepcion("Espectaculo no encontrado"));

		validarEspectaculo(nombre, fechaini, fechafin, id);
		espectaculo.setNombre(nombre);
		espectaculo.setFechaini(fechaini);
		espectaculo.setFechafin(fechafin);
		espectaculo.setCoordinador(coordinador);
		return espectaculoRepository.save(espectaculo);
	}

	public Numero crearNumero(String ordenTexto, String nombre,String duracionTexto, Espectaculo espectaculo) {
		int orden = validarOrden(ordenTexto);
		double duracion = validarDuracion(duracionTexto);
		validarNombreNumero(nombre);
		Numero numero = new Numero(orden, nombre, duracion, espectaculo);
		return numeroRepository.save(numero);
	}

	public Numero modificarNumero(Long id, String ordenTexto, String nombre,String duracionTexto) {
		Numero numero = numeroRepository.findById(id).orElseThrow(() -> new EntidadNoEncontradaExcepcion("Numero no encontrado"));
		int orden = validarOrden(ordenTexto);
		double duracion = validarDuracion(duracionTexto);
		validarNombreNumero(nombre);
		numero.setOrden(orden);
		numero.setNombre(nombre);
		numero.setDuracion(duracion);
		return numeroRepository.save(numero);
	}

	public Numero asignarArtistas(Long idNumero, List<Artista> artistas) {
		if (artistas == null || artistas.isEmpty()) 
		{
			throw new ValidacionExcepcion("Debe asignar al menos un artista al numero");
		}
		Numero numero = numeroRepository.findById(idNumero).orElseThrow(() -> new EntidadNoEncontradaExcepcion("Numero no encontrado"));
		numero.setArtistas(artistas);
		return numeroRepository.save(numero);
	}

	public List<Numero> listarNumerosDeEspectaculo(Long idEspectaculo) {
		return numeroRepository.findByEspectaculoIdOrderByOrdenAsc(idEspectaculo);
	}

	public List<Artista> listarArtistas() {
		return artistaRepository.findAll();
	}

	public List<Coordinacion> listarCoordinadores() {
		return coordinacionRepository.findAll();
	}

	private void validarEspectaculo(String nombre, LocalDate fechaini,LocalDate fechafin, Long idPropio) {
		if (nombre == null || nombre.isBlank()) 
		{
			throw new ValidacionExcepcion("El nombre no puede estar vacio");
		}
		if (nombre.length() > 25) 
		{
			throw new ValidacionExcepcion("El nombre no puede superar los 25 caracteres");
		}
		if (fechaini == null) {
			throw new ValidacionExcepcion("La fecha de inicio no puede estar vacia");
		}
		if (fechafin == null) {
			throw new ValidacionExcepcion("La fecha de fin no puede estar vacia");
		}
		if (!fechafin.isAfter(fechaini)) {
			throw new ValidacionExcepcion("La fecha de fin debe ser posterior a la de inicio");
		}

		long dias = ChronoUnit.DAYS.between(fechaini, fechafin);
		if (dias > 365) 
		{
			throw new ValidacionExcepcion("El periodo no puede superar 1 año");
		}

		Espectaculo existente = espectaculoRepository.findByNombre(nombre);
		if (existente != null && !existente.getId().equals(idPropio)) 
		{
			throw new ValidacionExcepcion("Ya existe un espectaculo con ese nombre");
		}
	}

	private void validarNombreNumero(String nombre) {
		if (nombre == null || nombre.isBlank()) 
		{
			throw new ValidacionExcepcion("El nombre del numero no puede estar vacio");
		}
	}

	private int validarOrden(String texto) {
		if (texto == null || texto.isBlank()) 
		{
			throw new ValidacionExcepcion("El orden no puede estar vacio");
		}
		try {
			int orden = Integer.parseInt(texto.trim());
			if (orden <= 0) {
				throw new ValidacionExcepcion("El orden debe ser un numero entero positivo");
			}
			return orden;
			
		} catch (NumberFormatException e) {
			throw new ValidacionExcepcion("El orden debe ser un numero entero");
		}
	}

	private double validarDuracion(String texto) {
		if (texto == null || texto.isBlank()) {
			throw new ValidacionExcepcion("La duracion no puede estar vacia");
		}
		try {
			double duracion = Double.parseDouble(texto.replace(",", "."));
			if (duracion <= 0) {
				throw new ValidacionExcepcion("La duracion debe ser mayor que 0");
			}
			double decimal = duracion - Math.floor(duracion);
			if (Math.abs(decimal - 0.0) > 0.001 && Math.abs(decimal - 0.5) > 0.001) 
			{
				throw new ValidacionExcepcion("La duracion solo puede tener ,0 o ,5");
			}
			return duracion;
			
		} catch (NumberFormatException e) {
			throw new ValidacionExcepcion("La duracion debe ser un numero valido");
		}
	}

}
