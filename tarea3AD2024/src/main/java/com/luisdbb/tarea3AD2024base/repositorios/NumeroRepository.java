/**
 *Clase NumeroRepository.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luisdbb.tarea3AD2024base.modelo.Numero;

@Repository
public interface NumeroRepository extends JpaRepository <Numero, Long>{
	 
	List<Numero> findByEspectaculoIdOrderByOrdenAsc(Long idEspectaculo);
	
}
