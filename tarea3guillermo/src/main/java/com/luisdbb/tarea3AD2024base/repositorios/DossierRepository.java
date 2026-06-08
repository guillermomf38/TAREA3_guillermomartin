/**
 *Clase DossierRepository.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.repositorios;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.luisdbb.tarea3AD2024base.mongodb.DossierArtista;

@Repository
public interface DossierRepository extends MongoRepository <DossierArtista, String> {
	
	Optional<DossierArtista> findByIdArtista(Long idArtista);

}
