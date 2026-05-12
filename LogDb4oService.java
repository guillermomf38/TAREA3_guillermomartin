/**
 *Clase LogDb4oService.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.luisdbb.tarea3AD2024base.modelo.LogOperacion;
import com.luisdbb.tarea3AD2024base.modelo.TipoOperacion;

@Service
public class LogDb4oService{
	private static final String rutaDb4o="ficheros/log.db4o";
	
	   private ObjectContainer conexion() {
	        return Db4oEmbedded.openFile(
	                Db4oEmbedded.newConfiguration(), rutaDb4o);
	    }

		public void registrarOperacion(String usuario, TipoOperacion tipo,String resumen) 
		{
			 ObjectContainer db = null;
			    try {
			        db = conexion();

			        long nuevoId = db.query(LogOperacion.class).size() + 1L;

			        String fechaHora = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

			        LogOperacion log = new LogOperacion(nuevoId, fechaHora, usuario, tipo.name(), resumen);

			        db.store(log);
			        db.commit();

			    } catch (Exception e) {
			       
			        if (db != null && !db.ext().isClosed()) {
			            db.rollback();
			        }
			        e.printStackTrace();
			    } finally {
			        
			        if (db != null && !db.ext().isClosed()) {
			            db.close();
			        }
			    }
		}

		public List<LogOperacion> consultarHistorial(String usuarioFiltro, List<TipoOperacion> tiposFiltro, String desde,
				String hasta) {

			ObjectContainer db = conexion();
			try {
				
				ObjectSet<LogOperacion> resultado = db.query(new Predicate<LogOperacion>() {
							
							@Override
							public boolean match(LogOperacion log) {

								if (usuarioFiltro != null && !usuarioFiltro.isBlank()) 
								{
									if (!log.getUsuario().equalsIgnoreCase(usuarioFiltro)) 
									{
										return false;
									}
								}
								
								if (tiposFiltro != null && !tiposFiltro.isEmpty()) 
								{
					              boolean coincide = false;
					              for (TipoOperacion t : tiposFiltro) 
					              {
					                if (t.name().equals(log.getTipoOperacion()))
					                {
					                  coincide = true;
					                   break;
					                 }
					               }
					                 if (!coincide) return false;
					                
								}
								
								if (desde != null && log.getFechaHora().compareTo(desde) < 0)
								{
									 return false;
								}
					            if (hasta != null && log.getFechaHora().compareTo(hasta) > 0) 
					            {
					                	return false;
					            }


								return true;
							}
						});

				return new ArrayList<>(resultado);

			} finally {
				db.close();
			}
		}

		
		public List<LogOperacion> total() {
			ObjectContainer db = conexion();
			try 
			{
				return new ArrayList<>(db.query(LogOperacion.class));
			} 
			finally
			{
				db.close();
			}
		}
	}
