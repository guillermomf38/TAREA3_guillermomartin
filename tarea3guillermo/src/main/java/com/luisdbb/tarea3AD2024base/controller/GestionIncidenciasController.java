/**
 *Clase GestionIncidenciasController.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package com.luisdbb.tarea3AD2024base.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.config.StageManager;
import com.luisdbb.tarea3AD2024base.excepciones.ValidacionExcepcion;
import com.luisdbb.tarea3AD2024base.modelo.Espectaculo;
import com.luisdbb.tarea3AD2024base.modelo.Incidencia;
import com.luisdbb.tarea3AD2024base.modelo.Persona;
import com.luisdbb.tarea3AD2024base.modelo.TipoIncidencia;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

@Controller
public class GestionIncidenciasController implements Initializable {

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
	private ComboBox<String> cbFiltroTipo;
	@FXML
	private ComboBox<String> cbFiltroEstado;
	@FXML
	private ComboBox<String> cbFiltroEspectaculo;
	@FXML
	private DatePicker dpDesde;
	@FXML
	private DatePicker dpHasta;
	@FXML
	private ListView<String> lvIncidencias;

	@FXML
	private Button btnAtras;
	@FXML
	private Button btnBuscar;
	@FXML
	private Button btnMostrar;
	@FXML
	private Button btnLimpiar;
	@FXML
	private Button btnResolver;

	@FXML
	private TextArea taDetalle;
	@FXML
	private TextArea taAcciones;
	@FXML
	private VBox panelResolucion;
	@FXML
	private Label lblTotal;

	private List<Incidencia> incidencias;
	private List<Espectaculo> espectaculos;
	private Incidencia incidenciaSeleccionada;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		ObservableList<String> tipos = FXCollections.observableArrayList();
		tipos.add("--Todos--");
		for (TipoIncidencia tipo : TipoIncidencia.values())
			tipos.add(tipo.name());
		cbFiltroTipo.setItems(tipos);
		cbFiltroTipo.getSelectionModel().selectFirst();

		
		cbFiltroEstado.setItems(FXCollections.observableArrayList("-- Todos --", "No resuelta", "Resuelta"));
		cbFiltroEstado.getSelectionModel().selectFirst();

		espectaculos = espectaculoService.listarEspectaculos();
		ObservableList<String> itemsEsp = FXCollections.observableArrayList();
		itemsEsp.add("-- Todos --");
		for (Espectaculo e : espectaculos) 
		{
			itemsEsp.add(e.getId() + " - " + e.getNombre());
		}
		cbFiltroEspectaculo.setItems(itemsEsp);
		cbFiltroEspectaculo.getSelectionModel().selectFirst();

		
		boolean puedeResolver = sesionService.isAdmin() || sesionService.isCoordinacion();
		
		panelResolucion.setVisible(puedeResolver);
		panelResolucion.setManaged(puedeResolver);

		
		lvIncidencias.getSelectionModel().selectedIndexProperty().addListener((obs, o, n) -> 
		{
					int idx = n.intValue();
					if (incidencias != null && idx >= 0 && idx < incidencias.size()) 
					{
						incidenciaSeleccionada = incidencias.get(idx);
						mostrarDetalle(incidenciaSeleccionada);
					}
				});

		mostrarTodas(null);
	}

	@FXML
	private void buscar(ActionEvent event) {
		TipoIncidencia tipo = null;
		int idxTipo = cbFiltroTipo.getSelectionModel().getSelectedIndex();
		if (idxTipo > 0) {
			tipo = TipoIncidencia.values()[idxTipo - 1];
		}

		Boolean resuelta = null;
		int idxEstado = cbFiltroEstado.getSelectionModel().getSelectedIndex();
		if (idxEstado == 1)
			resuelta = false;
		else if (idxEstado == 2)
			resuelta = true;

		Long idEspectaculo = null;
		int idxEsp = cbFiltroEspectaculo.getSelectionModel().getSelectedIndex();
		if (idxEsp > 0)
			idEspectaculo = espectaculos.get(idxEsp - 1).getId();

		LocalDateTime desde = dpDesde.getValue() != null ? dpDesde.getValue().atStartOfDay() : null;
		LocalDateTime hasta = dpHasta.getValue() != null ? dpHasta.getValue().atTime(LocalTime.MAX) : null;

		incidencias = incidenciaService.consultarIncidencias(tipo, resuelta, idEspectaculo, null, desde, hasta);
		cargarLista();
	}

	@FXML
	private void mostrarTodas(ActionEvent event) {
		incidencias = incidenciaService.listarTodas();
		cargarLista();
	}

	@FXML
	private void limpiarFiltros(ActionEvent event) {
		cbFiltroTipo.getSelectionModel().selectFirst();
		cbFiltroEstado.getSelectionModel().selectFirst();
		cbFiltroEspectaculo.getSelectionModel().selectFirst();
		dpDesde.setValue(null);
		dpHasta.setValue(null);
		mostrarTodas(event);
	}

	@FXML
	private void resolver() {
	    if (incidenciaSeleccionada == null) {
	        mostrarError("Selecciona una incidencia primero");
	        return;
	    }
	    if (incidenciaSeleccionada.isResuelta()) {
	        mostrarError("Esta incidencia ya esta resuelta");
	        return;
	    }
	    if (taAcciones.getText().isBlank()) {
	        mostrarError("Debes describir las acciones realizadas");
	        return;
	    }
	    try {
	        String usuarioActual = sesionService.getUsuarioActual().getNombre();

	       
	        Long idPersonaResuelve = null;
	        Persona persona = personaService.buscarPersonaPorUsuario(usuarioActual);
	        if (persona != null) {
	            idPersonaResuelve = persona.getId();
	        } else {
	           
	            idPersonaResuelve = 0L;
	        }

	        incidenciaService.resolverIncidencia(
	                incidenciaSeleccionada.getId(),
	                taAcciones.getText(),
	                idPersonaResuelve);

	        mostrarInfo("Incidencia resuelta correctamente");
	        taAcciones.clear();
	        incidenciaSeleccionada = null;
	        taDetalle.clear();
	        mostrarTodas(null);

	    } catch (ValidacionExcepcion e) {
	        mostrarError(e.getMessage());
	    }
	}

	private void cargarLista() {
		ObservableList<String> items = FXCollections.observableArrayList();
		for (Incidencia i : incidencias) {
			items.add("[" + (i.isResuelta() ? "Resuelta" : "No Resuelta") + "] " + i.getTipo()+ " | " + i.getFechaHora().toLocalDate() + " | "
							+ i.getDescripcion().substring(0,Math.min(40, i.getDescripcion().length())));
		}
		lvIncidencias.setItems(items);
		lblTotal.setText("Total: " + incidencias.size());
		taDetalle.clear();
		incidenciaSeleccionada = null;
	}

	private void mostrarDetalle(Incidencia i) 
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("ID: ").append(i.getId()).append("\n");
		 String fecha = i.getFechaHora().format(
		            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
		    sb.append("Fecha: ").append(fecha).append("\n");
		sb.append("Tipo: ").append(i.getTipo()).append("\n");
		sb.append("Estado: ").append(i.isResuelta() ? "Resuelta" : "Pendiente").append("\n");
		sb.append("Persona reporta ID: ").append(i.getIdPersonaReporta()).append("\n");
		if (i.getIdEspectaculo() != null)
			sb.append("Espectáculo ID: ").append(i.getIdEspectaculo()).append("\n");
		if (i.getIdNumero() != null)
			sb.append("Número ID: ").append(i.getIdNumero()).append("\n");
		sb.append("Descripción: ").append(i.getDescripcion());
		taDetalle.setText(sb.toString());
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
