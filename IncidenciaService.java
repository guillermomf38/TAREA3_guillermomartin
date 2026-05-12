/**
 *Clase IncidenciaService.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package com.luisdbb.tarea3AD2024base.services;

import java.time.LocalDateTime;

import java.util.List;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.luisdbb.tarea3AD2024base.excepciones.ValidacionExcepcion;
import com.luisdbb.tarea3AD2024base.modelo.Incidencia;
import com.luisdbb.tarea3AD2024base.modelo.ResolucionIncidencia;
import com.luisdbb.tarea3AD2024base.modelo.TipoIncidencia;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

@Service
public class IncidenciaService {
	  private EntityManagerFactory emf;

	    @Value("${objectdb.url}")
	    private String objectdbUrl;

	    @PostConstruct
	    public void init() {
	        
	        this.emf = Persistence.createEntityManagerFactory(objectdbUrl);
	    }

	public Incidencia registrarIncidencia(TipoIncidencia tipo, String descripcion, Long idPersonaReporta, Long idEspectaculo, Long idNumero) 
	{
		if (descripcion == null || descripcion.isBlank()) {
			throw new ValidacionExcepcion(
					"La descripcion no puede estar vacia");
		}
		if (descripcion.length() > 1000) {
			throw new ValidacionExcepcion(
					"La descripcion no puede superar los 1000 caracteres");
		}
		if (tipo == null) {
			throw new ValidacionExcepcion(
					"El tipo de incidencia es obligatorio");
		}
		if (idPersonaReporta == null) {
			throw new ValidacionExcepcion(
					"Debe indicarse quien reporta la incidencia");
		}
		
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			Incidencia incidencia = new Incidencia(tipo, descripcion, idPersonaReporta, idEspectaculo, idNumero);
			em.persist(incidencia);
			em.getTransaction().commit();
			return incidencia;
			
		} catch (Exception e) {
			
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw e;
		} finally {
			em.close();
		}
	}

	public void resolverIncidencia(Long idIncidencia, String accionesRealizadas,
			Long idPersonaResuelve) {
		if (accionesRealizadas == null || accionesRealizadas.isBlank()) 
		{
			throw new ValidacionExcepcion("Debes describir las acciones realizadas para resolver la incidencia");
		}

		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();

			Incidencia incidencia = em.find(Incidencia.class, idIncidencia);
			if (incidencia == null) 
			{
				throw new ValidacionExcepcion("Incidencia no encontrada");
			}
			if (incidencia.isResuelta()) 
			{
				throw new ValidacionExcepcion("Esta incidencia ya está resuelta");
			}


			incidencia.setResuelta(true);
			em.merge(incidencia);


			ResolucionIncidencia resolucion = new ResolucionIncidencia(accionesRealizadas, idPersonaResuelve, idIncidencia);
			em.persist(resolucion);

			em.getTransaction().commit();

		} catch (ValidacionExcepcion e) 
		{
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw e;
		} catch (Exception e)
		{
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw e;
		} finally {
			em.close();
		}
	}


	public List<Incidencia> consultarIncidencias(TipoIncidencia tipo, Boolean resuelta, Long idEspectaculo, Long idNumero, LocalDateTime desde, LocalDateTime hasta) 
	{

		EntityManager em = emf.createEntityManager();
		try {
			StringBuilder jpql = new StringBuilder("SELECT i FROM Incidencia i WHERE 1=1");

			if (tipo != null)
				jpql.append(" AND i.tipo = :tipo");
			if (resuelta != null)
				jpql.append(" AND i.resuelta = :resuelta");
			if (idEspectaculo != null)
				jpql.append(" AND i.idEspectaculo = :idEspectaculo");
			if (idNumero != null)
				jpql.append(" AND i.idNumero = :idNumero");
			if (desde != null)
				jpql.append(" AND i.fechaHora >= :desde");
			if (hasta != null)
				jpql.append(" AND i.fechaHora <= :hasta");
			jpql.append(" ORDER BY i.fechaHora DESC");

			TypedQuery<Incidencia> query = em.createQuery(jpql.toString(),Incidencia.class);

			if (tipo != null)
				query.setParameter("tipo", tipo);
			if (resuelta != null)
				query.setParameter("resuelta", resuelta);
			if (idEspectaculo != null)
				query.setParameter("idEspectaculo", idEspectaculo);
			if (idNumero != null)
				query.setParameter("idNumero", idNumero);
			if (desde != null)
				query.setParameter("desde", desde);
			if (hasta != null)
				query.setParameter("hasta", hasta);

			return query.getResultList();

		} finally {
			em.close();
		}
	}

	public List<Incidencia> listarTodas() {
		EntityManager em = emf.createEntityManager();
		try {
			return em.createQuery(
					"SELECT i FROM Incidencia i ORDER BY i.fechaHora DESC",
					Incidencia.class).getResultList();
		} finally {
			em.close();
		}
	}
}
