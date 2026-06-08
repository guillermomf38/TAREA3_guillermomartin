/**
 *Clase DossierService.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luisdbb.tarea3AD2024base.modelo.Artista;
import com.luisdbb.tarea3AD2024base.modelo.Especialidad;
import com.luisdbb.tarea3AD2024base.modelo.Numero;
import com.luisdbb.tarea3AD2024base.mongodb.DossierArtista;
import com.luisdbb.tarea3AD2024base.mongodb.DossierEspectaculo;
import com.luisdbb.tarea3AD2024base.mongodb.DossierEvaluacion;
import com.luisdbb.tarea3AD2024base.mongodb.DossierNumero;
import com.luisdbb.tarea3AD2024base.mongodb.DossierObservacion;
import com.luisdbb.tarea3AD2024base.repositorios.DossierRepository;

@Service
public class DossierService {

    @Autowired
    private DossierRepository dossierRepository;

   
    public DossierArtista crearDossier(Artista artista) {
        DossierArtista dossier = new DossierArtista();
        dossier.setIdArtista(artista.getId());
        dossier.setNombre(artista.getNombre());
        dossier.setNacionalidad(artista.getNacionalidad());
        dossier.setEmail(artista.getEmail());

        List<String> especialidades = new ArrayList<>();
        for (Especialidad e : artista.getEspecialidades()) {
            especialidades.add(e.name());
        }
        dossier.setEspecialidades(especialidades);

        if (artista.getApodo() != null && !artista.getApodo().isBlank()) {
            dossier.setApodo(artista.getApodo());
        }

        return dossierRepository.save(dossier);
    }

 
    public DossierArtista actualizarDatosBasicos(Artista artista) {
        Optional<DossierArtista> opt = dossierRepository.findByIdArtista(artista.getId());

        if (opt.isEmpty()) {
            
            return crearDossier(artista);
        }

        DossierArtista dossier = opt.get();

       
        dossier.setNombre(artista.getNombre());
        dossier.setNacionalidad(artista.getNacionalidad());
        dossier.setEmail(artista.getEmail());

        List<String> especialidades = new ArrayList<>();
        for (Especialidad e : artista.getEspecialidades()) {
            especialidades.add(e.name());
        }
        dossier.setEspecialidades(especialidades);
        dossier.setApodo(artista.getApodo());

        return dossierRepository.save(dossier);
    }

   
    public void actualizarTrayectoria(Artista artista, Numero numero) {
        Optional<DossierArtista> opt = dossierRepository.findByIdArtista(artista.getId());

        if (opt.isEmpty()) return;

        DossierArtista dossier = opt.get();
        List<DossierEspectaculo> trayectoria = dossier.getTrayectoria();

        Long idEspectaculo = numero.getEspectaculo().getId();
        String nombreEspectaculo = numero.getEspectaculo().getNombre();

       
        DossierEspectaculo espExistente = null;
        for (DossierEspectaculo de : trayectoria) {
            if (de.getIdEspectaculo().equals(idEspectaculo)) {
                espExistente = de;
                break;
            }
        }

        if (espExistente == null) {
            
            espExistente = new DossierEspectaculo(
                    idEspectaculo, nombreEspectaculo);
            trayectoria.add(espExistente);
        }

       
        boolean tieneNumero = false;
        for (DossierNumero dn : espExistente.getNumeros()) {
            if (dn.getIdNumero().equals(numero.getId())) {
                tieneNumero = true;
                break;
            }
        }

        if (!tieneNumero) {
            espExistente.getNumeros().add(
                    new DossierNumero(numero.getId(), numero.getNombre()));
        }

        dossierRepository.save(dossier);
    }

    
    public void añadirEvaluacion(Long idArtista, String comentario,
                                  String nivel, Long idPersona, String rol) {
        Optional<DossierArtista> opt = dossierRepository.findByIdArtista(idArtista);
        if (opt.isEmpty()) return;

        DossierArtista dossier = opt.get();

        String fecha = LocalDate.now().toString();
        DossierEvaluacion evaluacion = new DossierEvaluacion(
                fecha, idPersona, rol, comentario, nivel);

        dossier.getEvaluaciones().add(evaluacion);
        dossierRepository.save(dossier);
    }

    
    public void añadirObservacion(Long idArtista, String texto,
                                   String autor) {
        Optional<DossierArtista> opt = dossierRepository.findByIdArtista(idArtista);
        if (opt.isEmpty()) return;

        DossierArtista dossier = opt.get();

        String fecha = LocalDate.now().toString();
        DossierObservacion observacion = new DossierObservacion(
                fecha, texto, autor);

        dossier.getObservaciones().add(observacion);
        dossierRepository.save(dossier);
    }

 
    public Optional<DossierArtista> obtenerDossier(Long idArtista) {
        return dossierRepository.findByIdArtista(idArtista);
    }

    
    public List<DossierArtista> listarTodos() {
        return dossierRepository.findAll();
    }
}
