/**
 *Clase CoordinacionRepository.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luisdbb.tarea3AD2024base.modelo.Coordinacion;

@Repository
public interface CoordinacionRepository extends JpaRepository <Coordinacion, Long> {
}
