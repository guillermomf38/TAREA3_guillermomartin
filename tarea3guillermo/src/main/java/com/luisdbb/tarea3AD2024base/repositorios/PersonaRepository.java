/**
 *Clase PersonaRepository.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.luisdbb.tarea3AD2024base.modelo.Perfiles;
import com.luisdbb.tarea3AD2024base.modelo.Persona;

@Repository
public interface PersonaRepository extends JpaRepository <Persona, Long>{
	
	Persona findByEmail (String email);
	
	Persona findByCredenciales_Nombre(String nombre);
	
	
	List<Persona> findByCredenciales_PerfilNot(Perfiles perfil);
}
