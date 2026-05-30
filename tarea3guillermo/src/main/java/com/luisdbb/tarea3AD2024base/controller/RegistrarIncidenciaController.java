/**
 *Clase RegistrarIncidenciaController.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.config.StageManager;
import com.luisdbb.tarea3AD2024base.excepciones.ValidacionExcepcion;
import com.luisdbb.tarea3AD2024base.modelo.Espectaculo;
import com.luisdbb.tarea3AD2024base.modelo.Numero;
import com.luisdbb.tarea3AD2024base.modelo.Persona;
import com.luisdbb.tarea3AD2024base.objectdb.TipoIncidencia;
import com.luisdbb.tarea3AD2024base.services.EspectaculoService;
import com.luisdbb.tarea3AD2024base.services.IncidenciaService;
import com.luisdbb.tarea3AD2024base.services.PersonaService;
import com.luisdbb.tarea3AD2024base.services.SesionService;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

@Controller
public class RegistrarIncidenciaController  implements Initializable{
	
	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private IncidenciaService incidenciaService;

	@Autowired
	private EspectaculoService espectaculoService;

	@Autowired
	private PersonaService personaService;

	@Autowired
	private SesionService sesionService;

	@FXML
	private ComboBox<String> cbTipo;
	@FXML
	private TextArea taDescripcion;
	@FXML
	private ComboBox<String> cbEspectaculo;
	@FXML
	private ComboBox<String> cbNumero;

	@FXML
	private Button btnAtras;
	@FXML
	private Button btnRegistrar;
	
	
    private List<Espectaculo> espectaculos;
    private List<Numero> numeros;
    
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		 ObservableList<String> tipos = FXCollections.observableArrayList();
	        for (TipoIncidencia tipo : TipoIncidencia.values()) {
	            tipos.add(tipo.name());
	        }
	        cbTipo.setItems(tipos);
	        cbTipo.getSelectionModel().selectFirst();

	        
	        espectaculos = espectaculoService.listarEspectaculos();
	        ObservableList<String> itemsEsp = FXCollections.observableArrayList();
	        itemsEsp.add("-- Ninguno --");
	        for (Espectaculo e : espectaculos) 
	        {
	            itemsEsp.add(e.getId() + " - " + e.getNombre());
	        }
	        
	        cbEspectaculo.setItems(itemsEsp);
	        cbEspectaculo.getSelectionModel().selectFirst();
	        cargarTodosLosNumeros();
	        
	        cbEspectaculo.getSelectionModel().selectedIndexProperty()
            .addListener((obs, o, n) -> {
                int idx = n.intValue();
                if (idx <= 0) {
                 
                    cargarTodosLosNumeros();
                } else {
                   
                    Espectaculo e = espectaculos.get(idx - 1);
                    numeros = espectaculoService.listarNumerosDeEspectaculo(e.getId());
                    ObservableList<String> itemsNum =
                            FXCollections.observableArrayList();
                    itemsNum.add("-- Ninguno --");
                    for (Numero num : numeros) {
                        itemsNum.add(num.getId() + " - " + num.getNombre());
                    }
                    cbNumero.setItems(itemsNum);
                    cbNumero.getSelectionModel().selectFirst();
                }
            });
	}
	@FXML
	private void registrar() {
	    try {
	        TipoIncidencia tipo = TipoIncidencia.valueOf(
	                cbTipo.getSelectionModel().getSelectedItem());

	        String usuarioActual = sesionService.getUsuarioActual().getNombre();

	    
	        Long idPersonaReporta = null;
	        Persona persona = personaService.buscarPersonaPorUsuario(usuarioActual);
	        if (persona != null) {
	            idPersonaReporta = persona.getId();
	        } else {
	            idPersonaReporta = 0L; 
	        }

	        Long idEspectaculo = null;
	        int idxEsp = cbEspectaculo.getSelectionModel().getSelectedIndex();
	        if (idxEsp > 0) {
	            idEspectaculo = espectaculos.get(idxEsp - 1).getId();
	        }

	        Long idNumero = null;
	        int idxNum = cbNumero.getSelectionModel().getSelectedIndex();
	        if (idxNum > 0 && numeros != null) {
	            idNumero = numeros.get(idxNum - 1).getId();
	        }

	        incidenciaService.registrarIncidencia(
	                tipo,
	                taDescripcion.getText(),
	                idPersonaReporta,
	                idEspectaculo,
	                idNumero);

	        mostrarInfo("Incidencia registrada correctamente");
	        taDescripcion.clear();
	        cbEspectaculo.getSelectionModel().selectFirst();
	        cbNumero.getSelectionModel().selectFirst();

	    } catch (ValidacionExcepcion e) {
	        mostrarError(e.getMessage());
	    }
	}
		
		@FXML
		private void atras(ActionEvent event) {
			if (sesionService.isAdmin()) {
				stageManager.switchScene(FxmlView.MENU_ADMIN);
			} else if (sesionService.isCoordinacion()) {
				stageManager.switchScene(FxmlView.MENU_COORDINADOR);
			} else {
				stageManager.switchScene(FxmlView.MENU_ARTISTA);
			}
		}
		
		private void cargarTodosLosNumeros() {
		    numeros = espectaculoService.listarTodosLosNumeros();
		    ObservableList<String> itemsNum = FXCollections.observableArrayList();
		    itemsNum.add("-- Ninguno --");
		    for (Numero num : numeros) {
		        itemsNum.add(num.getId() + " - " + num.getNombre()
		                + " (Esp: " + num.getEspectaculo().getNombre() + ")");
		    }
		    cbNumero.setItems(itemsNum);
		    cbNumero.getSelectionModel().selectFirst();
		}

		private void mostrarError(String msg) {
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setHeaderText(null);
			a.setContentText(msg);
			a.showAndWait();
		}

		private void mostrarInfo(String msg) {
			Alert a = new Alert(Alert.AlertType.INFORMATION);
			a.setHeaderText(null);
			a.setContentText(msg);
			a.showAndWait();
		}

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

